package com.example.fujitsu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class ExtraFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conditionType;
    private String vehicleType;
    private double fee;
    private String conditionValue;
    private boolean isCritical;

    public ExtraFee() {
    }

    /**
     * Parameterized constructor for creating an ExtraFee with all fields.
     *
     * @param conditionType The type of condition (AIR_TEMPERATURE, WIND_SPEED, WEATHER_PHENOMENON)
     * @param vehicleType The vehicle type this extra fee applies to
     * @param fee The extra fee amount
     * @param conditionValue The condition value (e.g., "<-10", ">20", "Light rain")
     * @param isCritical Whether this condition is critical (prohibits delivery)
     */
    public ExtraFee(String conditionType, String vehicleType, double fee, String conditionValue, boolean isCritical) {
        this.conditionType = conditionType;
        this.vehicleType = vehicleType;
        this.fee = fee;
        this.conditionValue = conditionValue;
        this.isCritical = isCritical;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    public void setCritical(boolean critical) {
        isCritical = critical;
    }

    public String getConditionType() {
        return conditionType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public double getFee() {
        return fee;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public boolean isCritical() {
        return isCritical;
    }
}
