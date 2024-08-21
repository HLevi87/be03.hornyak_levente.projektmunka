package be03.projektmunka.UI;

import be03.projektmunka.Logic.IndividualMatchesLogic;
import be03.projektmunka.Models.IndividualMatch;
import be03.projektmunka.Models.Player;
import be03.projektmunka.Models.Team;
import be03.projektmunka.Models.TeamMatch;

public class SimulationsUI {
    private IndividualMatchesLogic individualMatchesLogic;

    public SimulationsUI() {
        this.individualMatchesLogic = new IndividualMatchesLogic();
        this.individualMatchesLogic.do_drawing();
        this.individualMatchesLogic.manage_individualMatches();
    }

    // public void printOut_FULLsimulation(){
    //     System.out.println("Szezon: " + this.individualMatchesLogic.getFixture().getSeason());
    //     this.individualMatchesLogic.getFixture().getNumberOfRounds()
    // }

    public void oneTeamMatch_wExternalDetails(Integer roundNo, Integer teamMatchNo){
        TeamMatch oneTeamMatch = this.individualMatchesLogic.getFixture().getRoundsArray()[roundNo].getTeamMatchesArray()[teamMatchNo];
        printOut_ONEteamMatch(oneTeamMatch);
    }

    public void printOut_ONEteamMatch(TeamMatch teamMatch){
        //VÁLTOZÓK
        Team homeTeam = teamMatch.getHomeTeam();
        Team guestTeam = teamMatch.getGuestTeam();
        IndividualMatch[][] individualMatchesArray = teamMatch.getIndividualMatches();
        Player[] homePlayersArray = teamMatch.getHomePlayersArray();
        Player[] guestPlayersArray = teamMatch.getGuestPlayersArray();
        String umpireName = teamMatch.getUmpireName();
        String teamMatchResult = teamMatch.getTeamMatchResult();
        
        //ÁLTALÁNOS KIÍRATÁS
        System.out.println(homeTeam.getTeamName() + "\t" + teamMatchResult.replace(" ", ":") + "\t" + guestTeam.getTeamName());
        System.out.println("Időpont: " + homeTeam.getGameDay() + " " + homeTeam.getGameTime());
        System.out.println("Helyszín: " + homeTeam.getLocation());
        System.out.println("Játékvezető: " + umpireName);
        System.out.println();

        System.out.println("Hazai játékosok: Név - ID  (Erősség)" );
        for (int i = 0; i < homePlayersArray.length; i++) {
            System.out.print(i+1 + ": " + homePlayersArray[i].getPlayerName() + " - " + homePlayersArray[i].getPlayerID() + "  (" + homePlayersArray[i].getPlayerStrength() + ")\n");
        }
        System.out.println("\n");

        System.out.println("Vendég játékosok: Név - ID  (Erősség)" );
        for (int i = 0; i < guestPlayersArray.length; i++) {
            System.out.print(i+1 + ": " + guestPlayersArray[i].getPlayerName() + " - " + guestPlayersArray[i].getPlayerID() + "  (" + guestPlayersArray[i].getPlayerStrength() + ")\n");
        }
        System.out.println("\n");

        //EREDMÉNY KIÍRATÁS
        for (int i = 0; i < homePlayersArray.length + 1; i++) {              //SOROK - Hazai játékosok
            for (int j = 0; j < guestPlayersArray.length + 1; j++) {         //OSZLOPOK - Vendég játékosok
                //A legelső sor feltöltése a vedég játékosok sorszámával
                if (i==0 && j>0) {
                    System.out.print(" " + j + "\t");
                //A bal felső sarok üresen hagyása
                } else if (i==0 && j==0) {
                    System.out.print("\t");
                } else {
                    //A hazai játékosok sorszámai az első oszlopban
                    if (j==0) {
                        System.out.print(i + "\t");
                    //Az eredmények
                    } else{
                        System.out.print(individualMatchesArray[i-1][j-1].getMatchResult() + " \t");
                    }
                }
            }
            System.out.println();   
        }
        //Teszteléshez
        // System.out.println("A legelső meccs adatai:     " +
        //                         "hazai - " + individualMatchesArray[0][0].getHomePlayer().getPlayerName() +
        //                          "    vendég: " + individualMatchesArray[0][0].getGuestPlayer().getPlayerName());
        // System.out.println();
        // System.out.println("Végeredmény: " + teamMatch.getTeamMatchResult() + "\n");
        System.out.println();
    }

    

}
