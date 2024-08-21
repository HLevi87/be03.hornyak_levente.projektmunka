package be03.hornyak_levente.Models;

public class TeamMatch {
    private Integer teamMatchID;
    private String homeTeam;
    private String guestTeam;
    private Integer roundNumber;

    private Integer homeTeamID;
    private Integer guestTeamID;

    private Boolean isOver;
    private String result;

    private String umpire;
    private String gameDay;
    private String gameTime;
    private String location;
    private String ball;

    //Konstruktor sorsoláshoz
    public TeamMatch(Integer teamMatchID, String homeTeam, String guestTeam, Integer roundNumber, Boolean isOver,
            String result, String umpire, String gameDay, String gameTime, String location, String ball) {

        this.teamMatchID = teamMatchID;
        this.homeTeam = homeTeam;
        this.guestTeam = guestTeam;
        this.roundNumber = roundNumber;
        this.isOver = isOver;
        this.result = result;
        this.umpire = umpire;
        this.gameDay = gameDay;
        this.gameTime = gameTime;
        this.location = location;
        this.ball = ball;
    }

    //Konstruktor tabellához - döntetlen esetére
    public TeamMatch(Integer teamMatchID, Integer homeTeamID, Integer guestTeamID, String homeTeam, String guestTeam, String result) {
        this.teamMatchID = teamMatchID;
        this.homeTeamID = homeTeamID;
        this.guestTeamID = guestTeamID;
        this.homeTeam = homeTeam;
        this.guestTeam = guestTeam;
        this.result = result;
    }


    public Integer getTeamMatchID() {
        return teamMatchID;
    }


    public String getHomeTeam() {
        return homeTeam;
    }


    public String getGuestTeam() {
        return guestTeam;
    }


    public Integer getRoundNumber() {
        return roundNumber;
    }


    public Boolean getIsOver() {
        return isOver;
    }


    public String getResult() {
        return result;
    }


    public String getUmpire() {
        return umpire;
    }


    public String getGameDay() {
        return gameDay;
    }


    public String getGameTime() {
        return gameTime;
    }


    public String getLocation() {
        return location;
    }


    public String getBall() {
        return ball;
    }

    public Integer getHomeTeamID() {
        return homeTeamID;
    }

    public Integer getGuestTeamID() {
        return guestTeamID;
    }

    

}
