package be03.hornyak_levente.DataBaseFiller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MatchResultsTable {
    private static String DBpath = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;";

    public static void deleteThen_manage_matchResultsTable(){
        if (doesMatchResultsTableExist()) {
            try{
                Connection connection = DriverManager.getConnection(DBpath);
                Statement statement = connection.createStatement();
                String query = "DROP TABLE MatchResultsOneVOne;";
                statement.executeUpdate(query);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        manage_matchResultsTable();
    }

    public static void manage_matchResultsTable(){
        if (!doesMatchResultsTableExist()) {
            System.out.println("A MatchResultsOneVOne táblázat még nem létezik.");
            if (create_MatchResultsTable()) {
                System.out.println("A MatchResultsOneVOne táblázat létrehozása sikeres volt.");
            } else{
                System.out.println("Nem sikerült létrehozni a MatchResultsOneVOne táblázatot.");
            }
        }
    }

    private static Boolean doesMatchResultsTableExist(){
        try {
            Connection connection = DriverManager.getConnection(DBpath);
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'MatchResultsOneVOne'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (!rs.getObject(1).equals(0)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Boolean create_MatchResultsTable(){
        try (Connection connection = DriverManager.getConnection(DBpath)){
            Statement statement = connection.createStatement();
            String query =  "CREATE TABLE MatchResultsOneVOne (" +
                            "IndividualMatchID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                            "SeasonID int, " +
                            "TeamMatchID int, " +
                            "HomePlayerID int, " +
                            "GuestPlayerID int, " +
                            "MatchResult varchar(5))";
            statement.executeUpdate(query);
            if (doesMatchResultsTableExist()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
