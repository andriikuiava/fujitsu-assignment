package com.example.fujitsu;

import com.example.fujitsu.controller.DeliveryFeeController;
import com.example.fujitsu.service.DeliveryFeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryFeeControllerTest {

    @Mock
    private DeliveryFeeService deliveryFeeService;

    @InjectMocks
    private DeliveryFeeController deliveryFeeController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetDeliveryFee_Success() {
        String city = "Tallinn";
        String vehicleType = "Car";
        double expectedFee = 4.5;

        when(deliveryFeeService.calculateDeliveryFee(city, vehicleType)).thenReturn(expectedFee);

        ResponseEntity<?> response = deliveryFeeController.getDeliveryFee(city, vehicleType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(expectedFee, responseBody.get("delivery_fee"));

        verify(deliveryFeeService, times(1)).calculateDeliveryFee(city, vehicleType);
    }

    @Test
    void testGetDeliveryFee_InvalidInput() {
        String city = "NonExistentCity";
        String vehicleType = "Car";
        String errorMessage = "Invalid city or vehicle type";

        when(deliveryFeeService.calculateDeliveryFee(city, vehicleType))
                .thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<?> response = deliveryFeeController.getDeliveryFee(city, vehicleType);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(errorMessage, responseBody.get("error"));

        verify(deliveryFeeService, times(1)).calculateDeliveryFee(city, vehicleType);
    }

    @Test
    void testGetDeliveryFee_CriticalWeather() {
        String city = "Tallinn";
        String vehicleType = "Bike";
        String errorMessage = "Usage of selected vehicle type is forbidden";

        when(deliveryFeeService.calculateDeliveryFee(city, vehicleType))
                .thenThrow(new IllegalStateException(errorMessage));

        ResponseEntity<?> response = deliveryFeeController.getDeliveryFee(city, vehicleType);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(errorMessage, responseBody.get("error"));

        verify(deliveryFeeService, times(1)).calculateDeliveryFee(city, vehicleType);
    }

    @Test
    void testGetDeliveryFee_EdgeCases() {
        String city = "";
        String vehicleType = "";

        when(deliveryFeeService.calculateDeliveryFee(city, vehicleType))
                .thenThrow(new IllegalArgumentException("Invalid city or vehicle type"));

        ResponseEntity<?> response = deliveryFeeController.getDeliveryFee(city, vehicleType);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(deliveryFeeService, times(1)).calculateDeliveryFee(city, vehicleType);
    }

    @Test
    void testGetDeliveryFee_ValidButLowercaseInput() {
        String city = "tallinn";
        String vehicleType = "car";
        double expectedFee = 4.0;

        when(deliveryFeeService.calculateDeliveryFee(city, vehicleType)).thenReturn(expectedFee);

        ResponseEntity<?> response = deliveryFeeController.getDeliveryFee(city, vehicleType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(expectedFee, responseBody.get("delivery_fee"));

        verify(deliveryFeeService, times(1)).calculateDeliveryFee(city, vehicleType);
    }
}