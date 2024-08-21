package be03.projektmunka.Models;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private Integer teamID;             //FONTOS! mert ezt használjuk a DUMMY (páratlan számú csapat esetén +1 ld. RoundRobin) azonosítására
    private Player contactPerson;
    private String teamName;
    private String emailAdress;
    private String phoneNumber;
    private String location;
    private String ball;
    private String gameDay;
    private String gameTime;
    private List<Player> players;
    private Integer matchesPlayed;
    private Integer matchesWon;
    private Integer matchesDraw;
    private Integer matchesLost;
    private Integer points;

    //Constructors
    public Team(Integer teamID, Player contactPerson, String teamName, String emailAdress, String phoneNumber,
    String location, String ball, String gameDay, String gameTime, List<Player> players, Integer matchesPlayed,
    Integer matchesWon, Integer matchesDraw, Integer matchesLost, Integer points) {
        this.teamID = teamID;
        this.contactPerson = contactPerson;
        this.teamName = teamName;
        this.emailAdress = emailAdress;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.ball = ball;
        this.gameDay = gameDay;
        this.gameTime = gameTime;
        this.players = players;

        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesDraw = matchesDraw;
        this.matchesLost = matchesLost;
        this.points = points;

    }
    //For dummy
    public Team(String teamName){
        this.teamName = teamName;
    }

    public void hasWon(){
        this.matchesPlayed++;
        this.matchesWon++;
        this.points += 2;
    }


    public void hasLost(){
        this.matchesPlayed++;
        this.matchesLost++;
    }

    public void hasPlayedDraw(){
        this.matchesPlayed++;
        this.matchesDraw++;
        this.points += 1;
    }


    //Getters 
    public Integer getTeamID() {
        return teamID;
    }

    public Player getContactPerson() {
        return contactPerson;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getBall() {
        return ball;
    }

    public String getGameDay() {
        return gameDay;
    }

    public String getGameTime() {
        return gameTime;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public Integer getMatchesDraw() {
        return matchesDraw;
    }

    public Integer getMatchesLost() {
        return matchesLost;
    }

    public Integer getPoints() {
        return points;
    }


    //Setters
    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }

    public void setMatchesDraw(Integer matchesDraw) {
        this.matchesDraw = matchesDraw;
    }

    public void setMatchesLost(Integer matchesLost) {
        this.matchesLost = matchesLost;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    
    //Add player
    public void add_player(Player player){
        this.players.add(player);
    }

    

}
