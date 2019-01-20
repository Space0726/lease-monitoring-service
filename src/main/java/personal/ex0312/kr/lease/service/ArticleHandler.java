package personal.ex0312.kr.lease.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.domain.Article;
import personal.ex0312.kr.lease.repository.ArticleRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleHandler {
    private final ArticleRepository articleRepository;

    public void processArticles(List<Article> articles) {
        Map<String, Article> existingArticles = articleRepository.findAllArticle().stream()
            .collect(Collectors.toMap(Article::getArticleNo, article -> article));

        List<Article> collect = articles.stream()
            .filter(article -> !existingArticles.containsKey(article.getArticleNo()))
            .peek(article -> {
                String price = article.getDealOrWarrantPrc();
                article.setDealOrWarrantPrc(price.replace("억,", ""));
            })
            .collect(Collectors.toList());
//        articleRepository.insertArticles(collect);
        // TODO: if article were duplicated, then should notify what is difference
    }
}
