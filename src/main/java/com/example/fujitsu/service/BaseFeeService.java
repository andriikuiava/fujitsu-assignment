package com.example.fujitsu.service;

import com.example.fujitsu.model.BaseFee;
import com.example.fujitsu.repository.BaseFeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseFeeService {

    private final BaseFeeRepository baseFeeRepository;

    public BaseFeeService(BaseFeeRepository baseFeeRepository) {
        this.baseFeeRepository = baseFeeRepository;
    }

    /**
     * Retrieves all base fees from the database.
     *
     * @return List of all BaseFee objects
     */
    public List<BaseFee> getAllBaseFees() {
        return baseFeeRepository.findAll();
    }

    /**
     * Creates a new base fee entry in the database.
     *
     * @param baseFee The BaseFee object to create
     * @return The created BaseFee object with generated ID
     */
    public BaseFee createBaseFee(BaseFee baseFee) {
        return baseFeeRepository.save(baseFee);
    }

    /**
     * Updates an existing base fee in the database.
     *
     * @param id The ID of the base fee to update
     * @param newBaseFee The updated BaseFee object
     * @return The updated BaseFee object
     * @throws IllegalArgumentException If the specified base fee doesn't exist
     */
    public BaseFee updateBaseFee(Long id, BaseFee newBaseFee) {
        return baseFeeRepository.findById(id)
                .map(baseFee -> {
                    baseFee.setCity(newBaseFee.getCity());
                    baseFee.setVehicleType(newBaseFee.getVehicleType());
                    baseFee.setFee(newBaseFee.getFee());
                    return baseFeeRepository.save(baseFee);
                }).orElseThrow(() -> new IllegalArgumentException("BaseFee not found"));
    }

    public void deleteBaseFee(Long id) {
        baseFeeRepository.deleteById(id);
    }
}
