package carsharing.entity;

public class Customer {

    private int id;
    private final String name;
    private Integer rentedCarId;

    public Customer(int id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public Customer(String name) {
        this.name = name;
        this.rentedCarId = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    public boolean isCarRented() {
        return this.rentedCarId != null;
    }
}
