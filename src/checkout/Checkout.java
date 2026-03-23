package checkout;

import customers.Customer;
import customers.VIPCustomer;

public class Checkout {

    public void processCheckout(Customer customer) {
        if (customer.getCart().isEmpty()) {
            System.out.println("Your cart is empty. Cannot proceed to checkout.");
            return;
        }

        System.out.println("===== Checkout =====");
        customer.getCart().viewCart();

        int totalItems = customer.getCart().getTotalItems();
        double subtotal = totalItems * 5.0;
        double discount = 0.0;
        double finalTotal = subtotal;

        if (customer instanceof VIPCustomer) {
            VIPCustomer vipCustomer = (VIPCustomer) customer;
            discount = subtotal * vipCustomer.getDiscountRate();
            finalTotal = subtotal - discount;
        }

        System.out.println("Total items: " + totalItems);
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Discount: $" + discount);
        System.out.println("Final total: $" + finalTotal);
    }

    public void completePurchase(Customer customer) {
        if (customer.getCart().isEmpty()) {
            System.out.println("Cart is empty. Purchase cannot be completed.");
            return;
        }

        System.out.println("Purchase successful!");
        customer.getCart().clearCart();
    }

    public void cancelCheckout() {
        System.out.println("Checkout cancelled.");
    }
}