package ru.practicum.ewm.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "ru.practicum")
public class StatsServer {

    public static void main(String[] args) {
        SpringApplication.run(StatsServer.class, args);
    }
}