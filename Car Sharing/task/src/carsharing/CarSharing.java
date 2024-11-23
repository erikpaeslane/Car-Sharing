package carsharing;

import carsharing.dao.DbCarSharingDao;
import carsharing.db.DatabaseConnection;
import carsharing.db.DatabaseInitializer;
import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;
import carsharing.logic.CarSharingService;
import carsharing.logic.InputHandler;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CarSharing {

    private final CarSharingService carSharingService;
    private final Connection connection;
    private final Scanner scanner;

    public CarSharing() {
        this.connection = DatabaseConnection.connect();
        this.scanner = new Scanner(System.in);
        DbCarSharingDao dbCarSharingDao = new DbCarSharingDao(connection);
        this.carSharingService = new CarSharingService(dbCarSharingDao);
    }

    public void start() {

        DatabaseInitializer.initialize(connection);

        boolean toExit = false;
        while (!toExit) {
            printStartMenu();

            int choice = InputHandler.handleIntInput(scanner.nextLine());

            switch (choice) {
                case 1:
                    while (true) {
                        printManagerMenu();
                        choice = InputHandler.handleIntInput(scanner.nextLine());
                        if (choice == 1)
                            allCompaniesMenu();
                        else if (choice == 2)
                            createCompanyMenu();
                         else if (choice == 0)
                            break;
                    }
                    break;
                case 2:
                    allCustomersMenu();
                    break;
                case 3:
                    createCustomerMenu();
                    break;
                case 0:
                    System.out.println("Exit");
                    toExit = true;
                    break;
            }
        }
        DatabaseConnection.closeConnection(connection);
    }

    public void printStartMenu() {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
    }

    public void printManagerMenu() {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }

    public void allCompaniesMenu() {
        List<Company> companies = carSharingService.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            System.out.println();
            return;
        }
        printAllCompanies(companies);
        int choice = InputHandler.handleIntInput(scanner.nextLine());
        if (choice != 0)
            oneCompanyMenu(choice);
    }

    public void oneCompanyMenu(int companyId) {
        System.out.println();
        Company company = carSharingService.getCompany(companyId);
        if (company == null) {
            return;
        }

        System.out.println("'" + company.getName() + "' company:");
        while (true) {
            printOneCompanyMenu();
            int choice = InputHandler.handleIntInput(scanner.nextLine());
            if (choice == 0) {
                break;
            } else if (choice == 1) {
                List<Car> cars = carSharingService.getAllCarsOfCompany(company.getId());
                printCars(cars);
            } else if (choice == 2)
                createCarMenu(company);
            else
                System.out.println("Invalid number");
        }
    }

    public void allCustomersMenu() {
        System.out.println();
        List<Customer> customers = carSharingService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            System.out.println();
            return;
        }
        System.out.println("Customer list:");
        for (Customer customer : customers) {
            System.out.println(customer.getId() + ". " + customer.getName());
        }
        System.out.println("0. Back");
        int choice = InputHandler.handleIntInput(scanner.nextLine());
        if (choice != 0) {
            oneCustomerMenu(choice);
        }
    }

    public void oneCustomerMenu(int customerId) {
        Customer customer = carSharingService.getCustomer(customerId);
        if (customer == null) {
            return;
        }
        while (true) {
            printCustomerMenu();
            int choice = InputHandler.handleIntInput(scanner.nextLine());
            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                if (customer.isCarRented())
                    System.out.println("You've already rented a car!");
                else
                    rentCarMenu(customer);
            }
            if (choice == 2) {
                System.out.println();
                if (!customer.isCarRented()) {
                    System.out.println("You didn't rent a car!");
                    System.out.println();
                    continue;
                }
                carSharingService.returnCar(customer);
                System.out.println("You've returned a rented car!");
            }
            if (choice == 3) {
                System.out.println();
                if (!customer.isCarRented()) {
                    System.out.println("You didn't rent a car!");
                    System.out.println();
                    continue;
                }
                Car rentedCar = carSharingService.getCar(customer.getRentedCarId());
                Company company = carSharingService.getCompany(rentedCar.getCompanyId());
                printRentedCar(company, rentedCar);
            }
        }

    }

    public void rentCarMenu(Customer customer) {
        List<Company> companies = carSharingService.getAllCompanies();
        printAllCompanies(companies);
        int choice = InputHandler.handleIntInput(scanner.nextLine());
        if (choice == 0)
            return;

        Company company = carSharingService.getCompany(choice);
        if (company == null) {
            return;
        }
        List<Car> cars = carSharingService.getAllCarsOfCompany(company.getId());
        if (cars.isEmpty()) {
            return;
        }
        printCars(cars);
        choice = InputHandler.handleIntInput(scanner.nextLine());
        if (choice == 0) return;
        Car rentedCar = cars.get(choice-1);
        carSharingService.rentCar(customer.getId(), rentedCar.getId());
        customer.setRentedCarId(rentedCar.getId());
        System.out.println();
        System.out.println("You rented '" + rentedCar.getName() + "'");
    }

    public void createCompanyMenu() {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        carSharingService.createCompany(companyName);
        System.out.println("The company was created!");
        System.out.println();
    }

    private void createCarMenu(Company company) {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        carSharingService.createCar(carName, company.getId());
        System.out.println("The car was added!");
        System.out.println();
    }

    public void createCustomerMenu() {
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        carSharingService.createCustomer(customerName);
        System.out.println("The customer was added!");
        System.out.println();
    }

    public void printAllCompanies(List<Company> companies) {
        System.out.println();

        System.out.println("Choose a company: ");
        for (Company company : companies) {
            System.out.println((company.getId()) + ". " + company.getName());
        }
        System.out.println("0. Back");
    }

    public void printOneCompanyMenu() {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    public void printCustomerMenu() {
        System.out.println();
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
    }

    public void printCars(List<Car> cars) {
        System.out.println();
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            System.out.println();
            return;
        }
        System.out.println("Car list:");
        for (int i = 0; i < cars.size(); i++) {
            if (!carSharingService.isCarRented(cars.get(i)))
                System.out.println((i + 1) + ". " + cars.get(i).getName());
        }
    }

    public void printRentedCar(Company company, Car rentedCar) {
        System.out.println("Your rented car:");
        System.out.println(rentedCar.getName());
        System.out.println("Company:");
        System.out.println(company.getName());
        System.out.println();
    }

    public static void main(String[] args) {
        CarSharing carSharing = new CarSharing();
        carSharing.start();
    }


}