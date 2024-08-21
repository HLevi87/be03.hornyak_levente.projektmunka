package be03.hornyak_levente.Models;

public class Team {
    //Csak tabellához
    private Integer teamID;
    private String teamName;
    private Integer matchesPlayed;
    private Integer matchesWon;
    private Integer matchesDraw;
    private Integer matchesLost;
    private Integer points;

    //Pluszban a csapatadatokhoz
    private String location;
    private String gameDay;
    private String gameTime;
    private String ball;
    private String email;
    private String phoneNumber;
    private String contactPersonName;

    //Pluszban egy csapat hozzáadásához
    private Integer seasonID;
    private Integer contactPersonID;
    private Integer contactDetailsID;
    private Integer locationID;
    private Integer ballID;
    private Player contactPerson;
    

    //KONSTRUKTOROK
    //Tabella
    public Team(Integer teamID, String teamName, Integer matchesPlayed, Integer matchesWon, Integer matchesDraw,
            Integer matchesLost, Integer points) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesDraw = matchesDraw;
        this.matchesLost = matchesLost;
        this.points = points;
    }

    //Csapatadatok
    public Team(Integer teamID, String teamName, String location, String gameDay,
            String gameTime, String ball, String email, String phoneNumber, String contactpersonName) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.location = location;
        this.gameDay = gameDay;
        this.gameTime = gameTime;
        this.ball = ball;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.contactPersonName = contactpersonName;
    }

    //Új csapat létrehozása
    public Team(String teamName, String location, String gameDay, String gameTime, String ball, String email,
            String phoneNumber, Integer seasonID) {
        this.teamName = teamName;
        this.location = location;
        this.gameDay = gameDay;
        this.gameTime = gameTime;
        this.ball = ball;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.seasonID = seasonID;
        this.matchesPlayed = 0;
        this.matchesWon = 0;
        this.matchesDraw = 0;
        this.matchesLost = 0;
        this.points = 0;
    }

    

    public void setContactPersonID(Integer contactPersonID) {
        this.contactPersonID = contactPersonID;
    }

    public void setTeamID(Integer teamID) {
        this.teamID = teamID;
    }

    public void setContactPerson(Player contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setSeasonID(Integer seasonID) {
        this.seasonID = seasonID;
    }

    public void setContactDetailsID(Integer contactDetailsID) {
        this.contactDetailsID = contactDetailsID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public void setBallID(Integer ballID) {
        this.ballID = ballID;
    }    



    public Player getContactPerson() {
        return contactPerson;
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

    public Integer getMatchesDraw() {
        return matchesDraw;
    }

    public Integer getMatchesLost() {
        return matchesLost;
    }

    public Integer getPoints() {
        return points;
    }

    public String getLocation() {
        return location;
    }

    public String getBall() {
        return ball;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public String getGameDay() {
        return gameDay;
    }

    public String getGameTime() {
        return gameTime;
    }
    
    public Integer getSeasonID() {
        return seasonID;
    }

    public Integer getContactPersonID() {
        return contactPersonID;
    }

    public Integer getContactDetailsID() {
        return contactDetailsID;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public Integer getBallID() {
        return ballID;
    }

 
    
}
