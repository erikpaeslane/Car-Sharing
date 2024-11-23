package carsharing.logic;

import carsharing.dao.DbCarSharingDao;
import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.util.List;

public class CarSharingService {

    private final DbCarSharingDao dao;

    public CarSharingService(DbCarSharingDao dao) {
        this.dao = dao;
    }

    public List<Company> getAllCompanies() {
        return dao.getAllCompanies();
    }

    public void createCompany(String companyName) {
        Company company = new Company(companyName);
        dao.createCompany(company);
    }

    public Company getCompany(int id) {
        return dao.getCompanyById(id);
    }

    public void createCar(String carName, int companyId) {
        Car car = new Car(carName, companyId);
        dao.createCar(car);
    }

    public Car getCar(int id) {
        return dao.getCarById(id);
    }

    public List<Car> getAllCarsOfCompany(int companyId) {
        return dao.getAllCarsOfCompany(companyId);
    }

    public void createCustomer(String customerName) {
        Customer customer = new Customer(customerName);
        dao.createCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return dao.getAllCustomers();
    }

    public Customer getCustomer(int id) {
        return dao.getCustomerById(id);
    }

    public void rentCar(int customerId, int carId) {
        Car car = dao.getCarById(carId);
        Customer customer = dao.getCustomerById(customerId);
        dao.rentCarForCustomer(customer, car);
    }

    public void returnCar(Customer customer) {
        dao.returnCar(customer);
        customer.setRentedCarId(null);
    }

    public boolean isCarRented(Car car) {
        return dao.isCarRented(car);
    }
}
