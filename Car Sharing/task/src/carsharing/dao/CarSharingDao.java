package carsharing.dao;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.util.List;

public interface CarSharingDao {

    void createCompany(Company company);
    List<Company> getAllCompanies();
    Company getCompanyById(int id);
    void createCar(Car car);
    List<Car> getAllCarsOfCompany(int id);
    Car getCarById(int id);
    List<Car> getAllCars();
    void createCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Customer getCustomerById(int id);
    void rentCarForCustomer(Customer customer, Car car);
    void returnCar(Customer customer);
    boolean isCarRented(Car car);

}
