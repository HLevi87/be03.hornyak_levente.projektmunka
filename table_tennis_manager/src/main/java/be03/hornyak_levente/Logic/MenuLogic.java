package be03.hornyak_levente.Logic;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be03.hornyak_levente.Models.IndividualMatch;
import be03.hornyak_levente.Models.Player;
import be03.hornyak_levente.Models.Round;
import be03.hornyak_levente.Models.Team;
import be03.hornyak_levente.Models.TeamMatch;
import be03.hornyak_levente.Repository.Repository;

public class MenuLogic {
    private Repository repository;
    private Integer seasonID;


    public void setSeasonID(Integer seasonID) {
        this.seasonID = seasonID;
    }

    public MenuLogic() {
        this.repository = new Repository();
    }

    // public MenuLogic(Integer seasonID) {
    //     this.repository = new Repository();
    //     this.seasonID = seasonID;
    // }

    public List<String> request_seasonList(){
        List<String> seasonList = new ArrayList<>();
        String query = "SELECT * FROM Seasons ORDER BY SeasonID ASC";
        try {
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                seasonList.add(rs.getString("SeasonName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seasonList;
    }

    public List<Round> request_roundList(Integer seasonID){
        this.seasonID = seasonID;
        List<Round> roundList = new ArrayList<>();
        String query =  "SELECT  f.TeamMatchID, ht.TeamName AS 'HomeTeam', gt.TeamName AS 'GuestTeam', f.RoundNumber, f.IsOver, f.Result, u.UmpireName, ht.GameDay, ht.GameTime, l.Location, b.Ball\r\n" +
                        "FROM Fixture f\r\n" +
                        "INNER JOIN Umpires u ON f.UmpireID = u.UmpireID\r\n" +
                        "INNER JOIN Teams" + convert_seasonName() + " ht ON f.HomeTeamID = ht.TeamID\r\n" +
                        "INNER JOIN Teams" + convert_seasonName() + " gt ON f.GuestTeamID = gt.TeamID\r\n" +
                        "INNER JOIN Locations l ON ht.LocationID = l.LocationID\r\n" +
                        "INNER JOIN Balls b ON ht.BallID = b.BallID\r\n" +
                        "WHERE f.SeasonID = " + seasonID +"\r\n" +
                        "ORDER BY f.RoundNumber ASC";
        try {
            ResultSet rs = this.repository.select_query(query);

            int lastRoundNumber = 1;
            List<TeamMatch> teamMatchList = new ArrayList<>();

            while (rs.next()) {
                Integer roundNumber = rs.getInt("RoundNumber");

                if (lastRoundNumber != roundNumber) {
                    lastRoundNumber = roundNumber;
                    List<TeamMatch> teamMatchListCopy = new ArrayList<>(teamMatchList);
                    teamMatchList = new ArrayList<>();
                    Round oneRound = new Round(teamMatchListCopy);
                    roundList.add(oneRound);
                }

                Integer teamMatchID = rs.getInt("TeamMatchID");
                String homeTeam = rs.getString("HomeTeam");
                String guestTeam = rs.getString("GuestTeam");

                Boolean isOver = rs.getBoolean("IsOver");
                String result = rs.getString("Result").replace(' ', ':');

                String umpire = rs.getString("UmpireName");
                String gameDay = rs.getString("GameDay");
                String gameTime = rs.getString("GameTime");
                String Location = rs.getString("Location");
                String ball = rs.getString("Ball");
                
                
                TeamMatch oneTeamMatch = new TeamMatch(teamMatchID, homeTeam, guestTeam, roundNumber, isOver, result, umpire, gameDay, gameTime, Location, ball);
                teamMatchList.add(oneTeamMatch);

                
            }
            Round oneRound = new Round(teamMatchList);
            roundList.add(oneRound);
            return roundList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<TeamMatch> orderTeamMatches_byDayTime(List<TeamMatch> teamMatches){
        List<TeamMatch> orderedList = new ArrayList<>(teamMatches);

        //Hashmap létrehozása a napokkal
        HashMap <String, Integer> dayHashMap = new HashMap<String, Integer>();
        dayHashMap.put("hétfő", 1);
        dayHashMap.put("kedd", 2);
        dayHashMap.put("szerda", 3);
        dayHashMap.put("csütörtök", 4);
        dayHashMap.put("péntek", 5);

        //Sorrendbe rakás
        for (int i = 0; i < orderedList.size() - 1; i++) {
            for (int j = 0; j < orderedList.size() - 1 - i; j++) {
                String current_matchDay = orderedList.get(j).getGameDay().toLowerCase();
                String next_matchDay = orderedList.get(j+1).getGameDay().toLowerCase();
                LocalTime current_matchTime = LocalTime.parse(orderedList.get(j).getGameTime());
                LocalTime next_matchTime = LocalTime.parse(orderedList.get(j+1).getGameTime());

                //Ha az i. elem egy késpbbi nap, mint az i+1. VAGY
                //ha a nap ugyan megegyezik, de az i. időpont későbbi, mint az i+1.:
                if (     dayHashMap.get(current_matchDay) > dayHashMap.get(next_matchDay) || 
                        (dayHashMap.get(current_matchDay) == dayHashMap.get(next_matchDay) && current_matchTime.isAfter(next_matchTime))) {

                    TeamMatch temp = orderedList.get(j);
                    orderedList.set(j, orderedList.get(j+1));
                    orderedList.set(j+1, temp);
                }
            }
        }
        return orderedList;
    }
    
    public List<IndividualMatch> request_individualMatchList(Integer teamMatchID){
        List<IndividualMatch> individualMatches = new ArrayList<>();
        String query =  "SELECT hp.PlayerName AS 'HomePlayer', gp.PlayerName AS 'GuestPlayer', mr.MatchResult FROM MatchResultsOneVOne mr\r\n" +
                        "INNER JOIN Players" + convert_seasonName() + " hp ON mr.HomePlayerID = hp.PlayerID\r\n" +
                        "INNER JOIN Players" + convert_seasonName() + " gp ON mr.GuestPlayerID = gp.PlayerID\r\n" +
                        "WHERE mr.TeamMatchID = " + teamMatchID + " AND mr.SeasonID = " + this.seasonID + "\r\n" +
                        "ORDER BY mr.IndividualMatchID ASC";
        try {
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                String homePlayer = rs.getString("HomePlayer");
                String guestPlayer = rs.getString("GuestPlayer");
                String result = rs.getString("MatchResult");
                IndividualMatch oneMatch = new IndividualMatch(homePlayer, guestPlayer, result);
                individualMatches.add(oneMatch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return individualMatches;
    }

    public List<Team> request_teamList_forLeagueTable(Integer seasonID){
        this.seasonID = seasonID;
        List<Team> teamList = new ArrayList<>();

        try {
            String query = "SELECT * FROM Teams" + convert_seasonName() + " WHERE IsActive = 1 AND SeasonID = " + seasonID + " ORDER BY Points DESC";
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
            //Egyenlő pontok ellenőrzése
            check_equalPoints(teamList, new ArrayList<Integer>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamList;
    }

    public List<Team> request_teamList_forTeamDetails(Integer seasonID){
        List<Team> teamList = new ArrayList<>();
        String query = "SELECT \r\n" + //
                        "    t.TeamID AS 'CsapatID',\r\n" + //
                        "    t.TeamName AS 'Csapatnév',\r\n" + //
                        "    loc.Location AS 'Hazai pálya',\r\n" + //
                        "    t.GameDay AS 'Játéknap',\r\n" + //
                        "    t.GameTime AS 'Kezdési időpont',\r\n" + //
                        "    b.Ball AS 'Labda',\r\n" + //
                        "    cd.Email AS \"Emailcím\",\r\n" + //
                        "    cd.PhoneNumber AS \"Telefonszám\",\r\n" + //
                        "    p.PlayerName AS 'Kapcsolattartó'\r\n" + //
                        "FROM Teams" + convert_seasonName() + " t\r\n" + //
                        "INNER JOIN Players" + convert_seasonName() + " p ON t.ContactPersonID = p.PlayerID\r\n" + //
                        "INNER JOIN ContactDetails cd ON t.ContactDetailsID = cd.ContactDetailsID\r\n" + //
                        "INNER JOIN Locations loc ON t.LocationID = loc.LocationID\r\n" + //
                        "INNER JOIN Balls b ON t.BallID = b.BallID\r\n" + //
                        "WHERE t.SeasonID = " + seasonID + " AND t.IsActive = 1\r\n" + //
                        "ORDER BY Csapatnév";
        try {
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                Integer teamID = rs.getInt(1);
                String teamName = rs.getString(2);
                String location = rs.getString(3);
                String gameDay = rs.getString(4);
                String gameTime = rs.getString(5);
                String ball = rs.getString(6);
                String email = rs.getString(7);
                String phoneNumber = rs.getString(8);
                String contactperson = rs.getString(9);

                Team oneTeam = new Team(teamID, teamName, location, gameDay, gameTime, ball, email, phoneNumber, contactperson);
                teamList.add(oneTeam);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return teamList;
    }

    private void check_equalPoints(List<Team> teamList, List<Integer> equalsIDsList, int idx){
        //Initial parameter values: teamList = all teams; equals = new list, idx = 0;
        //Rekurzió vége
        if (idx == teamList.size()-1) {
            return;
        }
        //Egyenlő pontszámok keresése
        if (teamList.get(idx).getPoints() == teamList.get(idx + 1).getPoints()) {
            equalsIDsList.add(teamList.get(idx).getTeamID());
            //Ha kettőnél több csapat van egyenlő pontszámmal, akkor a check metódus újbóli meghívása idx + 1-gyel
            if (teamList.get(idx+1).getPoints() == teamList.get(idx + 2).getPoints()) {
                check_equalPoints(teamList, equalsIDsList, idx + 1);
                //Ha csak két egyenlő pontszámú csapat volt, akkor: 
                //sorbarendező metódus meghíhívása + check folytatása idx + 2-vel és új equals listával a további egyenlő pontszámú csapatok kereséséhez
            } else {
                equalsIDsList.add(teamList.get(idx + 1).getTeamID());
                //SORBA RENDEZŐ METÓDUS MEGHÍVÁSA IDE!!!!
                sortOut_draw(teamList, equalsIDsList);
                check_equalPoints(teamList, new ArrayList<Integer>(), idx + 2);
            }
        } else{
            check_equalPoints(teamList, equalsIDsList, idx + 1);
        }
    }

    private void sortOut_draw(List<Team> teamList, List<Integer> equalsIDsList){
        //6 A helyezések eldöntése
        //6.1 A helyezések sorrendjét a szerzett pontok száma dönti el.
        //6.2 Azonos pontszám esetén a holtversenyben lévő csapatok egymás elleni eredménye dönt:
        //a szerzett pontok száma, egyéni győzelmek aránya, játszmaarány sorrendjében. (MOATSZ SZK. 5.1.1.10.-.5.1.1.16.)


        //Számlálók inicializálása
        //Key = ID; Value = nyert-vesztett egyéni meccsek kölönbsége, ILL. nyert-vesztett szettek különbsége
        HashMap<Integer, Integer> matchWin_diffMap = new HashMap<>();
        HashMap<Integer, Integer> setWin_diffList = new HashMap<>();        //Lehetséges fejlesztés

        //0 érték beírása for ciklussal, hogy 3 különböző csapatnál is működjön
        for (int i = 0; i < equalsIDsList.size(); i++) {
            matchWin_diffMap.put(equalsIDsList.get(i), 0);
            setWin_diffList.put(equalsIDsList.get(i), 0);
        }

        //Csapatok meccseinek lekérése páronként - MINDIG CSAK 2 csapatot hasonlít össze, így lehet a nyert meccsek különbségeit párosával feldolgozni -- 3 csapat esetén 3 * 2 csapatmeccset dolgoz fel
        for (int i = 0; i < equalsIDsList.size() - 1; i++) {
            for (int j = i + 1; j < equalsIDsList.size(); j++) {
                //Változósítás a jobb átláhatóságért
                Integer teamI_ID = equalsIDsList.get(i);
                Integer teamJ_ID = equalsIDsList.get(j);

                //Csapatmérkőzések és eredmények
                List<TeamMatch> twoTeamMatches = request_teamMatches_forDraw(teamI_ID, teamJ_ID, this.seasonID);
                String result0 = twoTeamMatches.get(0).getResult();
                String result1 = twoTeamMatches.get(1).getResult();

                //STRING eredmény átírása Integerre, és allokálása a csapatokhoz
                String[] resultSplit0 = result0.split(" ");
                String[] resultSplit1 = result1.split(" ");
                
                //...az odavágónál
                if (teamI_ID.equals(twoTeamMatches.get(0).getHomeTeamID())) {
                    Integer teamI_matchDiff = Integer.parseInt(resultSplit0[0]) - Integer.parseInt(resultSplit0[1]);
                    Integer teamJ_matchDiff = Integer.parseInt(resultSplit0[1]) - Integer.parseInt(resultSplit0[0]);

                    //HashMap frissítés
                    matchWin_diffMap.replace(teamI_ID, matchWin_diffMap.get(teamI_ID), matchWin_diffMap.get(teamI_ID) + teamI_matchDiff);
                    matchWin_diffMap.replace(teamJ_ID, matchWin_diffMap.get(teamJ_ID), matchWin_diffMap.get(teamJ_ID) + teamJ_matchDiff);
                } else if (teamI_ID.equals(twoTeamMatches.get(0).getGuestTeamID())) {
                    Integer teamJ_matchDiff = Integer.parseInt(resultSplit0[0]) - Integer.parseInt(resultSplit0[1]);
                    Integer teamI_matchDiff = Integer.parseInt(resultSplit0[1]) - Integer.parseInt(resultSplit0[0]);

                    //HashMap frissítés
                    matchWin_diffMap.replace(teamI_ID, matchWin_diffMap.get(teamI_ID), matchWin_diffMap.get(teamI_ID) + teamI_matchDiff);
                    matchWin_diffMap.replace(teamJ_ID, matchWin_diffMap.get(teamJ_ID), matchWin_diffMap.get(teamJ_ID) + teamJ_matchDiff);
                }

                //...a visszavágónál
                if (teamI_ID.equals(twoTeamMatches.get(1).getHomeTeamID())) {
                    Integer teamI_matchDiff = Integer.parseInt(resultSplit1[0]) - Integer.parseInt(resultSplit1[1]);
                    Integer teamJ_matchDiff = Integer.parseInt(resultSplit1[1]) - Integer.parseInt(resultSplit1[0]);

                    //HashMap frissítés
                    matchWin_diffMap.replace(teamI_ID, matchWin_diffMap.get(teamI_ID), matchWin_diffMap.get(teamI_ID) + teamI_matchDiff);
                    matchWin_diffMap.replace(teamJ_ID, matchWin_diffMap.get(teamJ_ID), matchWin_diffMap.get(teamJ_ID) + teamJ_matchDiff);
                } else if (teamI_ID.equals(twoTeamMatches.get(1).getGuestTeamID())) {
                    Integer teamJ_matchDiff = Integer.parseInt(resultSplit1[0]) - Integer.parseInt(resultSplit1[1]);
                    Integer teamI_matchDiff = Integer.parseInt(resultSplit1[1]) - Integer.parseInt(resultSplit1[0]);

                    //HashMap frissítés
                    matchWin_diffMap.replace(teamI_ID, matchWin_diffMap.get(teamI_ID), matchWin_diffMap.get(teamI_ID) + teamI_matchDiff);
                    matchWin_diffMap.replace(teamJ_ID, matchWin_diffMap.get(teamJ_ID), matchWin_diffMap.get(teamJ_ID) + teamJ_matchDiff);
                }
            }       //2. (j) for ciklus vége
        }           //1. (i) for ciklus vége
        //Egyenlő pontszámú csapatok sorrendbehelyezése
        //teamList; equalsIDsList -> matchWin_diffMap

        //A teamListben megkeressük, hol van az egyenlő pontszámú csapatokból az "első":
        int firstIdx = 0;
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).getTeamID().equals(equalsIDsList.get(0))) {
                firstIdx = i;
                break;
            }
        }
        //Dupla for ciklussal véglegesítjük a csapatok sorrendjét
        for (int i = 0; i < equalsIDsList.size() - 1; i++) {                                //Az egyező pontszámú csapatok száma által meghatározott tartomány
            for (int j = firstIdx; j < firstIdx + equalsIDsList.size() - 1 - i; j++) {      //A teamlist szelete -- ld. a 'firstIdx'
                //ÖSSZEVETÉS: az egymás elleni mérközéseken (tehát csak az egyenlő pontszám csapatoknál) melyik csapatnál nagyobb az egyéni győzelmek és veresegék különbsége?
                if (matchWin_diffMap.get(teamList.get(j).getTeamID()) < matchWin_diffMap.get(teamList.get(j + 1).getTeamID())) {
                    Team temp = teamList.get(j);
                    teamList.set(j, teamList.get(j + 1));
                    teamList.set(j + 1, temp);
                }
            }
        }

    }               //metódus vége

    private List<TeamMatch> request_teamMatches_forDraw(Integer teamID1, Integer teamID2, Integer SeasonID){
        List<TeamMatch> twoTeamMatches = new ArrayList<>();
        try {
            String query = "SELECT   f.TeamMatchID AS 'TeamMatchID',\r\n" + 
                                    "f.HomeTeamID AS 'HomeTeamID', \r\n" + 
                                    "f.GuestTeamID AS 'GuestTeamID', \r\n" + 
                                    "ht.TeamName AS 'Team1',\r\n" + 
                                    "gt.TeamName AS 'Team2',\r\n" + 
                                    "f.Result AS 'Result'\r\n" + 
                            "FROM Fixture f\r\n" + 
                            "INNER JOIN Teams" + convert_seasonName() + " ht ON ht.TeamID = f.HomeTeamID\r\n" + 
                            "INNER JOIN Teams" + convert_seasonName() + " gt ON gt.TeamID = f.HomeTeamID\r\n" + 
                            "WHERE   f.SeasonID = " + SeasonID + " AND\r\n" + 
                                    "f.IsOver = 1 AND\r\n" + 
                                    "(f.HomeTeamID = " + teamID1 + " AND f.GuestTeamID = " + teamID2 + " OR f.GuestTeamID = " + teamID1 + " AND f.HomeTeamID = " + teamID2 + ")\r\n";
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                Integer teamMatchID = rs.getInt(1);
                Integer homeTeamID = rs.getInt(2);
                Integer guesTeamID = rs.getInt(3);
                String homeTeam = rs.getString(4);
                String guestTeam = rs.getString(5);
                String result = rs.getString(6);
                TeamMatch oneTeamMatch = new TeamMatch(teamMatchID, homeTeamID, guesTeamID, homeTeam, guestTeam, result);
                twoTeamMatches.add(oneTeamMatch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return twoTeamMatches;
    }

    public List<Player> request_playerList(Integer seasonID){
        List<Player> playerList = new ArrayList<>();
        String query = "SELECT  p.PlayerID,\r\n" +
                                "p.PlayerName,\r\n" +
                                "p.LicenceNumber,\r\n" +
                                "p.TeamID,\r\n" +
                                "t.TeamName,\r\n" +
                                "p.MatchesPlayed,\r\n" +
                                "p.MatchesWon,\r\n" +
                                "CONVERT(float, p.MatchesWon)/CONVERT(float, p.MatchesPlayed)*100 AS 'Percentage'\r\n" +
                        "FROM Players" + convert_seasonName() + " p\r\n" +
                        "INNER JOIN Teams" + convert_seasonName() + " t ON p.TeamID = t.TeamID\r\n" +
                        "WHERE p.IsActive = 1 AND\r\n" +
                        "p.SeasonID = " + seasonID + "\r\n" +
                        "ORDER BY [Percentage] DESC";
        try {
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                Integer playerID = rs.getInt(1);
                String playerName = rs.getString(2);
                Integer licenceNumber = rs.getInt(3);
                Integer teamID = rs.getInt(4);
                String teamName = rs.getString(5);
                Integer matchesPlayed = rs.getInt(6);
                Integer matchesWon = rs.getInt(7);
                double percentage = rs.getDouble(8);
                Player onePlayer = new Player(playerID, playerName, licenceNumber, teamID, teamName, matchesPlayed, matchesWon, percentage);
                playerList.add(onePlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return playerList;
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

}
