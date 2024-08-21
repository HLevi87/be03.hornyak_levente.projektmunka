package be03.hornyak_levente.Factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactPersons {
    private final String DBpath = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;";

    private Connection connection;

    public ContactPersons() throws SQLException {
        this.connection = DriverManager.getConnection(DBpath);
    }

    public void closeConnection() throws SQLException{
        this.connection.close();
    }


    public ResultSet teamNames() throws SQLException{
        List<String> teamNames = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String query = "SELECT TeamName FROM Teams WHERE IsActive = 1";
        return statement.executeQuery(query);
    }


    public ResultSet playerIDs(String teamName) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement(
            "SELECT p.PlayerID FROM Teams t " +
                "INNER JOIN Players p ON t.TeamID = p.TeamID " +
                "WHERE t.TeamName = ?");
        ps.setString(1, teamName);

        return ps.executeQuery();
    }


    public Boolean add_contactPersonID(Integer contactPersonID, String teamName) throws SQLException{
        try(PreparedStatement ps = this.connection.prepareStatement(
            "UPDATE Teams " +
                "SET ContactPersonID = ? " +
                "WHERE TeamName = ?")){
            ps.setInt(1, contactPersonID);
            ps.setString(2, teamName);

            ps.executeUpdate();
        }
        try(PreparedStatement ps = this.connection.prepareStatement(
            "SELECT ContactPersonID FROM TEAMS WHERE TeamName = ?")){
                ps.setString(1, teamName);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt(1) == contactPersonID) {
                        return true;
                    }
                }
            }
        return false;
    }

    public void cheat(){
        try {
            //PlayerID-m lekérdezése
            int myID = 0;
            Statement statement = connection.createStatement();
            String query = "SELECT PlayerID FROM Players WHERE PlayerName = 'Hornyák Levente'";
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                myID = rs.getInt(1);
            }

            query = "UPDATE Teams SET ContactPersonID = " + myID + " WHERE TeamID = 11";
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
