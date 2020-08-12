package ro.ugal.learnromanian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class LearnromanianRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnromanianRestApplication.class, args);
	}

}
