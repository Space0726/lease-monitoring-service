package personal.ex0312.kr.lease.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.api.NaverNewLandApiClient;
import personal.ex0312.kr.lease.domain.Article;
import personal.ex0312.kr.lease.domain.Lease;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LeaseMonitorService {
    private final NaverNewLandApiClient naverNewLandApiClient;
    private final ArticleHandler articleHandler;
    private final ArticlePolishService articlePolishService;

    @Scheduled(initialDelay = 5000L, fixedDelay = 60000L)
    public void collectLeasesEveryOneMinute() {
        int pageNumber = 0;
        Lease lease = Lease.builder().isMoreData(true).build();

        List<Article> articlesFromNaver = new ArrayList<>();

        while (lease.isMoreData()) {
            pageNumber++;
            lease = naverNewLandApiClient.findLeases(1132010800, "dateDesc", "VL:DDDGG:JWJT", "B1", 10000, 15000, pageNumber);
            articlesFromNaver.addAll(lease.getArticleList());
            log.info("Getting lease from naver: {}", lease);
        }
        log.info("The total number of Articles : {}", articlesFromNaver.size());

        List<Article> polishedArticles = articlePolishService.polishArticles(articlesFromNaver);
        articleHandler.processArticles(polishedArticles);
    }
}
