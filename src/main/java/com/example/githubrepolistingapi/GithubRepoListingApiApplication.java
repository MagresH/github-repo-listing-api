package com.example.githubrepolistingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class GithubRepoListingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubRepoListingApiApplication.class, args);}


}
