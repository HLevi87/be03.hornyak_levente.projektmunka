package be03.hornyak_levente.Factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LogicQueries {
    public static Integer count_numberOfTeams(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) FROM Teams";
            ResultSet rs = statement.executeQuery(query);
            Integer numberOfTeams = 0;
            while (rs.next()) {
                numberOfTeams = rs.getInt(1);
            }
            return numberOfTeams;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer count_playerPerTeam(Integer teamId){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(*) FROM Players WHERE TeamId = " + teamId;
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
