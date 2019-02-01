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
        String areaIdentifier = "1040203412";

        List<Article> articles = Arrays.asList(
            Article.builder().articleId("1").warrantPrice("1억2,000").build(),
            Article.builder().articleId("2").warrantPrice("7,000").build(),
            Article.builder().articleId("2").warrantPrice("2억70").build(),
            Article.builder().articleId("2").warrantPrice("5억").build()
        );

        // when
        articlePolishService.polishArticles(articles, areaIdentifier);

        // then
        assertThat(articles.size()).isEqualTo(4);

        assertThat(articles.get(0).getWarrantPrice()).isEqualTo("12000");
        assertThat(articles.get(1).getWarrantPrice()).isEqualTo("7000");
        assertThat(articles.get(2).getWarrantPrice()).isEqualTo("20070");
        assertThat(articles.get(3).getWarrantPrice()).isEqualTo("50000");

        assertThat(articles.get(0).getAreaIdentifier()).isEqualTo(areaIdentifier);
        assertThat(articles.get(0).getRegisteredAt()).isNotNull();
        assertThat(articles.get(0).getMobileDetailLink()).isNotNull();
        assertThat(articles.get(0).getPcDetailLink()).isNotNull();
    }
}
