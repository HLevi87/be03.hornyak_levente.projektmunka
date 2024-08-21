package be03.hornyak_levente.DataBaseFiller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FixtureTable {
    private static String DBpath = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;";


    public static void deleteThenManage_FixtureTable(){
        if (doesFixtureTableExist()) {
            try{
                Connection connection = DriverManager.getConnection(DBpath);
                Statement statement = connection.createStatement();
                String query = "DROP TABLE Fixture;";
                statement.executeUpdate(query);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        manage_fixtureTable();
    }


    public static void manage_fixtureTable(){
        if (!doesFixtureTableExist()) {
            System.out.println("A Fixture tábla még nem létezik.");
            if (create_fixtureTable()) {
                System.out.println("A Fixture tábla létrehozása sikeres volt.");
            } else{
                System.out.println("Nem sikerült létrehozni a Fixture táblát.");
            }
        }
    }

    private static Boolean doesFixtureTableExist(){
        try (Connection connection = DriverManager.getConnection(DBpath)){
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'Fixture'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (!rs.getObject(1).equals(0)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Boolean create_fixtureTable(){
        try (Connection connection = DriverManager.getConnection(DBpath)){
            try(PreparedStatement ps = connection.prepareStatement(
                "CREATE TABLE Fixture (" +
                    "TeamMatchID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                    "SeasonID int, " +
                    "HomeTeamID int, " +
                    "GuestTeamID int, " +
                    "UmpireID int, " +
                    "RoundNumber int, " +
                    "Season varchar(10), " +
                    "Result varchar(5), " +
                    "IsOver bit NOT NULL)")){
                ps.executeUpdate();
            }
            if (doesFixtureTableExist()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Integer get_currentSeasonID(){
        int seasonID = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
            Statement statement = connection.createStatement();
            String query = "SELECT MAX(SeasonID) FROM Seasons";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                seasonID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seasonID;
    }

    
}
