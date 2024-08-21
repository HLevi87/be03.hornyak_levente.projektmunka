package be03.hornyak_levente.DataBaseFiller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SeasonsTable {
    private static final String DBpath = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;";
    private static Connection connection = getConncetion();

    public static void deleteThen_manage_SeasonsTable(){
        try{
            if (doesSeasonsTableExist()) {
                Connection connection = DriverManager.getConnection(DBpath);
                Statement statement = connection.createStatement();
                String query = "DROP TABLE Seasons;";
                statement.executeUpdate(query);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        manage_seasonsTable();
    }

    public static void manage_seasonsTable(){
        try {
            if (!doesSeasonsTableExist()) {
                System.out.println("A Seasons tábla még nem létezik.");
                if (create_seasonsTable()) {
                    System.out.println("A Seasons tábla létrehozása sikeres volt.");
                } else{
                    System.out.println("Nem sikerült létrehozni a Seasons táblát.");
                    return;
                }
            }
            insert_season("2023/2024", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static Boolean doesSeasonsTableExist() throws SQLException{
        Statement statement = connection.createStatement();
        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'Seasons'";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            if (rs.getInt(1) != 0) {
                return true;
            }
        }
        return false;

    }

    private static Boolean create_seasonsTable() throws SQLException{
        Statement statement = connection.createStatement();
        String query =  "CREATE TABLE Seasons (" +
                        "SeasonID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                        "SeasonName varchar(9), " +
                        "IsOver bit)";
        statement.executeUpdate(query);
        if (doesSeasonsTableExist()) {
            return true;
        }
        return false;
    }

    private static Boolean insert_season(String seasonName, Boolean isOver) throws SQLException{
        int rowsAffected = 0;
        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO Seasons VALUES(?, ?)")){
            ps.setString(1, seasonName);
            ps.setBoolean(2, isOver);

            rowsAffected = ps.executeUpdate();
        }

        if (rowsAffected != 0) {
            if (rowsAffected > 1) {
                System.out.println("Valamiért több sort is beírt.");
            }
            return true;
        }

        return false;
    }










    private static Connection getConncetion(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
