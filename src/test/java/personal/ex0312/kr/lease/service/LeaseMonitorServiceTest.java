package personal.ex0312.kr.lease.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import personal.ex0312.kr.lease.api.NaverNewLandApiClient;
import personal.ex0312.kr.lease.domain.Article;
import personal.ex0312.kr.lease.domain.Lease;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LeaseMonitorServiceTest {
    @InjectMocks
    private LeaseMonitorService leaseMonitorService;
    @Mock
    private NaverNewLandApiClient naverNewLandApiClient;
    @Mock
    private ArticleHandler articleHandler;

    @Test
    public void testCollectLeasesEveryOneMinute() {
        // given
        List<Article> firstArticleList = Arrays.asList(
            Article.builder().articleNo("firstArticle").build()
        );

        List<Article> secondArticleList = Arrays.asList(
            Article.builder().articleNo("secondArticle-1").build(),
            Article.builder().articleNo("secondArticle-2").build()
        );

        Lease firstLease = Lease.builder()
            .isMoreData(true)
            .articleList(firstArticleList)
            .build();
        Lease secondLease = Lease.builder()
            .isMoreData(false)
            .articleList(secondArticleList)
            .build();

        when(naverNewLandApiClient.findLeases(anyLong(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyInt()))
            .thenReturn(firstLease)
            .thenReturn(secondLease);

        ArgumentCaptor<List<Article>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        // when
        leaseMonitorService.collectLeasesEveryOneMinute();

        // then
        verify(naverNewLandApiClient, times(2)).findLeases(anyLong(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyInt());
        verify(articleHandler, times(1)).processArticles(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isNotNull();
        assertThat(argumentCaptor.getValue().size()).isEqualTo(3);
    }
}
