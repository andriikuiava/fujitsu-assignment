package com.example.fujitsu.service;

import com.example.fujitsu.model.ExtraFee;
import com.example.fujitsu.repository.ExtraFeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtraFeeService {

    private final ExtraFeeRepository extraFeeRepository;

    public ExtraFeeService(ExtraFeeRepository extraFeeRepository) {
        this.extraFeeRepository = extraFeeRepository;
    }

    /**
     * Retrieves all extra fees from the database.
     *
     * @return List of all ExtraFee objects
     */
    public List<ExtraFee> getAllExtraFees() {
        return extraFeeRepository.findAll();
    }

    /**
     * Creates a new extra fee entry in the database.
     *
     * @param extraFee The ExtraFee object to create
     * @return The created ExtraFee object with generated ID
     */
    public ExtraFee createExtraFee(ExtraFee extraFee) {
        return extraFeeRepository.save(extraFee);
    }

    /**
     * Updates an existing extra fee in the database.
     *
     * @param id The ID of the extra fee to update
     * @param newExtraFee The updated ExtraFee object
     * @return The updated ExtraFee object
     * @throws IllegalArgumentException If the specified extra fee doesn't exist
     */
    public ExtraFee updateExtraFee(Long id, ExtraFee newExtraFee) {
        return extraFeeRepository.findById(id)
                .map(extraFee -> {
                    extraFee.setConditionType(newExtraFee.getConditionType());
                    extraFee.setVehicleType(newExtraFee.getVehicleType());
                    extraFee.setFee(newExtraFee.getFee());
                    extraFee.setConditionValue(newExtraFee.getConditionValue());
                    extraFee.setCritical(newExtraFee.isCritical());
                    return extraFeeRepository.save(extraFee);
                }).orElseThrow(() -> new IllegalArgumentException("ExtraFee not found"));
    }

    /**
     * Deletes an extra fee from the database.
     *
     * @param id The ID of the extra fee to delete
     */
    public void deleteExtraFee(Long id) {
        extraFeeRepository.deleteById(id);
    }
}
