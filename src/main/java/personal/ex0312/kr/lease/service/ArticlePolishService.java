package personal.ex0312.kr.lease.service;

import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.domain.Article;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticlePolishService {
    List<Article> polishArticles(List<Article> articlesFromNaver) {
        return articlesFromNaver.stream()
            .peek(article -> {
                String price = article.getWarrantPrice().replaceAll("[억,]", "");
                article.setWarrantPrice(price);
                article.setRegisteredAt(LocalDateTime.now());
            })
            .collect(Collectors.toList());
    }
}
