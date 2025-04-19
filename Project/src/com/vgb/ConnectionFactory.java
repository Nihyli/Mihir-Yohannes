package com.vgb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	/**
	 * User name used to connect to the SQL server
	 */
	private static final String USERNAME = "yhailu2";

	/**
	 * Password used to connect to the SQL server
	 */
	private static final String PASSWORD = "Ne5Ahling1Ki";

	/**
	 * Connection parameters that may be necessary for server configuration
	 * 
	 */
	private static final String PARAMETERS = "";

	/**
	 * SQL server to connect to
	 */
	private static final String SERVER = "cse-linux-01.unl.edu";

	/**
	 * Fully formatted URL for a JDBC connection
	 */
	private static final String URL = String.format("jdbc:mysql://%s/%s?%s", SERVER, USERNAME, PARAMETERS);

    // Method to get a new DB connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    public static void closeConnection() throws SQLException
    {
    	DriverManager.getConnection(URL).close();
    }
    
}
