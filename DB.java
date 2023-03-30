package carsharing;

import java.sql.*;

public class DB {
        static final String JDBC_DRIVER = "org.h2.Driver";
        static final String DB_URL = "jdbc:h2:./src/carsharing/db/";

        private Connection conn = null;
        private Statement stmt = null;


        ResultSet executeSelectStatement(String sql) {
            try {
                if (!conn.isClosed()) {
                    return stmt.executeQuery(sql);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        void executeUpdateStatement(String sql) {
            try {
                if (!conn.isClosed()) {
                    stmt.execute(sql);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



        public void connectToDB(String[] args) {
            String dbName = args.length == 2 ? args[1] : "carsharing";
            String url =  DB_URL + dbName;
            try {
                // STEP 1: Register JDBC driver
                Class.forName(JDBC_DRIVER);

                //STEP 2: Open a connection
                //System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(url);

                conn.setAutoCommit(true);

                //STEP 3: Execute a query
                stmt = conn.createStatement();
            } catch(SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch(Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }
        }

        public  void closeDB() {
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {} // nothing we can do
            try {
                if(conn!=null) conn.close();
            }
            catch(SQLException se){
                se.printStackTrace();
            }
        }

    }
