package com.example.fujitsu;

import com.example.fujitsu.model.BaseFee;
import com.example.fujitsu.model.ExtraFee;
import com.example.fujitsu.model.WeatherData;
import com.example.fujitsu.repository.BaseFeeRepository;
import com.example.fujitsu.repository.ExtraFeeRepository;
import com.example.fujitsu.repository.WeatherDataRepository;
import com.example.fujitsu.service.DeliveryFeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryFeeServiceTest {

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @Mock
    private BaseFeeRepository baseFeeRepository;

    @Mock
    private ExtraFeeRepository extraFeeRepository;

    @InjectMocks
    private DeliveryFeeService deliveryFeeService;

    private WeatherData normalWeather;
    private WeatherData coldWeather;
    private WeatherData windyWeather;
    private WeatherData rainyWeather;
    private WeatherData snowyWeather;
    private WeatherData hailWeather;

    @BeforeEach
    void setUp() {
        normalWeather = new WeatherData();
        normalWeather.setStationName("Tallinn");
        normalWeather.setAirTemperature(15.0);
        normalWeather.setWindSpeed(5.0);
        normalWeather.setWeatherPhenomenon("Clear");
        normalWeather.setTimestamp(LocalDateTime.now());

        coldWeather = new WeatherData();
        coldWeather.setStationName("Tallinn");
        coldWeather.setAirTemperature(-15.0);
        coldWeather.setWindSpeed(5.0);
        coldWeather.setWeatherPhenomenon("Clear");
        coldWeather.setTimestamp(LocalDateTime.now());

        windyWeather = new WeatherData();
        windyWeather.setStationName("Tallinn");
        windyWeather.setAirTemperature(15.0);
        windyWeather.setWindSpeed(25.0);
        windyWeather.setWeatherPhenomenon("Clear");
        windyWeather.setTimestamp(LocalDateTime.now());

        rainyWeather = new WeatherData();
        rainyWeather.setStationName("Tallinn");
        rainyWeather.setAirTemperature(15.0);
        rainyWeather.setWindSpeed(5.0);
        rainyWeather.setWeatherPhenomenon("Light rain");
        rainyWeather.setTimestamp(LocalDateTime.now());

        snowyWeather = new WeatherData();
        snowyWeather.setStationName("Tallinn");
        snowyWeather.setAirTemperature(0.0);
        snowyWeather.setWindSpeed(5.0);
        snowyWeather.setWeatherPhenomenon("Light snow");
        snowyWeather.setTimestamp(LocalDateTime.now());

        hailWeather = new WeatherData();
        hailWeather.setStationName("Tallinn");
        hailWeather.setAirTemperature(15.0);
        hailWeather.setWindSpeed(5.0);
        hailWeather.setWeatherPhenomenon("Hail");
        hailWeather.setTimestamp(LocalDateTime.now());
    }

    @Test
    void calculateDeliveryFee_BaseFeeOnly_ReturnsCorrectFee() {
        BaseFee baseFee = new BaseFee("Tallinn", "Car", 4.0);
        when(baseFeeRepository.findByCityAndVehicleType("Tallinn", "Car"))
                .thenReturn(Optional.of(baseFee));
        when(weatherDataRepository.findLatestWeatherByCity("Tallinn"))
                .thenReturn(Optional.of(normalWeather));
        when(extraFeeRepository.findByVehicleType("Car"))
                .thenReturn(Collections.emptyList());

        double fee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Car");

        assertEquals(4.0, fee, 0.01);
    }

    @Test
    void calculateDeliveryFee_InvalidCityOrVehicle_ThrowsException() {
        when(baseFeeRepository.findByCityAndVehicleType("InvalidCity", "Car"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deliveryFeeService.calculateDeliveryFee("InvalidCity", "Car");
        });

        assertEquals("Invalid city or vehicle type", exception.getMessage());
    }

    @Test
    void calculateDeliveryFee_ColdWeatherBike_AppliesExtraFee() {
        BaseFee baseFee = new BaseFee("Tallinn", "Bike", 3.0);
        ExtraFee extraFee1 = new ExtraFee("AIR_TEMPERATURE", "Bike", 1.0, "<-10", false);

        when(baseFeeRepository.findByCityAndVehicleType("Tallinn", "Bike"))
                .thenReturn(Optional.of(baseFee));
        when(weatherDataRepository.findLatestWeatherByCity("Tallinn"))
                .thenReturn(Optional.of(coldWeather));
        when(extraFeeRepository.findByVehicleType("Bike"))
                .thenReturn(List.of(extraFee1));

        double fee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Bike");

        assertEquals(4.0, fee, 0.01);
    }

    @Test
    void calculateDeliveryFee_WindyWeatherBike_BlocksDelivery() {
        BaseFee baseFee = new BaseFee("Tallinn", "Bike", 3.0);
        ExtraFee extraFee = new ExtraFee("WIND_SPEED", "Bike", 0.0, ">20", true);

        when(baseFeeRepository.findByCityAndVehicleType("Tallinn", "Bike"))
                .thenReturn(Optional.of(baseFee));
        when(weatherDataRepository.findLatestWeatherByCity("Tallinn"))
                .thenReturn(Optional.of(windyWeather));
        when(extraFeeRepository.findByVehicleType("Bike"))
                .thenReturn(List.of(extraFee));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            deliveryFeeService.calculateDeliveryFee("Tallinn", "Bike");
        });

        assertEquals("Usage of selected vehicle type is forbidden", exception.getMessage());
    }

    @Test
    void calculateDeliveryFee_RainyWeatherScooter_AppliesExtraFee() {
        BaseFee baseFee = new BaseFee("Tallinn", "Scooter", 3.5);
        ExtraFee extraFee = new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.5, "Light rain", false);

        when(baseFeeRepository.findByCityAndVehicleType("Tallinn", "Scooter"))
                .thenReturn(Optional.of(baseFee));
        when(weatherDataRepository.findLatestWeatherByCity("Tallinn"))
                .thenReturn(Optional.of(rainyWeather));
        when(extraFeeRepository.findByVehicleType("Scooter"))
                .thenReturn(List.of(extraFee));

        double fee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Scooter");

        assertEquals(4.0, fee, 0.01);
    }

    @Test
    void calculateDeliveryFee_SnowyWeatherBike_AppliesExtraFee() {
        BaseFee baseFee = new BaseFee("Tallinn", "Bike", 3.0);
        ExtraFee extraFee = new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Light snow", false);

        when(baseFeeRepository.findByCityAndVehicleType("Tallinn", "Bike"))
                .thenReturn(Optional.of(baseFee));
        when(weatherDataRepository.findLatestWeatherByCity("Tallinn"))
                .thenReturn(Optional.of(snowyWeather));
        when(extraFeeRepository.findByVehicleType("Bike"))
                .thenReturn(List.of(extraFee));

        double fee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Bike");

        assertEquals(4.0, fee, 0.01);
    }

    @Test
    void calculateDeliveryFee_ThunderWeatherScooter_BlocksDelivery() {
        BaseFee baseFee = new BaseFee("Tallinn", "Scooter", 3.5);
        ExtraFee extraFee = new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.0, "Hail", true);

        when(baseFeeRepository.findByCityAndVehicleType("Tallinn", "Scooter"))
                .thenReturn(Optional.of(baseFee));
        when(weatherDataRepository.findLatestWeatherByCity("Tallinn"))
                .thenReturn(Optional.of(hailWeather));
        when(extraFeeRepository.findByVehicleType("Scooter"))
                .thenReturn(List.of(extraFee));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            deliveryFeeService.calculateDeliveryFee("Tallinn", "Scooter");
        });

        assertEquals("Usage of selected vehicle type is forbidden", exception.getMessage());
    }

    @Test
    void calculateDeliveryFee_MultipleExtraFees_AppliesAllApplicableFees() {
        BaseFee baseFee = new BaseFee("Tallinn", "Bike", 3.0);

        WeatherData coldAndSnowWeather = new WeatherData();
        coldAndSnowWeather.setStationName("Tallinn");
        coldAndSnowWeather.setAirTemperature(-5.0);
        coldAndSnowWeather.setWindSpeed(5.0);
        coldAndSnowWeather.setWeatherPhenomenon("Light snow");
        coldAndSnowWeather.setTimestamp(LocalDateTime.now());

        ExtraFee temperatureFee = new ExtraFee("AIR_TEMPERATURE", "Bike", 0.5, "-10<X<=0", false);
        ExtraFee snowFee = new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Light snow", false);

        when(baseFeeRepository.findByCityAndVehicleType("Tallinn", "Bike"))
                .thenReturn(Optional.of(baseFee));
        when(weatherDataRepository.findLatestWeatherByCity("Tallinn"))
                .thenReturn(Optional.of(coldAndSnowWeather));
        when(extraFeeRepository.findByVehicleType("Bike"))
                .thenReturn(Arrays.asList(temperatureFee, snowFee));

        double fee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Bike");

        assertEquals(4.5, fee, 0.01);
    }

    @Test
    void calculateDeliveryFee_NoWeatherData_ThrowsException() {
        BaseFee baseFee = new BaseFee("Tallinn", "Car", 4.0);
        when(baseFeeRepository.findByCityAndVehicleType("Tallinn", "Car"))
                .thenReturn(Optional.of(baseFee));
        when(weatherDataRepository.findLatestWeatherByCity("Tallinn"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            deliveryFeeService.calculateDeliveryFee("Tallinn", "Car");
        });
    }
}