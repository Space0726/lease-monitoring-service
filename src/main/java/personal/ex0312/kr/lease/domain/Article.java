package personal.ex0312.kr.lease.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {
    private String articleNo; // will be mongo unique index
    private String articleName; // 빌라
    private int area1; // 공급면적
    private int area2; // 전용면적
    private String direction; // 남향
    private String floorInfo; // 5/5
    private String cpPcArticleUrl; // 상세정보 링크
    private String realtorName; // 세기부동산
    private String dealOrWarrantPrc; // "1억5,000"
}
