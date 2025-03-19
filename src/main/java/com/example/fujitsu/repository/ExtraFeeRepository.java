package com.example.fujitsu.repository;

import com.example.fujitsu.model.ExtraFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraFeeRepository extends JpaRepository<ExtraFee, Long> {
    List<ExtraFee> findByVehicleType(String vehicleType);
}
