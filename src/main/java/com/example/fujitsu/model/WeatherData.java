package com.example.fujitsu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stationName;
    private Integer wmoCode;
    private double airTemperature;
    private double windSpeed;
    private String weatherPhenomenon;
    private LocalDateTime timestamp;

    public String getStationName() {
        return stationName;
    }

    public double getAirTemperature() {
        return airTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setWmoCode(Integer wmoCode) {
        this.wmoCode = wmoCode;
    }

    public void setAirTemperature(double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        this.weatherPhenomenon = weatherPhenomenon;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

