package personal.ex0312.kr.lease.service;

import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.domain.Article;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticlePolishService {
    List<Article> polishArticles(List<Article> articlesFromNaver) {
        return articlesFromNaver.stream()
            .peek(article -> {
                String price = article.getPrice().replaceAll("[억,]", "");
                article.setPrice(price);
                System.out.println();
            })
            .collect(Collectors.toList());
    }
}
