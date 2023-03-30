package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDao {
    ArrayList<Customer> getAllCustomers(DB database) throws SQLException;
    void addCustomer(String name, DB database);
    Customer getCustomer(int id, DB database) throws SQLException;

    boolean returnRentedCar(int customerID, int rentedCarID, DB database) throws SQLException;

    boolean getRentedCar(int customerID, DB database) throws SQLException;

    void setRentedCar(int customerID, int carID, DB database) throws SQLException;
}

class CustomerDaoImpl implements CustomerDao {
    @Override
    public ArrayList<Customer> getAllCustomers(DB database) throws SQLException {

        ArrayList<Customer> customerArrayList = new ArrayList<Customer>();

        String sql =  "SELECT id, name \n" +
                "FROM Customer \n" +
                "ORDER BY id;";

        ResultSet customerResultSet = database.executeSelectStatement(sql);
        if (customerResultSet.next()) {
            do {
                String name = customerResultSet.getString("Name");
                int id = customerResultSet.getInt("ID");
                customerArrayList.add(new Customer(id, name));
               }
            while (customerResultSet.next());
        }
        return customerArrayList;
    }

    @Override
    public void addCustomer(String name, DB database) {

        String sqlInsert = "INSERT INTO Customer (name) " +
                "VALUES ('" + name + "');";
        database.executeUpdateStatement(sqlInsert);
        System.out.println("The customer was added!");
    }

    @Override
    public Customer getCustomer(int id, DB database) throws SQLException {
        String sqlSelect = "SELECT name " +
                "FROM Customer " +
                "WHERE id = " + id +
                ";";
        ResultSet resultSet = database.executeSelectStatement(sqlSelect);
        resultSet.next();
        String name = resultSet.getString("name");
        return new Customer(id, name);
    }
    @Override
    public boolean returnRentedCar(int customerID, int rentedCarID, DB database) throws SQLException {
        String sqlSelectRentedCar = "SELECT *" +
                " FROM Customer" +
                " WHERE ID = " + customerID + " AND rented_car_id IS NOT NULL;";
        ResultSet resultSet = database.executeSelectStatement(sqlSelectRentedCar);
        if (resultSet.next()) {
            String sqlUpdate = "UPDATE Customer" +
                    " SET rented_car_id = null" +
                    " WHERE id = " + customerID + ";";
            database.executeUpdateStatement(sqlUpdate);
            sqlUpdate = "Update Car\n" +
                    "Set is_rented = false\n" +
                    "WHERE id = " + rentedCarID + ";";
            database.executeUpdateStatement(sqlUpdate);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean getRentedCar(int customerID, DB database) throws SQLException {
        String sqlSelectRentedCar = "SELECT Car.name carName, Company.name companyName" +
                " FROM Car, Customer, Company" +
                " WHERE Customer.id = " + customerID +
                " AND Customer.rented_car_id = Car.id" +
                " AND Company.id = Car.company_id;";
        ResultSet resultSet = database.executeSelectStatement(sqlSelectRentedCar);
        if (resultSet.next()) {
            String carName = resultSet.getString("carName");
            String companyName = resultSet.getString("companyName");
            System.out.println("Your rented car:\n" + carName + "\nCompany:\n" + companyName);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void setRentedCar(int customerID, int carID, DB database) {
        String sqlUpdateRentedCar = "UPDATE Customer" +
                " SET rented_car_id = " + carID +
                " WHERE id = " + customerID + ";";
        database.executeUpdateStatement(sqlUpdateRentedCar);
        sqlUpdateRentedCar = "UPDATE Car" +
                " SET is_rented = true" +
                " WHERE id = " + carID + ";";
        database.executeUpdateStatement(sqlUpdateRentedCar);
    }
}
