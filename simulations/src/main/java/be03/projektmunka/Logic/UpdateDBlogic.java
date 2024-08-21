package be03.projektmunka.Logic;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import be03.projektmunka.Models.IndividualMatch;
import be03.projektmunka.Models.Player;
import be03.projektmunka.Models.Team;
import be03.projektmunka.Models.TeamMatch;
import be03.projektmunka.Repository.Repository;

public class UpdateDBlogic extends IndividualMatchesLogic{
    private Repository repository;
    private String season;

    public UpdateDBlogic() {
        this.repository = new Repository();
    }

    public void update_dataBase_WITHdelete(String season){
        this.season = season;
        //Frissítés csak akkor, ha a törlések sikeresek voltak
        if (delete_FixtureRecords().equals(0) && delete_MatchResultsOneVOneRecords().equals(0)) {
            update_dataBase(this.season);
        } else{
            System.out.println("Az adatbázistáblák bejegyzéseinek törlése nem sikerült.\n");
        }
    }

    public void update_dataBase(String season){
        /*
         * FRISSÍTÉS EZEKBEN A TÁBLÁKBAN:
         * 
         * !!!Külön kell venni a 2-féle meccseredményeket, ILLETVE a csapatok és a játékosok frissítéseit!!!
         * 
         * Fixture                  - Round Robin + csapatmeccseredmények
         * Teams                    - played, won, draw, lost, points
         * Players                  - played, won
         * MatchResultsOneVOne      - szemuláció + eredmények
         */
        //Az összes firrsített játékos kilistázása ellenőrzéshez
        this.season = season;
        List<Player> allPlayersList = new ArrayList<>();


        //Csapatok és játékosok frissítése
        List<Team> teamList = this.fixture.getTeamsList();
        for (int i = 0; i < teamList.size(); i++) {
            Integer checkInteger = update_team(teamList.get(i));
            //Csapatok
            if (checkInteger != 5) {
                System.out.println("Hiba történt a csapat statjainak frissítésekor:\nCsapatnév: " + teamList.get(i).getTeamName());
            }

            //Játékosok
            List<Player> playerList = this.fixture.getTeamsList().get(i).getPlayers();
            for (int j = 0; j < playerList.size(); j++) {
                checkInteger = update_players(playerList.get(j));
                if (checkInteger != 2) {
                    System.out.println("Hiba történt a játékos statjainak frissítésekor:\nJátékosnév: " + playerList.get(j).getPlayerName());
                }
                allPlayersList.add(playerList.get(j));
            }


        }

        int numberOfRounds = this.fixture.getNumberOfRounds();
        for (int i = 0; i < numberOfRounds; i++) {                                                      //Fordulók
            int matchesPerRound = this.fixture.getRoundsArray()[i].getTeamMatchesArray().length;
            for (int j = 0; j < matchesPerRound; j++) {                                                 //Csapatmeccsek/forduló
                TeamMatch oneTeamMatch = this.fixture.getRoundsArray()[i].getTeamMatchesArray()[j];
                Team homeTeam = oneTeamMatch.getHomeTeam();
                Team guestTeam = oneTeamMatch.getGuestTeam();

                //Fixture frissítése
                int checkInteger = update_fixture(oneTeamMatch, i+1);
                if (checkInteger != 1) {
                    System.out.println("Sor létrehozása a Fixture táblában nem sikerült:\nKör: " + i+1 +
                                         ", meccs: " + homeTeam.getTeamName() + " - " + guestTeam.getTeamName() + "\nUtolsó TeamMatchID: " + fetch_latestTeamMatchID());
                }

                //IndividualMatches frissítése
                IndividualMatch[][] indiMatchArray = oneTeamMatch.getIndividualMatches();
                for (int k = 0; k < indiMatchArray.length; k++) {                //SOROK     - hazai játékosok
                    for (int l = 0; l < indiMatchArray[0].length; l++) {         //OSZLOPOK  - vendég játékosok
                        checkInteger = update_individualMatch(indiMatchArray[k][l], fetch_latestTeamMatchID());
                        if (checkInteger != 1) {
                            System.out.println("Sor létrehozása a MatchResultsOneVOne táblában nem sikerült:\nKör: " + i+1 +
                                                 ", meccs: " + homeTeam.getTeamName() + " - " + guestTeam.getTeamName() + "\n" +
                                                 "TeamMatchID: " + fetch_latestTeamMatchID() +
                                                 " Hazai játékos: " + indiMatchArray[k][l].getHomePlayer() + " - Vendég játékos: " + indiMatchArray[k][l].getGuestPlayer());
                        }
                    }
                }
                
            }
        }
        //Ellenőrzés
        check_singularPlayerIDs(allPlayersList);
        System.out.println("Az adatbázis frissítése sikseresen megtörtént.\n");
    }



    //----------------------------------------------------------------------------------------------

