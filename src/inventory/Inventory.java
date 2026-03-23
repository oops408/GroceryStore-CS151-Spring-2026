package inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import products.Products;

public class Inventory {
    private static final int MAX_PRODUCTS = 100;

    // section -> (productId -> product)
    private HashMap<String, HashMap<Integer, Products>> inventory;

    public Inventory() {
        inventory = new HashMap<>();
    }

    public void addProduct(String section, Products product) {
        if (getTotalProductCount() >= MAX_PRODUCTS) {
            throw new IllegalStateException("Inventory is at maximum capacity of 100 products.");
        }

        inventory.putIfAbsent(section, new HashMap<>());
        HashMap<Integer, Products> sectionProducts = inventory.get(section);

        if (sectionProducts.containsKey(product.getID())) {
            throw new IllegalArgumentException("Product ID already exists in section " + section);
        }

        sectionProducts.put(product.getID(), product);
    }

    public void removeProduct(String section, int productID) {
        if (!inventory.containsKey(section)) {
            throw new IllegalArgumentException("Section not found: " + section);
        }

        HashMap<Integer, Products> sectionProducts = inventory.get(section);

        if (!sectionProducts.containsKey(productID)) {
            throw new IllegalArgumentException("Product ID " + productID + " not found in section " + section);
        }

        sectionProducts.remove(productID);

        if (sectionProducts.isEmpty()) {
            inventory.remove(section);
        }
    }

    public Products getProduct(String section, int productID) {
        if (!inventory.containsKey(section)) {
            return null;
        }
        return inventory.get(section).get(productID);
    }

    public Products findProduct(int productID) {
        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            if (sectionProducts.containsKey(productID)) {
                return sectionProducts.get(productID);
            }
        }
        return null;
    }

    public void restockProduct(String section, int productID, int quantity) {
        Products product = getProduct(section, productID);

        if (product == null) {
            throw new IllegalArgumentException("Product not found in section " + section);
        }

        product.setQuantity(product.getQuantity() + quantity);
    }

    public void decreaseStock(String section, int productID, int quantity) {
        Products product = getProduct(section, productID);

        if (product == null) {
            throw new IllegalArgumentException("Product not found in section " + section);
        }

        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        product.setQuantity(product.getQuantity() - quantity);
    }

    public List<Products> listLowStock(int threshold) {
        List<Products> lowStockProducts = new ArrayList<>();

        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            for (Products product : sectionProducts.values()) {
                if (product.getQuantity() < threshold) {
                    lowStockProducts.add(product);
                }
            }
        }

        return lowStockProducts;
    }

    public List<Products> searchByName(String keyword) {
        List<Products> matches = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            for (Products product : sectionProducts.values()) {
                if (product.getName().toLowerCase().contains(lowerKeyword)) {
                    matches.add(product);
                }
            }
        }

        return matches;
    }

    public void printInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("Full Inventory:");
        for (Map.Entry<String, HashMap<Integer, Products>> entry : inventory.entrySet()) {
            String section = entry.getKey();
            HashMap<Integer, Products> sectionProducts = entry.getValue();

            System.out.println("Section: " + section);
            for (Products product : sectionProducts.values()) {
                System.out.println("- " + product.getName()
                        + " (ID: " + product.getID()
                        + ", Price: $" + product.getPrice()
                        + ", Stock: " + product.getQuantity() + ")");
            }
        }
    }

    public int getTotalProductCount() {
        int count = 0;
        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            count += sectionProducts.size();
        }
        return count;
    }

    public boolean hasSection(String section) {
        return inventory.containsKey(section);
    }

    public HashMap<String, HashMap<Integer, Products>> getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "Inventory with " + getTotalProductCount() + " products across " + inventory.size() + " sections.";
    }
}