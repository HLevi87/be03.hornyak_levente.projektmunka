package be03.projektmunka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import be03.projektmunka.Logic.CreateFixtureLogic;
import be03.projektmunka.Logic.IndividualMatchesLogic;
import be03.projektmunka.Logic.RoundRobinLogic;
import be03.projektmunka.Models.Player;
import be03.projektmunka.Repository.Repository;

public class AppTest 
{
    
    /*
     * 1. TESZT - fourPlayers(): adott csapat négy játékossal áll ki?
     * 2. TESZT - umpireList(): a játékvezetők száma nagyobb, mint 0?
     * 3. TESZT - teamMatchCount(): a csapatmeccsek száma egyenlő n*(n-1)-gyel, ha n a csapatok száma?
     */

    @Test
    public void fourPlayers(){
        IndividualMatchesLogic individualMatchesLogic = new IndividualMatchesLogic();
        Random rnd = new Random();
        int teamID = rnd.nextInt(1, 15);
        int numberOfPlayers = individualMatchesLogic.pick_playersFor_teamMatch(individualMatchesLogic.extract_playerIDsList(teamID), teamID).length;
        assertEquals(4, numberOfPlayers);
    }

    //CreateFixtureLogic PlayerbyID

    @Test
    public void umpireList(){
        RoundRobinLogic roundRobinLogic = new RoundRobinLogic();
        List<String> umpireList = roundRobinLogic.get_umpireList();
        assertTrue(umpireList.size() > 0);
    }

    @Test
    public void teamMatchCount(){
        Repository repository = new Repository();

        //Csapatok száma
        ResultSet rs = repository.readFrom_table("COUNT(*)", "Teams", "SeasonId = 1");
        Integer numberOfTeams= 0;
        try {
            if (rs.next()) {
                numberOfTeams = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Csapatmeccsek száma
        rs = repository.readFrom_table("COUNT(*)", "Fixture", "SeasonId = 1");
        Integer numberOfMatches= 0;
        try {
            if (rs.next()) {
                numberOfMatches = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Integer expected = numberOfTeams * (numberOfTeams - 1);

        assertEquals(numberOfMatches, expected);
    }
}
