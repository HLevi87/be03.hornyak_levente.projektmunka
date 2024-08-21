package be03.hornyak_levente.DataBaseFiller;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import be03.hornyak_levente.Factory.TeamLists;

public class FillUpTeamsV2 {

    private static final String DB_PATH = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;";

    public static void deleteAnd_createAndfillUp_TeamsAndCoTables(Integer seasonID){
        if (doesTeamsTableExist()) {
            deleteTable("Teams");
        }
        if (doesLocationsTableExist()) {
            deleteTable("Locations");
        }
        if (doesContactDetailsTableExist()) {
            deleteTable("ContactDetails");
        }
        if (doesBallsTableExist()) {
            deleteTable("Balls");
        }
        createAndfillUp_TeamsAndCoTables(seasonID);
    }

    private static void deleteTable(String tableName){
        try{
            Connection connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            String query = "DROP TABLE " + tableName + ";";
            statement.executeUpdate(query);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void createAndfillUp_TeamsAndCoTables(Integer seasonID){
        TeamLists teamLists = new TeamLists("cimek.txt");

        //A Teams tábla ltérehozása
        if (!doesTeamsTableExist()) {
            System.out.println("A Teams tábla még nem létezik.");
            if (create_teamsTable()) {
                System.out.println("A Teams tábla létrehozása sikeres volt.");
            } else{
                System.out.println("Nem sikerült létrehozni a Teams táblát.");
            }
        }

        //A ContactDetails tábla létrehozása
        if (!doesContactDetailsTableExist()) {
            System.out.println("A ContactDetails tábla még nem létezik.");
            if (create_contactDetailsTable()) {
                System.out.println("A ContactDetails tábla létrehozása sikeres volt.");
            } else{
                System.out.println("Nem sikerült létrehozni a ContactDetails táblát.");
            }
        }

        //A Locations tábla létrehozása
        if (!doesLocationsTableExist()) {
            System.out.println("A Locations tábla még nem létezik.");
            if (create_locationsTable()) {
                System.out.println("A Locations tábla létrehozása sikeres volt.");
            } else{
                System.out.println("Nem sikerült létrehozni a Locations táblát.");
            }
        }

        //A Balls tábla létrehozása
        if (!doesBallsTableExist()) {
            System.out.println("A Balls tábla még nem létezik.");
            if (create_ballsTable()) {
                System.out.println("A Balls tábla létrehozása sikeres volt.");
            } else{
                System.out.println("Nem sikerült létrehozni a Balls táblát.");
            }
        }

        //A táblák feltöltése
        try {
            fillUp_ContactDetails(teamLists);


            storedProcedure_forLocations();
            fillUp_Locations(teamLists);
            
            
            fillUp_balls(teamLists);
            fillUp_teams(teamLists, seasonID);
            System.out.println("\nA csapatok és a hozzá tartozó táblák felöltése sikeresen megtörtént.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------//
    //TÁBLÁKAT FELTÖLTŐ METÓDUSOK

    private static void fillUp_ContactDetails(TeamLists teamLists) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        for (int i = 0; i < teamLists.getEmailek().size(); i++) {
            if (email_isNotInDB(teamLists.getEmailek().get(i)) && phone_isNotInDB(teamLists.getTelefonszamok().get(i))) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO ContactDetails (Email, PhoneNumber) VALUES (?, ?)"
                )){
                    preparedStatement.setString(1, teamLists.getEmailek().get(i));
                    preparedStatement.setString(2, teamLists.getTelefonszamok().get(i));
                 
                    preparedStatement.executeUpdate();
                }
            }
        }
        connection.close();
    }

    private static Integer find_emailIdx(String email) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM ContactDetails WHERE Email = '" + email + "'";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            String contactDetailIDString = rs.getString("ContactDetailsID");
            Integer contactDetailID = Integer.parseInt(contactDetailIDString);
            return contactDetailID;
        }
        connection.close();
        return -1;
    }

    private static Integer find_phoneIdx(String phone) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM ContactDetails WHERE PhoneNumber = ?")){
            ps.setString(1, phone);
            try(ResultSet rs = ps.executeQuery()){
                String DBphone = rs.getString("PhoneNumber");
                if (DBphone.equals(phone)) {
                    return rs.getInt("ContactDetailsID");
                }
            }
        }
        connection.close();
        return -1;
    }

    private static Boolean email_isNotInDB(String email) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT Email FROM ContactDetails WHERE Email = ?")){
            ps.setString(1, email);
            ps.executeQuery();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                return false;
            }
        }
        connection.close();
        return true;
    }

    private static Boolean phone_isNotInDB(String phone) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT PhoneNumber FROM ContactDetails WHERE PhoneNumber = ?")){
            ps.setString(1, phone);
            ps.executeQuery();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                return false;
            }
        }
        connection.close();
        return true;
    }



    private static void fillUp_Locations(TeamLists teamLists) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        for (int i = 0; i < teamLists.getCimekLista().size(); i++) {
            if (location_isNotInDB(teamLists.getCimekLista().get(i))) {
                try (PreparedStatement ps = connection.prepareStatement(
                    "EXEC InsertLocation @Location = ?")){
                    ps.setString(1, teamLists.getCimekLista().get(i));

                    ps.executeUpdate();
                }
            }
        }
        connection.close();
    }

    private static Integer find_locationIdx(String location) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM Locations WHERE Location = ?")){
            ps.setString(1, location);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getObject("Location").equals(location)) {
                    return rs.getInt("LocationID");
                }
            }
        }
        connection.close();
        return -1;
    }

    private static Boolean location_isNotInDB(String location) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);

        try(PreparedStatement ps = connection.prepareStatement("SELECT Location FROM Locations WHERE Location = ?")){
            ps.setString(1, location);
            ps.executeQuery();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                return false;
            }
        }

        // Statement statement = connection.createStatement();
        // String query = "SELECT Location FROM Locations";
        // ResultSet rs = statement.executeQuery(query);
        // while (rs.next()) {
        //     if (rs.getObject("Location").equals(location)) {
        //         return false;
        //     }
        // }
        connection.close();
        return true;
    }



    private static void fillUp_balls(TeamLists teamLists) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        for (int i = 0; i < teamLists.getLabdak().size(); i++) {
            if (ball_isNotInDB(teamLists.getLabdak().get(i))) {
                try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Balls (Ball) VALUES (?)")){
                    ps.setString(1, teamLists.getLabdak().get(i));

                    ps.executeUpdate();
                }
            }
        }
        connection.close();
    }

    private static Integer find_ballIdx(String ball) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM Balls WHERE Ball = '" + ball + "'";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            return rs.getInt(1);
        }
        connection.close();
        return -1;
    }

    private static Boolean ball_isNotInDB(String ball) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT Ball FROM Balls WHERE Ball = ?")){
            ps.setString(1, ball);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return false;
            }
        }
        connection.close();
        return true;
    }



    private static void fillUp_teams(TeamLists teamLists, Integer seasonID) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        for (int i = 0; i < teamLists.getCsapatnevekLista().size(); i++) {
            if (team_isNotInDB(teamLists.getCsapatnevekLista().get(i))) {
                try (PreparedStatement ps = connection.prepareStatement(
                    /*  "TeamID int, " +
                        "ValidFrom date, " +
                        "ValidTo date, " +
                        "IsActive bit, " +
                        "SeasonID int, " +
                        --------
                        "ContactPersonID int, " +
                        "TeamName varchar(80), " +
                        "ContactDetailsID int, " +
                        "LocationID int, " +
                        "BallID int, " +
                        "GameDay varchar(10), " +
                        "GameTime varchar(10), " +
                        "MatchesPlayed int, " +
                        "MatchesWon int, " +
                        "MatchesDraw int, " +
                        "MatchesLost int, " +
                        "Points int)"; 
                        */
                    "INSERT INTO Teams (TeamID, ValidFrom, ValidTo, IsActive, " +
                        "SeasonID, TeamName, ContactDetailsID, LocationID, BallID, GameDay, GameTime, " +
                        "MatchesPlayed, MatchesWon, MatchesDraw, MatchesLost, Points)" +
                    "VALUES (?, ?, NULL, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, 0, 0)")){

                    //----------TeamID
                    ps.setInt(1, i+1);
                    //----------ValidFrom
                    ps.setString(2, LocalDate.now().toString());
                    //----------ValidTo = NULL
                    //----------IsActive
                    ps.setInt(3, 1);
                    //----------SeasonID
                    ps.setInt(4, seasonID);
                    //----------Csapatnév
                    ps.setString(5, teamLists.getCsapatnevekLista().get(i));

                    //----------Kontakt index
                    int emailIdx = find_emailIdx(teamLists.getEmailek().get(i));
                    if (emailIdx > 0) {
                        ps.setInt(6, emailIdx);
                    } else{
                        System.out.println("A csapat feltöltése sikertelen: nem megfelelő emailcím index.");
                        break;
                    }

                    //----------Cím index
                    int locationIdx = find_locationIdx(teamLists.getCimekLista().get(i));
                    if (locationIdx > 0) {
                        ps.setInt(7, locationIdx);
                    } else{
                        System.out.println("A csapat feltöltése sikertelen: nem megfelelő cím index.");
                        break;
                    }

                    //----------Labda index
                    int ballIdx = find_ballIdx(teamLists.getLabdak().get(i));
                    if (ballIdx > 0) {
                        ps.setInt(8, ballIdx);
                    } else{
                        System.out.println("A csapat feltöltése sikertelen: nem megfelelő labda index.");
                        break;
                    }

                    //----------Játéknap
                    ps.setString(9, teamLists.getJatekNapok().get(i));

                    //----------Játék időpont
                    ps.setString(10, teamLists.getJatekIdopontok().get(i));

                    ps.executeUpdate();
                } 
            }
        }
        connection.close();
    }

    private static Boolean team_isNotInDB(String teamName) throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        try(PreparedStatement ps = connection.prepareStatement("SELECT TeamName FROM Teams WHERE TeamName = ?")){
            ps.setString(1, teamName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return false;
            }
        }
        connection.close();
        return true;
    }

    //------------------------------------------------------------------------------------------------------------------------//
    //TÁBLÁKAT LÉTREHOZÓ METÓDUSOK


    private static Boolean doesTeamsTableExist(){
        try {
            Connection connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'Teams'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (!rs.getObject(1).equals(0)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Boolean create_teamsTable(){
        try {
            Connection connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE Teams (" +
            "TechnicalID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
            "TeamID int, " +
            "ValidFrom date, " +
            "ValidTo date, " +
            "IsActive bit, " +
            "SeasonID int, " +
            "ContactPersonID int, " +
            "TeamName varchar(80), " +
            "ContactDetailsID int, " +
            "LocationID int, " +
            "BallID int, " +
            "GameDay varchar(10), " +
            "GameTime varchar(10), " +
            "MatchesPlayed int, " +
            "MatchesWon int, " +
            "MatchesDraw int, " +
            "MatchesLost int, " +
            "Points int)";
            statement.executeUpdate(query);

            //Sikeres létrehozás ellenőrzése
            query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'Teams'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt(1) != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    private static Boolean doesContactDetailsTableExist(){
        try {
            Connection connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'ContactDetails'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (!rs.getObject(1).equals(0)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Boolean create_contactDetailsTable(){
        try {
            Connection connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE ContactDetails (" +
            "ContactDetailsID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
            "Email varchar(80), " +
            "PhoneNumber varchar(80))";
            statement.executeUpdate(query);

            //Sikeres létrehozás ellenőrzése
            query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'ContactDetails'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt(1) != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    private static Boolean doesLocationsTableExist(){
        try {
            Connection connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'Locations'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (!rs.getObject(1).equals(0)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Boolean create_locationsTable(){
        try {
            Connection connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE Locations (" +
            "LocationID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
            "Location varchar(120))";
            statement.executeUpdate(query);

            //Sikeres létrehozás ellenőrzése
            query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'Locations'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt(1) != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    private static Boolean doesBallsTableExist(){
        try {
            Connection connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'Balls'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (!rs.getObject(1).equals(0)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Boolean create_ballsTable(){
        try {
            Connection connection = DriverManager.getConnection(DB_PATH);
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE Balls (" +
            "BallID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
            "Ball varchar(120))";
            statement.executeUpdate(query);

            //Sikeres létrehozás ellenőrzése
            query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'Balls'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt(1) != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    //------------------------------------------------------------------------------------------------------------------------------//
    //EGYÉB SEGÉDMETÓDUS

    private static void storedProcedure_forLocations() {
        try {
            if (!doesProcedureExist()) {
                create_storedProcedure();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Boolean doesProcedureExist() throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        Statement statement = connection.createStatement();
        String query = "SELECT COUNT(*) FROM information_schema.routines WHERE SPECIFIC_NAME = 'InsertLocation'";
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        if (rs.getInt(1) != 0) {
            return true;
        }
        return false;
    }

    private static void create_storedProcedure() throws SQLException{
        Connection connection = DriverManager.getConnection(DB_PATH);
        Statement statement = connection.createStatement();
        String query =  "CREATE PROCEDURE InsertLocation\r\n" +
                        "@Location varchar(120)\r\n" +
                        "AS\r\n" +
                        "BEGIN\r\n" +
                        "\tINSERT INTO Locations (Location) VALUES (@Location)\r\n" +
                        "END;";

        statement.executeUpdate(query);
    }




    private static Integer get_currentSeasonID(){
        int seasonID = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
            Statement statement = connection.createStatement();
            String query = "SELECT MAX(SeasonID) FROM Seasons";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                seasonID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seasonID;
    }


}
