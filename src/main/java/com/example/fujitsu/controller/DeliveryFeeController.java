package com.example.fujitsu.controller;

import com.example.fujitsu.service.DeliveryFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/delivery-fee")
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;

    public DeliveryFeeController(DeliveryFeeService deliveryFeeService) {
        this.deliveryFeeService = deliveryFeeService;
    }

    /**
     * Calculates the delivery fee for a given city and vehicle type based on current weather conditions.
     *
     * @param city The city for delivery (Tallinn, Tartu, or Pärnu)
     * @param vehicleType The vehicle type for delivery (Car, Scooter, or Bike)
     * @return ResponseEntity containing the calculated delivery fee or an error message
     */
    @GetMapping
    @Operation(summary = "Get delivery fee for a given city and vehicle type")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Delivery fee calculated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid city or vehicle type"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "Service unavailable due to the weather")
    })
    public ResponseEntity<?> getDeliveryFee(@RequestParam String city, @RequestParam String vehicleType) {
        try {
            double fee = deliveryFeeService.calculateDeliveryFee(city, vehicleType);
            return ResponseEntity.ok(Map.of("delivery_fee", fee));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(503).body(Map.of("error", e.getMessage()));
        }
    }
}