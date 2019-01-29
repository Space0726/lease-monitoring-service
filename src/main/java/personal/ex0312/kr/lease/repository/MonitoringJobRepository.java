package personal.ex0312.kr.lease.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import personal.ex0312.kr.lease.domain.MonitoringJob;

import java.util.List;

@Repository
@AllArgsConstructor
public class MonitoringJobRepository {
    private final MongoTemplate mongoTemplate;

    public List<MonitoringJob> findAllMonitoringJobs() {
        return mongoTemplate.findAll(MonitoringJob.class);
    }

    public MonitoringJob findMonitoringJobByEmailAddress(String emailAddress) {
        return mongoTemplate.findOne(Query.query(Criteria.where("emailAddress").is(emailAddress)), MonitoringJob.class);
    }

    public void insertMonitoringJob(MonitoringJob monitoringJob) {
        mongoTemplate.insert(monitoringJob);
    }
}
