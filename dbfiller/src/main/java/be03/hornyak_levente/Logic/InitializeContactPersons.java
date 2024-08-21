package be03.hornyak_levente.Logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be03.hornyak_levente.Factory.ContactPersons;

public class InitializeContactPersons {
    private ContactPersons contactPersons;
    public Integer linesModified;

    public InitializeContactPersons() {
        try {
            this.contactPersons = new ContactPersons();
            this.linesModified = 0;
            manage_contactPersons();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void manage_contactPersons() throws SQLException{
        List<String> teamNamesList = list_teamNames();
        for (int i = 0; i < teamNamesList.size(); i++) {

            String oneTeamName = teamNamesList.get(i);
            List<Integer> playerIDsList = list_playerIDs(oneTeamName);

            Random rnd = new Random();
            Integer contactPersonID = playerIDsList.get(rnd.nextInt(playerIDsList.size()));
            
            if (this.contactPersons.add_contactPersonID(contactPersonID, oneTeamName)) {
                linesModified++;
            }
        }
        this.contactPersons.cheat();
    }

    private List<String> list_teamNames() throws SQLException{
        List<String> teamNamesList = new ArrayList<>();
        ResultSet rs = this.contactPersons.teamNames();
        while (rs.next()) {
            teamNamesList.add(rs.getString(1));
        }
        return teamNamesList;
    }    
    
    private List<Integer> list_playerIDs(String teamName) throws SQLException{
        List<Integer> playerIDsList = new ArrayList<>();
        ResultSet rs = this.contactPersons.playerIDs(teamName);
        while (rs.next()) {
            playerIDsList.add(rs.getInt(1));
        }
        return playerIDsList;
    }

    
}
