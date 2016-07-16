package com.awroby.auto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.awroby.auto.service.RaspPiInterface;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class WholeHomeAutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WholeHomeAutoApplication.class, args);
		
	}
}
