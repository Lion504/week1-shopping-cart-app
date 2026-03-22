package com.shoppingcart;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartAppTest {

    @Test
    void testCalculateItemCost() {
        double price = 10.50;
        int quantity = 3;
        double expected = 31.50;
        
        double result = ShoppingCartApp.calculateItemCost(price, quantity);
        
        assertEquals(expected, result, 0.001);
    }

    @Test
    void testCalculateItemCostWithZeroQuantity() {
        double price = 10.00;
        int quantity = 0;
        double expected = 0.0;
        
        double result = ShoppingCartApp.calculateItemCost(price, quantity);
        
        assertEquals(expected, result, 0.001);
    }

    @Test
    void testCalculateItemCostWithDecimals() {
        double price = 7.99;
        int quantity = 2;
        double expected = 15.98;
        
        double result = ShoppingCartApp.calculateItemCost(price, quantity);
        
        assertEquals(expected, result, 0.001);
    }

    @Test
    void testCalculateTotalCost() {
        List<Double> itemCosts = Arrays.asList(10.00, 20.00, 30.00);
        double expected = 60.00;
        
        double result = ShoppingCartApp.calculateTotalCost(itemCosts);
        
        assertEquals(expected, result, 0.001);
    }

    @Test
    void testCalculateTotalCostWithEmptyList() {
        List<Double> itemCosts = Arrays.asList();
        double expected = 0.0;
        
        double result = ShoppingCartApp.calculateTotalCost(itemCosts);
        
        assertEquals(expected, result, 0.001);
    }

    @Test
    void testCalculateTotalCostWithSingleItem() {
        List<Double> itemCosts = Arrays.asList(25.50);
        double expected = 25.50;
        
        double result = ShoppingCartApp.calculateTotalCost(itemCosts);
        
        assertEquals(expected, result, 0.001);
    }

    @Test
    void testCalculateTotalCostWithDecimals() {
        List<Double> itemCosts = Arrays.asList(10.99, 5.50, 3.51);
        double expected = 20.00;
        
        double result = ShoppingCartApp.calculateTotalCost(itemCosts);
        
        assertEquals(expected, result, 0.001);
    }
}
