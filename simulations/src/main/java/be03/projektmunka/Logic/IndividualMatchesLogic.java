package be03.projektmunka.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import be03.projektmunka.Models.IndividualMatch;
import be03.projektmunka.Models.Player;
import be03.projektmunka.Models.Team;
import be03.projektmunka.Models.TeamMatch;

public class IndividualMatchesLogic extends RoundRobinLogic{
    private String[] resultsArray;
    private Integer cntWins_forHomeTeam;        //Hazai játékosok győzelme
    private Integer cntWins_forGuestTeam;       //Vendég játékosok győzelmei

    /*
     * HÁTRALÉVŐ FELADATOK:
     * osztályváltozók: 2 counter a csapatok győzelmeihez --- CHECK
     * for ciklus az egyes csapatmeccsekre --- CHECK
     * meccsek szimulálása
     * egyéni eredmények beírása
     * csapatmeccs eredményének beírása
     * statisztikai változók léptetése
     *      - egyén szintjén &
     *      - csapat szintjén is
     * 1 public void manage metódus, a többi pedig legyen private
     */

    public IndividualMatchesLogic() {
        this.resultsArray = new String[]{"3:0", "3:1", "3:2", "2:3", "1:3", "0:3"};
        cntWins_forHomeTeam = 0;            //Egy csapatmeccsen belül
        cntWins_forGuestTeam = 0;           //Egy csapatmeccsen belül
    }


    public void manage_individualMatches(){
        //Végiglépegetés a fordulókon és a csapatmeccseken
        Integer numberOfRounds = this.fixture.getRoundsArray().length;;
        for (int i = 0; i < numberOfRounds; i++) {                      //Fordulók
            Integer teamMatchesPerRounds = this.fixture.getRoundsArray()[0].getTeamMatchesArray().length;;
            for (int j = 0; j < teamMatchesPerRounds; j++) {            //Csapatmeccsek/forduló
                
                //Főbb osztályok "változósítása" a jobb átláthatóságért
                Team homeTeam = this.fixture.getRoundsArray()[i].getTeamMatchesArray()[j].getHomeTeam();
                Team guestTeam = this.fixture.getRoundsArray()[i].getTeamMatchesArray()[j].getGuestTeam();

                //A "DUMMY" csapat kiszűrése páratlan számú csapat esetén - lásd RoundRobinLogic.fillUp_roundRobinWithTeams
                if (homeTeam.getTeamID() != null && guestTeam.getTeamID() != null) {

                    //Egyéni meccs számlálók nullázása
                    this.cntWins_forHomeTeam = 0;
                    this.cntWins_forGuestTeam = 0;

                    //Játékosok kiválasztása csapatonként az adott meccsekhez
                    Integer homeTeamID = homeTeam.getTeamID();
                    Integer guestTeamID = guestTeam.getTeamID();
                    List<Integer> homePlayerIDsList = extract_playerIDsList(homeTeamID);
                    List<Integer> guestPlayerIDsList = extract_playerIDsList(guestTeamID);
                    //Játékosok hozzáadása az adott TeamMatch példányhoz
                    this.fixture.getRoundsArray()[i].getTeamMatchesArray()[j].
                                 setHomePlayersArray(pick_playersFor_teamMatch(homePlayerIDsList, homeTeamID));
                    this.fixture.getRoundsArray()[i].getTeamMatchesArray()[j].
                                 setGuestPlayersArray(pick_playersFor_teamMatch(guestPlayerIDsList, guestTeamID));                


                    //Egyéni meccsek az egyes csapatmeccseken belül -- szimuláció erősség alapján
                    manage_IndividualMatches(this.fixture.getRoundsArray()[i].getTeamMatchesArray()[j]);
                    

                    //Csapatok statiksztikáinak frissítése a végeredmény alapján
                    if (cntWins_forHomeTeam > cntWins_forGuestTeam) {           //HAZAI GYŐZELEM
                        homeTeam.hasWon();
                        guestTeam.hasLost();
                    }
                    if (cntWins_forHomeTeam < cntWins_forGuestTeam) {           //VENDÉG GYŐZELEM
                        homeTeam.hasLost();
                        guestTeam.hasWon();
                    }
                    if (cntWins_forHomeTeam == cntWins_forGuestTeam) {           //DÖNTETLEN
                        homeTeam.hasPlayedDraw();
                        guestTeam.hasPlayedDraw();
                    }

                    //Végeredmény beírása a TeamMatch példányba
                    this.fixture.getRoundsArray()[i].getTeamMatchesArray()[j].setTeamMatchResult(cntWins_forHomeTeam + " " + cntWins_forGuestTeam);
                }       //DUMMY feltétel vége

            }
        }
        System.out.println("Az egyéni meccsek szimulálása lefutott.");

    }

