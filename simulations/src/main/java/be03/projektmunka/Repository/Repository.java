package be03.projektmunka.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * MEGJEGYZÉS!
 * AZ ADATBÁZIS-KEZELŐ METÓDUSOK PARAMÉTEREI ÚGY LETTEK MEGÍRVA,
 * HOGY AZOK A METÓDUS MEGHÍVÁSAKOR ÚGY TŰNJENEK,
 * MINTHA EGY QUERY FIX RÉSZEI LENNÉNEK,
 * AMI MEGKÖNNYÍTI AZ ALÁBBI METÓDUSOK MEGHÍVÁSÁT A LOGIC OSZTÁLYOKBÓL.
 */

public class Repository extends MainRepository {
    
    @Override
    public Integer insertInto_table(String INSERT_INTO, String columnName, String VALUES) {;

        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO " + INSERT_INTO + " (" + columnName + ") VALUES (" + VALUES + ")")){
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer insertInto_table(String INSERT_INTO, String VALUES) {;

        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO " + INSERT_INTO + " VALUES (" + VALUES + ")")){
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Integer insertInto_Fixture(Integer seasonID, Integer homeTeamID, Integer guestTeamID, Integer umpireID, Integer roundNumber, String season, String result){
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO Fixture VALUES (?, ?, ?, ?, ?, ?, ?, ?)")){
            ps.setInt(1, seasonID);
            ps.setInt(2, homeTeamID);
            ps.setInt(3, guestTeamID);
            ps.setInt(4, umpireID);
            ps.setInt(5, roundNumber);
            ps.setString(6, season);
            ps.setString(7, result);
            ps.setBoolean(8, true);

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer insertInto_MatchResultsOneVOne(Integer seasonID, Integer teamMatchID, Integer homePlayerID, Integer guestPlayerID, String matchResult){
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO MatchResultsOneVOne VALUES (?, ?, ?, ?, ?)")){
            ps.setInt(1, seasonID);
            ps.setInt(2, teamMatchID);
            ps.setInt(3, homePlayerID);
            ps.setInt(4, guestPlayerID);
            ps.setString(5, matchResult);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public ResultSet readFrom_table(String SELECT, String FROM, String WHERE) {
        ResultSet rs = null;
        try {
            Statement statement = this.connection.createStatement();
            String query = "SELECT " + SELECT + " FROM " + FROM + " WHERE " + WHERE;
            rs = statement.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet readFrom_table(String SELECT, String FROM) {
        ResultSet rs = null;
        try {
            Statement statement = this.connection.createStatement();
            String query = "SELECT " + SELECT + " FROM " + FROM;
            rs = statement.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer update_table(String UPDATE, String SET, String EQUALS, String WHERE) {
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE " + UPDATE + " SET " + SET + "=" + EQUALS + " WHERE " + WHERE)){
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer deleteFrom_table(String DELETE_FROM, String WHERE) {
        try (PreparedStatement ps = this.connection.prepareStatement("DELETE FROM " + DELETE_FROM + " WHERE " + WHERE)){
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet read_teamsToClass(){
        try {
            Statement statement = this.connection.createStatement();
            String teamToClass_query = "SELECT \r\n" + //
                                "    t.TeamID, \r\n" + //
                                "    p.PlayerID, \r\n" + //
                                "    t. TeamName, \r\n" + //
                                "    cd.Email, \r\n" + //
                                "    cd.PhoneNumber, \r\n" + //
                                "    l.Location, \r\n" + //
                                "    b.Ball, \r\n" + //
                                "    t.GameDay, \r\n" + //
                                "    t.GameTime, \r\n" + //
                                //Ide jönnek a játékosok!!!\r\n" + //
                                "    t.MatchesPlayed, \r\n" + //
                                "    t.MatchesWon, \r\n" + //
                                "    t.MatchesDraw, \r\n" + //
                                "    t.MatchesLost,\r\n" + //
                                "    t.Points\r\n" + //
                                "FROM Teams t\r\n" + //
                                "\r\n" + //
                                "INNER JOIN Players p ON t.ContactPersonID = p.PlayerID\r\n" + //
                                "INNER JOIN ContactDetails cd ON t.ContactDetailsID = cd.ContactDetailsID\r\n" + //
                                "INNER JOIN Locations l ON t.LocationID = l.LocationID\r\n" + //
                                "INNER JOIN Balls b ON t.BallID = b.BallID\r\n" +
                                "ORDER BY TeamID";
            return statement.executeQuery(teamToClass_query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
