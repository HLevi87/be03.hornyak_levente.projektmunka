package be03.projektmunka.Logic;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be03.projektmunka.Models.Round;
import be03.projektmunka.Models.Team;
import be03.projektmunka.Models.TeamMatch;



/*          INFO
 *  https://en.wikipedia.org/wiki/Round-robin_tournament
 * 
 * Round Robin 8 csapatra:
 * 1 2 3 4
 * 8 7 6 5
 * 1 forgatás után:
 * 1 8 2 3
 * 7 6 5 4
 * 
 */



public class RoundRobinLogic extends CreateFixtureLogic{
    private Team[] roundRobin;
    private Integer numberOfTeams;


    public void do_drawing(){
        this.numberOfTeams = this.fixture.getTeamsList().size();
        List<Integer> teamIDsList_shiftedBy1 = extract_teamIDs();
        fillUp_roundRobinWithTeams(teamIDsList_shiftedBy1);
        //Az őszi félév feltöltése - A "RoundsArray" csak páros szám lehet, ld. Fixture osztály konstruktora
        for (int i = 0; i < this.fixture.getRoundsArray().length / 2; i++) {
            // printOut_roundRobin();
            this.fixture.getRoundsArray()[i] = fillUp_round_fromRoundRobin(i % 2 == 0);
            this.roundRobin = rotate_roundRobin(this.roundRobin);
        }
        //A tavaszi félév feltöltése
        fillUp_mirrorRounds();
    }

    //A randomizáláshoz a csapatok ID-ját használom
    private List<Integer> extract_teamIDs(){
        List<Integer> teamIDsList_shiftedBy1 = new ArrayList<>();
        for (int i = 0; i < this.fixture.getTeamsList().size(); i++) {
            teamIDsList_shiftedBy1.add((this.fixture.getTeamsList().get(i).getTeamID()) - 1);
        }
        return teamIDsList_shiftedBy1;
    }

    private void fillUp_roundRobinWithTeams(List<Integer> teamIDsList_shiftedBy1){
        this.roundRobin = new Team[this.numberOfTeams];
        if (this.numberOfTeams % 2 != 0){
            this.roundRobin = new Team[this.numberOfTeams + 1];
        }

        Random rnd = new Random();
        for (int i = 0; i < roundRobin.length; i++) {
            if (i == roundRobin.length - 1 && this.numberOfTeams % 2 == 1) {
                //DUMMY páratlan számú csapat esetén
                roundRobin[i] = new Team("PIHENŐ HÉT");
            } else{
                Integer randomTeamIndex = teamIDsList_shiftedBy1.remove(rnd.nextInt(teamIDsList_shiftedBy1.size()));
                randomTeamIndex++;
                
                for (int j = 0; j < this.fixture.getTeamsList().size(); j++) {
                    //Csapat keresése ID alapján
                    if (this.fixture.getTeamsList().get(j).getTeamID().equals(randomTeamIndex)) {
                        roundRobin[i] = this.fixture.getTeamsList().get(j);
                    }
                }
            }
        }
    }

    private Round fillUp_round_fromRoundRobin(Boolean evenWeek){
        Random rnd = new Random();
        //VÁLTOZÓ: hány meccs van egy körben?
        Integer teamMatch_perRound = this.numberOfTeams / 2;
        if (this.numberOfTeams % 2 == 1) {
            teamMatch_perRound++;
        }

        Round oneRound = new Round(teamMatch_perRound, get_umpireList());
        for (int i = 0; i < oneRound.getTeamMatchesArray().length; i++) {
            if (this.roundRobin[i].getTeamID() != null && this.roundRobin[roundRobin.length - 1 - i].getTeamID() != null) {
                Team team1 = this.roundRobin[i];
                Team team2 = this.roundRobin[roundRobin.length - 1 - i];
                String randomUmpire = oneRound.getUmpireList().remove(rnd.nextInt(oneRound.getUmpireList().size()));
                TeamMatch oneTeamMatch;
                if (evenWeek){
                    oneTeamMatch = new TeamMatch(team1, team2, randomUmpire);
                } else {
                    oneTeamMatch = new TeamMatch(team2, team1, randomUmpire);
                }
                oneRound.getTeamMatchesArray()[i] = oneTeamMatch;
            }
        }
        return oneRound;
    }

    private Team[] rotate_roundRobin(Team[] roundRobin){
        Team[] roundRobin_rotated = roundRobin;
        Team lastItem = roundRobin_rotated[roundRobin_rotated.length-1];
        for (int j = roundRobin_rotated.length - 1; j > 1; j--) {
            roundRobin_rotated[j] = roundRobin_rotated[j-1];
        }
        roundRobin_rotated[1] = lastItem;
        return roundRobin_rotated;
    }

    public List<String> get_umpireList(){
        List<String> availableUmpiresList = new ArrayList<>();
        try {
            ResultSet rs = this.repository.readFrom_table("UmpireName", "Umpires", "1=1");
            while (rs.next()) {
                availableUmpiresList.add(rs.getString(1));
            }
            return availableUmpiresList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    private void printOut_roundRobin(){
        for (int i = 0; i < roundRobin.length / 2; i++) {
            System.out.print(roundRobin[i].getTeamID()  + "\t");
        }
        System.out.println();
        for (int i = roundRobin.length-1; i >= roundRobin.length / 2; i--) {
            System.out.print(roundRobin[i].getTeamID()  + "\t");
        }
        System.out.println("\n");
    }

    private void fillUp_mirrorRounds(){
        /*
         * Le kell másolnom a RoundsArray első felét
         * 
         */
        Random rnd = new Random();
        Integer teamMatch_perRound = this.getFixture().getRoundsArray()[0].getTeamMatchesArray().length;        //Minden körben ugyanannyi csapatmeccs van

        for (int i = 0; i < this.fixture.getRoundsArray().length / 2; i++) {
            //INDEX a fixture tavaszi félévében játszandó fordulókhoz (az őszi forduló + a tavaszi fordulólista [RoundsArray] hosszának a fele -- a "RoundsArray" csak páros szám lehet, ld. Fixture osztály konstruktora)
            Integer secondHalfIdx = (this.fixture.getRoundsArray().length / 2) + i;

            //Új forduló létrehozása
            Round oneRound = new Round(teamMatch_perRound, get_umpireList());
            //Egy forduló feltöltése meccsekkel
            for (int j = 0; j < oneRound.getTeamMatchesArray().length; j++) {
                //Aki ősszel otthon volt, az most idegenben lesz, és vice-verza
                Team team1 = this.fixture.getRoundsArray()[i].getTeamMatchesArray()[j].getGuestTeam();
                Team team2 = this.fixture.getRoundsArray()[i].getTeamMatchesArray()[j].getHomeTeam();
                String randomUmpire = oneRound.getUmpireList().remove(rnd.nextInt(oneRound.getUmpireList().size()));
                TeamMatch oneTeamMatch = new TeamMatch(team1, team2, randomUmpire);
                oneRound.getTeamMatchesArray()[j] = oneTeamMatch;
            }
            this.fixture.getRoundsArray()[secondHalfIdx] = oneRound;
        }
    }

}
