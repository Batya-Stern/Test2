package com.example;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderProcessorTest {
    @Test
    public void testPlaceOrder_SuccessfulPayment() {
        Mockery context = new Mockery();
        final PaymentService paymentService = context.mock(PaymentService.class);
        OrderProcessor processor = new OrderProcessor(paymentService);

        context.checking(new Expectations() {{
            oneOf(paymentService).processPayment(with(equal("user123")), with(equal(100.0)));
            will(returnValue(true));
        }});

        boolean result = processor.placeOrder("user123", 100.0);
        assertTrue(result);
        context.assertIsSatisfied();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlaceOrder_InvalidAmount() {
        Mockery context = new Mockery();
        final PaymentService paymentService = context.mock(PaymentService.class);
        OrderProcessor processor = new OrderProcessor(paymentService);

        processor.placeOrder("user123", -50.0);
    }

    @Test
    public void testPlaceOrder_PaymentFails() {
        Mockery context = new Mockery();
        final PaymentService paymentService = context.mock(PaymentService.class);
        OrderProcessor processor = new OrderProcessor(paymentService);

        context.checking(new Expectations() {{
            oneOf(paymentService).processPayment(with(any(String.class)), with(greaterThan(0.0)));
            will(returnValue(false));
        }});

        boolean result = processor.placeOrder("user456", 200.0);
        assertFalse(result);
        context.assertIsSatisfied();
    }

    @Test
    public void testPlaceOrder_SuccessfulPayment_Mockito() {
        PaymentService paymentService = mock(PaymentService.class);
        when(paymentService.processPayment(eq("user123"), eq(100.0))).thenReturn(true);
        OrderProcessor processor = new OrderProcessor(paymentService);

        boolean result = processor.placeOrder("user123", 100.0);
        assertTrue(result);
        verify(paymentService, times(1)).processPayment("user123", 100.0);
    }

    @Test
    public void testPlaceOrder_PaymentFails_Mockito() {
        PaymentService paymentService = mock(PaymentService.class);
        when(paymentService.processPayment(anyString(), gt(0.0))).thenReturn(false);
        OrderProcessor processor = new OrderProcessor(paymentService);

        boolean result = processor.placeOrder("user456", 200.0);
        assertFalse(result);
        verify(paymentService).processPayment("user456", 200.0);
    }

    @Test
    public void testPlaceOrder_ArgumentCaptor_Mockito() {
        PaymentService paymentService = mock(PaymentService.class);
        when(paymentService.processPayment(anyString(), anyDouble())).thenReturn(true);
        OrderProcessor processor = new OrderProcessor(paymentService);

        processor.placeOrder("captorUser", 55.5);

        ArgumentCaptor<String> accountCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> amountCaptor = ArgumentCaptor.forClass(Double.class);
        verify(paymentService).processPayment(accountCaptor.capture(), amountCaptor.capture());
        assertEquals("captorUser", accountCaptor.getValue());
        assertEquals(55.5, amountCaptor.getValue(), 0.001);
    }
}
