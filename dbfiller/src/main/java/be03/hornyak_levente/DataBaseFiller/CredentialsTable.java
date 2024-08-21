package be03.hornyak_levente.DataBaseFiller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class CredentialsTable {

    private static String DBpath = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;";

    public static void deleteThen_manage_CredentialsTable(){
        if (doesCredentialsTableExist()) {
            try{
                Connection connection = DriverManager.getConnection(DBpath);
                Statement statement = connection.createStatement();
                String query = "DROP TABLE Credentials;";
                statement.executeUpdate(query);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        manage_credentialsTable();
    }

    public static void manage_credentialsTable(){
        if (!doesCredentialsTableExist()) {
            System.out.println("A Credentials tábla még nem létezik.");
            if (create_credentialsTable()) {
                System.out.println("A Credentials tábla létrehozása sikeres volt.");
            } else{
                System.out.println("Nem sikerült létrehozni a Credentials táblát.");
            }
        }
        if (upload_initialTestData()) {
            System.out.println("A Credentials tábla feltöltése a tesztadatokkal sikeres volt.");
        } else{
            System.out.println("Nem sikerült a Credentials táblát feltölteni a tesztadatokkal.");
        }
    }

    private static Boolean doesCredentialsTableExist(){
        try {
            Connection connection = DriverManager.getConnection(DBpath);
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'Credentials'";
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

    private static Boolean create_credentialsTable(){
        try {
            Connection connection = DriverManager.getConnection(DBpath);
            try(PreparedStatement ps = connection.prepareStatement(
                "CREATE TABLE Credentials (" +
                        "TechnicalID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                        "CredentialsID int, " +
                        "ValidFrom date, " +
                        "ValidTo date, " +
                        "IsActive bit, " +
                        "IsAdmin bit, " +
                        "ContactPersonID int, " +
                        "Username varchar(50), " +
                        "Password varchar(30))")){
                ps.executeUpdate();
            }
            if (doesCredentialsTableExist()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Boolean upload_initialTestData(){
        try {
            Connection connection = DriverManager.getConnection(DBpath);
            Statement statement = connection.createStatement();
            Integer topSpin_teamID = 11;
            String update1 = "INSERT INTO Credentials VALUES(1, '" + LocalDate.now().toString() + "', NULL, 1, 1, NULL, 'Admin', 'Admin')";
            String update2 = "INSERT INTO Credentials VALUES(2, '" + LocalDate.now().toString() + "', NULL, 1, 0, " + get_contactPersonID(topSpin_teamID) + ", 'Levi', 'Levi')";
            statement.executeUpdate(update1);
            statement.executeUpdate(update2);

            String check = "SELECT COUNT(*) FROM Credentials";
            ResultSet rs = statement.executeQuery(check);
            while (rs.next()) {
                if (rs.getInt(1) == 2) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Integer get_contactPersonID(Integer teamID){
        try {
            Connection connection = DriverManager.getConnection(DBpath);
            Statement statement = connection.createStatement();
            String query = "SELECT ContactPersonID FROM Teams WHERE teamID = " + teamID;
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}

