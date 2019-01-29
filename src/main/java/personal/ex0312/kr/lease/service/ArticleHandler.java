package personal.ex0312.kr.lease.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.domain.Article;
import personal.ex0312.kr.lease.domain.MonitoringJob;
import personal.ex0312.kr.lease.repository.ArticleRepository;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleHandler {
    private final ArticleRepository articleRepository;
    private final EmailService emailService;
    private final ArticlePolishService articlePolishService;

    public void processArticles(List<MonitoringJob> allJobs, Map<String, List<Article>> articlesByAreaId) {
        articlePolishService.polishArticles(articlesByAreaId);

        Map<String, Article> existingArticles = articleRepository.findAllArticle().stream()
            .collect(Collectors.toMap(Article::getArticleId, article -> article));

        Map<String, List<Article>> willBeInsertedArticles = new HashMap<>();

        articlesByAreaId.forEach((articleId, articles) -> {
            List<Article> newArticles = articles.stream()
                .filter(article -> isNewArticle(existingArticles, article))
                .collect(Collectors.toList());
            willBeInsertedArticles.put(String.valueOf(articleId), newArticles);
        });

        if (!willBeInsertedArticles.isEmpty()) {
            articleRepository.insertArticles(mergeListsFromMap(willBeInsertedArticles));
        }

        Map<String, List<Article>> willBeUpdatedArticles = new HashMap<>();

        articlesByAreaId.forEach((articleId, articles) -> {
            List<Article> newArticles = articles.stream()
                .filter(article -> !isNewArticle(existingArticles, article))
                .filter(article -> !isSamePrice(existingArticles.get(article.getArticleId()), article))
                .collect(Collectors.toList());
            willBeUpdatedArticles.put(String.valueOf(articleId), newArticles);
        });

        if (!willBeUpdatedArticles.isEmpty()) {
            articleRepository.insertArticles(mergeListsFromMap(willBeUpdatedArticles));
        }

        Map<String, List<Article>> willBeSentToMailArticles = new HashMap<>(willBeInsertedArticles);
        willBeUpdatedArticles
            .forEach((areaId, articles) ->
                willBeSentToMailArticles.merge(
                    areaId,
                    articles,
                    (insertedArticles, updatedArticles) -> {
                        return Stream.concat(insertedArticles.stream(), updatedArticles.stream())
                            .collect(Collectors.toList());
                    }
                )
            );

        if (!willBeSentToMailArticles.isEmpty()) {
            allJobs.forEach(job -> {
                List<Article> articles = new ArrayList<>();

                job.getAreaIdentifiers().forEach(areaId ->
                    articles.addAll(
                        willBeSentToMailArticles.get(areaId).stream()
                            .filter(article -> {
                                int price = Integer.parseInt(article.getWarrantPrice());
                                return price <= job.getMaximumPrice() && price >= job.getMinimumPrice();
                            })
                            .collect(Collectors.toList())
                    )
                );

                try {
                    emailService.sendArticles(job.getEmailAddress(), articles);
                    log.info("Sent email successfully. recipient : {}, articles : {}", job.getEmailAddress(), articles);
                } catch (IOException | MessagingException e) {
                    e.printStackTrace();
                    log.error("Failed to send e-mail. e-mail address : {}, articles : {}", job.getEmailAddress(), articles);
                }
            });
        }
    }

    private List<Article> mergeListsFromMap(Map<String, List<Article>> articles) {
        return articles.values().stream()
            .reduce((articles1, articles2) -> Stream.concat(articles1.stream(), articles2.stream())
                .collect(Collectors.toList())
            )
            .orElseThrow(RuntimeException::new);
    }

    private boolean isSamePrice(Article existingArticle, Article mayBeUpdatedArticle) {
        return existingArticle.getWarrantPrice().equals(mayBeUpdatedArticle.getWarrantPrice());
    }

    private boolean isNewArticle(Map<String, Article> existingArticles, Article article) {
        return !existingArticles.containsKey(article.getArticleId());
    }
}
