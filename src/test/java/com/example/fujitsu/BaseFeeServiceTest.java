package com.example.fujitsu;

import com.example.fujitsu.model.BaseFee;
import com.example.fujitsu.repository.BaseFeeRepository;
import com.example.fujitsu.service.BaseFeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BaseFeeServiceTest {

    @Mock
    private BaseFeeRepository baseFeeRepository;

    @InjectMocks
    private BaseFeeService baseFeeService;

    @Test
    void getAllBaseFees_ReturnsAllFees() {
        BaseFee fee1 = new BaseFee("Tallinn", "Car", 4.0);
        BaseFee fee2 = new BaseFee("Tartu", "Car", 3.5);
        List<BaseFee> fees = Arrays.asList(fee1, fee2);

        when(baseFeeRepository.findAll()).thenReturn(fees);

        List<BaseFee> result = baseFeeService.getAllBaseFees();

        assertEquals(2, result.size());
        assertEquals("Tallinn", result.get(0).getCity());
        assertEquals("Tartu", result.get(1).getCity());
    }

    @Test
    void createBaseFee_ShouldSaveFee() {
        BaseFee fee = new BaseFee("Tallinn", "Car", 4.0);
        when(baseFeeRepository.save(any(BaseFee.class))).thenReturn(fee);

        BaseFee result = baseFeeService.createBaseFee(fee);

        assertEquals("Tallinn", result.getCity());
        assertEquals("Car", result.getVehicleType());
        assertEquals(4.0, result.getFee(), 0.01);
        verify(baseFeeRepository, times(1)).save(fee);
    }

    @Test
    void updateBaseFee_WhenFeeExists_UpdatesAndReturnsFee() {
        Long id = 1L;
        BaseFee existingFee = new BaseFee("Tallinn", "Car", 4.0);
        BaseFee updatedFee = new BaseFee("Tallinn", "Car", 5.0);

        when(baseFeeRepository.findById(id)).thenReturn(Optional.of(existingFee));
        when(baseFeeRepository.save(any(BaseFee.class))).thenReturn(updatedFee);

        BaseFee result = baseFeeService.updateBaseFee(id, updatedFee);

        assertEquals(5.0, result.getFee(), 0.01);
        verify(baseFeeRepository, times(1)).findById(id);
        verify(baseFeeRepository, times(1)).save(any(BaseFee.class));
    }

    @Test
    void updateBaseFee_WhenFeeDoesNotExist_ThrowsException() {
        Long id = 1L;
        BaseFee updatedFee = new BaseFee("Tallinn", "Car", 5.0);

        when(baseFeeRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            baseFeeService.updateBaseFee(id, updatedFee);
        });

        assertEquals("BaseFee not found", exception.getMessage());
        verify(baseFeeRepository, times(1)).findById(id);
        verify(baseFeeRepository, never()).save(any(BaseFee.class));
    }

    @Test
    void deleteBaseFee_ShouldDeleteFee() {
        Long id = 1L;
        doNothing().when(baseFeeRepository).deleteById(id);

        baseFeeService.deleteBaseFee(id);

        verify(baseFeeRepository, times(1)).deleteById(id);
    }
}