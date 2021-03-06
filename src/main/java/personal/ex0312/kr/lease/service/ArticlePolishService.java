package personal.ex0312.kr.lease.service;

import org.springframework.stereotype.Service;
import personal.ex0312.kr.lease.domain.Article;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticlePolishService {
    private final String mobileDetailLinkUri = "https://m.land.naver.com/article/info/";
    private final String pcDetailLinkUri = "https://new.land.naver.com/houses?articleNo=";

    List<Article> polishArticles(List<Article> articles, String areaIdentifier) {

        LocalDateTime now = LocalDateTime.now();

        return articles.stream()
            .peek(article -> {
                String unpolishedPrice = article.getWarrantPrice();

                article.setAreaIdentifier(areaIdentifier);
                article.setWarrantPrice(getPolishedPrice(unpolishedPrice));
                article.setRegisteredAt(now);
                article.setMobileDetailLink(mobileDetailLinkUri + article.getArticleId());
                article.setPcDetailLink(pcDetailLinkUri + article.getArticleId());
            })
            .collect(Collectors.toList());
    }

    private String getPolishedPrice(String unpolishedPrice) {
        String commaRemovedPrice = unpolishedPrice.replaceAll("[,]", "");
        String[] splitPrice = commaRemovedPrice.split("[억]");
        int price = 0;
        if (splitPrice.length != 1) {
            price += Integer.parseInt(splitPrice[0]) * 10000 + Integer.parseInt(splitPrice[1]);
        } else {
            if (unpolishedPrice.contains("억")) {
                price += Integer.parseInt(splitPrice[0]) * 10000;
            } else {
                price += Integer.parseInt(splitPrice[0]);
            }
        }
        return String.valueOf(price);
    }
}
