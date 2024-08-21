package be03.hornyak_levente.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainRepo {
    protected Connection connection;
    private final String URL = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;";
    protected String user;
    protected String pw;

    public MainRepo() {
        //KÜLSŐ BEJELENTKEZÉSI ADATOK MEGADÁSAKOR EZT A 3 SORT KI KELL TÖRÖLNI!!!
        this.user = "java";
        this.pw = "java";
        set_connection();
    }

    public void insert_userName(String userName){
        this.user = userName;
    }

    public void insert_password(String password){
        this.pw = password;
    }

    public void set_connection(){
        try {
            String DBpath = URL + "user=" + this.user + ";password=" + this.pw + ";";
            this.connection = DriverManager.getConnection(DBpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean test_connection(){
        //Ha a felhasználó által megadott adatokkal be tud lépni az adatbázisba, és le tud kérni egy egyszerű adatot, akkor a kapcsolat megfelelően létrejött.
        try {
            Statement statement = this.connection.createStatement();
            String query = "SELECT COUNT(*) FROM Balls";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    

}
