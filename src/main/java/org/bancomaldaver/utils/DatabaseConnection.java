package org.bancomaldaver.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DatabaseConnection {
  private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
  private static final String URL = "jdbc:mysql://localhost:3306/banco_malvader";
  private static final String USER = "root";
  private static final String PASSWORD = "masterkey";

  public static Connection getConnection() {
    try {
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (Exception e) {
      logger.log(java.util.logging.Level.SEVERE, e.getMessage());
      throw new RuntimeException("Error connecting to the database");
    }
  }
}
