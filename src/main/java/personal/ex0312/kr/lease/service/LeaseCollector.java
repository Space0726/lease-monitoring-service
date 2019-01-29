package personal.ex0312.kr.lease.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.api.NaverNewLandApiClient;
import personal.ex0312.kr.lease.domain.Article;
import personal.ex0312.kr.lease.domain.Lease;
import personal.ex0312.kr.lease.domain.MonitoringJob;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class LeaseCollector {
    private final NaverNewLandApiClient naverNewLandApiClient;
    private final ArticleHandler articleHandler;

    void collectLeases(List<MonitoringJob> allJobs) {
        Set<Long> allAreaIdentifiers = new HashSet<>();
        allJobs.stream().map(MonitoringJob::getAreaIdentifiers).forEach(allAreaIdentifiers::addAll);

        Map<Long, List<Article>> articlesByAreaId = new HashMap<>();

        allAreaIdentifiers.forEach(areaId -> {
            int pageNumber = 0;
            Lease lease = Lease.builder().isMoreData(true).build();

            List<Article> gettingArticles = new ArrayList<>();

            while (lease.isMoreData()) {
                pageNumber++;
                lease = naverNewLandApiClient.findLeases(areaId, "dateDesc", "APT:OPST:ABYG:OBYG:GM:OR:VL:DDDGG:JWJT:SGJT:HOJT", "B1:B2", pageNumber);
                gettingArticles.addAll(lease.getArticleList());
            }
            articlesByAreaId.put(areaId, gettingArticles);
            log.info("Getting articles by areaId... areaId : {}, articlesSize : {}", areaId, gettingArticles.size());
        });
        articleHandler.processArticles(allJobs, articlesByAreaId);
    }
}
