package com.main.beach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableJpaRepositories("com.main.beach.Repositories")
@SpringBootApplication
public class TakeMeToTheBeachApplication {

	public static void main(String[] args) {
		SpringApplication.run(TakeMeToTheBeachApplication.class, args);
	}

}
