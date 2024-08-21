package be03.projektmunka.Logic;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import be03.projektmunka.Models.Player;
import be03.projektmunka.Models.Testing.TeamStats;
import be03.projektmunka.Repository.Repository;

public class DBwithSim_testingLogic {
    private Repository repository;
    private ResultSet rs;
    private List<TeamStats> teamStatsList;

    public DBwithSim_testingLogic() {
        this.repository = new Repository();
        this.rs = read_TeamsTableFull();
        create_teamStatList();
    }




    public List<Integer> matchesPlayed(){
        List<Integer> teams_withIncorrectStats = new ArrayList<>();
        try {
            while (this.rs.next()) {
                if (this.rs.getInt("MatchesPlayed") != 26) {
                    teams_withIncorrectStats.add(this.rs.getInt("TeamID"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teams_withIncorrectStats;
    }

    public List<Integer> points(){
        List<Integer> teamIDsWith_incorrectPoints = new ArrayList<>();
        for (int i = 0; i < this.teamStatsList.size(); i++) {
            if (this.teamStatsList.get(i).getPoints() != this.teamStatsList.get(i).getMatchesWon() * 2 + this.teamStatsList.get(i).getMatchesDraw()) {
                teamIDsWith_incorrectPoints.add(this.teamStatsList.get(i).getTeamID());
            }
        }
        return teamIDsWith_incorrectPoints;
    }

    public Boolean isWinLossEqual(){
        Integer winSum = 0;
        Integer lossSum = 0;
        for (int i = 0; i < this.teamStatsList.size(); i++) {
            winSum += this.teamStatsList.get(i).getMatchesWon();
            lossSum += this.teamStatsList.get(i).getMatchesLost();
        }
        if (winSum.equals(lossSum)) {
            return true;
        }
        return false;
    }

    public List<Integer> unequalWDL(){
        List<Integer> teamIDs = new ArrayList<>();
        for (TeamStats teamStat : this.teamStatsList) {
            if (!teamStat.getMatchesPlayed().equals(teamStat.getMatchesWon() + teamStat.getMatchesDraw() + teamStat.getMatchesLost())) {
                teamIDs.add(teamStat.getTeamID());
            }
        }
        return teamIDs;
    }

    public List<Integer> matchesPlayed_teamVSplayers(){
        List<Integer> teamIDs = new ArrayList<>();
        for (TeamStats teamStats : teamStatsList) {
            if ((teamStats.getMatchesPlayed() * 16) != sumOfMatches_byTeamPlayers(teamStats.getTeamID())) {
                teamIDs.add(teamStats.getTeamID());
            }
        }
        return teamIDs;
    }




    private ResultSet read_TeamsTableFull(){
        return this.repository.readFrom_table("*", "Teams");
    }

    private void create_teamStatList(){
        this.teamStatsList = new ArrayList<>();
        try {
            while (this.rs.next()) {
                Integer teamID = this.rs.getInt("TeamID");
                Integer matchesPlayed = this.rs.getInt("MatchesPlayed");
                Integer matchesWon = this.rs.getInt("MatchesWon");
                Integer matchesDraw = this.rs.getInt("MatchesDraw");
                Integer matchesLost = this.rs.getInt("MatchesLost");
                Integer points = this.rs.getInt("Points");

                TeamStats oneTeamStats = new TeamStats(teamID, matchesPlayed, matchesWon, matchesDraw, matchesLost, points);
                teamStatsList.add(oneTeamStats);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                Player onePlayer = new Player(playerID, playerName, licenceNumber, teamID, matchesPlayed, matchesWon, playerStrength);
                playerList.add(onePlayer);
            }
            return playerList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer sumOfMatches_byTeamPlayers(Integer teamID){
        List<Player> playerList = get_playerList_byTeamID(teamID);
        Integer matchSum = 0;
        for (Player player : playerList) {
            matchSum += player.getMatchesPlayed();
        }
        return matchSum;
    }

}
