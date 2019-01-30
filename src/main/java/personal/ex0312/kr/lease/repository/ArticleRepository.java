package personal.ex0312.kr.lease.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    public void updateArticles(List<Article> articles) {
        articles.parallelStream().forEach(article ->
            mongoTemplate.findAndModify(
                Query.query(Criteria.where("articleId").is(article.getArticleId())),
                Update.update("warrantPrice", article.getWarrantPrice()),
                Article.class
            )
        );
    }
}
