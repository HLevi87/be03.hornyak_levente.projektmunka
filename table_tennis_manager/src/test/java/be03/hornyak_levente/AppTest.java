package be03.hornyak_levente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import be03.hornyak_levente.Logic.MenuLogic;
import be03.hornyak_levente.Models.Round;
import be03.hornyak_levente.Models.Team;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void numberOfRounds(){
        MenuLogic menuLogic = new MenuLogic();
        List<Round> roundList = menuLogic.request_roundList(1);
        Integer expected = 26;
        Integer roundListSize = roundList.size();
        assertEquals(expected, roundListSize);
    }

    @Test
    public void teamsByPoints(){
        MenuLogic menuLogic = new MenuLogic();
        List<Team> teamList = menuLogic.request_teamList_forLeagueTable(1);
        assertTrue(teamList.get(0).getPoints() > teamList.get(1).getPoints());
    }
}
