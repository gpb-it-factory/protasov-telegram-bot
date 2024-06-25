package ru.gpb.minibank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.gpb.minibank.config.BotProperties;

@SpringBootApplication
@EnableConfigurationProperties(BotProperties.class)
public class MinibankApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinibankApplication.class, args);
    }
}
