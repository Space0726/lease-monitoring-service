package personal.ex0312.kr.lease.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.domain.MonitoringJob;
import personal.ex0312.kr.lease.repository.MonitoringJobRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class MonitoringJobService {
    private final MonitoringJobRepository monitoringJobRepository;
    private final LeaseCollector leaseCollector;

    public List<MonitoringJob> findAllMonitoringJobs() {
        return monitoringJobRepository.findAllMonitoringJobs();
    }

    public MonitoringJob findMonitoringJobByEmailAddress(String emailAddress) {
        return monitoringJobRepository.findMonitoringJobByEmailAddress(emailAddress);
    }

    public void insertMonitoringJob(MonitoringJob monitoringJob) {
        monitoringJobRepository.insertMonitoringJob(monitoringJob);
    }

    @Scheduled(initialDelay = 5000L, fixedDelay = 60000L)
    public void triggerMonitoringJob() {
        List<MonitoringJob> allJobs = this.findAllMonitoringJobs();
        leaseCollector.collectLeases(allJobs);
    }
}
