package be03.hornyak_levente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Factory;
import org.junit.Test;

import be03.hornyak_levente.Factory.LogicQueries;
import be03.hornyak_levente.Factory.TeamLists;
import be03.hornyak_levente.Logic.PlayerListClass;


public class AppTest 
{

    /*
     * 1. TESZT - equalTeamLists(): a fájlból beolvasás minden csapatadatból ugyanannyit produkált?
     * 2. TESZT - numberOfTeams(): az adatbázisba bekerült csapatok száma megegyezik a beolvasottal?
     * 3. TESZT - checkPlayersPerTeam(): Minden csapatban van legalább 4 játékos?
     */

    @Test
    public void equalTeamLists() {
        TeamLists teamLists = new TeamLists("cimek.txt");
        assertEquals(teamLists.getCsapatnevekLista().size(), teamLists.getLabdak().size());
        assertEquals(teamLists.getCsapatnevekLista().size(), teamLists.getCimekLista().size());
        assertEquals(teamLists.getCsapatnevekLista().size(), teamLists.getEmailek().size());
        assertEquals(teamLists.getCsapatnevekLista().size(), teamLists.getJatekIdopontok().size());
        assertEquals(teamLists.getCsapatnevekLista().size(), teamLists.getJatekNapok().size());
        assertEquals(teamLists.getCsapatnevekLista().size(), teamLists.getTelefonszamok().size());
    }

    @Test
    public void numberOfTeams() {
        TeamLists teamLists = new TeamLists("cimek.txt");
        assertTrue(teamLists.getCsapatnevekLista().size() == LogicQueries.count_numberOfTeams());
        
    }

    @Test
    public void playersPerTeam() {
        int numberOfTeam = LogicQueries.count_numberOfTeams();
        for (int i = 0; i < numberOfTeam; i++) {
            Integer numberOfPlayers = LogicQueries.count_playerPerTeam(i+1);
            assertFalse("A csapathoz nem lett játékos rendelve.", numberOfPlayers.equals(null));
            assertTrue("Kevés játékos. CsapatId: " + i+1, numberOfPlayers >= 4);                        //Minden csapatban legalább 4 játékosnak kell lennie (de lehet akár 8 is, változó)
        }
    }

    @Test
    public void bestPlayer(){
        PlayerListClass playerListClass = new PlayerListClass();
        Integer expected = 69420;
        for (int i = 0; i < playerListClass.playerList.size(); i++) {
            if (playerListClass.playerList.get(i).getLicenceNumber().equals(expected)) {
                assertEquals("Hornyák Levente", playerListClass.playerList.get(i).getName());
            }
        }
    }
}
