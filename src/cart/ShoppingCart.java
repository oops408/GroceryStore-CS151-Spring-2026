package cart;

import java.util.ArrayList;

public class ShoppingCart {

    private ArrayList<String> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public void addToCart(String itemName) {
        items.add(itemName);
        System.out.println(itemName + " added to cart.");
    }

    public void removeFromCart(String itemName) {
        if (items.remove(itemName)) {
            System.out.println(itemName + " removed from cart.");
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("Items in cart:");
        for (String item : items) {
            System.out.println("- " + item);
        }
    }

    public int getTotalItems() {
        return items.size();
    }

    public void clearCart() {
        items.clear();
        System.out.println("Cart cleared.");
    }
}