    private Integer update_team(Team team){
        Integer matchesPlayed = team.getMatchesPlayed();
        Integer matchesWon = team.getMatchesWon();
        Integer matchesDraw = team.getMatchesDraw();
        Integer matchesLost = team.getMatchesLost();
        Integer points = team.getPoints();

        int cnt = 0;
        String conditionString = "TeamID = " + team.getTeamID() + " AND IsActive = 1";
        try {
            cnt += this.repository.update_table("Teams", "MatchesPlayed", matchesPlayed.toString(), conditionString);
            cnt += this.repository.update_table("Teams", "MatchesWon", matchesWon.toString(), conditionString);
            cnt += this.repository.update_table("Teams", "MatchesDraw", matchesDraw.toString(), conditionString);
            cnt += this.repository.update_table("Teams", "MatchesLost", matchesLost.toString(), conditionString);
            cnt += this.repository.update_table("Teams", "Points", points.toString(), conditionString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt;
    }

    private Integer update_players(Player player){
        Integer matchesPlayed = player.getMatchesPlayed();
        Integer matchesWon = player.getMatchesWon();

        int cnt = 0;
        String conditionString = "PlayerID = " + player.getPlayerID() + " AND IsActive = 1";
        try {
            cnt += this.repository.update_table("Players", "MatchesPlayed", matchesPlayed.toString(), conditionString);
            cnt += this.repository.update_table("Players", "MatchesWon", matchesWon.toString(), conditionString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt;
    }

    private Integer update_fixture(TeamMatch teamMatch, Integer roundNumber){
        //UmpireID & SeasonID
        Integer seasonID = fetch_seasonID();
        Integer umpireID = null;
        try {
            //UmpireID
            ResultSet rs = this.repository.readFrom_table("UmpireID", "Umpires", "UmpireName = '" + teamMatch.getUmpireName() + "'");
            while (rs.next()) {
                umpireID = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Integer homeTeamID = teamMatch.getHomeTeam().getTeamID();
        Integer guestTeamID = teamMatch.getGuestTeam().getTeamID();
        String result = teamMatch.getTeamMatchResult();
        return this.repository.insertInto_Fixture(seasonID, homeTeamID, guestTeamID, umpireID, roundNumber, this.season, result);
    }

    private Integer update_individualMatch(IndividualMatch match, Integer teamMatchID){
        Integer seasonID = fetch_seasonID();
        Integer homePlayerID = match.getHomePlayer().getPlayerID();
        Integer guestPlayerID = match.getGuestPlayer().getPlayerID();
        String result = match.getMatchResult();

        return this.repository.insertInto_MatchResultsOneVOne(seasonID, teamMatchID, homePlayerID, guestPlayerID, result);
    }

    //----------------------------------------------------------------------------------------------


    //SEGÉDMETÓDUSOK

    private Integer fetch_latestTeamMatchID(){
        Integer teamMatchID = 0;
        try {
            ResultSet rs = this.repository.readFrom_table("MAX(TeamMatchID)", "Fixture");
            while (rs.next()) {
                teamMatchID = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamMatchID;
    }

    private Integer fetch_seasonID(){
        Integer seasonID = 0;
        try {
            ResultSet rs = this.repository.readFrom_table("SeasonID", "Seasons", "SeasonName = '" + this.season + "'");
            while (rs.next()) {
                seasonID = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seasonID;
    }

    private Integer delete_FixtureRecords(){
        //Az adott évad (csapatmeccs)sorsolásának törlése
        try {
            this.repository.deleteFrom_table("Fixture", "SeasonID = " + fetch_seasonID());
            ResultSet rs = this.repository.readFrom_table("COUNT(*)", "Fixture","SeasonID = " + fetch_seasonID() );
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Integer delete_MatchResultsOneVOneRecords(){
        //Az adott évad egyéni meccseinek törlése
        try {
            this.repository.deleteFrom_table("MatchResultsOneVOne", "SeasonID = " + fetch_seasonID());
            ResultSet rs = this.repository.readFrom_table("COUNT(*)", "MatchResultsOneVOne","SeasonID = " + fetch_seasonID() );
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //TESZTELÉSHEZ
    private void check_singularPlayerIDs(List<Player> playerList){
        //Sorba rendezés
        for (int i = 0; i < playerList.size(); i++) {
            for (int j = 0; j < playerList.size() - 1 - i; j++) {
                if (playerList.get(j).getPlayerID() > playerList.get(j+1).getPlayerID()) {
                    Player temp = playerList.get(j);
                    playerList.set(j, playerList.get(j+1));
                    playerList.set(j+1, temp);
                }
            }
        }
        
        //Ismétlődés ellenőrzése
        for (int i = 0; i < playerList.size() - 1; i++) {
            if (playerList.get(i).getPlayerID().equals(playerList.get(i+1).getPlayerID())) {
                System.out.println("Ez a játékos már volt: " + playerList.get(i));
            }
        }
    }

}

