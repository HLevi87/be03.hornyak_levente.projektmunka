package be03.hornyak_levente.Models;

public class IndividualMatch {
    private String homePlayer;
    private String guestPlayer;
    private String result;


    public IndividualMatch(String homePlayer, String guestPlayer, String result) {
        this.homePlayer = homePlayer;
        this.guestPlayer = guestPlayer;
        this.result = result;
    }


    public String getHomePlayer() {
        return homePlayer;
    }


    public String getGuestPlayer() {
        return guestPlayer;
    }


    public String getResult() {
        return result;
    }

    

}
