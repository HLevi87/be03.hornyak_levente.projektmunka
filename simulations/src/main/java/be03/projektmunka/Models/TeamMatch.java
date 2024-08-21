package be03.projektmunka.Models;

public class TeamMatch {
    //Valamelyik 'PIHENŐ HÉT' lehet!!!! - a round robin rendszerű sorsolás miatt (ha páratlan számú csapat van)
    private Team homeTeam;
    private Team guestTeam;

    private IndividualMatch[][] individualMatchesArray;
    private Player[] homePlayersArray;
    private Player[] guestPlayersArray;
    private String umpireName;
    private String teamMatchResult;


    //Constructor
    public TeamMatch(Team homeTeam, Team guestTeam, String umpireName) {
        this.homeTeam = homeTeam;
        this.guestTeam = guestTeam;
        this.umpireName = umpireName;
        this.individualMatchesArray = new IndividualMatch[4][4]; //Nem az elején kerül hozzáadásra - létrehozáskor még nem tudni, melyik 4 játékos játszik
        this.homePlayersArray = new Player[4];                        //Nem az elején kerül hozzáadásra - létrehozáskor még nem tudni, melyik 4 játékos játszik
        this.guestPlayersArray = new Player[4];                       //Nem az elején kerül hozzáadásra - létrehozáskor még nem tudni, melyik 4 játékos játszik
        //teamMatchResult                                        //Nem az elején kerül hozzáadásra
    }


    //Getters
    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getGuestTeam() {
        return guestTeam;
    }

    public IndividualMatch[][] getIndividualMatches() {
        return individualMatchesArray;
    }

    public String getUmpireName() {
        return umpireName;
    }

    public String getTeamMatchResult() {
        return teamMatchResult;
    }

    public Player[] getHomePlayersArray(){
        return homePlayersArray;
    }

    public Player[] getGuestPlayersArray(){
        return guestPlayersArray;
    }


    //Setter
    public void setHomePlayersArray(Player[] homePlayers) {
        this.homePlayersArray = homePlayers;
    }

    public void setGuestPlayersArray(Player[] guestPlayers) {
        this.guestPlayersArray = guestPlayers;
    }

    public void setTeamMatchResult(String teamMatchResult) {
        this.teamMatchResult = teamMatchResult;
    }

    
    
    
    //Add individual match
    public void addIndividualMatch(IndividualMatch individualMatch, Integer homePlayer, Integer guestPlayer){
        this.individualMatchesArray[homePlayer][guestPlayer] = individualMatch;
    }


    
}
