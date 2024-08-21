package be03.projektmunka.Logic;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import be03.projektmunka.Models.Fixture;
import be03.projektmunka.Models.Player;
import be03.projektmunka.Models.Team;
import be03.projektmunka.Repository.Repository;

public class CreateFixtureLogic {
    protected Fixture fixture;
    
    protected Repository repository;

    public CreateFixtureLogic() {
        this.repository = new Repository();
        List<Team> teamList = get_teamList();
        this.fixture = new Fixture(repository.getSeason(), get_numberOfTeams(), teamList);
    }

    public CreateFixtureLogic(String season) {
        this.repository = new Repository();
        List<Team> teamList = get_teamList();
        this.fixture = new Fixture(season, get_numberOfTeams(), teamList);
    }

    public Integer get_numberOfTeams(){
        try {
            ResultSet rs = repository.readFrom_table("COUNT(*)", "Teams");
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Team> get_teamList(){
        List<Team> teamList = new ArrayList<>();
        try {
            ResultSet rs = repository.read_teamsToClass();
            while (rs.next()) {
                /*
                 *      t.TeamlID
                        p.PlayerID
                        t. TeamName
                        cd.Email
                        cd.PhoneNumber
                        l.Location
                        b.Ball
                        t.GameDay
                        t.GameTime
                        //Ide jönnek a játékosok!!!
                        t.MatchesPlayed
                        t.MatchesWon
                        t.MatchesDraw
                        t.MatchesLost
                        t.Points
                 */
                Integer teamID = rs.getInt(1);
                Player contactPerson = get_player_byPlayerID(rs.getInt(2));
                String teamName = rs.getString(3);
                String emailAdress = rs.getString(4);
                String phoneNumber = rs.getString(5);
                String location = rs.getString(6);
                String ball = rs.getString(7);
                String gameDay = rs.getString(8);
                String gameTime = rs.getString(9);
                List<Player> players = get_playerList_byTeamID(teamID);
                Integer matchesPlayed = rs.getInt(10);
                Integer matchesWon = rs.getInt(11);
                Integer matchesDraw = rs.getInt(12);
                Integer matchesLost = rs.getInt(13);
                Integer points = rs.getInt(14);
                
                Team oneTeam = new Team(teamID, contactPerson, teamName, emailAdress, 
                                        phoneNumber, location, ball, gameDay, gameTime, players,
                                        0, 0, 0, 0, 0);
                teamList.add(oneTeam);
            }

            return teamList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Player get_player_byPlayerID(Integer playerID){
        try {
            ResultSet rs = repository.readFrom_table(
                "PlayerName, LicenceNumber, TeamID, MatchesPlayed, MatchesWon, PlayerStrength", 
                "Players", "TechnicalID = " + playerID);
            while (rs.next()) {
                //A playerID már megvan: paraméter
                String playerName = rs.getString("PlayerName");
                Integer licenceNumber = rs.getInt("LicenceNumber");
                Integer teamID = rs.getInt("TeamID");
                Integer matchesPlayed = rs.getInt("MatchesPlayed");
                Integer matchesWon = rs.getInt("MatchesWon");
                Integer playerStrength = rs.getInt("PlayerStrength");
                return new Player(playerID, playerName, licenceNumber, teamID, matchesPlayed, matchesWon, playerStrength);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Player> get_playerList_byTeamID(Integer teamID){
        List<Player> playerList = new ArrayList<>();
        try {
            ResultSet rs = repository.readFrom_table(
                "PlayerID, PlayerName, LicenceNumber, TeamID, MatchesPlayed, MatchesWon, PlayerStrength", 
                "Players", "TeamID = " + teamID);
            while (rs.next()) {
                Integer playerID = rs.getInt("PlayerID");
                String playerName = rs.getString("PlayerName");
                Integer licenceNumber = rs.getInt("LicenceNumber");
                //A teamID már megvan: paraméter
                Integer matchesPlayed = rs.getInt("MatchesPlayed");
                Integer matchesWon = rs.getInt("MatchesWon");
                Integer playerStrength = rs.getInt("PlayerStrength");
                Player onePlayer = new Player(playerID, playerName, licenceNumber, teamID, 0, 0, playerStrength);
                playerList.add(onePlayer);
            }
            return playerList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Fixture getFixture() {
        return fixture;
    }

}
