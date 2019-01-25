package personal.ex0312.kr.lease.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.domain.Article;
import personal.ex0312.kr.lease.repository.ArticleRepository;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleHandler {
    private final ArticleRepository articleRepository;
    private final EmailService emailService;

    public void processArticles(List<Article> articles) throws IOException, MessagingException {
        Map<String, Article> existingArticles = articleRepository.findAllArticle().stream()
            .collect(Collectors.toMap(Article::getArticleId, article -> article));

        StringBuilder newlyArticleLinks = new StringBuilder();
        List<Article> willBeInsertedArticles = articles.stream()
            .filter(article -> isNewArticle(existingArticles, article))
            .peek(article -> newlyArticleLinks.append(article.toString()).append("\n"))
            .collect(Collectors.toList());

        if (!willBeInsertedArticles.isEmpty()) {
            articleRepository.insertArticles(willBeInsertedArticles);
            emailService.sendEmailWithHtmlFormat("신규주택 : " + LocalDateTime.now().toString(), newlyArticleLinks.toString());
        }

        StringBuilder updatedArticleLinks = new StringBuilder();
        List<Article> willBeUpdatedArticles = articles.stream()
            .filter(article -> !isNewArticle(existingArticles, article))
            .filter(article -> !isSamePrice(existingArticles.get(article.getArticleId()), article))
            .peek(article -> {
                updatedArticleLinks.append(article.toString()).append("\n");
            })
            .collect(Collectors.toList());

        if (!willBeUpdatedArticles.isEmpty()) {
            articleRepository.updateArticles(willBeUpdatedArticles);
            emailService.sendEmailWithHtmlFormat("기존주택 : " + LocalDateTime.now().toString(), updatedArticleLinks.toString());
        }
    }

    private boolean isSamePrice(Article existingArticle, Article mayBeUpdatedArticle) {
        return existingArticle.getPrice().equals(mayBeUpdatedArticle.getPrice());
    }

    private boolean isNewArticle(Map<String, Article> existingArticles, Article article) {
        return !existingArticles.containsKey(article.getArticleId());
    }
}
