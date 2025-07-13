package com.example;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CalculatorTest {
    @Test
    void testAdd() {
        // Arrange: create a mock MathService
        MathService mathService = mock(MathService.class);
        when(mathService.add(2, 3)).thenReturn(5);
        Calculator calculator = new Calculator(mathService);

        // Act: call the method under test
        int result = calculator.add(2, 3);

        // Assert: verify the result and interaction
        assertEquals(5, result);
        verify(mathService).add(2, 3);
    }
}

