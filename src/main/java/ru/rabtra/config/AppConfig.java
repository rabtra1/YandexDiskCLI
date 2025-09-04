package ru.rabtra.config;

import com.google.gson.Gson;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.rabtra")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public CloseableHttpClient client() {
        return HttpClients.createDefault();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

}
