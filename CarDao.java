package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CarDao {

    void printAllCars(int companyId, DB database, String titleMessage, boolean filtered) throws SQLException;
    void addCar(String name, int companyId, DB database);

    String getCarName(int id, DB database) throws SQLException;

}

class CarDaoImpl implements CarDao {
    @Override
    public void printAllCars(int companyId, DB database, String titleMessage, boolean filtered) throws SQLException {
        String sqlSelectCarsOfCompany =  filtered ?
                "SELECT id, name\n" +
                        "FROM Car \n" +
                        "WHERE company_id = " + companyId + "\n" +
                        "AND is_rented = false \n" +
                        "ORDER BY ID;"
                :
                "SELECT id, name\n" +
                        "FROM Car \n" +
                        "WHERE company_id = " + companyId + "\n" +
                        "ORDER BY ID;";

        String sqlSelectCompanyName = "SELECT name\n" +
                "FROM Company \n" +
                "WHERE id = " + companyId + ";";

        ResultSet companyResultSet = database.executeSelectStatement(sqlSelectCompanyName);
        companyResultSet.next();
        String companyName = companyResultSet.getString("name");

        ResultSet carResultSet = database.executeSelectStatement(sqlSelectCarsOfCompany);
        if (!carResultSet.next()) {
            System.out.println("The car list is empty!");
        }
        else {
            //System.out.println(companyName + " cars:");
            System.out.println(titleMessage);
            int carIndex = 1;
            do {
                String name = carResultSet.getString("name");
                int id = carResultSet.getInt("id");
                System.out.println(carIndex++ + ". " + name);
            }
            while (carResultSet.next());
            //System.out.println("");
        }
    }
    @Override
    public void addCar(String name, int companyId, DB database) {
        String sqlInsert = "INSERT INTO Car (name, company_id) " +
                "VALUES ('" + name + "', " + companyId + ");";
        database.executeUpdateStatement(sqlInsert);
        System.out.println("The car was added!");

    }

    @Override
    public String getCarName(int id, DB database) throws SQLException {
        String sqlSelect = "SELECT name" +
                " FROM CAR" +
                " WHERE ID = " + id + ";";
        ResultSet resultSet = database.executeSelectStatement(sqlSelect);
        resultSet.next();
        return resultSet.getString("name");
    }

}