package personal.ex0312.kr.lease.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.api.NaverNewLandApiClient;

@Service
@AllArgsConstructor
@Slf4j
public class LeaseMonitorService {
    private final NaverNewLandApiClient naverNewLandApiClient;

    @Scheduled(initialDelay = 5000L, fixedDelay = 60000L)
    public void collectLeasesEveryOneMinute() {
        String leases = naverNewLandApiClient.findLeases(1132010800, "dateDesc", "VL:DDDGG:JWJT", "B1", 10000, 15000, 1);
        log.info(leases);
    }
}
