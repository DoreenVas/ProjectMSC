package Model;

import Resources.AlertMessages;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {

    /**
     *
     * Executes given query and returns an array of all the lines of the result.
     * @param myStatement The sql statement
     * @param query The sql query
     * @param columns The wanted columns from the table
     * @return A string array of all the lines from the query's answer
     *
     */
    static String[] executeQuery(Statement myStatement, String query, String[] columns) throws SQLException {
        String result = "";
        try {
            // execute query and get result
            ResultSet myRes = myStatement.executeQuery(query);
            result = joinResult(myRes, columns);
            // close result
            myRes.close();
        } catch (Exception e) {
            throw new SQLException(AlertMessages.queryExecutionFailure(), e);
        }
        return result.split("\n");
    }

    /**
     *
     * Executes given query and returns an array of all the lines of the result.
     * @param myStatement The sql statement
     * @param query The sql query
     * @param columns The wanted columns from the table
     * @return A string array of all the lines from the query's answer
     *
     */
    static void executeInsertQuery(Statement myStatement, String query, String[] columns) throws SQLException {
        try {
            // execute insert query
            myStatement.executeUpdate(query);
        } catch (Exception e) {
            throw new SQLException(AlertMessages.queryExecutionFailure(), e);
        }
    }
    /**
     *
     * Executes given query from a prepared statement, and returns an array of all the lines of the result.
     * @param myStatement The sql statement
     * @param columns The wanted columns from the table
     * @return A string array of all the lines from the query's answer
     *
     */
    static String[] executeQuery(PreparedStatement myStatement, String[] columns) throws SQLException {
        String result = "";
        try {
            // execute query and get result
            ResultSet myRes = myStatement.executeQuery();
            result = joinResult(myRes, columns);
            // close result
            myRes.close();
        } catch (Exception e) {
            throw new SQLException(AlertMessages.queryExecutionFailure(), e);
        }
        return result.split("\n");
    }

    /**
     *
     * @param myRes the result set that was returnes from the query.
     * @param columns the results columns.
     * @return the queries data as a string.
     * @throws SQLException throws exception if there is a problem reading from given resultSet.
     */
    private static String joinResult(ResultSet myRes, String[] columns) throws SQLException {
        int j = 0;
        // initialize builder
        StringBuilder builder = new StringBuilder();
        // go over results
        while(myRes.next()) {
            // append each result to builder
            for (int i = 0; i < columns.length - 1; i++) {
                builder.append(myRes.getString(columns[i])).append(",");
            }
            builder.append(myRes.getString(columns[columns.length - 1]));
            builder.append('\n');
            j++;
        }
        return builder.toString();
    }
}
