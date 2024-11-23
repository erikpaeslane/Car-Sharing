package carsharing.dao;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbCarSharingDao implements CarSharingDao {

    private final Connection connection;

    public DbCarSharingDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createCompany(Company company) {
        String query = "INSERT INTO COMPANY (NAME) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, company.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
        }

    }

    @Override
    public List<Company> getAllCompanies() {
        String query = "SELECT * FROM COMPANY";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Company> companies = new ArrayList<>();
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getInt("ID"), resultSet.getString("NAME")));
            }
            return companies;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Company getCompanyById(int id) {
        for (Company company : getAllCompanies()) {
            if (company.getId() == id) {
                return company;
            }
        }
        return null;
    }

    @Override
    public void createCar(Car car) {
        String query = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, car.getName());
            preparedStatement.setInt(2, car.getCompanyId());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public List<Car> getAllCarsOfCompany(int companyId) {
        String query = "SELECT * FROM CAR WHERE COMPANY_ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, companyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (resultSet.next()) {
                cars.add(new Car(
                        resultSet.getInt("ID"),
                        resultSet.getString("NAME"),
                        resultSet.getInt("COMPANY_ID")
                        )
                );
            }
            return cars;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Car getCarById(int id) {
        for (Car car : getAllCars()) {
            if (car.getId() == id) {
                return car;
            }
        }
        return null;
    }

    @Override
    public List<Car> getAllCars() {
        String query = "SELECT * FROM CAR";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("COMPANY_ID")));
            }
            return cars;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void createCustomer(Customer customer) {
        String query = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setObject(2, customer.getRentedCarId());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        String query = "SELECT * FROM CUSTOMER";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                Integer rentedCarId = null;
                int tempRentedCarId = resultSet.getInt("RENTED_CAR_ID");
                if (!resultSet.wasNull()) {
                    rentedCarId = tempRentedCarId;
                }
                customers.add(new Customer(id, name, rentedCarId));
            }
            return customers;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Customer getCustomerById(int id) {
        for (Customer customer : getAllCustomers()) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public void rentCarForCustomer(Customer customer, Car car) {
        String query = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            preparedStatement.setInt(1, car.getId());
            preparedStatement.setInt(2, customer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void returnCar(Customer customer) {
        String query = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?";
         try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.executeUpdate();
         } catch (SQLException ignored) {
         }
    }

    @Override
    public boolean isCarRented(Car car) {
        String query = "SELECT 1 FROM CUSTOMER WHERE RENTED_CAR_ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, car.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException ignored) {
            return false;
        }

    }


}
