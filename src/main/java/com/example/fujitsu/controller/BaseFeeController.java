package com.example.fujitsu.controller;

import com.example.fujitsu.model.BaseFee;
import com.example.fujitsu.service.BaseFeeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base-fees")
public class BaseFeeController {

    private final BaseFeeService baseFeeService;

    public BaseFeeController(BaseFeeService baseFeeService) {
        this.baseFeeService = baseFeeService;
    }

    /**
     * Retrieves all configured base fees in the system.
     *
     * @return List of all BaseFee objects
     */
    @GetMapping
    @Operation(summary = "Get all base fees")
    public List<BaseFee> getAllBaseFees() {
        return baseFeeService.getAllBaseFees();
    }

    /**
     * Creates a new base fee configuration.
     *
     * @param baseFee The BaseFee object to create
     * @return The created BaseFee object with generated ID
     */
    @PostMapping
    @Operation(summary = "Create a new base fee")
    public BaseFee createBaseFee(@RequestBody BaseFee baseFee) {
        return baseFeeService.createBaseFee(baseFee);
    }

    /**
     * Updates an existing base fee configuration.
     *
     * @param id The ID of the base fee to update
     * @param baseFee The updated BaseFee object
     * @return The updated BaseFee object
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a base fee")
    public BaseFee updateBaseFee(@PathVariable Long id, @RequestBody BaseFee baseFee) {
        return baseFeeService.updateBaseFee(id, baseFee);
    }

    /**
     * Deletes a base fee configuration.
     *
     * @param id The ID of the base fee to delete
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a base fee")
    public void deleteBaseFee(@PathVariable Long id) {
        baseFeeService.deleteBaseFee(id);
    }
}

