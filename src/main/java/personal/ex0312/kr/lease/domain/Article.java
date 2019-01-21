package personal.ex0312.kr.lease.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {
    @JsonProperty(value = "articleNo")
    private String id; // will be mongo unique index
    @JsonProperty(value = "articleName")
    private String kind; // 빌라
    @JsonProperty(value = "area1")
    private int supplyingArea; // 공급면적
    @JsonProperty(value = "area2")
    private int exclusiveUsingArea; // 전용면적
    private String direction; // 남향
    private String floorInfo; // 5/5
    @JsonProperty(value = "cpPcArticleUrl")
    private String detailLink; // 상세정보 링크
    private String realtorName; // 세기부동산
    @JsonProperty(value = "dealOrWarrantPrc")
    private String price; // "1억5,000"
}
