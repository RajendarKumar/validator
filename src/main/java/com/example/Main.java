package com.example;

import com.example.model.Customer;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.validator.ObjectValidator;
import com.example.validator.config.ValidationConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Create test order
        Order order = new Order();
        order.setOrderId("ORD-123456");

        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        order.setCustomer(customer);

        OrderItem item = new OrderItem();
        item.setProductId("PROD-001");
        item.setQuantity(2);
        order.setItems(Arrays.asList(item));

        // Validate
        ObjectValidator validator = new ObjectValidator();
        ValidationConfig config = new ValidationConfig();

        try {
            Map<String, Object> rules = config.getRequestValidation("createOrder");
            List<Map<String, Object>> validations = 
                (List<Map<String, Object>>) rules.get("validations");
            validator.validateObject(order, validations);
            System.out.println("Validation successful!");
        } catch (Exception e) {
            System.err.println("Validation failed: " + e.getMessage());
        }
    }
} 