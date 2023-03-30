package carsharing;

public class Customer {
    private String name;
    private int id;
    private int rentedCarId;

    private boolean isCarReturned;

    Customer (int id, String name) {
        this.name = name;
        this.id = id;
        this.isCarReturned = false;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setID(int id) {
        this.id = id;
    }
    public void setRentedCarId(int carId) {
        this.rentedCarId = carId;
    }
    public String getName() {
        return  this.name;
    }

    public int getId() {
        return this.id;
    }
    public int getRentedCarId() { return this.rentedCarId; }

    public boolean getIsCarReturned() {
        return this.isCarReturned;
    }

    public void setIsCarReturned(boolean isCarReturned) {
        this.isCarReturned = isCarReturned;
    }

}
