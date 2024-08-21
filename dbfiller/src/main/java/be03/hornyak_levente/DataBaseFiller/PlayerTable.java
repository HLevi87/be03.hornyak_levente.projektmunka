package be03.hornyak_levente.DataBaseFiller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import be03.hornyak_levente.Logic.PlayerListClass;

public class PlayerTable {
    private PlayerListClass playerListClass;

    public PlayerTable() {
        this.playerListClass = new PlayerListClass();
    }

    public void deleteThen_manage_PlayersTable(Integer seasonID){
        if (does_playersTableExist()) {
            try{
                Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
                Statement statement = connection.createStatement();
                String query = "DROP TABLE Players;";
                statement.executeUpdate(query);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        manage_playersTable(seasonID);
    }

    public void manage_playersTable(Integer seasonID){
        if (!does_playersTableExist()) {
            System.out.println("Players tábla még nem létezik.");
            if (create_playersTable()) {
                System.out.println("A Players tábla létrehozása sikeres volt.");
            } else{
                System.out.println("Hiba történt a Players tábla létrehozása során.");
            }
        }
        if (sync_playersTable(seasonID)) {
            System.out.println("A Players tábla adatfeltöltése sikeres volt.");
        } else{
            System.out.println("Hiba történt a Players tábla adatfeltöltése során.");
        }
    }

    private Boolean does_playersTableExist(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
            Statement statement = connection.createStatement();
            String query = "SELECT count(*) FROM information_schema.tables WHERE table_name = 'Players'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (!rs.getObject(1).equals(0)) {
                    return true;
                } 
            }        
        } catch (Exception e) {
            System.out.println("Hiba: ");
            e.printStackTrace();
        }
        return false;
    }

    private Boolean create_playersTable(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
            Statement statement = connection.createStatement();
            String query =  "CREATE TABLE Players (" +
                            "TechnicalID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                            "PlayerID int, " +
                            "ValidFrom date, " +
                            "ValidTo date, " +
                            "IsActive bit, " +
                            "SeasonID int, " +
                            "PlayerName varchar(50), " +
                            "LicenceNumber int, " +
                            "TeamID int, " +
                            "MatchesPlayed int, " +
                            "MatchesWon int, " +
                            "PlayerStrength int)";
            statement.executeUpdate(query);
            if (does_playersTableExist()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private Boolean sync_playersTable(Integer seasonID){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");

            for (int i = 0; i < this.playerListClass.playerList.size(); i++) {
                try(PreparedStatement ps = connection. prepareStatement("INSERT INTO Players VALUES(?, ?, NULL, ?, ?, ?, ?, ?, ?, ?, ?)")){
                    /*  "PlayerID int, " +
                        "ValidFrom date, " +
                        "ValidTo date, " +
                        "IsActive bit, " +
                        "SeasonID int, " +
                        "Name varchar(50), " +
                        "LicenceNumber int, " +
                        "TeamID int, " +
                        "MatchesPlayed int, " +
                        "MatchesWon int, " +
                        "PlayerStrength int)" */
                    ps.setInt(1, i+1);
                    ps.setString(2, LocalDate.now().toString());
                    ps.setInt(3, 1);
                    ps.setInt(4, seasonID);
                    ps.setString(5, this.playerListClass.playerList.get(i).getName());
                    ps.setInt(6, this.playerListClass.playerList.get(i).getLicenceNumber());
                    ps.setInt(7, this.playerListClass.playerList.get(i).getTeamID());
                    ps.setInt(8, this.playerListClass.playerList.get(i).getMatchesPlayed());
                    ps.setInt(9, this.playerListClass.playerList.get(i).getMatchesWon());
                    ps.setInt(10, this.playerListClass.playerList.get(i).getPlayerStrength());

                    ps.executeUpdate();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Integer get_currentSeasonID(){
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
