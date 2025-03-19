package com.example.fujitsu.controller;

import com.example.fujitsu.model.ExtraFee;
import com.example.fujitsu.service.ExtraFeeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extra-fees")
public class ExtraFeeController {

    private final ExtraFeeService extraFeeService;

    public ExtraFeeController(ExtraFeeService extraFeeService) {
        this.extraFeeService = extraFeeService;
    }

    /**
     * Retrieves all configured extra fees in the system.
     *
     * @return List of all ExtraFee objects
     */
    @GetMapping
    @Operation(summary = "Get all extra fees")
    public List<ExtraFee> getAllExtraFees() {
        return extraFeeService.getAllExtraFees();
    }

    /**
     * Creates a new extra fee configuration.
     *
     * @param extraFee The ExtraFee object to create
     * @return The created ExtraFee object with generated ID
     */
    @PostMapping
    @Operation(summary = "Create a new extra fee")
    public ExtraFee createExtraFee(@RequestBody ExtraFee extraFee) {
        return extraFeeService.createExtraFee(extraFee);
    }

    /**
     * Updates an existing extra fee configuration.
     *
     * @param id The ID of the extra fee to update
     * @param extraFee The updated ExtraFee object
     * @return The updated ExtraFee object
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an extra fee")
    public ExtraFee updateExtraFee(@PathVariable Long id, @RequestBody ExtraFee extraFee) {
        return extraFeeService.updateExtraFee(id, extraFee);
    }

    /**
     * Deletes an extra fee configuration.
     *
     * @param id The ID of the extra fee to delete
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an extra fee")
    public void deleteExtraFee(@PathVariable Long id) {
        extraFeeService.deleteExtraFee(id);
    }
}
