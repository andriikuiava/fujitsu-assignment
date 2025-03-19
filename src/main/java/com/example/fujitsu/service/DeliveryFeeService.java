package com.example.fujitsu.service;

import com.example.fujitsu.model.BaseFee;
import com.example.fujitsu.model.ExtraFee;
import com.example.fujitsu.model.WeatherData;
import com.example.fujitsu.repository.BaseFeeRepository;
import com.example.fujitsu.repository.ExtraFeeRepository;
import com.example.fujitsu.repository.WeatherDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryFeeService {

    private final WeatherDataRepository weatherDataRepository;
    private final BaseFeeRepository baseFeeRepository;
    private final ExtraFeeRepository extraFeeRepository;

    public DeliveryFeeService(WeatherDataRepository weatherDataRepository, BaseFeeRepository baseFeeRepository,
                              ExtraFeeRepository extraFeeRepository) {
        this.weatherDataRepository = weatherDataRepository;
        this.baseFeeRepository = baseFeeRepository;
        this.extraFeeRepository = extraFeeRepository;
    }

    /**
     * Calculates the delivery fee for a given city and vehicle type based on current weather conditions.
     * The calculation considers the base fee for the city/vehicle combination and any applicable extra fees
     * based on current weather conditions.
     *
     * @param city The city for delivery (Tallinn, Tartu, or Pärnu)
     * @param vehicleType The vehicle type for delivery (Car, Scooter, or Bike)
     * @return The calculated delivery fee
     * @throws IllegalArgumentException If the city or vehicle type is invalid
     * @throws IllegalStateException If weather data is not available or if a critical weather condition prohibits delivery
     */
    public double calculateDeliveryFee(String city, String vehicleType) {
        BaseFee baseFee = baseFeeRepository.findByCityAndVehicleType(city, vehicleType)
                .orElseThrow(() -> new IllegalArgumentException("Invalid city or vehicle type"));

        double fee = baseFee.getFee();

        Optional<WeatherData> latestWeatherOpt = weatherDataRepository.findLatestWeatherByCity(city);
        WeatherData weather = latestWeatherOpt.orElseThrow(() -> new IllegalStateException("Weather data not found"));

        List<ExtraFee> extraFees = extraFeeRepository.findByVehicleType(vehicleType);
        for (ExtraFee extraFee : extraFees) {
            if (appliesToWeather(extraFee, weather)) {
                if (extraFee.isCritical()) {
                    throw new IllegalStateException("Usage of selected vehicle type is forbidden");
                }
                fee += extraFee.getFee();
            }
        }

        return fee;
    }

    private boolean appliesToWeather(ExtraFee extraFee, WeatherData weather) {
        return switch (extraFee.getConditionType()) {
            case "AIR_TEMPERATURE" -> matchesCondition(weather.getAirTemperature(), extraFee.getConditionValue());
            case "WIND_SPEED" -> matchesCondition(weather.getWindSpeed(), extraFee.getConditionValue());
            case "WEATHER_PHENOMENON" -> weather.getWeatherPhenomenon() != null &&
                    weather.getWeatherPhenomenon().equals(extraFee.getConditionValue());
            default -> false;
        };
    }

    private boolean matchesCondition(double value, String condition) {
        condition = condition.replaceAll("\\s+", "");

        try {
            if (condition.matches(".*<X<=.*")) {
                String[] parts = condition.split("<X<=");
                if (parts.length == 2) {
                    double lowerBound = Double.parseDouble(parts[0]);
                    double upperBound = Double.parseDouble(parts[1]);
                    return value > lowerBound && value <= upperBound;
                }
            } else if (condition.matches(".*<=X<=.*")) {
                String[] parts = condition.split("<=X<=");
                if (parts.length == 2) {
                    double lowerBound = Double.parseDouble(parts[0]);
                    double upperBound = Double.parseDouble(parts[1]);
                    return value >= lowerBound && value <= upperBound;
                }
            } else if (condition.matches(".*<=X<.*")) {
                String[] parts = condition.split("<=X<");
                if (parts.length == 2) {
                    double lowerBound = Double.parseDouble(parts[0]);
                    double upperBound = Double.parseDouble(parts[1]);
                    return value >= lowerBound && value < upperBound;
                }
            } else if (condition.matches(".*<X<.*")) {
                String[] parts = condition.split("<X<");
                if (parts.length == 2) {
                    double lowerBound = Double.parseDouble(parts[0]);
                    double upperBound = Double.parseDouble(parts[1]);
                    return value > lowerBound && value < upperBound;
                }
            } else if (condition.contains("<=")) {
                double threshold = Double.parseDouble(condition.replace("<=", ""));
                return value <= threshold;
            } else if (condition.contains(">=")) {
                double threshold = Double.parseDouble(condition.replace(">=", ""));
                return value >= threshold;
            } else if (condition.contains("<")) {
                double threshold = Double.parseDouble(condition.replace("<", ""));
                return value < threshold;
            } else if (condition.contains(">")) {
                double threshold = Double.parseDouble(condition.replace(">", ""));
                return value > threshold;
            } else if (condition.matches("-?\\d+(\\.\\d+)?")) {
                return value == Double.parseDouble(condition);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid condition format: " + condition);
        }

        return false;
    }
}