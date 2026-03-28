package inventory;

import exceptions.CapacityExceededException;
import exceptions.InvalidQuantityException;
import exceptions.NotFoundException;
import java.util.List;
import products.Products;

// Optional console demo of inventory operations; mutates the given inventory.
public final class InventoryConsoleDemo {

    private InventoryConsoleDemo() {
    }

    public static void run(Inventory inventory) {
        System.out.println("\n========== INVENTORY TESTS ==========");

        System.out.println("\n--- Print Inventory ---");
        inventory.printInventory();

        System.out.println("\n--- Find Product by ID (valid) ---");
        try {
            Products found = inventory.findProduct(1002);
            System.out.println("Found: " + found);
        } catch (NotFoundException e) {
            System.out.println("Find error: " + e.getMessage());
        }

        System.out.println("\n--- Search By Name: 'i' ---");
        List<Products> matches = inventory.searchByName("i");
        if (matches.isEmpty()) {
            System.out.println("No matching products found.");
        } else {
            for (Products product : matches) {
                System.out.println(product);
            }
        }

        System.out.println("\n--- Low Stock Products (< 10) ---");
        List<Products> lowStock = inventory.listLowStock(10);
        if (lowStock.isEmpty()) {
            System.out.println("No low-stock products.");
        } else {
            for (Products product : lowStock) {
                System.out.println(product);
            }
        }

        System.out.println("\n--- Restock Milk by 10 ---");
        try {
            inventory.restockProduct("Dairy", 1002, 10);
            System.out.println("Restock successful.");
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Restock error: " + e.getMessage());
        }

        System.out.println("\n--- Decrease Apples stock by 5 ---");
        try {
            inventory.decreaseStock("Produce", 1001, 5);
            System.out.println("Decrease stock successful.");
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Decrease stock error: " + e.getMessage());
        }

        System.out.println("\n--- Remove Chips from Snacks ---");
        try {
            inventory.removeProduct("Snacks", 1003);
            System.out.println("Remove product successful.");
        } catch (NotFoundException e) {
            System.out.println("Remove error: " + e.getMessage());
        }

        System.out.println("\n--- Updated Inventory ---");
        inventory.printInventory();

        System.out.println("\n========== INVENTORY EDGE CASES ==========");

        System.out.println("\n--- Edge Case: duplicate product ID ---");
        try {
            inventory.addProduct("Produce", new Products("Green Apples", 2.49, 15, 1001));
        } catch (CapacityExceededException e) {
            System.out.println("Capacity error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: invalid product ID ---");
        try {
            inventory.findProduct(9999);
        } catch (NotFoundException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: invalid restock quantity ---");
        try {
            inventory.restockProduct("Dairy", 1002, 0);
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: insufficient stock ---");
        try {
            inventory.decreaseStock("Produce", 1001, 999);
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: remove missing product ---");
        try {
            inventory.removeProduct("Snacks", 9999);
        } catch (NotFoundException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }
}
