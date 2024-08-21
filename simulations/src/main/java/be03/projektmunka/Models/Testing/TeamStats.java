package be03.projektmunka.Models.Testing;

public class TeamStats {
    private Integer teamID;
    private Integer matchesPlayed;
    private Integer matchesWon;
    private Integer matchesDraw;
    private Integer matchesLost;
    private Integer points;


    public TeamStats(Integer teamID, Integer matchesPlayed, Integer matchesWon, Integer matchesDraw,
            Integer matchesLost, Integer points) {
        this.teamID = teamID;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesDraw = matchesDraw;
        this.matchesLost = matchesLost;
        this.points = points;
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


    public Integer getMatchesDraw() {
        return matchesDraw;
    }


    public Integer getMatchesLost() {
        return matchesLost;
    }


    public Integer getPoints() {
        return points;
    }

    
    
}
