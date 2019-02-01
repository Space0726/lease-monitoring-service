package personal.ex0312.kr.lease.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import personal.ex0312.kr.lease.domain.MonitoringJob;
import personal.ex0312.kr.lease.repository.MonitoringJobRepository;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringJobServiceTest {
    @InjectMocks
    private MonitoringJobService monitoringJobService;
    @Mock
    private LeaseCollector leaseCollector;
    @Mock
    private MonitoringJobRepository monitoringJobRepository;

    @Test
    public void testTriggerMonitoringJob() {
        // given
        List<MonitoringJob> monitoringJobs = Collections.singletonList(
            MonitoringJob.builder().emailAddress("example0312@gmail.com").build()
        );

        when(monitoringJobRepository.findAllMonitoringJobs()).thenReturn(monitoringJobs);

        // when
        monitoringJobService.triggerMonitoringJob();

        // then
        verify(leaseCollector, times(1)).collectLeases(monitoringJobs);
    }
}
