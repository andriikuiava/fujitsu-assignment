package com.example.fujitsu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class BaseFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String vehicleType;
    private double fee;

    public BaseFee() {
    }

    /**
     * Parameterized constructor for creating a BaseFee with all fields.
     *
     * @param city The city name
     * @param vehicleType The vehicle type
     * @param fee The base fee amount
     */
    public BaseFee(String city, String vehicleType, double fee) {
        this.city = city;
        this.vehicleType = vehicleType;
        this.fee = fee;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getCity() {
        return city;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public double getFee() {
        return fee;
    }
}

