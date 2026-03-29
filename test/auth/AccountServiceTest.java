package auth;

import static org.junit.jupiter.api.Assertions.*;

import customers.RegularCustomer;
import customers.VIPCustomer;
import employee.Employee;
import employee.Manager;
import employee.Stocker;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountServiceTest {

    private Map<Integer, RegularCustomer> regularCustomers;
    private Map<Integer, VIPCustomer> vipCustomers;
    private Map<Integer, Employee> employees;

    @BeforeEach
    void setUp() {
        regularCustomers = new HashMap<>();
        vipCustomers = new HashMap<>();
        employees = new HashMap<>();
    }

    @Test
    void signUpRegularCustomerShouldCreateAndStoreCustomer() {
        RegularCustomer customer = AccountService.signUpRegularCustomer(
                "John", "Doe", regularCustomers);

        assertNotNull(customer);
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertTrue(regularCustomers.containsKey(customer.getCustomerId()));
        assertEquals(customer, regularCustomers.get(customer.getCustomerId()));
    }

    @Test
    void signUpRegularCustomerShouldUseFallbackNamesWhenBlank() {
        RegularCustomer customer = AccountService.signUpRegularCustomer(
                "   ", "", regularCustomers);

        assertEquals("Guest", customer.getFirstName());
        assertEquals("Customer", customer.getLastName());
    }

    @Test
    void signInRegularCustomerShouldReturnMatchingCustomer() {
        RegularCustomer customer = AccountService.signUpRegularCustomer(
                "Alice", "Smith", regularCustomers);

        RegularCustomer signedIn = AccountService.signInRegularCustomer(
                "Alice", "Smith", customer.getCustomerId(), regularCustomers);

        assertNotNull(signedIn);
        assertEquals(customer, signedIn);
    }

    @Test
    void signInRegularCustomerShouldIgnoreCase() {
        RegularCustomer customer = AccountService.signUpRegularCustomer(
                "Alice", "Smith", regularCustomers);

        RegularCustomer signedIn = AccountService.signInRegularCustomer(
                "alice", "smith", customer.getCustomerId(), regularCustomers);

        assertNotNull(signedIn);
        assertEquals(customer, signedIn);
    }

    @Test
    void signInRegularCustomerShouldReturnNullForWrongName() {
        RegularCustomer customer = AccountService.signUpRegularCustomer(
                "Alice", "Smith", regularCustomers);

        RegularCustomer signedIn = AccountService.signInRegularCustomer(
                "Bob", "Smith", customer.getCustomerId(), regularCustomers);

        assertNull(signedIn);
    }

    @Test
    void signInRegularCustomerShouldReturnNullForWrongId() {
        AccountService.signUpRegularCustomer("Alice", "Smith", regularCustomers);

        RegularCustomer signedIn = AccountService.signInRegularCustomer(
                "Alice", "Smith", 9999, regularCustomers);

        assertNull(signedIn);
    }

    @Test
    void signUpVipCustomerShouldCreateAndStoreCustomer() {
        VIPCustomer customer = AccountService.signUpVipCustomer(
                "Jane", "Doe", vipCustomers);

        assertNotNull(customer);
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals(0.10, customer.getDiscountRate());
        assertTrue(vipCustomers.containsKey(customer.getCustomerId()));
    }

    @Test
    void signInVipCustomerShouldReturnMatchingCustomer() {
        VIPCustomer customer = AccountService.signUpVipCustomer(
                "Jane", "Doe", vipCustomers);

        VIPCustomer signedIn = AccountService.signInVipCustomer(
                "Jane", "Doe", customer.getCustomerId(), vipCustomers);

        assertNotNull(signedIn);
        assertEquals(customer, signedIn);
    }

    @Test
    void signInVipCustomerShouldReturnNullForWrongCredentials() {
        VIPCustomer customer = AccountService.signUpVipCustomer(
                "Jane", "Doe", vipCustomers);

        VIPCustomer signedIn = AccountService.signInVipCustomer(
                "Jane", "Wrong", customer.getCustomerId(), vipCustomers);

        assertNull(signedIn);
    }

    @Test
    void signUpEmployeeShouldCreateStockerWhenRoleIsOneOrDefault() {
        Employee employee = AccountService.signUpEmployee(
                "Sam", "Lee", 1, employees);

        assertTrue(employee instanceof Stocker);
        assertTrue(employees.containsKey(employee.getEmployeeID()));
    }

    @Test
    void signUpEmployeeShouldCreateManagerWhenRoleIsTwo() {
        Employee employee = AccountService.signUpEmployee(
                "Sam", "Lee", 2, employees);

        assertTrue(employee instanceof Manager);
        assertTrue(employees.containsKey(employee.getEmployeeID()));
    }

    @Test
    void signInEmployeeShouldReturnMatchingEmployee() {
        Employee employee = AccountService.signUpEmployee(
                "Mark", "Stone", 2, employees);

        Employee signedIn = AccountService.signInEmployee(
                "Mark", "Stone", employee.getEmployeeID(), employees);

        assertNotNull(signedIn);
        assertEquals(employee, signedIn);
    }

    @Test
    void signInEmployeeShouldReturnNullForWrongId() {
        AccountService.signUpEmployee("Mark", "Stone", 2, employees);

        Employee signedIn = AccountService.signInEmployee(
                "Mark", "Stone", 9999, employees);

        assertNull(signedIn);
    }

    @Test
    void signInEmployeeShouldReturnNullForWrongName() {
        Employee employee = AccountService.signUpEmployee(
                "Mark", "Stone", 2, employees);

        Employee signedIn = AccountService.signInEmployee(
                "John", "Stone", employee.getEmployeeID(), employees);

        assertNull(signedIn);
    }
    
    @Test
    void signUpVipCustomerShouldUseFallbackNamesWhenBlank() {
        VIPCustomer customer = AccountService.signUpVipCustomer("   ", "", vipCustomers);

        assertEquals("Guest", customer.getFirstName());
        assertEquals("Customer", customer.getLastName());
        }
        
        @Test
        void signUpEmployeeShouldUseFallbackNamesWhenBlank() {
        Employee employee = AccountService.signUpEmployee("   ", "", 1, employees);

        assertEquals("Employee", employee.getFirstName());
        assertEquals("User", employee.getLastName());
        }

        @Test
        void signUpEmployeeShouldDefaultToStockerForInvalidRole() {
        Employee employee = AccountService.signUpEmployee("Sam", "Lee", 99, employees);

        assertTrue(employee instanceof Stocker);
        }

        @Test
        void signInRegularCustomerShouldTrimWhitespace() {
        RegularCustomer customer = AccountService.signUpRegularCustomer(
                "Alice", "Smith", regularCustomers);

        RegularCustomer signedIn = AccountService.signInRegularCustomer(
                "  Alice  ", "  Smith  ", customer.getCustomerId(), regularCustomers);

        assertNotNull(signedIn);
        assertEquals(customer, signedIn);
        }

        @Test
        void signUpRegularCustomerShouldHandleNullNames() {
        RegularCustomer customer = AccountService.signUpRegularCustomer(
                null, null, regularCustomers);

        assertEquals("Guest", customer.getFirstName());
        assertEquals("Customer", customer.getLastName());
        }

        @Test
        void signUpShouldCreateDifferentIdsForDifferentCustomers() {
        RegularCustomer c1 = AccountService.signUpRegularCustomer("A", "One", regularCustomers);
        RegularCustomer c2 = AccountService.signUpRegularCustomer("B", "Two", regularCustomers);

        assertNotEquals(c1.getCustomerId(), c2.getCustomerId());
        }
}