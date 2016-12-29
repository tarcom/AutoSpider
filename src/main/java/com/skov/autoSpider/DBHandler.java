package com.skov.autoSpider;

/**
 * Created by aogj on 20-12-2016.
 */

import org.apache.commons.lang.StringEscapeUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class DBHandler {
    //public static final String DRIVER_NAME = "org.sqlite.JDBC";
    //public static final String DRIVER_CON_NAME = "jdbc:sqlite:auto.db";

    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final String DRIVER_CON_NAME = "jdbc:mysql://localhost/AutoSpider?user=root&password=";


    public static void main(String[] args) {
        //insert("http://www.gege.asd/asdasd", "toyota aygo", 2011, 40000, 60000, "det er rn rigtig fin bil...");
        //insert("http://www.gege.asd/asdasd", "toyota aygo", 2011, 40000, 60000, "det er rn rigtig fin bil...");

        ArrayList<AutoWrapper> autos = selectAllAutos();
        for (AutoWrapper auto : autos) {
            System.out.println(auto);
        }
    }

    public static ArrayList<AutoWrapper> selectAllAutos() {
        // load the sqlite-JDBC driver using the current class loader
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<AutoWrapper> autos = new ArrayList<AutoWrapper>();
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection(DRIVER_CON_NAME);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from auto");
            while (rs.next()) {

                AutoWrapper auto = new AutoWrapper(
                        rs.getString("urlBase"),
                        rs.getString("urlDetail"),
                        rs.getString("headline1"),
                        rs.getString("headline2"),
                        rs.getInt("price"),
                        rs.getInt("year"),
                        rs.getInt("km"),
                        rs.getInt("kml"),
                        rs.getInt("newPrice"),
                        rs.getInt("distance"),
                        rs.getString("town"),
                        rs.getInt("greenEA"),
                        rs.getInt("hk"),
                        rs.getInt("acc100"),
                        rs.getString("thumbImg"),
                        rs.getString("text"),
                        rs.getString("baseHeadline"),
                        rs.getDate("indregDate").getYear(),
                        rs.getDate("indregDate").getMonth() + 1
                );
                autos.add(auto);
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
        return autos;
    }

    public static void createTableIfNotExists(boolean alwaysDrop) {
        // load the sqlite-JDBC driver using the current class loader
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection(DRIVER_CON_NAME);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            if (alwaysDrop) {
                statement.executeUpdate("drop table if exists auto");

            }

            String sql = "create table if not exists auto (" +
                    "  `urlDetail` varchar(255) PRIMARY KEY,\n" +
                    "  `urlBase` varchar(255) DEFAULT NULL,\n" +
                    "  `headline1` varchar(255) DEFAULT NULL,\n" +
                    "  `headline2` varchar(255) DEFAULT NULL,\n" +
                    "  `price` int(11) DEFAULT NULL,\n" +
                    "  `year` int(11) DEFAULT NULL,\n" +
                    "  `km` int(11) DEFAULT NULL,\n" +
                    "  `kml` int(11) DEFAULT NULL,\n" +
                    "  `newPrice` int(11) DEFAULT NULL,\n" +
                    "  `distance` int(11) DEFAULT NULL,\n" +
                    "  `town` varchar(255) DEFAULT NULL,\n" +
                    "  `greenEA` int(11) DEFAULT NULL,\n" +
                    "  `hk` int(11) DEFAULT NULL,\n" +
                    "  `acc100` int(11) DEFAULT NULL,\n" +
                    "  `thumbImg` varchar(255) DEFAULT NULL,\n" +
                    "  `baseHeadline` varchar(255) DEFAULT NULL,\n" +
                    "  `indregDate` date DEFAULT NULL,\n" +
                    "  `insertDateTime` datetime DEFAULT NULL,\n" +
                    "  `lastUpdated` datetime DEFAULT NULL,\n" +
                    "  `text` varchar(1024) DEFAULT NULL\n" +
                    ")";

            statement.executeUpdate(sql);
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

    public static void insert(AutoWrapper auto) {
        //System.out.println("persisting...");
        createTableIfNotExists(false);
        // load the sqlite-JDBC driver using the current class loader
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection(DRIVER_CON_NAME);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            //statement.executeUpdate("drop table if exists person");
            //statement.executeUpdate("insert into person values(1, 'leo222')");
            String text = auto.getText();
            text = escapeSql(text);

            String sql = "insert IGNORE into auto " +
                    "(`urlDetail`, `urlBase`, `headline1`, `headline2`, `price`, `year`, `km`, `kml`, `newPrice`, `distance`, `town`, `greenEA`, `hk`, `acc100`, `thumbImg`, `baseHeadline`, `indregDate`, `insertDateTime`, `lastUpdated`, `text`)" +
                    " values(" +
                    "'" + auto.getUrlDetail() + "', " +
                    "'" + auto.getUrlBase() + "', " +
                    "'" + auto.getHeadline1() + "', " +
                    "'" + auto.getHeadline2() + "', " +
                    "'" + auto.getPrice() + "', " +
                    "'" + auto.getYear() + "', " +
                    "'" + auto.getKm() + "', " +
                    "'" + auto.getKml() + "', " +
                    "'" + auto.getNewPrice() + "', " +
                    "'" + auto.getDistance() + "', " +
                    "'" + auto.getTown() + "', " +
                    "'" + auto.getGreenEA() + "', " +
                    "'" + auto.getHk() + "', " +
                    "'" + auto.getAcc100() + "', " +
                    "'" + auto.getThumbImg() + "', " +
                    "'" + auto.getBaseHeadline() + "', " +
                    "'" + auto.getIndregDate() + "', " +
                    "'" + LocalDateTime.now() + "', " +
                    "'" + LocalDateTime.now() + "', " +
                    "'" + text + "') " +
                    "";//"ON DUPLICATE KEY UPDATE lastUpdated=VALUES('" + LocalDateTime.now() + "')";

            statement.executeUpdate(sql);
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
        //System.out.println("Persisting done.");
    }

    public static String escapeSql(String input) {
        String out = StringEscapeUtils.escapeHtml(input);
        out = out.replace('\'', ' ');
        return out;
    }
}
