package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
/*
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
 */

public interface CompanyDao {
    boolean printAllCompanies(DB database) throws SQLException;
    void addCompany(String name, DB database);

    Company getCompany(int id, DB database) throws SQLException;

}

class CompanyDaoImpl implements CompanyDao {
    @Override
    public boolean printAllCompanies(DB database) throws SQLException {

        //List<Company> companyArrayList = new ArrayList<>();

        String sql =  "SELECT ID, NAME \n" +
                "FROM COMPANY \n" +
                "ORDER BY ID;";

        ResultSet companyResultSet = database.executeSelectStatement(sql);
        if (!companyResultSet.next()) {
            System.out.println("The company list is empty");
            return false;
        }
        else {
            System.out.println("Choose a company:");
            do {
                String companyName = companyResultSet.getString("Name");
                int id = companyResultSet.getInt("ID");
                //companyArrayList.add(new Company(companyName));
                System.out.println(id + ". " + companyName);
            }
            while (companyResultSet.next());
            System.out.println("0. Back");
                    //System.out.println("");
        }
        return true;
        //return companyArrayList;
    }

    @Override
    public void addCompany(String name, DB database) {

        String sqlInsert = "INSERT INTO COMPANY (NAME) " +
                        "VALUES ('" + name + "');";
        database.executeUpdateStatement(sqlInsert);
        System.out.println("The company was created!");


    }

    @Override
    public Company getCompany(int id, DB database) throws SQLException {
        String sqlSelect = "SELECT name " +
                "FROM Company " +
                "WHERE id = " + id +
                ";";
        ResultSet resultSet = database.executeSelectStatement(sqlSelect);
        resultSet.next();
        String companyName = resultSet.getString("name");
        return new Company(id, companyName);
    }
}
