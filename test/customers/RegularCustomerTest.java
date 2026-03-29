package customers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegularCustomerTest {

    private RegularCustomer customer;

    @BeforeEach
    void setUp() throws Exception {
        customer = new RegularCustomer(101, "Vishal", "Raichur");
    }

    @Test
    void constructorShouldInitializeFields() {
        assertEquals(101, customer.getCustomerId());
        assertEquals("Vishal", customer.getFirstName());
        assertEquals("Raichur", customer.getLastName());
        assertEquals("Vishal Raichur", customer.getFullName());
        assertNotNull(customer.getCart());
    }

    @Test
    void regularCustomerShouldHaveNoDiscountByDefault() {
        assertEquals(0.0, customer.getDiscountRate());
    }

    @Test
    void toStringShouldContainCustomerInfo() {
        String text = customer.toString();
        assertTrue(text.contains("101"));
        assertTrue(text.contains("Vishal"));
    }

    @Test
    void addPurchaseRecordShouldStoreHistory() {
        customer.addPurchaseRecord("Receipt #1");
        assertEquals(1, customer.getPurchaseHistory().size());
        assertEquals("Receipt #1", customer.getPurchaseHistory().get(0));
    }

    @Test
    void addPurchaseRecordShouldIgnoreBlankText() {
        customer.addPurchaseRecord("   ");

        assertTrue(customer.getPurchaseHistory().isEmpty());
    }

    @Test
    void getPurchaseHistoryShouldBeUnmodifiable() {
        customer.addPurchaseRecord("Receipt #1");

        assertThrows(UnsupportedOperationException.class,
                () -> customer.getPurchaseHistory().add("Receipt #2"));
    }
}