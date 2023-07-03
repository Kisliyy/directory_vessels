package com.smartgeosystems.directory_vessels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class DirectoryVesselsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DirectoryVesselsApplication.class, args);
    }

}
