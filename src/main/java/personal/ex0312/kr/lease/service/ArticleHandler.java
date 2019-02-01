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

    public void processArticles(List<MonitoringJob> allJobs, List<Article> collectedArticles) {
        Map<String, Article> existingArticles = articleRepository.findAllArticle().stream()
            .collect(Collectors.toMap(Article::getArticleId, article -> article));

        List<Article> willBeInsertedArticles = collectedArticles.stream()
            .filter(article -> isNewArticle(existingArticles, article))
            .collect(Collectors.toList());

        if (!willBeInsertedArticles.isEmpty()) {
            articleRepository.insertArticles(willBeInsertedArticles);
        }

        List<Article> willBeUpdatedArticles = collectedArticles.stream()
            .filter(article -> !isNewArticle(existingArticles, article))
            .filter(article -> !isSamePrice(existingArticles.get(article.getArticleId()), article))
            .collect(Collectors.toList());

        if (!willBeUpdatedArticles.isEmpty()) {
            articleRepository.updateArticles(willBeUpdatedArticles);
        }

        List<Article> willBeSentToMailArticles = Stream.concat(willBeInsertedArticles.stream(), willBeUpdatedArticles.stream())
            .collect(Collectors.toList());

        allJobs.forEach(job -> {
            List<Article> articles = new ArrayList<>();

            job.getAreaIdentifiers().forEach(areaId -> articles.addAll(
                willBeSentToMailArticles.stream()
                    .filter(article -> areaId.equals(article.getAreaIdentifier()))
                    .filter(article -> isMetPriceCondition(job, article))
                    .filter(article -> isMetTradeType(job, article.getTradeType()))
                    .collect(Collectors.toList())
                )
            );

            try {
                emailService.sendArticles(job.getEmailAddress(), articles);
            } catch (IOException | MessagingException e) {
                e.printStackTrace();
                log.error("Failed to send e-mail. e-mail address : {}, articles : {}", job.getEmailAddress(), articles);
            }
        });
    }

    private boolean isMetTradeType(MonitoringJob job, String tradeType) {
        return job.getTradeTypes().stream()
            .map(Enum::toString)
            .collect(Collectors.toSet())
            .contains(tradeType);
    }

    private boolean isMetPriceCondition(MonitoringJob job, Article article) {
        int price = Integer.parseInt(article.getWarrantPrice());
        return price <= job.getMaximumPrice() && price >= job.getMinimumPrice();
    }

    private boolean isSamePrice(Article existingArticle, Article mayBeUpdatedArticle) {
        return existingArticle.getWarrantPrice().equals(mayBeUpdatedArticle.getWarrantPrice());
    }

    private boolean isNewArticle(Map<String, Article> existingArticles, Article article) {
        return !existingArticles.containsKey(article.getArticleId());
    }
}
