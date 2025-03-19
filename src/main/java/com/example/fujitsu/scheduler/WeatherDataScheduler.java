package com.example.fujitsu.scheduler;

import com.example.fujitsu.service.WeatherImportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherDataScheduler {

    private final WeatherImportService weatherImportService;

    public WeatherDataScheduler(WeatherImportService weatherImportService) {
        this.weatherImportService = weatherImportService;
    }
    /**
     * Schedules the fetching of weather data from the Estonian Weather Service API.
     * The cron expression is defined in the application.properties file.
     */
    @Scheduled(cron = "${weather.cron}")
    public void scheduleWeatherImport() {
        weatherImportService.fetchWeatherData();
    }
}
