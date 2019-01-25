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

        StringBuilder links = new StringBuilder();

        List<Article> collect = articles.stream()
            .filter(article -> isNewArticle(existingArticles, article))
            .peek(article -> links.append(article.toString()).append("\n"))
            .collect(Collectors.toList());

        if (!collect.isEmpty()) {
            articleRepository.insertArticles(collect);
            emailService.sendEmail("신규주택 : " + LocalDateTime.now().toString(), links.toString());
        }

        // TODO: if article were duplicated, then should notify what is difference
    }

    private boolean isNewArticle(Map<String, Article> existingArticles, Article article) {
        return !existingArticles.containsKey(article.getArticleId());
    }
}
