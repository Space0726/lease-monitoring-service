package personal.ex0312.kr.lease.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import personal.ex0312.kr.lease.domain.MonitoringJob;
import personal.ex0312.kr.lease.service.MonitoringJobService;

import java.util.List;

@RestController
@AllArgsConstructor
public class MonitoringJobController {
    private final MonitoringJobService monitoringJobService;

    @GetMapping("/monitoring-jobs")
    public List<MonitoringJob> findAllMonitoringJobs() {
        return monitoringJobService.findAllMonitoringJobs();
    }

    @GetMapping("/monitoring-jobs/by-email-address/{emailAddress}")
    public MonitoringJob findMonitoringJobByEmailAddress(@PathVariable String emailAddress) {
        return monitoringJobService.findMonitoringJobByEmailAddress(emailAddress);
    }

    @PostMapping("/monitoring-jobs")
    public void insertMonitoringJob(@RequestBody MonitoringJob monitoringJob) {
        monitoringJobService.insertMonitoringJob(monitoringJob);
    }
}
