package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.practicum")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}