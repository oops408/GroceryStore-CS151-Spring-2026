package checkout;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import aisles.Aisles;
import customers.RegularCustomer;
import customers.VIPCustomer;
import exceptions.CapacityExceededException;
import inventory.Inventory;
import products.Products;

public class CheckoutTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() throws Exception{
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void printReceiptShouldHandleEmptyCart() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();

        Checkout.printReceipt(customer, inventory);

        assertTrue(outContent.toString().contains("Cart is empty. Nothing to checkout."));
    }

    @Test
    void printReceiptShouldClearCartAfterCheckout() throws Exception {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();
        inventory.addProduct("Dairy", new Products("Milk", 3.49, 10, 1001));

        customer.getCart().addItem("Milk");
        customer.getCart().addItem("Milk");

        Checkout.printReceipt(customer, inventory);

        assertTrue(outContent.toString().contains("GROCERY STORE RECEIPT"));
        assertEquals(0, customer.getCart().getTotalItems());
    }

    @Test
    void printReceiptShouldApplyVipDiscount() throws CapacityExceededException {
        VIPCustomer customer = new VIPCustomer(102, "John", "Doe", 0.10);
        Inventory inventory = new Inventory();
        inventory.addProduct("Dairy", new Products("Milk", 10.00, 10, 1001));

        customer.getCart().addItem("Milk");

        Checkout.printReceipt(customer, inventory);

        String output = outContent.toString();
        assertTrue(output.contains("VIP discount"));
        assertTrue(output.contains("TOTAL"));
    }

    @Test
    void printReceiptShouldResolvePriceFromAislesWhenMissingInInventory() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();

        Aisles aisle = new Aisles("Dairy", 1);
        aisle.addProductToShelf(1, new Products("Milk", 3.49, 10, 1001));

        customer.getCart().addItem("Milk");

        Checkout.printReceipt(customer, inventory, List.of(aisle));

        String output = outContent.toString();
        assertTrue(output.contains("Milk"));
        assertTrue(output.contains("3.49"));
    }

    @Test
    void printReceiptShouldHandleBlankItemNames() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();

        customer.getCart().addItem("   ");

        Checkout.printReceipt(customer, inventory);

        assertTrue(outContent.toString().contains("Cart has no valid item names. Nothing to checkout."));
    }

    @Test
    void printReceiptShouldAddVipPoints() throws Exception {
        VIPCustomer customer = new VIPCustomer(102, "John", "Doe", 0.10);
        Inventory inventory = new Inventory();
        inventory.addProduct("Dairy", new Products("Milk", 10.00, 10, 1001));

        customer.getCart().addItem("Milk");

        Checkout.printReceipt(customer, inventory);

        assertTrue(customer.getPoints() > 0);
        assertEquals(1, customer.getPurchaseHistory().size());
    }

    @Test
    void printReceiptShouldSavePurchaseHistory() throws Exception {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();
        inventory.addProduct("Dairy", new Products("Milk", 3.49, 10, 1001));

        customer.getCart().addItem("Milk");

        Checkout.printReceipt(customer, inventory);

        assertEquals(1, customer.getPurchaseHistory().size());
        assertTrue(customer.getPurchaseHistory().get(0).contains("GROCERY STORE RECEIPT"));
        assertTrue(customer.getPurchaseHistory().get(0).contains("Milk"));
        assertTrue(customer.getPurchaseHistory().get(0).contains("Customer Name:   Vishal Raichur"));
    }

    @Test
    void printReceiptShouldCreateSeparateHistoryForDifferentCustomers() throws Exception {
        RegularCustomer customer1 = new RegularCustomer(101, "Vishal", "Raichur");
        RegularCustomer customer2 = new RegularCustomer(102, "John", "Doe");
        Inventory inventory = new Inventory();

        inventory.addProduct("Dairy", new Products("Milk", 3.49, 10, 1001));
        inventory.addProduct("Produce", new Products("Apples", 1.99, 10, 1002));

        customer1.getCart().addItem("Milk");
        customer2.getCart().addItem("Apples");

        Checkout.printReceipt(customer1, inventory);
        Checkout.printReceipt(customer2, inventory);

        assertEquals(1, customer1.getPurchaseHistory().size());
        assertEquals(1, customer2.getPurchaseHistory().size());

        assertTrue(customer1.getPurchaseHistory().get(0).contains("Milk"));
        assertFalse(customer1.getPurchaseHistory().get(0).contains("Apples"));

        assertTrue(customer2.getPurchaseHistory().get(0).contains("Apples"));
        assertFalse(customer2.getPurchaseHistory().get(0).contains("Milk"));
    }

    @Test
    void printReceiptShouldAddVipPointsAndSaveHistory() throws Exception {
        VIPCustomer customer = new VIPCustomer(102, "John", "Doe", 0.10);
        Inventory inventory = new Inventory();
        inventory.addProduct("Dairy", new Products("Milk", 10.00, 10, 1001));

        customer.getCart().addItem("Milk");

        Checkout.printReceipt(customer, inventory);

        assertTrue(customer.getPoints() > 0);
        assertEquals(1, customer.getPurchaseHistory().size());
        assertTrue(customer.getPurchaseHistory().get(0).contains("GROCERY STORE RECEIPT"));
    }

    @Test
    void printReceiptShouldNotSaveHistoryForEmptyCart() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();

        Checkout.printReceipt(customer, inventory);

        assertTrue(customer.getPurchaseHistory().isEmpty());
    }

    @Test
    void printReceiptShouldNotSaveHistoryForBlankOnlyItems() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();

        customer.getCart().addItem("   ");

        Checkout.printReceipt(customer, inventory);

        assertTrue(customer.getPurchaseHistory().isEmpty());
    }
}