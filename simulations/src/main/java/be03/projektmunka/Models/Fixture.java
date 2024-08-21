package be03.projektmunka.Models;

import java.util.ArrayList;
import java.util.List;

public class Fixture {
    private Round[] roundsArray;
    private String season;
    private Integer numberOfRounds;
    private List<Team> teamsList;

    //Constructor
    public Fixture(String season, Integer numberOfTeams, List<Team> teamsList) {
        this.season = season;
        initialize_numberOfRounds(numberOfTeams);
        this.roundsArray = new Round[this.numberOfRounds];
        this.teamsList = teamsList;
    }


    private void initialize_numberOfRounds(Integer numberOfTeams){
        if (numberOfTeams % 2 == 0) {
            this.numberOfRounds = (numberOfTeams - 1) * 2;
        } else{
            this.numberOfRounds = numberOfTeams * 2;
        }
    }


    public Round[] getRoundsArray() {
        return roundsArray;
    }

    public String getSeason() {
        return season;
    }

    public Integer getNumberOfRounds() {
        return numberOfRounds;
    }

    public List<Team> getTeamsList() {
        return teamsList;
    }


    
}
