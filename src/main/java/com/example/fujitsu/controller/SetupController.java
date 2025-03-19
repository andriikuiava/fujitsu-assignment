package com.example.fujitsu.controller;

import com.example.fujitsu.service.SetupService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setup")
public class SetupController {

    private final SetupService setupService;

    public SetupController(SetupService setupService) {
        this.setupService = setupService;
    }

    /**
     * Resets all base fees and extra fees to their default values.
     * This endpoint is used to initialize or reset the system to its default configuration.
     *
     * @return ResponseEntity with a success message
     */
    @PostMapping("/reset-fees")
    @Operation(summary = "Reset base fees and extra fees to default values mentioned in the assignment")
    public ResponseEntity<?> resetFees() {
        setupService.resetFees();
        return ResponseEntity.ok("Base fees and extra fees reset to default values");
    }
}
