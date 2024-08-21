package be03.projektmunka.Models;

public class Player {
    
    private Integer playerID;
    private String playerName;
    private Integer licenceNumber;
    private Integer teamID;
    private Integer matchesPlayed;
    private Integer matchesWon;
    private Double winPercentage;
    private Integer playerStrength;


    //Constructor
    public Player(Integer playerID, String playerName, Integer licenceNumber, Integer teamID,
            Integer matchesPlayed, Integer matchesWon, Integer playerStrength) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.licenceNumber = licenceNumber;
        this.teamID = teamID;

        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        
        this.winPercentage = calculate_winPercentage();           //Nem kell példányosításnál megadni
        this.playerStrength = playerStrength;
    }

    //Win percentage
    public Double calculate_winPercentage(){
        if (this.matchesPlayed.equals(0)) {
            return 0.0;
        }
        return winPercentage = (double) this.matchesWon / this.matchesPlayed;
    }

    public void hasWon(){
        this.matchesPlayed++;
        this.matchesWon++;
        this.winPercentage = calculate_winPercentage();
    }

    public void hasLost(){
        this.matchesPlayed++;
        this.winPercentage = calculate_winPercentage();
    }

    //Getters
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

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public Double getWinPercentage() {
        return winPercentage;
    }

    public Integer getPlayerStrength() {
        return playerStrength;
    }


    //Setters
    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }
 

}
