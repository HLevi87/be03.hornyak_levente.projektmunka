package be03.hornyak_levente.Models;

public class Player {
    private String name;
    private Integer licenceNumber;
    private Integer teamID;
    private Integer matchesPlayed;
    private Integer matchesWon;
    private Double winPercentage;
    private Integer playerStrength;


    public Player(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Integer getLicenceNumber() {
        return licenceNumber;
    }


    public void setLicenceNumber(Integer licenceNumber) {
        this.licenceNumber = licenceNumber;
    }


    public Integer getTeamID() {
        return teamID;
    }


    public void setTeamID(Integer teamID) {
        this.teamID = teamID;
    }


    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }


    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }


    public Integer getMatchesWon() {
        return matchesWon;
    }


    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }


    public Double getWinPercentage() {
        return winPercentage;
    }


    public void setWinPercentage(Double winPercentage) {
        this.winPercentage = winPercentage;
    }


    public Integer getPlayerStrength() {
        return playerStrength;
    }


    public void setPlayerStrength(Integer playerStrength) {
        this.playerStrength = playerStrength;
    }

    

}
