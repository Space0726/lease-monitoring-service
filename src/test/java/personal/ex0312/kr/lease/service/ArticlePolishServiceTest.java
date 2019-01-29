package personal.ex0312.kr.lease.service;

import org.junit.Test;
import personal.ex0312.kr.lease.domain.Article;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticlePolishServiceTest {
    private final ArticlePolishService articlePolishService = new ArticlePolishService();

    @Test
    public void testPolishArticles() {
        // given
        List<Article> articles = Arrays.asList(
            Article.builder().articleId("1").warrantPrice("1억2,000").build(),
            Article.builder().articleId("2").warrantPrice("7,000").build(),
            Article.builder().articleId("2").warrantPrice("2억70").build()
        );
        Map<String, List<Article>> articlesByAreaId = new HashMap<>();
        articlesByAreaId.put("areaId", articles);

        // when
        articlePolishService.polishArticles(articlesByAreaId);

        // then
        assertThat(articlesByAreaId.get("areaId").size()).isEqualTo(3);
        assertThat(articlesByAreaId.get("areaId").get(0).getWarrantPrice()).isEqualTo("12000");
        assertThat(articlesByAreaId.get("areaId").get(1).getWarrantPrice()).isEqualTo("7000");
        assertThat(articlesByAreaId.get("areaId").get(2).getWarrantPrice()).isEqualTo("20070");
    }
}
