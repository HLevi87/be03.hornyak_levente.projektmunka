package be03.hornyak_levente.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Repository extends MainRepo{

    //Create
    public Integer insertInto_query(String query) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeUpdate(query);
    }

    //Read
    public ResultSet select_query(String query) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeQuery(query);
    }

    //Update
    public Integer update_query(String query) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeUpdate(query);
    }

    //Delete
    public Integer delete_query(String query) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeUpdate(query);
    }

    public Integer add_newTeam(String tableName, Integer teamID, Integer seasonID, Integer contactPersonID, String teamName,
                                Integer contactDetailsID, Integer locationID, Integer ballID, String gameDay, String gameTime){
        /*DB sorrend:
              TeamID
              ValidFrom
              ValidTo
              IsActive
              SeasonID
              ContactPersonID
              TeamName
              ContactDetailsID
              LocationID
              BallID
              GameDay
              GameTime
              MatchesPlayed
              MatchesWon
              MatchesDraw
              MatchesLost
              Points
             */
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO ? VALUES(?, ?, null, 1, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, 0, 0)")){
            ps.setString(1, tableName);
            ps.setInt(2, teamID);
            ps.setString(3, LocalDate.now().toString());
            ps.setInt(4, seasonID);
            ps.setInt(5, contactPersonID);
            ps.setString(6, teamName);
            ps.setInt(7, contactDetailsID);
            ps.setInt(8, locationID);
            ps.setInt(9, ballID);
            ps.setString(10, gameDay);
            ps.setString(11, gameTime);

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
     
        return 0;
    }

    public Integer add_newPlayer(String tableName, Integer playerID, Integer seasonID, String playerName, Integer licenceNumber, Integer teamID){
        /*
            PlayerID
            ValidFrom
            ValidTo
            IsActive
            SeasonID
            PlayerName
            LicenceNumber
            TeamID
            MatchesPlayed
            MatchesWon
            PlayerStrength
         */
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO ? VALUES(?, ?, null, 1, ?, ?, ?, ?, 0, 0, null)")){
            ps.setString(1, tableName);
            ps.setInt(2, playerID);
            ps.setString(3, LocalDate.now().toString());
            ps.setInt(4, seasonID);
            ps.setString(5, playerName);
            ps.setInt(6, licenceNumber);
            ps.setInt(7, teamID);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public Integer add_newCredential(Integer credentialsID, Integer contactPersonID, String userName, String password){
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO Credentials VALUES(?, ?, null, 1, 0, ?, ?, ?)")){
            ps.setInt(1, credentialsID);
            ps.setString(2, LocalDate.now().toString());
            ps.setInt(3, contactPersonID);
            ps.setString(4, userName);
            ps.setString(5, password);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
