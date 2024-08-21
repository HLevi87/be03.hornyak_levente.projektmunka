package be03.hornyak_levente.Logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be03.hornyak_levente.Repository.Repository;

public class ContactPerson_MenuLogic {
    private Repository repository;
    private Integer contactPersonID;
    private Integer seasonID;


    public ContactPerson_MenuLogic() {
        this.repository = new Repository();
        get_seasonID();
    }

    public ContactPerson_MenuLogic(Integer seasonID) {
        this.repository = new Repository();
        this.seasonID = seasonID;
    }

    private void get_seasonID(){
        //MAX --> kapcsolattartói jogosultsággal csak a legfrissebb szezonban lehet változtatásokat végezni
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
        String query = "SELECT ContactPersonID FROM Credentials\r\n" + //
                        "WHERE   Username = '" + inputName + "' AND\r\n" + //
                        "Password = '" + inputPassword + "' AND\r\n" + //
                        "IsAdmin = 0 AND\r\n" + //
                        "IsActive = 1";
        try {
            ResultSet rs = this.repository.select_query(query);
            if (rs.next()) {
                this.contactPersonID = rs.getInt(1);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String fetch_name(){
        String query =  "SELECT p.PlayerName FROM Players" + convert_seasonName() + " p\r\n" +
                        "WHERE SeasonID = " + this.seasonID + " AND\r\n" +
                        "p.IsActive = 1 AND\r\n" +
                        "p.PlayerID = " + this.contactPersonID;
        try {
            ResultSet rs = this.repository.select_query(query);
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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


    //----------------------------------------------------------------------------------------------------------------
    ////Csapat adataiank frissítése

    public String get_currentDetail(String tableName, String columnName, String columnName_detailID){
        String currentDetail = "";
        String query =  "SELECT x." + columnName + " FROM Teams" + convert_seasonName() + " t\r\n" + //
                        "INNER JOIN " + tableName + " x ON x." + columnName_detailID + " = t." + columnName_detailID + "\r\n" + //
                        "WHERE t.ContactPersonID = " + this.contactPersonID;
        try {
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                currentDetail = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDetail;
    }

    public List<String> request_teamDetailList(String tableName, String columnName, String orderBy_columnName){
        List<String> locationList = new ArrayList<>();
        String query = "SELECT " + columnName + " FROM " + tableName  + " ORDER BY " + orderBy_columnName;
        try {
            ResultSet rs = this.repository.select_query(query);
            while (rs.next()) {
                locationList.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationList;
    }
 
    public Boolean manage_newDetailUpdate(String tableName, String columnName_detailID, String newDetail){
        try {
            if (insert_newTeamDetail(tableName, newDetail)) {
                Integer newDetailID = fetch_newID(tableName, columnName_detailID);
                return update_newID(tableName, columnName_detailID, newDetailID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Boolean insert_newTeamDetail(String tableName, String newDetail) throws SQLException{
        String query = "INSERT INTO " + tableName + " VALUES ('" + newDetail + "')";
        int rowsAffected = this.repository.insertInto_query(query);
        if (rowsAffected == 1) {
            return true;
        } else if (rowsAffected > 1) {
            System.out.println("\n\n!!!Több hozzáadás történt az INSERT során. Új adat: " + newDetail + ". Valami nem stimmel!!!\n");
        }
        return false;
    }

    public Boolean update_detail(String tableName, String columnName, String detailID, String newDetail){
        String query = "UPDATE " + tableName + " SET " + columnName + " = '" + newDetail + "'\r\n" + //
                        "WHERE " + detailID + " =   \r\n" + //
                        "            (SELECT " + detailID + " FROM Teams" + convert_seasonName() + "\r\n" + //
                        "            WHERE ContactPersonID = " + this.contactPersonID + " AND\r\n" + //
                        "            IsActive = 1 AND\r\n" + //
                        "            SeasonID = " + this.seasonID + ") ";
        try {
            Integer rowsAffected = this.repository.update_query(query);
            if (rowsAffected == 1) {
                return true;
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt az UPDATE során. ID: " + newDetail + ". Valami nem stimmel!!!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean update_password(String tableName, String columnName, String newDetail){

        String query = "UPDATE " + tableName + " SET " + columnName + " = '" + newDetail + "' WHERE IsAdmin = 0 AND ContactPersonID = " + this.contactPersonID;
        try {
            Integer rowsAffected = this.repository.update_query(query);
            if (rowsAffected == 1) {
                return true;
            } else if (rowsAffected > 1) {
                System.out.println("\n\n!!!Több hozzáadás történt az UPDATE során. ID: " + newDetail + ". Valami nem stimmel!!!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Integer fetch_newID(String tableName, String columnName_detailID) throws SQLException{
        String query = "SELECT MAX(" + columnName_detailID + ") FROM " + tableName;
        ResultSet rs = this.repository.select_query(query);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return null;
    }

    public Boolean update_newID(String tableName, String columnName_detailID, Integer newID) throws SQLException{
        String query =  "UPDATE Teams" + convert_seasonName() + " SET " + columnName_detailID + " = " + newID + "\r\n" +
                        "WHERE IsActive = 1 AND\r\n" +
                        "ContactPersonID = " + this.contactPersonID + " AND\r\n" +
                        "SeasonID = " + this.seasonID;
        Integer rowsAffected = this.repository.update_query(query);
        if (rowsAffected == 1) {
            return true;
        } else if (rowsAffected > 1) {
            System.out.println("\n\n!!!Több hozzáadás történt az UPDATE során. ID: " + newID + ". Valami nem stimmel!!!\n");
        }
        return false;
    }


}
