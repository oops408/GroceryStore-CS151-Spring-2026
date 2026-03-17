package customers;

import cart.ShoppingCart;

public abstract class Customer {

    protected int customerId;
    protected String firstName;
    protected String lastName;
    protected ShoppingCart cart;

    public Customer(int customerId, String firstName, String lastName) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    /**
     * Discount rate as a decimal (e.g., 0.15 == 15% off).
     */
    public abstract double getDiscountRate();
}