package be03.hornyak_levente.Models;

import java.util.List;

public class Round {
    List<TeamMatch> teamMatches;

    public Round(List<TeamMatch> teamMatches) {
        this.teamMatches = teamMatches;
    }

    public List<TeamMatch> getTeamMatches() {
        return teamMatches;
    }

    

    
}
