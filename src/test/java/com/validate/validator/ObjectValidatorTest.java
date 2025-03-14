package com.validate.validator;

import com.validate.model.Customer;
import com.validate.model.Order;
import com.validate.model.OrderItem;
import com.validate.model.ShippingAddress;
import com.validate.validator.config.ValidationConfig;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ObjectValidatorTest {
    private ObjectValidator validator;
    private ValidationConfig config;
    private Order order;
    private List<Map<String, Object>> validations;

    @BeforeEach
    void setUp() {
        validator = new ObjectValidator();
        config = new ValidationConfig();
        order = createValidOrder();
        validations = createValidations();
    }

    @Test
    void shouldPassValidation_WhenAllFieldsValid() {
        assertDoesNotThrow(() -> 
            validator.validateObject(order, validations)
        );
    }

    @Test
    void shouldThrowException_WhenOrderIdInvalid() {
        order.setOrderId("INVALID");
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("orderId"));
    }

    @Test
    void shouldThrowException_WhenCustomerEmailInvalid() {
        order.getCustomer().setEmail("invalid-email");
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    void shouldThrowException_WhenItemQuantityInvalid() {
        order.getItems().get(0).setQuantity(0);
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("quantity"));
    }

    private Order createValidOrder() {
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

        return order;
    }

    private List<Map<String, Object>> createValidations() {
        List<Map<String, Object>> validations = new ArrayList<>();

        // 1. Order ID validation
        Map<String, Object> orderIdValidation = new HashMap<>();
        orderIdValidation.put("field", "orderId");
        List<Map<String, Object>> orderIdRules = new ArrayList<>();
        
        Map<String, Object> orderIdRequired = new HashMap<>();
        orderIdRequired.put("type", "required");
        
        Map<String, Object> orderIdPattern = new HashMap<>();
        orderIdPattern.put("type", "pattern");
        orderIdPattern.put("value", "^ORD-[0-9]{6}$");
        orderIdPattern.put("message", "must match format ORD-XXXXXX where X is a digit");
        
        Map<String, Object> orderIdMaxLength = new HashMap<>();
        orderIdMaxLength.put("type", "maxLength");
        orderIdMaxLength.put("value", 10);
        
        orderIdRules.add(orderIdRequired);
        orderIdRules.add(orderIdPattern);
        orderIdRules.add(orderIdMaxLength);
        orderIdValidation.put("rules", orderIdRules);
        validations.add(orderIdValidation);

        // 2. Customer object validation
        Map<String, Object> customerValidation = new HashMap<>();
        customerValidation.put("field", "customer");
        customerValidation.put("type", "object");
        
        List<Map<String, Object>> customerRules = new ArrayList<>();
        Map<String, Object> customerRequired = new HashMap<>();
        customerRequired.put("type", "required");
        customerRules.add(customerRequired);
        customerValidation.put("rules", customerRules);

        // 2.1 Customer fields validation
        List<Map<String, Object>> customerFields = new ArrayList<>();

        // 2.1.1 Customer name validation
        Map<String, Object> nameValidation = new HashMap<>();
        nameValidation.put("field", "name");
        List<Map<String, Object>> nameRules = new ArrayList<>();
        
        Map<String, Object> nameRequired = new HashMap<>();
        nameRequired.put("type", "required");
        
        Map<String, Object> nameMinLength = new HashMap<>();
        nameMinLength.put("type", "minLength");
        nameMinLength.put("value", 2);
        
        Map<String, Object> nameMaxLength = new HashMap<>();
        nameMaxLength.put("type", "maxLength");
        nameMaxLength.put("value", 50);
        
        nameRules.add(nameRequired);
        nameRules.add(nameMinLength);
        nameRules.add(nameMaxLength);
        nameValidation.put("rules", nameRules);
        customerFields.add(nameValidation);

        // 2.1.2 Customer email validation
        Map<String, Object> emailValidation = new HashMap<>();
        emailValidation.put("field", "email");
        List<Map<String, Object>> emailRules = new ArrayList<>();
        
        Map<String, Object> emailRequired = new HashMap<>();
        emailRequired.put("type", "required");
        
        Map<String, Object> emailPattern = new HashMap<>();
        emailPattern.put("type", "pattern");
        emailPattern.put("value", "^[A-Za-z0-9+_.-]+@(.+)$");
        emailPattern.put("message", "must be a valid email address");
        
        Map<String, Object> emailMaxLength = new HashMap<>();
        emailMaxLength.put("type", "maxLength");
        emailMaxLength.put("value", 100);
        
        emailRules.add(emailRequired);
        emailRules.add(emailPattern);
        emailRules.add(emailMaxLength);
        emailValidation.put("rules", emailRules);
        customerFields.add(emailValidation);

        customerValidation.put("fields", customerFields);
        validations.add(customerValidation);

        // 3. Items list validation
        Map<String, Object> itemsValidation = new HashMap<>();
        itemsValidation.put("field", "items");
        itemsValidation.put("type", "list");
        
        List<Map<String, Object>> itemsRules = new ArrayList<>();
        Map<String, Object> itemsRequired = new HashMap<>();
        itemsRequired.put("type", "required");
        itemsRules.add(itemsRequired);
        itemsValidation.put("rules", itemsRules);

        // 3.1 Item validations
        List<Map<String, Object>> itemValidations = new ArrayList<>();

        // 3.1.1 Product ID validation
        Map<String, Object> productIdValidation = new HashMap<>();
        productIdValidation.put("field", "productId");
        List<Map<String, Object>> productIdRules = new ArrayList<>();
        
        Map<String, Object> productIdRequired = new HashMap<>();
        productIdRequired.put("type", "required");
        
        Map<String, Object> productIdMaxLength = new HashMap<>();
        productIdMaxLength.put("type", "maxLength");
        productIdMaxLength.put("value", 20);
        
        productIdRules.add(productIdRequired);
        productIdRules.add(productIdMaxLength);
        productIdValidation.put("rules", productIdRules);
        itemValidations.add(productIdValidation);

        // 3.1.2 Quantity validation
        Map<String, Object> quantityValidation = new HashMap<>();
        quantityValidation.put("field", "quantity");
        List<Map<String, Object>> quantityRules = new ArrayList<>();
        
        Map<String, Object> quantityRequired = new HashMap<>();
        quantityRequired.put("type", "required");
        
        Map<String, Object> quantityMin = new HashMap<>();
        quantityMin.put("type", "min");
        quantityMin.put("value", 1);
        
        quantityRules.add(quantityRequired);
        quantityRules.add(quantityMin);
        quantityValidation.put("rules", quantityRules);
        itemValidations.add(quantityValidation);

        itemsValidation.put("itemValidations", itemValidations);
        validations.add(itemsValidation);

        return validations;
    }

    // Add more specific test cases for each validation rule
    @Test
    void shouldThrowException_WhenCustomerNameTooShort() {
        order.getCustomer().setName("A");
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("name must be at least 2 characters"));
    }

    @Test
    void shouldThrowException_WhenCustomerNameTooLong() {
        order.getCustomer().setName("A".repeat(51));
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("name must not exceed 50 characters"));
    }

    @Test
    void shouldThrowException_WhenEmailInvalid() {
        order.getCustomer().setEmail("invalid-email");
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("must be a valid email address"));
    }

    @Test
    void shouldThrowException_WhenQuantityZero() {
        order.getItems().get(0).setQuantity(0);
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("quantity must be at least 1"));
    }

    @Test
    void shouldThrowException_WhenProductIdTooLong() {
        order.getItems().get(0).setProductId("A".repeat(21));
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("productId must not exceed 20 characters"));
    }

    @Test
    void shouldThrowException_WhenItemsListEmpty() {
        order.setItems(Collections.emptyList());
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("Required list cannot be empty"));
    }

    @Test
    void shouldThrowException_WhenCustomerNull() {
        order.setCustomer(null);
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertTrue(exception.getMessage().contains("Required nested object cannot be null"));
    }

    @Test
    void shouldPassValidation_WhenOptionalNestedObjectIsNull() {
        // Create validation rules for optional nested object
        List<Map<String, Object>> validations = new ArrayList<>();
        Map<String, Object> optionalObjectValidation = new HashMap<>();
        optionalObjectValidation.put("field", "customer");
        optionalObjectValidation.put("type", "object");
        
        // No required rule
        List<Map<String, Object>> rules = new ArrayList<>();
        optionalObjectValidation.put("rules", rules);
        
        optionalObjectValidation.put("fields", createCustomerFields());
        validations.add(optionalObjectValidation);

        // Set customer to null
        order.setCustomer(null);

        // Should not throw exception
        assertDoesNotThrow(() -> 
            validator.validateObject(order, validations)
        );
    }

    @Test
    void shouldThrowException_WhenRequiredNestedObjectIsNull() {
        // Create validation rules for required nested object
        List<Map<String, Object>> validations = new ArrayList<>();
        Map<String, Object> requiredObjectValidation = new HashMap<>();
        requiredObjectValidation.put("field", "customer");
        requiredObjectValidation.put("type", "object");
        
        // Add required rule
        List<Map<String, Object>> rules = new ArrayList<>();
        Map<String, Object> requiredRule = new HashMap<>();
        requiredRule.put("type", "required");
        rules.add(requiredRule);
        requiredObjectValidation.put("rules", rules);
        
        requiredObjectValidation.put("fields", createCustomerFields());
        validations.add(requiredObjectValidation);

        // Set customer to null
        order.setCustomer(null);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(order, validations)
        );
        assertEquals("Required nested object cannot be null", exception.getMessage());
    }

    @Test
    void shouldPassValidation_WhenOptionalListIsNull() {
        // Create validation rules for optional list
        List<Map<String, Object>> validations = new ArrayList<>();
        Map<String, Object> optionalListValidation = new HashMap<>();
        optionalListValidation.put("field", "items");
        optionalListValidation.put("type", "list");
        
        // No required rule
        List<Map<String, Object>> rules = new ArrayList<>();
        optionalListValidation.put("rules", rules);
        
        optionalListValidation.put("itemValidations", createItemValidations());
        validations.add(optionalListValidation);

        // Set items to null
        order.setItems(null);

        // Should not throw exception
        assertDoesNotThrow(() -> 
            validator.validateObject(order, validations)
        );
    }

    @Test
    void shouldValidateCustomerFields() {
        List<Map<String, Object>> customerFields = createCustomerFields();
        
        // Test valid customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        
        assertDoesNotThrow(() -> 
            validator.validateObject(customer, customerFields)
        );
        
        // Test invalid email
        customer.setEmail("invalid-email");
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(customer, customerFields)
        );
        assertTrue(exception.getMessage().contains("must be a valid email address"));
    }

    @Test
    void shouldValidateOrderItem() {
        List<Map<String, Object>> itemValidations = createItemValidations();
        
        // Test valid item
        OrderItem item = new OrderItem();
        item.setProductId("PROD-001");
        item.setQuantity(2);
        
        assertDoesNotThrow(() -> 
            validator.validateObject(item, itemValidations)
        );
        
        // Test invalid quantity
        item.setQuantity(0);
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(item, itemValidations)
        );
        assertTrue(exception.getMessage().contains("quantity must be at least 1"));
    }

    @Test
    void shouldValidateCompleteOrder() {
        List<Map<String, Object>> validations = new ArrayList<>();
        
        // Add order ID validation
        List<Map<String, Object>> orderIdRules = new ArrayList<>();
        orderIdRules.add(createRule("required", null, null));
        orderIdRules.add(createRule("pattern", "^ORD-[0-9]{6}$", 
            "must match format ORD-XXXXXX where X is a digit"));
        validations.add(createFieldValidation("orderId", orderIdRules));
        
        // Add customer validation
        Map<String, Object> customerValidation = new HashMap<>();
        customerValidation.put("field", "customer");
        customerValidation.put("type", "object");
        customerValidation.put("rules", Collections.singletonList(createRule("required", null, null)));
        customerValidation.put("fields", createCustomerFields());
        validations.add(customerValidation);
        
        // Add items validation
        Map<String, Object> itemsValidation = new HashMap<>();
        itemsValidation.put("field", "items");
        itemsValidation.put("type", "list");
        itemsValidation.put("rules", Collections.singletonList(createRule("required", null, null)));
        itemsValidation.put("itemValidations", createItemValidations());
        validations.add(itemsValidation);
        
        // Test valid order
        Order order = createValidOrder();
        assertDoesNotThrow(() -> 
            validator.validateObject(order, validations)
        );
    }

    private List<Map<String, Object>> createCustomerFields() {
        List<Map<String, Object>> customerFields = new ArrayList<>();

        // Customer name validation
        Map<String, Object> nameValidation = new HashMap<>();
        nameValidation.put("field", "name");
        List<Map<String, Object>> nameRules = new ArrayList<>();
        
        Map<String, Object> nameRequired = new HashMap<>();
        nameRequired.put("type", "required");
        
        Map<String, Object> nameMinLength = new HashMap<>();
        nameMinLength.put("type", "minLength");
        nameMinLength.put("value", 2);
        
        Map<String, Object> nameMaxLength = new HashMap<>();
        nameMaxLength.put("type", "maxLength");
        nameMaxLength.put("value", 50);
        
        nameRules.add(nameRequired);
        nameRules.add(nameMinLength);
        nameRules.add(nameMaxLength);
        nameValidation.put("rules", nameRules);
        customerFields.add(nameValidation);

        // Customer email validation
        Map<String, Object> emailValidation = new HashMap<>();
        emailValidation.put("field", "email");
        List<Map<String, Object>> emailRules = new ArrayList<>();
        
        Map<String, Object> emailRequired = new HashMap<>();
        emailRequired.put("type", "required");
        
        Map<String, Object> emailPattern = new HashMap<>();
        emailPattern.put("type", "pattern");
        emailPattern.put("value", "^[A-Za-z0-9+_.-]+@(.+)$");
        emailPattern.put("message", "must be a valid email address");
        
        Map<String, Object> emailMaxLength = new HashMap<>();
        emailMaxLength.put("type", "maxLength");
        emailMaxLength.put("value", 100);
        
        emailRules.add(emailRequired);
        emailRules.add(emailPattern);
        emailRules.add(emailMaxLength);
        emailValidation.put("rules", emailRules);
        customerFields.add(emailValidation);

        return customerFields;
    }

    private List<Map<String, Object>> createItemValidations() {
        List<Map<String, Object>> itemValidations = new ArrayList<>();

        // Product ID validation
        Map<String, Object> productIdValidation = new HashMap<>();
        productIdValidation.put("field", "productId");
        List<Map<String, Object>> productIdRules = new ArrayList<>();
        
        Map<String, Object> productIdRequired = new HashMap<>();
        productIdRequired.put("type", "required");
        
        Map<String, Object> productIdMaxLength = new HashMap<>();
        productIdMaxLength.put("type", "maxLength");
        productIdMaxLength.put("value", 20);
        
        productIdRules.add(productIdRequired);
        productIdRules.add(productIdMaxLength);
        productIdValidation.put("rules", productIdRules);
        itemValidations.add(productIdValidation);

        // Quantity validation
        Map<String, Object> quantityValidation = new HashMap<>();
        quantityValidation.put("field", "quantity");
        List<Map<String, Object>> quantityRules = new ArrayList<>();
        
        Map<String, Object> quantityRequired = new HashMap<>();
        quantityRequired.put("type", "required");
        
        Map<String, Object> quantityMin = new HashMap<>();
        quantityMin.put("type", "min");
        quantityMin.put("value", 1);
        
        quantityRules.add(quantityRequired);
        quantityRules.add(quantityMin);
        quantityValidation.put("rules", quantityRules);
        itemValidations.add(quantityValidation);

        return itemValidations;
    }

    // Helper method to create a single validation rule
    private Map<String, Object> createRule(String type, Object value, String message) {
        Map<String, Object> rule = new HashMap<>();
        rule.put("type", type);
        if (value != null) {
            rule.put("value", value);
        }
        if (message != null) {
            rule.put("message", message);
        }
        return rule;
    }

    // Helper method to create a field validation
    private Map<String, Object> createFieldValidation(String field, List<Map<String, Object>> rules) {
        Map<String, Object> validation = new HashMap<>();
        validation.put("field", field);
        validation.put("rules", rules);
        return validation;
    }

    @Test
    void shouldValidateOptionalObjectWithRequiredFields() {
        // Create validation rules for optional object with required fields
        List<Map<String, Object>> validations = new ArrayList<>();
        
        // Create shipping address validation (optional object with required fields)
        Map<String, Object> addressValidation = new HashMap<>();
        addressValidation.put("field", "shippingAddress");
        addressValidation.put("type", "object");
        // No required rule for the address object itself
        addressValidation.put("rules", new ArrayList<>());
        
        // Add address fields validation (some fields are required if address exists)
        addressValidation.put("fields", createShippingAddressFields());
        validations.add(addressValidation);

        // Test case 1: Null address (should pass as address is optional)
        Order orderWithNoAddress = createValidOrder();
        orderWithNoAddress.setShippingAddress(null);
        assertDoesNotThrow(() -> 
            validator.validateObject(orderWithNoAddress, validations)
        );

        // Test case 2: Present address with missing required fields (should fail)
        Order orderWithInvalidAddress = createValidOrder();
        ShippingAddress invalidAddress = new ShippingAddress();
        // Not setting required fields
        orderWithInvalidAddress.setShippingAddress(invalidAddress);
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validateObject(orderWithInvalidAddress, validations)
        );
        assertTrue(exception.getMessage().contains("street is required"));

        // Test case 3: Present address with all required fields (should pass)
        Order orderWithValidAddress = createValidOrder();
        ShippingAddress validAddress = createValidShippingAddress();
        orderWithValidAddress.setShippingAddress(validAddress);
        assertDoesNotThrow(() -> 
            validator.validateObject(orderWithValidAddress, validations)
        );
    }

    private List<Map<String, Object>> createShippingAddressFields() {
        List<Map<String, Object>> addressFields = new ArrayList<>();

        // Street validation (required)
        Map<String, Object> streetValidation = new HashMap<>();
        streetValidation.put("field", "street");
        List<Map<String, Object>> streetRules = new ArrayList<>();
        streetRules.add(createRule("required", null, null));
        streetRules.add(createRule("maxLength", 100, "must not exceed 100 characters"));
        streetValidation.put("rules", streetRules);
        addressFields.add(streetValidation);

        // City validation (required)
        Map<String, Object> cityValidation = new HashMap<>();
        cityValidation.put("field", "city");
        List<Map<String, Object>> cityRules = new ArrayList<>();
        cityRules.add(createRule("required", null, null));
        cityRules.add(createRule("maxLength", 50, "must not exceed 50 characters"));
        cityValidation.put("rules", cityRules);
        addressFields.add(cityValidation);

        // Zip code validation (required)
        Map<String, Object> zipValidation = new HashMap<>();
        zipValidation.put("field", "zipCode");
        List<Map<String, Object>> zipRules = new ArrayList<>();
        zipRules.add(createRule("required", null, null));
        zipRules.add(createRule("pattern", "^\\d{5}(-\\d{4})?$", "must be a valid ZIP code"));
        zipValidation.put("rules", zipRules);
        addressFields.add(zipValidation);

        // Additional notes (optional)
        Map<String, Object> notesValidation = new HashMap<>();
        notesValidation.put("field", "notes");
        List<Map<String, Object>> notesRules = new ArrayList<>();
        notesRules.add(createRule("maxLength", 200, "must not exceed 200 characters"));
        notesValidation.put("rules", notesRules);
        addressFields.add(notesValidation);

        return addressFields;
    }

    private ShippingAddress createValidShippingAddress() {
        ShippingAddress address = new ShippingAddress();
        address.setStreet("123 Main St");
        address.setCity("Anytown");
        address.setZipCode("12345");
        address.setNotes("Leave at front door");
        return address;
    }

} 