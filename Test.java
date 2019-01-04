import java.sql.*;
import java.util.Properties;
import java.util.ArrayList;

/**
 * Class to insert values into tables created from Project.java and tests for exception handling.
 */
public class Test {

  public static void main(String[] args) throws SQLException {

    String protocol = "jdbc:derby:";
    String dbName = "iRate";
    String connStr = protocol + dbName + ";create=true";
    Properties props = new Properties(); // connection properties
    props.put("user", "user1");
    props.put("password", "user1");
    try (
        // connect to the database using URL
        Connection conn = DriverManager.getConnection(connStr, props);

        // statement is channel for sending commands thru connection
        Statement stmt = conn.createStatement();
    ) {

      // List of customer First names to add in customer table.
      String customerFirstNames[] = {
          "'Siddhant'",
          "'Shree Rajatha'",
          "'Shraddha'",
          "'Ashutosh'",
          "'Kiran'",
          "'Monica'",
          "'Pooja'",
          "'Arcey'",
          "'Max'",
          "'Sushrut'"
      };

// List of customer Last names to add in customer table.
      String customerLastNames[] = {
          "'V'",
          "'V'",
          "'S'",
          "'M'",
          "'B'",
          "'K'",
          "'S'",
          "'S'",
          "'Z'",
          "'B'"
      };

// List of Movie names to add in Movie table.
      String movieNames[] = {
          "'Sivaji'",
          "'La La Land'",
          "'Harry Potter'",
          "'Mission Impossible'",
          "'Dabbang'"
      };

      /**
       * Insert Data successfully into Customer table.
       */
      System.out.println("TEST to Insert Data successfully into Customer table.");
      for (int i = 0; i < 10; i++) {
        try {
          stmt.executeUpdate("insert into CUSTOMER values "
              + "("
              + "GENERATEUUID(),"
              + customerFirstNames[i] + ","
              + customerLastNames[i] + ","
              + "'abc@gmail.com',"
              + "'1962-09-23 03:23:34.23'"
              + ")");
          System.out.println("Inserted Custumer " + customerFirstNames[i]);
        } catch (SQLException ex) {
          System.out.println("Did not insert Customer " + customerFirstNames[i]);
        }

      }

      /**
       * Insert Data successfully into Movie table.
       */

      System.out.println("TEST to Insert Data successfully into Movie table.");
      for (int i = 0; i < 5; i++) {
        try {
          stmt.executeUpdate("insert into MOVIE values "
              + "("
              + "GENERATEUUID(),"
              + movieNames[i]
              + ")");
          System.out.println("Inserted Movie " + movieNames[i]);
        } catch (SQLException ex) {
          System.out.println("Did not insert Movie " + movieNames[i]);
        }

      }
      String customerUUID;
      String movieUUID;
      Statement stmt2 = conn.createStatement();
      Statement stmt3 = conn.createStatement();
      Statement stmt4 = conn.createStatement();
      ResultSet rs = stmt.executeQuery(
          "select CUSTOMERID from CUSTOMER");
      ResultSet moveRS = stmt2.executeQuery(
          "select MOVIEID from MOVIE");

      /**
       * Insert Data successfully into Attendance table.
       */

      System.out.println("TEST to Insert Data successfully into Attendance table.");
      for (int i = 0; i < 5; i++) {
        rs.next();
        moveRS.next();
        customerUUID = rs.getString(1);
        movieUUID = moveRS.getString(1);

        //Insert valid data into Attendance
        try {
          stmt3.executeUpdate("insert into ATTENDANCE values "
              + "("
              + "'" + movieUUID + "'" + ","
              + "'1962-09-23 03:23:34.23',"
              + "'" + customerUUID + "'"
              + ")");
          System.out.println("Inserted Attendance for " + customerUUID);
        } catch (SQLException ex) {
          System.out.println("Did not insert Attendance for " + customerUUID);
        }

      }

      rs = stmt.executeQuery(
          "select CUSTOMERID from CUSTOMER");
      moveRS = stmt2.executeQuery(
          "select MOVIEID from MOVIE");

      /**
       * Insert Data successfully into Review table.
       */

      System.out.println("TEST to Insert Data successfully into Review table.");
      for (int i = 0; i < 5; i++) {
        rs.next();
        moveRS.next();
        customerUUID = rs.getString(1);
        movieUUID = moveRS.getString(1);
        try {
          stmt3.executeUpdate("insert into REVIEW values "
              + "("
              + "'" + movieUUID + "'" + ","
              + "'1962-09-23 03:23:34.23',"
              + "'" + customerUUID + "',"
              + "5,"
              + "'Good Movie',"
              + "GENERATEUUID()"
              + ")");
          System.out.println("Inserted review for " + customerUUID);
        } catch (SQLException ex) {
          System.out.println("Did not insert review for " + customerUUID);
        }

      }

      //Insert valid data into Endorsement
      rs = stmt.executeQuery(
          "select CUSTOMERID from CUSTOMER");
      for (int i = 0; i < 5; i++) {
        rs.next();
      }
      ResultSet reviewRS = stmt2.executeQuery(
          "select REVIEWID from REVIEW");
      String reviewUUID;

      /**
       * Insert Data successfully into Endorsement table.
       */

      System.out.println("TEST to Insert Data successfully into Endorsement table.");
      for (int i = 0; i < 5; i++) {
        try {
          rs.next();
          reviewRS.next();
        } catch (Exception e) {
          System.out.println("NEXT ERROR");
        }
        customerUUID = rs.getString(1);
        reviewUUID = reviewRS.getString(1);
        try {
          stmt3.executeUpdate("insert into ENDORSEMENT values "
              + "("
              + "'" + customerUUID + "'" + ","
              + "'" + reviewUUID + "'" + ","
              + "'1962-09-23 03:23:34.23'"
              + ")");
          System.out.println("Inserted endorsement for " + reviewUUID);
          //stmt3.close();
        } catch (SQLException ex) {
          System.out.println("Did not endorsement for " + reviewUUID);
        }

      }

      /**
       * Test to check if insertion with invalid email ID fails.
       */
      System.out.println("TEST to check if insertion with invalid email ID fails.");
      try {
        stmt.executeUpdate("insert into CUSTOMER values "
            + "("
            + "GENERATEUUID(),"
            + "abc" + ","
            + "efg" + ","
            + "'abc@',"
            + "'1962-09-23 03:23:34.23'"
            + ")");
        System.out.println("Inserted Customer " + "abc");
      } catch (SQLException ex) {
        System.out.println("Did not insert Customer " + "abc");
      }

      /**
       * Test to check for review without attendance.
       */

      System.out.println("TEST to check for review without attendance.");
      movieUUID = "'a7d3362f-bb56-4e80-b105-1a1c2d47f40d'";
      customerUUID = "'a7d3362f-bb56-4e80-b105-1a1c2d47f401'";
      try {
        stmt3.executeUpdate("insert into REVIEW values "
            + "("
            + "'" + movieUUID + "'" + ","
            + "'1962-09-23 03:23:34.23',"
            + "'" + customerUUID + "',"
            + "5,"
            + "'Good Movie',"
            + "GENERATEUUID()"
            + ")");
        System.out.println("Inserted review for " + customerUUID);
      } catch (SQLException ex) {
        System.out.println("Did not insert review for " + customerUUID);
      }

      rs = stmt.executeQuery(
          "select CUSTOMERID from CUSTOMER");
      moveRS = stmt2.executeQuery(
          "select MOVIEID from MOVIE");

      ResultSetMetaData rsmd = rs.getMetaData();
      int columnCount = rsmd.getColumnCount();
      ;
      ArrayList<String> customerIDList = new ArrayList<>(columnCount);

      ResultSetMetaData rsmd1 = moveRS.getMetaData();
      int columnCount1 = rsmd1.getColumnCount();
      ArrayList<String> movieList = new ArrayList<>(columnCount);

      while (rs.next()) {
        int i = 1;
        while (i <= columnCount) {
          customerIDList.add(rs.getString(i++));
        }
      }

      while (moveRS.next()) {
        int i = 1;
        while (i <= columnCount1) {
          movieList.add(moveRS.getString(i++));
        }
      }

      rs = stmt4.executeQuery("select REVIEWID from REVIEW");
      ResultSetMetaData resultSetMetaData = rs.getMetaData();
      int reviewColumnSize = resultSetMetaData.getColumnCount();
      ArrayList<String> reviewList = new ArrayList<>(reviewColumnSize);

      while (rs.next()) {
        reviewList.add(rs.getString(1));
      }

      customerUUID = customerIDList.get(0);
      movieUUID = movieList.get(0);

      /**
       * Test to check if insert fails when same customer writes review for the same movie again.
       */

      System.out.println(
          "TEST to check if insert fails when same customer writes review for the same movie again.");
      try {
        stmt3.executeUpdate("insert into REVIEW values "
            + "("
            + "'" + movieUUID + "'" + ","
            + "'1962-29-23 03:23:34.23',"
            + "'" + customerUUID + "',"
            + "5,"
            + "'Good Movie',"
            + "GENERATEUUID()"
            + ")");
        System.out.println("Inserted review for " + customerUUID);
      } catch (SQLException ex) {
        System.out.println("Did not insert review for " + customerUUID);
      }

      /**
       * Test to check if user is able to attend same movie multiple times.
       */

      System.out.println("TEST to check if user is able to attend same movie multiple times.");
      try {
        stmt3.executeUpdate("insert into ATTENDANCE values "
            + "("
            + "'" + movieUUID + "'" + ","
            + "'1962-09-23 03:23:34.23',"
            + "'" + customerUUID + "'"
            + ")");
        System.out.println("Inserted Attendance for " + customerUUID);
      } catch (SQLException ex) {
        System.out.println("Did not insert Attendance for " + customerUUID);
      }

      /**
       * Test to check if insertion fails if you endorse a 2nd review before 24 hours of endorsing a first review of the same.
       */
      System.out.println(
          "TEST to check if insertion fails if you endorse a 2nd review before 24 hours of endorsing a first review of the same.");
      String reviewer = customerIDList.get(1);
      String customerID = customerIDList.get(5);
      String movieID = movieList.get(0);
      String reviewID = reviewList.get(0);

      //Making the custumer attend the movie.
      try {
        stmt3.executeUpdate("insert into ATTENDANCE values "
            + "("
            + "'" + movieID + "'" + ","
            + "'1962-09-23 03:23:34.23',"
            + "'" + reviewer + "'"
            + ")");
        System.out.println("Inserted Attendance for " + customerUUID);
      } catch (SQLException ex) {
        System.out.println("Did not insert Attendance for " + customerUUID);
      }

      //Making the custumer write review for the movie he attended.
      try {
        stmt3.executeUpdate("insert into REVIEW values "
            + "("
            + "'" + movieID + "'" + ","
            + "'1962-09-23 03:23:34.23',"
            + "'" + reviewer + "',"
            + "5,"
            + "'Good Movie',"
            + "GENERATEUUID()"
            + ")");
        System.out.println("Inserted review for " + customerUUID);
      } catch (SQLException ex) {
        System.out.println("Did not insert review for " + customerUUID);
      }

      rs = stmt.executeQuery(
          "select REVIEWID from REVIEW WHERE (CUSTOMERID =" + "'" + reviewer + "'" + "AND "
              + "MOVIEID = '" + movieID + "'" + ")");
      rs.next();

      reviewID = rs.getString(1);

      //Making the endorser endorse the review of same move for before 24 hours are over.
      try {
        stmt3.executeUpdate("insert into ENDORSEMENT values "
            + "("
            + "'" + customerID + "'" + ","
            + "'" + reviewID + "'" + ","
            + "'1962-09-23 03:23:34.23'"
            + ")");
        System.out.println("Inserted endorsement for " + reviewID);
        //stmt3.close();
      } catch (SQLException ex) {
        System.out.println("Did not endorsement for " + reviewID);
      }


    }
  }

}
