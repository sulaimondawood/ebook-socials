package com.dawood.ebook_network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EbookSocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbookSocialNetworkApplication.class, args);
	}

}
