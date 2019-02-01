package personal.ex0312.kr.lease.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.api.NaverNewLandApiClient;
import personal.ex0312.kr.lease.domain.Article;
import personal.ex0312.kr.lease.domain.Lease;
import personal.ex0312.kr.lease.domain.MonitoringJob;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class LeaseCollector {
    private final NaverNewLandApiClient naverNewLandApiClient;
    private final ArticleHandler articleHandler;
    private final ArticlePolishService articlePolishService;

    void collectLeases(List<MonitoringJob> allJobs) {
        Set<String> allAreaIdentifiers = new HashSet<>();
        allJobs.stream().map(MonitoringJob::getAreaIdentifiers).forEach(allAreaIdentifiers::addAll);

        List<Article> collectedArticles = new ArrayList<>();

        allAreaIdentifiers.forEach(areaId -> {
            int pageNumber = 0;
            Lease lease = Lease.builder().isMoreData(true).build();

            while (lease.isMoreData()) {
                pageNumber++;
                lease = naverNewLandApiClient.findLeases(areaId, "dateDesc", "APT:OPST:ABYG:OBYG:GM:OR:VL:DDDGG:JWJT:SGJT:HOJT", "B1:B2", pageNumber);
                collectedArticles.addAll(articlePolishService.polishArticles(lease.getArticleList(), areaId));
            }
            log.info("Getting articles by areaId... areaId : {}, articlesSize : {}", areaId, collectedArticles.size());
        });
        articleHandler.processArticles(allJobs, collectedArticles);
    }
}
