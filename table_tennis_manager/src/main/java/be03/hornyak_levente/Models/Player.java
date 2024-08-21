package be03.hornyak_levente.Models;

public class Player {
    private Integer playerID;
    private String playerName;
    private Integer licenceNumber;
    private Integer teamID;
    private String teamName;
    private Integer matchesPlayed;
    private Integer matchesWon;
    private double percentage;


    //LEKÉRDEZÉSHEZ
    public Player(Integer playerID, String playerName, Integer licenceNumber, Integer teamID, String teamName,
            Integer matchesPlayed, Integer matchesWon, double percentage) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.licenceNumber = licenceNumber;
        this.teamID = teamID;
        this.teamName = teamName;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.percentage = percentage;
    }

    //ÚJ JÁTÉKOS HOZZÁADÁSÁHOZ
    public Player(String playerName, Integer licenceNumber) {
        this.playerName = playerName;
        this.licenceNumber = licenceNumber;
        this.matchesPlayed = 0;
        this.matchesWon = 0;
        this.percentage = 0.0;
    }


    public Integer getPlayerID() {
        return playerID;
    }


    public String getPlayerName() {
        return playerName;
    }


    public Integer getLicenceNumber() {
        return licenceNumber;
    }


    public Integer getTeamID() {
        return teamID;
    }


    public String getTeamName() {
        return teamName;
    }


    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }


    public Integer getMatchesWon() {
        return matchesWon;
    }


    public double getPercentage() {
        return percentage;
    }


    //SETTEREK
    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
    }


    
}
