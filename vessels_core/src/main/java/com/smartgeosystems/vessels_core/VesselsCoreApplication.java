package com.smartgeosystems.vessels_core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class VesselsCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(VesselsCoreApplication.class, args);
    }

}
