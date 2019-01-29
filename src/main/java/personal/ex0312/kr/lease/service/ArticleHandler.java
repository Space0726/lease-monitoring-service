package personal.ex0312.kr.lease.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.domain.Article;
import personal.ex0312.kr.lease.repository.ArticleRepository;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ArticleHandler {
    private final ArticleRepository articleRepository;
    private final EmailService emailService;

    public void processArticles(List<Article> articles) throws IOException, MessagingException {
        Map<String, Article> existingArticles = articleRepository.findAllArticle().stream()
            .collect(Collectors.toMap(Article::getArticleId, article -> article));

        List<Article> willBeInsertedArticles = articles.stream()
            .filter(article -> isNewArticle(existingArticles, article))
            .collect(Collectors.toList());

        if (!willBeInsertedArticles.isEmpty()) {
            articleRepository.insertArticles(willBeInsertedArticles);
        }

        List<Article> willBeUpdatedArticles = articles.stream()
            .filter(article -> !isNewArticle(existingArticles, article))
            .filter(article -> !isSamePrice(existingArticles.get(article.getArticleId()), article))
            .collect(Collectors.toList());

        if (!willBeUpdatedArticles.isEmpty()) {
            articleRepository.updateArticles(willBeUpdatedArticles);
        }

        List<Article> willBeSentToMailArticles = Stream.concat(willBeInsertedArticles.stream(), willBeUpdatedArticles.stream())
            .collect(Collectors.toList());

        if (!willBeSentToMailArticles.isEmpty()) {
            emailService.sendArticles(willBeSentToMailArticles);
        }
    }

    private boolean isSamePrice(Article existingArticle, Article mayBeUpdatedArticle) {
        return existingArticle.getWarrantPrice().equals(mayBeUpdatedArticle.getWarrantPrice());
    }

    private boolean isNewArticle(Map<String, Article> existingArticles, Article article) {
        return !existingArticles.containsKey(article.getArticleId());
    }
}
