package be03.projektmunka.Models;

import java.util.List;

public class Round {
    private TeamMatch[] teamMatchesArray;
    private List<String> umpireList;


    //Constructor
    public Round(Integer matchesPerRound, List<String> umpireList) {
        this.teamMatchesArray = new TeamMatch[matchesPerRound];
        this.umpireList = umpireList;
    }


    //Getter
    public TeamMatch[] getTeamMatchesArray() {
        return teamMatchesArray;
    }

    public List<String> getUmpireList(){
        return umpireList;
    }
    
}
