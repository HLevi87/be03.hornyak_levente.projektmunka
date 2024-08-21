package be03.projektmunka.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class MainRepository implements IMainRepository{
    private final String DBpath = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;";
    protected Connection connection;
    private String season;


    public MainRepository() {
        try {
            this.connection = DriverManager.getConnection(DBpath);
            set_defaultSeason();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void set_defaultSeason() throws SQLException{
        Statement statement = this.connection.createStatement();
        String query = "SELECT * FROM Seasons WHERE SeasonID = (SELECT MAX(SeasonID) FROM Seasons)";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            this.season = rs.getString(2);
        }
    }

    public String getSeason() {
        return season;
    }

    

    
}


