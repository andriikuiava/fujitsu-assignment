package com.example.fujitsu.repository;

import com.example.fujitsu.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    /**
     * Finds the most recent weather data for a specified city.
     *
     * @param city The city name to search for (partial match is supported)
     * @return An Optional containing the latest WeatherData for the city if found
     */
    @Query("SELECT w FROM WeatherData w WHERE w.stationName LIKE %:city% ORDER BY w.timestamp DESC LIMIT 1")
    Optional<WeatherData> findLatestWeatherByCity(@Param("city") String city);
}