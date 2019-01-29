package personal.ex0312.kr.lease.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {
    @JsonProperty(value = "articleNo")
    private String articleId; // will be mongo unique index
    @JsonProperty(value = "articleName")
    private String buildingType; // 빌라
    @JsonProperty(value = "area1")
    private int supplyingArea; // 공급면적
    @JsonProperty(value = "area2")
    private int exclusiveUsingArea; // 전용면적
    private String direction; // 남향
    private String floorInfo; // 5/5
    private String mobileDetailLink;
    private String pcDetailLink;
    private String realtorName; // 세기부동산
    @JsonProperty(value = "dealOrWarrantPrc")
    private String warrantPrice; // "1억5,000" or "2억500" or "5억"
    @JsonProperty(value = "rentPrc")
    private String monthlyPrice; // 30
    @JsonProperty(value = "tradeTypeName")
    private String tradeType; // 전세 or 월세
    private LocalDateTime registeredAt;
}