    //--------------------------------------------------------------
    //Segédmetódusok a játékosok egyes meccsekhez való kiválasztásához
    //A játékosok indexei random kiválasztáshoz
    public List<Integer> extract_playerIDsList(Integer teamID){
        List<Integer> playerIDsList = new ArrayList<>();
        List<Player> playersList = new ArrayList<>();
        //Csapat keresése ID alapján
        for (int i = 0; i < this.fixture.getTeamsList().size(); i++) {
            if (this.fixture.getTeamsList().get(i).getTeamID().equals(teamID)) {
                playersList = this.fixture.getTeamsList().get(i).getPlayers();
                //"Juhász Dániel"   "Varga Dávid"   "Mészáros Zsombor"  "Szűcs Boglárka"    "Mészáros Gellért"  "Tóth Piroska"
            }
        }
        for (int i = 0; i < playersList.size(); i++) {
            playerIDsList.add(playersList.get(i).getPlayerID());
        }
        return playerIDsList;
    }


    //Játékosok random kiválasztása az egyes csapatmeccsekre
    public Player[] pick_playersFor_teamMatch(List<Integer> playerIDsList, Integer teamID){
        Random rnd = new Random();
        Player[] teamPlayersArray = new Player[4];
        for (int i = 0; i < teamPlayersArray.length; i++) {
            Integer onePlayerID = playerIDsList.remove(rnd.nextInt(playerIDsList.size()));

            List<Player> playersInTeamList = new ArrayList<>();
            //Csapat keresése ID alapján
            for (int j = 0; j < this.fixture.getTeamsList().size(); j++) {
                if (this.fixture.getTeamsList().get(j).getTeamID().equals(teamID)) {
                    playersInTeamList = this.fixture.getTeamsList().get(j).getPlayers();
                }
            }
            //Játékos keresése ID alapján
            for (int j = 0; j < playersInTeamList.size(); j++) {
                if (playersInTeamList.get(j).getPlayerID().equals(onePlayerID)) {
                    teamPlayersArray[i] = playersInTeamList.get(j);
                }
            }
        }

        return teamPlayersArray;
    }


    //--------------------------------------------------------------
    //Segédmetódusok a meccsek szimulálásához
    private void manage_IndividualMatches(TeamMatch teamMatch){
        for (int i = 0; i < teamMatch.getHomePlayersArray().length; i++) {          //Hazai játékosok
            Player oneHomePlayer = teamMatch.getHomePlayersArray()[i];
            for (int j = 0; j < teamMatch.getGuestPlayersArray().length; j++) {     //Vendég játékosok
                Player oneGuestPlayer = teamMatch.getGuestPlayersArray()[j];

                Integer resultIdx = simulate_individualMatch(oneHomePlayer.getPlayerStrength(), oneGuestPlayer.getPlayerStrength());

                //Osztály szintű változók és PLAYER példányok változóinak frissítése az eredmény függvényében
                if (resultIdx <= 2) {
                    cntWins_forHomeTeam++;
                    oneHomePlayer.hasWon();
                    oneGuestPlayer.hasLost();
                }
                if (resultIdx >= 3) {
                    cntWins_forGuestTeam++;
                    oneHomePlayer.hasLost();
                    oneGuestPlayer.hasWon();
                }

                IndividualMatch one1V1match = new IndividualMatch(oneHomePlayer, oneGuestPlayer, resultsArray[resultIdx]);
                teamMatch.addIndividualMatch(one1V1match, i, j);

            }
        }
    }

    private Integer simulate_individualMatch(Integer homePlayerStrength, Integer guestPlayerStrength){
        Random rnd = new Random();
        Integer redultIdx = guestPlayerStrength - homePlayerStrength + rnd.nextInt(6);
        if (redultIdx < 0) {
            redultIdx = 0;
        }
        if (redultIdx > 5) {
            redultIdx = 5;
        }
        return redultIdx;
    }


}
