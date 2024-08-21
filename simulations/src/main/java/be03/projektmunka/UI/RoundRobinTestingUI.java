package be03.projektmunka.UI;

import be03.projektmunka.Logic.RoundRobinLogic;
import be03.projektmunka.Models.Round;
import be03.projektmunka.Models.TeamMatch;

public class RoundRobinTestingUI {
    private RoundRobinLogic roundRobinClass;

    public RoundRobinTestingUI() {
        this.roundRobinClass = new RoundRobinLogic();
        roundRobinClass.do_drawing();
    }

    public void printOut_drawing_byTeamName(){
        System.out.println("\nMérkőzések fordulónként csapatnevekkel\n");
        Round[] rounds = roundRobinClass.getFixture().getRoundsArray();
        for (int i = 0; i < rounds.length; i++) {                                        //Fordulók
            System.out.println(i+1 + ". forduló");
            TeamMatch[] teamMatchesArray = rounds[i].getTeamMatchesArray();              //Csapatmeccsek
            for (int j = 0; j < teamMatchesArray.length; j++) {
                String homeTeamName = teamMatchesArray[j].getHomeTeam().getTeamName();
                String guestTeamName = teamMatchesArray[j].getGuestTeam().getTeamName();
                System.out.println("\t\t" + homeTeamName + " - " + guestTeamName);
            }
            System.out.println();
        }
    }

    public void printOut_drawing_byTeamID(){
        System.out.println("\nMérkőzések fordulónként csapatID-kkal\n");
        Round[] rounds = roundRobinClass.getFixture().getRoundsArray();
        for (int i = 0; i < rounds.length; i++) {                                        //Fordulók
            System.out.println(i+1 + ". forduló");
            TeamMatch[] teamMatchesArray = rounds[i].getTeamMatchesArray();              //Csapatmeccsek
            for (int j = 0; j < teamMatchesArray.length; j++) {
                if (teamMatchesArray[j].getHomeTeam().getTeamName() == "PIHENŐ HÉT") {
                    System.out.println("\t\t" + teamMatchesArray[j].getGuestTeam().getTeamID() + " - PIHENŐ HÉT");
                } else if (teamMatchesArray[j].getGuestTeam().getTeamName() == "PIHENŐ HÉT") {
                    System.out.println("\t\t" + teamMatchesArray[j].getHomeTeam().getTeamID() + " - PIHENŐ HÉT");
                } else{
                    Integer homeTeamID = teamMatchesArray[j].getHomeTeam().getTeamID();
                    Integer geustTeamID = teamMatchesArray[j].getGuestTeam().getTeamID();
                    System.out.println("\t\t" + homeTeamID + " - " + geustTeamID);
                }
                
            }
            System.out.println();
        }
    }
    
}
