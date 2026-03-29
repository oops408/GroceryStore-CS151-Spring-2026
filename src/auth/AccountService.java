package auth;

import customers.CustomerIdRegistry;
import customers.RegularCustomer;
import customers.VIPCustomer;
import employee.Employee;
import employee.EmployeeIdRegistry;
import employee.Manager;
import employee.Stocker;
import java.util.Map;

public final class AccountService {

    private AccountService() {
    }

    public static RegularCustomer signUpRegularCustomer(
            String firstName,
            String lastName,
            Map<Integer, RegularCustomer> regularCustomers) {

        String safeFirst = normalizeName(firstName, "Guest");
        String safeLast = normalizeName(lastName, "Customer");

        int id = CustomerIdRegistry.nextId();
        RegularCustomer customer = new RegularCustomer(id, safeFirst, safeLast);
        regularCustomers.put(id, customer);
        return customer;
    }

    public static VIPCustomer signUpVipCustomer(
            String firstName,
            String lastName,
            Map<Integer, VIPCustomer> vipCustomers) {

        String safeFirst = normalizeName(firstName, "Guest");
        String safeLast = normalizeName(lastName, "Customer");

        int id = CustomerIdRegistry.nextId();
        VIPCustomer customer = new VIPCustomer(id, safeFirst, safeLast, 0.10);
        vipCustomers.put(id, customer);
        return customer;
    }

    public static Employee signUpEmployee(
            String firstName,
            String lastName,
            int roleChoice,
            Map<Integer, Employee> employees) {

        String safeFirst = normalizeName(firstName, "Employee");
        String safeLast = normalizeName(lastName, "User");

        int id = EmployeeIdRegistry.nextId();
        Employee employee;

        if (roleChoice == 2) {
            employee = new Manager(safeFirst, safeLast, id);
        } else {
            employee = new Stocker(safeFirst, safeLast, id);
        }

        employees.put(id, employee);
        return employee;
    }

    public static RegularCustomer signInRegularCustomer(
            String firstName,
            String lastName,
            int id,
            Map<Integer, RegularCustomer> regularCustomers) {

        RegularCustomer customer = regularCustomers.get(id);
        if (customer == null) {
            return null;
        }

        if (customer.getFirstName().equalsIgnoreCase(firstName.trim())
                && customer.getLastName().equalsIgnoreCase(lastName.trim())) {
            return customer;
        }

        return null;
    }

    public static VIPCustomer signInVipCustomer(
            String firstName,
            String lastName,
            int id,
            Map<Integer, VIPCustomer> vipCustomers) {

        VIPCustomer customer = vipCustomers.get(id);
        if (customer == null) {
            return null;
        }

        if (customer.getFirstName().equalsIgnoreCase(firstName.trim())
                && customer.getLastName().equalsIgnoreCase(lastName.trim())) {
            return customer;
        }

        return null;
    }

    public static Employee signInEmployee(
            String firstName,
            String lastName,
            int id,
            Map<Integer, Employee> employees) {

        Employee employee = employees.get(id);
        if (employee == null) {
            return null;
        }

        if (employee.getFirstName().equalsIgnoreCase(firstName.trim())
                && employee.getLastName().equalsIgnoreCase(lastName.trim())) {
            return employee;
        }

        return null;
    }

    private static String normalizeName(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }
}