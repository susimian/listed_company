package com.gcloud.listedcompany;

import com.gcloud.listedcompany.controller.Spider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListedcompanyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ListedcompanyApplication.class, args);
    }
    @Autowired
    private Spider spider;
    @Override
    public void run(String... args) throws Exception {
        spider.run();
    }
}
