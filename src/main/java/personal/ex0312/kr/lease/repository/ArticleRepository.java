package personal.ex0312.kr.lease.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import personal.ex0312.kr.lease.domain.Article;

import java.util.Collection;
import java.util.List;

@Repository
@AllArgsConstructor
public class ArticleRepository {
    private final MongoTemplate mongoTemplate;

    public List<Article> findAllArticle() {
        return mongoTemplate.findAll(Article.class);
    }

    public void insertArticles(Collection<Article> articles) {
        mongoTemplate.insertAll(articles);
    }
}
