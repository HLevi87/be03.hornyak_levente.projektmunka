package be03.hornyak_levente.Logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be03.hornyak_levente.Models.NameID;
import be03.hornyak_levente.Models.Player;
import be03.hornyak_levente.Models.Team;
import be03.hornyak_levente.Repository.Repository;

public class AdminMenuLogic {
    private Repository repository;
    private Integer seasonID;


    public AdminMenuLogic() {
        this.repository = new Repository();
        get_seasonID();
    }


    private void get_seasonID(){
        //MAX --> az admin menü csak olyan dolgok változtatására ad lehetőséget, amiknek csak az aktuális szezonban van értelme.
        String query = "SELECT MAX(SeasonID) FROM Seasons";
        try {
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                this.seasonID = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean check_credentials(String inputName, String inputPassword){
        //Ha az adatbázisban lévő felhasználónév és jelszó eredményez ResultSet-et, a felhasználó továbbléphet
        String query = "SELECT CredentialsID FROM Credentials\r\n" + //
                        "WHERE   Username = '" + inputName + "' AND\r\n" + //
                        "Password = '" + inputPassword + "' AND\r\n" + //
                        "IsAdmin = 1 AND\r\n" + //
                        "IsActive = 1";
        try {
            ResultSet rs = this.repository.select_query(query);
            if (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Boolean update_password(String tableName, String columnName, String detailID, String newDetail){
        String query =  "UPDATE " + tableName + " SET " + columnName + " = '" + newDetail + "'\r\n" + //
                        "WHERE IsAdmin = 1 AND IsActive = 1";
        try {
            Integer rowsAffected = this.repository.update_query(query);
            if (rowsAffected == 1) {
                return true;
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt az UPDATE során. Valami nem stimmel!!!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String convert_seasonName(){
        String seasonName = "";
        if (seasonID != 1) {
            String query =  "SELECT SeasonName FROM Seasons\r\n" +
                            "WHERE SeasonID = " + seasonID + "\r\n";
            seasonName = "_";
            try {
                ResultSet rs = this.repository.select_query(query);
                if (rs.next()) {
                    seasonName += rs.getString(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            seasonName = seasonName.replace('/', '_');
        }
        return seasonName;
    }

    public Boolean substract_pointsFromTeam(Integer penaltyPoints, Integer teamID){
        String query =  "UPDATE Teams" + convert_seasonName() + " SET Points = (SELECT Points FROM Teams" + convert_seasonName() +
                        " WHERE TeamID = " + teamID + " AND IsActive = 1 AND SeasonID = " + this.seasonID + ") - " + penaltyPoints;
        try {
            Integer rowsAffected = this.repository.update_query(query);
            if (rowsAffected == 1) {
                return true;
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt az UPDATE során. Valami nem stimmel!!!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Team> request_teamList_withPoints(){
        List<Team> teamList = new ArrayList<>();

        try {
            String query = "SELECT * FROM Teams" + convert_seasonName() + " WHERE IsActive = 1 AND SeasonID = " + this.seasonID + " ORDER BY TeamID ASC";
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                Integer teamID = rs.getInt("TeamID");
                String teamName = rs.getString("TeamName");
                Integer matchesPlayed = rs.getInt("MatchesPlayed");
                Integer matchesWon = rs.getInt("MatchesWon");
                Integer matchesDraw = rs.getInt("MatchesDraw");
                Integer matchesLost = rs.getInt("MatchesLost");
                Integer points = rs.getInt("Points");

                Team oneTeam = new Team(teamID, teamName, matchesPlayed, matchesWon, matchesDraw, matchesLost, points);
                teamList.add(oneTeam);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamList;
    }

    public List<NameID> get_simpleList(String tableName, String columnName_stringName, String columnName_intID){
        List<NameID> nameIDlist = new ArrayList<>();
        String query = "";
        if (tableName.equals("Players") || tableName.equals("Teams")) {
            query = "SELECT " + columnName_intID + ", " + columnName_stringName + " FROM " + tableName + convert_seasonName() + "\r\n" +
                        "WHERE IsActive = 1 AND SeasonID = " + this.seasonID + "\r\n" + 
                        "ORDER BY " + columnName_intID;
        } else {
            query = "SELECT " + columnName_intID + ", " + columnName_stringName + " FROM " + tableName + "\r\n" +
                        "WHERE IsActive = 1 AND SeasonID = " + this.seasonID + "\r\n" + 
                        "ORDER BY " + columnName_intID;
        }
        
        try {
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                Integer id = rs.getInt(1);
                String name = rs.getString(2);
                NameID oneNameID = new NameID(id, name);
                nameIDlist.add(oneNameID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameIDlist;
    }

    public Boolean remove_team(Integer teamID){
        //CSAPAT KIVONÁSA

        //Kapcsolattartási adatok törlése (email + telefon)
        if (delete_forTeamRemoval(teamID, "ContactDetails", "ContactDetailsID")) {
            //Kapcsolattartó személy --> credentials sor törlése
            if (delete_forTeamRemoval(teamID, "Credentials", "ContactPersonID")) {
                //Csapat játékosainak inaktiválása és a TeamID törlése
                if (updatePlayers_forTeamRemoval(teamID)) {
                    //Csapat törlése
                    if (updateTeam_forTeamRemoval(teamID)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private Boolean delete_forTeamRemoval(Integer teamID, String tableName, String columnName_detailID){
        String query = "DELETE FROM " + tableName + "\r\n" + //
                        "WHERE " + columnName_detailID + " =\r\n" +
                            "(SELECT " + columnName_detailID + "\r\n" + //
                            "FROM Teams" + convert_seasonName() + "\r\n" + //
                            "WHERE TeamID = " + teamID + ")";
        try {
            Integer rowsAffected = this.repository.delete_query(query);
            if (rowsAffected == 1) {
                return true;
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több törlés történt a DELETE során: TeamID: " + teamID + " Valami nem stimmel!!!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    private Boolean updatePlayers_forTeamRemoval(Integer teamID){
        String query = "UPDATE Players" + convert_seasonName() + "\r\n" + //
                        "SET ValidTo = " + LocalDate.now() + ", IsActive = 0, TeamID = null\r\n" + //
                        "WHERE TeamID = " + teamID + " AND\r\n" + //
                        "IsActive = 1 AND\r\n" + //
                        "SeasonID = " + this.seasonID;

        try {
            Integer rowsAffected = this.repository.update_query(query);
            if (rowsAffected == 1) {
                return true;
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt a DELETE során: TeamID: " + teamID + " Valami nem stimmel!!!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    private Boolean updateTeam_forTeamRemoval(Integer teamID){
        String query =  "UPDATE Teams" + convert_seasonName() + "\r\n" + //
                        "SET ValidTo = " + LocalDate.now() + ", IsActive = 0\r\n" + //
                        "WHERE TeamID = " + teamID + " AND\r\n" + //
                        "IsActive = 1 AND\r\n" + //
                        "SeasonID = " + this.seasonID;

        try {
            Integer rowsAffected = this.repository.update_query(query);
            if (rowsAffected == 1) {
                return true;
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt a DELETE során: TeamID: " + teamID + " Valami nem stimmel!!!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public Integer fetch_newID(String tableName, String columnName_detailID) throws SQLException{
        //A LEGUTOLSÓ BEVITT REKORD ID-ja
        String query = "SELECT MAX(" + columnName_detailID + ") FROM " + tableName;
        ResultSet rs = this.repository.select_query(query);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return null;
    }

    public Boolean add_newTeam(Team team, List<Player> playerList, String userName, String pw){
        /* FELÉPÍTÉS
        * 1)  Előzetes bevitelek + ID-k kinyerése:
        *      - kontakt       => ContactDetailsID
        *      - labda         => BallID            !!! előbb ellenőrzése, hogy fent van-e már
        *      - hazai pálya   => LocationID        !!! előbb ellenőrzése, hogy fent van-e már
        * 
        * 2a) Csapat bevitele
        *      - ContactPersonID = null
        * 2b) TeamID lekérése
        * 
        * 3a) Játékosok bevitele
        * 3b) ContactPersonID lekérése + BEÍRÁS a csapathoz
        * 
        * 4)  Credentials
        */ 

        //KAPCSOLATTARTÁSI ADATOK
        String email = team.getEmail();
        String phoneNumnber = team.getPhoneNumber();
        String query = "INSERT INTO ContactDetails VALUES('" + email + "'', '" + phoneNumnber + "'')";
        int rowsAffected = 0;
        int contactDetailsID = 0;
        try {
            rowsAffected = this.repository.insertInto_query(query);
            if (rowsAffected == 1) {
                contactDetailsID = fetch_newID("ContactDetails", "ContactDetailsID");
                team.setContactDetailsID(contactDetailsID);
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt az INSERT során: Csapatnév: " + team.getTeamName() + " - Valami nem stimmel!!!\n");
                return false;
            } else{
                System.out.println("Nem sikerült a kapcsolattartási adatokat felvenni az adatbázisba.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //LABDA
        String ball = team.getBall();
        int ballID = 0;
        rowsAffected = 0;

        try {
            //Ellenőrzés, hogy van-e már ilyen
            query = "SELECT BallID FROM Balls WHERE Ball = '" + ball + "'";
            ResultSet rs = this.repository.select_query(query);
            if (rs.next()) {
                ballID = rs.getInt(1);
            } else{
                //Ha még nincs ilyen, új labda beírása
                query = "INSERT INTO Balls VALUES('" + ball + "'')";
                rowsAffected = this.repository.insertInto_query(query);
                if (rowsAffected == 1) {
                    ballID = fetch_newID("Balls", "BallID");
                } else if (rowsAffected > 1) {
                    System.out.println("\n\n!!!Több hozzáadás történt az INSERT során: Csapatnév: " + team.getTeamName() + " - Valami nem stimmel!!!\n");
                    return false;
                } else{
                    System.out.println("Nem sikerült a labdát felvenni az adatbázisba.");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        team.setBallID(ballID);

        //HAZAI PÁLYA
        String location = team.getLocation();
        int locationID = 0;
        rowsAffected = 0;

        try {
            //Ellenőrzés, hogy van-e már ilyen
            query = "SELECT LocationID FROM Locations WHERE Location = '" + location + "'";
            ResultSet rs = this.repository.select_query(query);
            if (rs.next()) {
                ballID = rs.getInt(1);
            } else{
                //Ha még nincs ilyen, új labda beírása
                query = "INSERT INTO Locations VALUES('" + location + "'')";
                rowsAffected = this.repository.insertInto_query(query);
                if (rowsAffected == 1) {
                    locationID = fetch_newID("Locations", "LocationID");
                } else if (rowsAffected > 1) {
                    System.out.println("\n\n!!!Több hozzáadás történt az INSERT során: Csapatnév: " + team.getTeamName() + " - Valami nem stimmel!!!\n");
                    return false;
                } else{
                    System.out.println("Nem sikerült a címet felvenni az adatbázisba.");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        team.setLocationID(locationID);

        //CSAPAT BEVITELE
        int teamID = 0;
        rowsAffected = 0;

        try {
            //Legnagyobb TeamID
            teamID = fetch_newID("Teams" + convert_seasonName(), "TeamID");
            //Új csapat TeamID-ja
            teamID++;

            //Új csapat bevitele
            /*DB sorrend:
              TeamID
              ValidFrom
              ValidTo
              IsActive
              SeasonID
              ContactPersonID
              TeamName
              ContactDetailsID
              LocationID
              BallID
              GameDay
              GameTime
              MatchesPlayed
              MatchesWon
              MatchesDraw
              MatchesLost
              Points
             */

            rowsAffected = this.repository.add_newTeam("Teams" + convert_seasonName(), teamID, this.seasonID, null, team.getTeamName(),
                                                        contactDetailsID, locationID, ballID, team.getGameDay(), team.getGameTime());
            if (rowsAffected == 1) {
                System.out.println("A csapat felvétele az adatbázisba sikeres volt.");
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt az INSERT során: Csapatnév: " + team.getTeamName() + " - Valami nem stimmel!!!\n");
                return false;
            } else{
                System.out.println("Nem sikerült a csapatot felvenni az adatbázisba.");
                return false;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        //JÁTÉKOSOK BEVITELE
        int playerID = 0;
        int contactPersonID = 0;

        try {
            //Legnagyobb playerID
            playerID = fetch_newID("Players" + convert_seasonName(), "PlayerID");
            //Következő új játékos ID-ja
            playerID++;
            
            for (int i = 0; i < playerList.size(); i++) {
                playerList.get(i).setPlayerID(playerID);        //(a DB-ben a kulcs egy TechnicalID)
                //Játékost hozzáadü függvény meghívása
                if (add_newPlayer(playerList.get(i), teamID)) {
                    System.out.println("A játékos (" + playerList.get(i).getPlayerName() + ") felvétele az adatbázisba sikeres volt.");
                } else{
                    System.out.println("A játékos (" + playerList.get(i).getPlayerName() + ") felvétele az adatbázisba nem sikerült.");
                    return false;
                }

                //ContactPerson keresése
                String contactName = team.getContactPerson().getPlayerName();
                Integer licenceNumber = team.getContactPerson().getLicenceNumber();
                if (playerList.get(i).getPlayerName().equals(contactName) &&
                    playerList.get(i).getLicenceNumber().equals(licenceNumber)) {
                    contactPersonID = playerID;
                }

                playerID++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //ContactPersonID BEÍRÁSA A CSAPATHOZ
        try {
            query = "UPDATE Teams" + convert_seasonName() + " " +
                    "SET ContactPersonID = " + contactPersonID + " " +
                    "WHERE TeamID = " + teamID + " AND IsActive = 1 AND SeasonID = " + this.seasonID;
            rowsAffected = this.repository.update_query(query);

            if (rowsAffected == 1) {
                System.out.println("Kapcsolattartó személy frissítve.");
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt az UPDATE során: Csapatnév: " + team.getTeamName() + " - Valami nem stimmel!!!\n");
                return false;
            } else {
                System.out.println("Kapcsolattartó személy frissítése nem sikerült!!!");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //CREDENTIALS
        try {
            int credentialsID = fetch_newID("Credentials", "CredentialsID");
            credentialsID++;
            rowsAffected = this.repository.add_newCredential(credentialsID, contactPersonID, userName, pw);
            if (rowsAffected == 1) {
                System.out.println("Belépési adatok.");
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt a belépési adatok frissítése során: Csapatnév: " + team.getTeamName() + " - Valami nem stimmel!!!\n");
                return false;
            } else {
                System.out.println("A belépési adatok beírása nem sikerült!!!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        return true;
    }

    public Boolean add_newPlayer(Player player, Integer teamID){
        int rowsAffected = 0;
        try {
            rowsAffected = this.repository.add_newPlayer("Players" + convert_seasonName(), player.getPlayerID(), this.seasonID,
                                                    player.getPlayerName(), player.getLicenceNumber(), teamID);
                if (rowsAffected == 1) {
                    return true;
                } else if (rowsAffected > 1) {
                    System.out.println("\n\n!!!Több hozzáadás történt az INSERT során: Játékos neve: " + player.getPlayerName() + " - Valami nem stimmel!!!\n");
                } else{
                    System.out.println("Nem sikerült a játékost (" + player.getPlayerName() + ") felvenni az adatbázisba.");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Integer getSeasonID() {
        return seasonID;
    }




}
