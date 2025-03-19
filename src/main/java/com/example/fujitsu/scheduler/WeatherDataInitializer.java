package com.example.fujitsu.scheduler;

import com.example.fujitsu.service.WeatherImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WeatherDataInitializer implements CommandLineRunner {

    private final WeatherImportService weatherImportService;

    public WeatherDataInitializer(WeatherImportService weatherImportService) {
        this.weatherImportService = weatherImportService;
    }

    /**
     * This method is called when the application is started.
     * It fetches the weather data from the external API.
     */
    @Override
    public void run(String... args) {
        weatherImportService.fetchWeatherData();
    }
}