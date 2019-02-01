package personal.ex0312.kr.lease.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import personal.ex0312.kr.lease.domain.Article;
import personal.ex0312.kr.lease.repository.ArticleRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArticleHandlerTest {
    @InjectMocks
    private ArticleHandler articleHandler;
    @Mock
    private EmailService emailService;
    @Mock
    private ArticleRepository articleRepository;

    @Test
    public void testProcessNewlyArticles_whenNotIncludingNewlyArticles_thenShouldNotInsertArticles() {
        // given
        Map<String, Article> existingArticles = createExistingArticleMap();

        List<Article> collectedArticles = Arrays.asList(
            Article.builder().articleId("article1").build(),
            Article.builder().articleId("article2").build()
        );

        // when
        List<Article> actual = articleHandler.processNewlyArticles(existingArticles, collectedArticles);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(0);

        verify(articleRepository, times(0)).insertArticles(any());
    }

    @Test
    public void testProcessNewlyArticles_whenIncludingNewlyArticles_thenShouldInsertArticles() {
        // given
        Map<String, Article> existingArticles = createExistingArticleMap();

        List<Article> collectedArticles = Arrays.asList(
            Article.builder().articleId("article1").build(),
            Article.builder().articleId("article999").build()
        );

        // when
        List<Article> actual = articleHandler.processNewlyArticles(existingArticles, collectedArticles);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getArticleId()).isEqualTo("article999");

        verify(articleRepository, times(1)).insertArticles(any());
    }

    @Test
    public void testProcessDuplicatedArticles_whenNotIncludingDuplicatedArticles_thenShouldNotUpdateArticles() {
        // given
        Map<String, Article> existingArticles = createExistingArticleMap();

        List<Article> collectedArticles = new ArrayList<>();

        // when
        List<Article> actual = articleHandler.processDuplicatedArticles(existingArticles, collectedArticles);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(0);

        verify(articleRepository, times(0)).updateArticles(any());
    }

    @Test
    public void testProcessDuplicatedArticles_whenIncludingDuplicatedArticles_thenShouldUpdateArticles() {
        // given
        Map<String, Article> existingArticles = createExistingArticleMap();

        List<Article> collectedArticles = Arrays.asList(
            Article.builder().articleId("article1").warrantPrice("100").build(),
            Article.builder().articleId("article2").warrantPrice("10000").build()
        );

        // when
        List<Article> actual = articleHandler.processDuplicatedArticles(existingArticles, collectedArticles);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getArticleId()).isEqualTo("article2");
        assertThat(actual.get(0).getWarrantPrice()).isEqualTo("10000");

        verify(articleRepository, times(1)).updateArticles(any());
    }

    private Map<String, Article> createExistingArticleMap() {
        Map<String, Article> existingArticles = new HashMap<>();
        existingArticles.put("article1", Article.builder().articleId("article1").warrantPrice("100").build());
        existingArticles.put("article2", Article.builder().articleId("article2").warrantPrice("100").build());
        return existingArticles;
    }
}
