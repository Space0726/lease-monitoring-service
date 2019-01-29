package personal.ex0312.kr.lease.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonitoringJob {
    private String emailAddress;
    private long areaIdentifier;
    private int minimumPrice;
    private int maximumPrice;
    private String tradeType;
}
