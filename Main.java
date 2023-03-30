package carsharing;
import java.sql.*;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        DB database = new DB();
        database.connectToDB(args);

        String startMessage = "1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit";

        String sqlCreateTableCompany = "CREATE TABLE IF NOT EXISTS COMPANY \n" +
                "(ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "NAME VARCHAR(255) NOT NULL UNIQUE\n" +
                ");";
        String sqlCreateTableCar = "CREATE TABLE IF NOT EXISTS Car \n" +
                "(id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "name VARCHAR(255) NOT NULL UNIQUE,\n" +
                "is_rented BOOLEAN DEFAULT (FALSE),\n" +
                "company_id INT NOT NULL," +
                "FOREIGN KEY (company_id) REFERENCES Company(id)" +
                ");";
        String sqlCreateTableCustomer = "CREATE TABLE IF NOT EXISTS Customer \n" +
                "(id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "name VARCHAR(255) NOT NULL UNIQUE,\n" +
                "rented_car_id INT," +
                "FOREIGN KEY (rented_car_id) REFERENCES Car(id)" +
                "ON DELETE SET NULL" +
                ");";
        String sqlDropTableCompany = "DROP TABLE IF EXISTS Company;";
        String sqlDropTableCar = "DROP TABLE IF EXISTS Car;";
        String sqlDropTableCustomer = "DROP TABLE IF EXISTS Customer;";


        //database.executeUpdateStatement(sqlDropTableCustomer);
        //database.executeUpdateStatement(sqlDropTableCar);
        //database.executeUpdateStatement(sqlDropTableCompany);
        database.executeUpdateStatement(sqlCreateTableCompany);
        database.executeUpdateStatement(sqlCreateTableCar);
        database.executeUpdateStatement(sqlCreateTableCustomer);


        CarSharingUI carSharingUI = new CarSharingUI();
        System.out.println(startMessage);
        String choice = scanner.nextLine();
        while(carSharingUI.proceedUserChoice(choice, database)) {
            choice = scanner.nextLine();
        }

        database.closeDB();

    }
}