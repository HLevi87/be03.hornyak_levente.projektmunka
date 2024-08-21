package be03.projektmunka.Repository;

import java.sql.ResultSet;

public interface IMainRepository {
    Integer insertInto_table(String table, String columnString, String valuesString);
    ResultSet readFrom_table(String selectionString, String table, String conditionString);
    Integer update_table(String table, String columnName, String newValue, String conditionString);
    Integer deleteFrom_table(String table, String conditionString);
}
