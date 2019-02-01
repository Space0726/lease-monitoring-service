package personal.ex0312.kr.lease.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonitoringJob {
    private String emailAddress;
    private Set<String> areaIdentifiers;
    private int minimumPrice;
    private int maximumPrice;
    private TradeType tradeType;
}
