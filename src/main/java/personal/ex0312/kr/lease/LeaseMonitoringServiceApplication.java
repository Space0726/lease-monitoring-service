package personal.ex0312.kr.lease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LeaseMonitoringServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaseMonitoringServiceApplication.class, args);
	}

}

