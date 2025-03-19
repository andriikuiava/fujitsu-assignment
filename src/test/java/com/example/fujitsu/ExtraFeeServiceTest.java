package com.example.fujitsu;

import com.example.fujitsu.model.ExtraFee;
import com.example.fujitsu.repository.ExtraFeeRepository;
import com.example.fujitsu.service.ExtraFeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExtraFeeServiceTest {

    @Mock
    private ExtraFeeRepository extraFeeRepository;

    @InjectMocks
    private ExtraFeeService extraFeeService;

    @Test
    void getAllExtraFees_ReturnsAllFees() {
        ExtraFee fee1 = new ExtraFee("AIR_TEMPERATURE", "Bike", 1.0, "<-10", false);
        ExtraFee fee2 = new ExtraFee("WIND_SPEED", "Bike", 0.5, "10<X<=20", false);
        List<ExtraFee> fees = Arrays.asList(fee1, fee2);

        when(extraFeeRepository.findAll()).thenReturn(fees);

        List<ExtraFee> result = extraFeeService.getAllExtraFees();

        assertEquals(2, result.size());
        assertEquals("AIR_TEMPERATURE", result.get(0).getConditionType());
        assertEquals("WIND_SPEED", result.get(1).getConditionType());
    }

    @Test
    void createExtraFee_ShouldSaveFee() {
        ExtraFee fee = new ExtraFee("AIR_TEMPERATURE", "Bike", 1.0, "<-10", false);
        when(extraFeeRepository.save(any(ExtraFee.class))).thenReturn(fee);

        ExtraFee result = extraFeeService.createExtraFee(fee);

        assertEquals("AIR_TEMPERATURE", result.getConditionType());
        assertEquals("Bike", result.getVehicleType());
        assertEquals(1.0, result.getFee(), 0.01);
        assertEquals("<-10", result.getConditionValue());
        assertFalse(result.isCritical());
        verify(extraFeeRepository, times(1)).save(fee);
    }

    @Test
    void updateExtraFee_WhenFeeExists_UpdatesAndReturnsFee() {
        Long id = 1L;
        ExtraFee existingFee = new ExtraFee("AIR_TEMPERATURE", "Bike", 1.0, "<-10", false);
        ExtraFee updatedFee = new ExtraFee("AIR_TEMPERATURE", "Bike", 2.0, "<-15", true);

        when(extraFeeRepository.findById(id)).thenReturn(Optional.of(existingFee));
        when(extraFeeRepository.save(any(ExtraFee.class))).thenReturn(updatedFee);

        ExtraFee result = extraFeeService.updateExtraFee(id, updatedFee);

        assertEquals(2.0, result.getFee(), 0.01);
        assertEquals("<-15", result.getConditionValue());
        assertTrue(result.isCritical());
        verify(extraFeeRepository, times(1)).findById(id);
        verify(extraFeeRepository, times(1)).save(any(ExtraFee.class));
    }

    @Test
    void updateExtraFee_WhenFeeDoesNotExist_ThrowsException() {
        Long id = 1L;
        ExtraFee updatedFee = new ExtraFee("AIR_TEMPERATURE", "Bike", 2.0, "<-15", true);

        when(extraFeeRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            extraFeeService.updateExtraFee(id, updatedFee);
        });

        assertEquals("ExtraFee not found", exception.getMessage());
        verify(extraFeeRepository, times(1)).findById(id);
        verify(extraFeeRepository, never()).save(any(ExtraFee.class));
    }

    @Test
    void deleteExtraFee_ShouldDeleteFee() {
        Long id = 1L;
        doNothing().when(extraFeeRepository).deleteById(id);

        extraFeeService.deleteExtraFee(id);

        verify(extraFeeRepository, times(1)).deleteById(id);
    }
}
