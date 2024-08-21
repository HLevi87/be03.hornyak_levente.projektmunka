package be03.hornyak_levente.DataBaseFiller;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import be03.hornyak_levente.Factory.TeamLists;

public class FillUpTeams {

    private static final String DB_PATH = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;";

    public static void fillUp_TeamsDB(){
        TeamLists teamLists = new TeamLists("cimek.txt");
        try {
            fillUp_ContactDetails(teamLists);
            fillUp_Locations(teamLists);
            fillUp_balls(teamLists);
            fillUp_teams(teamLists);
            System.out.println("\nA csapatok és a hozzá tartozó táblák felöltése sikeresen megtörtént.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void fillUp_ContactDetails(TeamLists teamLists) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        for (int i = 0; i < teamLists.getEmailek().size(); i++) {
            if (email_isNotInDB(teamLists.getEmailek().get(i)) && phone_isNotInDB(teamLists.getTelefonszamok().get(i))) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO ContactDetails (Email, PhoneNumber) VALUES (?, ?)"
                )){
                    preparedStatement.setString(1, teamLists.getEmailek().get(i));
                    preparedStatement.setString(2, teamLists.getTelefonszamok().get(i));
                 
                    preparedStatement.executeUpdate();
                }
            }
        }
        connection.close();
    }

    private static Integer find_emailIdx(String email) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM ContactDetails WHERE Email = '" + email + "'";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            String contactDetailIDString = rs.getString("ContactDetailsID");
            Integer contactDetailID = Integer.parseInt(contactDetailIDString);
            return contactDetailID;
        }
        connection.close();
        return -1;
    }

    private static Integer find_phoneIdx(String phone) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM ContactDetails WHERE PhoneNumber = ?")){
            ps.setString(1, phone);
            try(ResultSet rs = ps.executeQuery()){
                String DBphone = rs.getString("PhoneNumber");
                if (DBphone.equals(phone)) {
                    return rs.getInt("ContactDetailsID");
                }
            }
        }
        connection.close();
        return -1;
    }

    private static Boolean email_isNotInDB(String email) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT Email FROM ContactDetails WHERE Email = ?")){
            ps.setString(1, email);
            ps.executeQuery();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                return false;
            }
        }
        connection.close();
        return true;
    }

    private static Boolean phone_isNotInDB(String phone) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT PhoneNumber FROM ContactDetails WHERE PhoneNumber = ?")){
            ps.setString(1, phone);
            ps.executeQuery();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                return false;
            }
        }
        connection.close();
        return true;
    }



    private static void fillUp_Locations(TeamLists teamLists) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        for (int i = 0; i < teamLists.getCimekLista().size(); i++) {
            if (location_isNotInDB(teamLists.getCimekLista().get(i))) {
                try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Locations (Location) VALUES (?)")){
                    ps.setString(1, teamLists.getCimekLista().get(i));

                    ps.executeUpdate();
                }
            }
        }
        connection.close();
    }

    private static Integer find_locationIdx(String location) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM Locations WHERE Location = ?")){
            ps.setString(1, location);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getObject("Location").equals(location)) {
                    return rs.getInt("LocationID");
                }
            }
        }
        connection.close();
        return -1;
    }

    private static Boolean location_isNotInDB(String location) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);

        try(PreparedStatement ps = connection.prepareStatement("SELECT Location FROM Locations WHERE Location = ?")){
            ps.setString(1, location);
            ps.executeQuery();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                return false;
            }
        }

        // Statement statement = connection.createStatement();
        // String query = "SELECT Location FROM Locations";
        // ResultSet rs = statement.executeQuery(query);
        // while (rs.next()) {
        //     if (rs.getObject("Location").equals(location)) {
        //         return false;
        //     }
        // }
        connection.close();
        return true;
    }



    private static void fillUp_balls(TeamLists teamLists) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        for (int i = 0; i < teamLists.getLabdak().size(); i++) {
            if (ball_isNotInDB(teamLists.getLabdak().get(i))) {
                try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Balls (Ball) VALUES (?)")){
                    ps.setString(1, teamLists.getLabdak().get(i));

                    ps.executeUpdate();
                }
            }
        }
        connection.close();
    }

    private static Integer find_ballIdx(String ball) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM Balls WHERE Ball = '" + ball + "'";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            return rs.getInt(1);
        }
        connection.close();
        return -1;
    }

    private static Boolean ball_isNotInDB(String ball) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT Ball FROM Balls WHERE Ball = ?")){
            ps.setString(1, ball);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return false;
            }
        }
        connection.close();
        return true;
    }



    private static void fillUp_teams(TeamLists teamLists) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        for (int i = 0; i < teamLists.getCsapatnevekLista().size(); i++) {
            if (team_isNotInDB(teamLists.getCsapatnevekLista().get(i))) {
                try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Teams (TeamName, ContactDetailsID, LocationID, BallID, GameDay, GameTime) VALUES (?, ?, ?, ?, ?, ?)")){
                    //Csapatnév
                    ps.setString(1, teamLists.getCsapatnevekLista().get(i));
                    //Kontakt index
                    int emailIdx = find_emailIdx(teamLists.getEmailek().get(i));
                    if (emailIdx > 0) {
                        ps.setInt(2, emailIdx);
                    } else{
                        System.out.println("A csapat feltöltése sikertelen: nem megfelelő emailcím index.");
                        break;
                    }
                    //Cím index
                    int locationIdx = find_locationIdx(teamLists.getCimekLista().get(i));
                    if (locationIdx > 0) {
                        ps.setInt(3, locationIdx);
                    } else{
                        System.out.println("A csapat feltöltése sikertelen: nem megfelelő cím index.");
                        break;
                    }
                    //Labda index
                    int ballIdx = find_ballIdx(teamLists.getLabdak().get(i));
                    if (ballIdx > 0) {
                        ps.setInt(4, ballIdx);
                    } else{
                        System.out.println("A csapat feltöltése sikertelen: nem megfelelő labda index.");
                        break;
                    }
                    //Játéknap
                    ps.setString(5, teamLists.getJatekNapok().get(i));
                    //Játék indőpont
                    ps.setString(6, teamLists.getJatekIdopontok().get(i));

                    ps.executeUpdate();
                } 
            }
        }
        connection.close();
    }

    private static Boolean team_isNotInDB(String teamName) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT TeamName FROM Teams WHERE TeamName = ?")){
            ps.setString(1, teamName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return false;
            }
        }
        connection.close();
        return true;
    }
}
