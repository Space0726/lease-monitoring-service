package personal.ex0312.kr.lease.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import personal.ex0312.kr.lease.domain.Lease;

@FeignClient(name = "naverNewLand", url = "${apiUrls.naver.land}")
public interface NaverNewLandApiClient {
    @GetMapping("/articles")
    Lease findLeases(@RequestParam("cortarNo") long areaIdentifier,
                     @RequestParam("order") String order,
                     @RequestParam("realEstateType") String buildingType,
                     @RequestParam("tradeType") String tradeType,
                     @RequestParam("page") int pageNumber);
}
