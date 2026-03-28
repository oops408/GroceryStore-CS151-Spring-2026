package customers;

import cart.ShoppingCart;

// Abstract base class for all customers in the grocery store system
public abstract class Customer implements Displayable {

    // Basic customer information
    protected int customerId;
    protected String firstName;
    protected String lastName;
    protected ShoppingCart cart;

    // Constructor to create a customer with basic details
    public Customer(int customerId, String firstName, String lastName) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cart = new ShoppingCart();
    }

    // Returns the customer ID
    public int getCustomerId() {
        return customerId;
    }

    // Returns the customer's first name
    public String getFirstName() {
        return firstName;
    }

    // Returns the customer's last name
    public String getLastName() {
        return lastName;
    }

    // for Manager
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Returns the customer's shopping cart
    public ShoppingCart getCart() {
        return cart;
    }

    public double getDiscountRate() {
        return 0.0;
    }

    // for Manager
    public void printCustomerHistory() {
    System.out.println("No purchase history available yet.");
}

    // Displays customer information
    @Override
    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + firstName + " " + lastName);
    }

    // Returns customer details as a string
    @Override
    public String toString() {
        return "Customer ID: " + customerId +
               ", Name: " + firstName + " " + lastName;
    }
}
