package personal.ex0312.kr.lease.service;

import org.junit.Test;
import personal.ex0312.kr.lease.domain.Article;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticlePolishServiceTest {
    private final ArticlePolishService articlePolishService = new ArticlePolishService();

    @Test
    public void testPolishArticles() {
        // given
        List<Article> articles = Arrays.asList(
            Article.builder().articleId("1").price("1억2,000").build(),
            Article.builder().articleId("2").price("7,000").build()
        );

        // when
        List<Article> polishedArticles = articlePolishService.polishArticles(articles);

        // then
        assertThat(polishedArticles).isNotNull();
        assertThat(polishedArticles.size()).isEqualTo(2);
        assertThat(polishedArticles.get(0).getPrice()).isEqualTo("12000");
        assertThat(polishedArticles.get(1).getPrice()).isEqualTo("7000");
    }
}
