package com.skov.autoSpider;

/**
 * Created by aogj on 20-12-2016.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBHandler {

    public static void main(String[] args) throws ClassNotFoundException {
        insert("http://www.gege.asd/asdasd", "toyota aygo", 2011, 40000, 60000, "det er rn rigtig fin bil...");
        insert("http://www.gege.asd/asdasd", "toyota aygo", 2011, 40000, 60000, "det er rn rigtig fin bil...");

        select();
    }

    public static StringBuffer select() throws ClassNotFoundException {
        // load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:auto.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from auto");
            while (rs.next()) {
                // read the result set
                System.out.print("url = " + rs.getString("url"));
                System.out.print(", headline = " + rs.getString("headline"));
                System.out.print(", year = " + rs.getInt("year"));
                System.out.print(", price = " + rs.getInt("price"));
                System.out.print(", headkmline = " + rs.getInt("km"));
                System.out.println(", description = " + rs.getString("description"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
        return new StringBuffer("done");
    }


    public static void insert(String url, String headline, int year, int price, int km, String description) throws ClassNotFoundException {
        createTableIfNotExists(false);
        // load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:auto.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            //statement.executeUpdate("drop table if exists person");
            //statement.executeUpdate("insert into person values(1, 'leo222')");
            statement.executeUpdate("insert into auto values(" +
                    "'" + url + "', " +
                    "'" + headline + "', " +
                    "" + year + ", " +
                    "" + price + ", " +
                    "" + km + ", " +
                    "'" + description + "')");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
    }

    public static void createTableIfNotExists(boolean alwaysDrop) throws ClassNotFoundException {
        // load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:auto.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            if(alwaysDrop) {
                statement.executeUpdate("drop table if exists auto");

            }
            statement.executeUpdate("create table if not exists auto (" +
                    "url string, " +
                    "headline string, " +
                    "year integer, " +
                    "price integer, " +
                    "km integer, " +
                    "description string)");
            //statement.executeUpdate("insert into person values(1, 'leo222')");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
    }

}
