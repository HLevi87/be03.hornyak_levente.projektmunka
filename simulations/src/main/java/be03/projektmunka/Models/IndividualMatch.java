package be03.projektmunka.Models;

public class IndividualMatch {
    private Player homePlayer;
    private Player guestPlayer;
    private String matchResult;

    
    //Constructor
    public IndividualMatch(Player homePlayer, Player guestPlayer, String matchResult) {
        this.homePlayer = homePlayer;
        this.guestPlayer = guestPlayer;
        this.matchResult = matchResult;
    }


    //Getters
    public Player getHomePlayer() {
        return homePlayer;
    }

    public Player getGuestPlayer() {
        return guestPlayer;
    }

    public String getMatchResult() {
        return matchResult;
    }


    //Setter
    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }





}
