package com.example.fujitsu.service;

import com.example.fujitsu.model.BaseFee;
import com.example.fujitsu.model.ExtraFee;
import com.example.fujitsu.repository.BaseFeeRepository;
import com.example.fujitsu.repository.ExtraFeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SetupService {

    private final BaseFeeRepository baseFeeRepository;
    private final ExtraFeeRepository extraFeeRepository;

    public SetupService(BaseFeeRepository baseFeeRepository, ExtraFeeRepository extraFeeRepository) {
        this.baseFeeRepository = baseFeeRepository;
        this.extraFeeRepository = extraFeeRepository;
    }

    /**
     * Resets all fees to their default values.
     * Deletes all existing base fees and extra fees, then inserts default values.
     * This operation is transactional to ensure data consistency.
     */
    @Transactional
    public void resetFees() {
        baseFeeRepository.deleteAll();
        extraFeeRepository.deleteAll();

        insertBaseFees();

        insertExtraFees();
    }

    private void insertBaseFees() {
        baseFeeRepository.save(new BaseFee("Tallinn", "Car", 4.0));
        baseFeeRepository.save(new BaseFee("Tallinn", "Scooter", 3.5));
        baseFeeRepository.save(new BaseFee("Tallinn", "Bike", 3.0));

        baseFeeRepository.save(new BaseFee("Tartu", "Car", 3.5));
        baseFeeRepository.save(new BaseFee("Tartu", "Scooter", 3.0));
        baseFeeRepository.save(new BaseFee("Tartu", "Bike", 2.5));

        baseFeeRepository.save(new BaseFee("Pärnu", "Car", 3.0));
        baseFeeRepository.save(new BaseFee("Pärnu", "Scooter", 2.5));
        baseFeeRepository.save(new BaseFee("Pärnu", "Bike", 2.0));
    }

    private void insertExtraFees() {
        // Air Temperature Extra Fees (ATEF)
        extraFeeRepository.save(new ExtraFee("AIR_TEMPERATURE", "Scooter", 1.0, "<-10", false));
        extraFeeRepository.save(new ExtraFee("AIR_TEMPERATURE", "Bike", 1.0, "<-10", false));
        extraFeeRepository.save(new ExtraFee("AIR_TEMPERATURE", "Scooter", 0.5, "-10<X<=0", false));
        extraFeeRepository.save(new ExtraFee("AIR_TEMPERATURE", "Bike", 0.5, "-10<X<=0", false));

        // Wind Speed Extra Fees (WSEF)
        extraFeeRepository.save(new ExtraFee("WIND_SPEED", "Bike", 0.5, "10<X<=20", false));
        extraFeeRepository.save(new ExtraFee("WIND_SPEED", "Bike", 0.0, ">20", true));

        // Weather Phenomenon Extra Fees (WPEF)

        // Snow or sleet → Extra fee = 1 €
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 1.0, "Light snow shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 1.0, "Moderate snow shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 1.0, "Heavy snow shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 1.0, "Light sleet", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 1.0, "Moderate sleet", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 1.0, "Light snowfall", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 1.0, "Moderate snowfall", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 1.0, "Heavy snowfall", false));

        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Light snow shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Moderate snow shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Heavy snow shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Light sleet", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Moderate sleet", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Light snowfall", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Moderate snowfall", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 1.0, "Heavy snowfall", false));

        // Rain → Extra fee = 0.5 €
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.5, "Light shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.5, "Moderate shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.5, "Heavy shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.5, "Light rain", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.5, "Moderate rain", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.5, "Heavy rain", false));

        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 0.5, "Light shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 0.5, "Moderate shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 0.5, "Heavy shower", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 0.5, "Light rain", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 0.5, "Moderate rain", false));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 0.5, "Heavy rain", false));

        // Order forbidden → Glaze, Hail
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.0, "Glaze", true));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Scooter", 0.0, "Hail", true));

        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 0.0, "Glaze", true));
        extraFeeRepository.save(new ExtraFee("WEATHER_PHENOMENON", "Bike", 0.0, "Hail", true));

    }
}
