import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 *
 */
public class MovieTheatre {

  /**
   * Static function to generate a UUID.
   *
   * @return The generated UUID as string to store in the tables.
   */
// Using java function to create a unnique id.
  static public String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }


  /**
   * Function which takes an emailID as input and checks whether it is in the correct format or
   * not.
   *
   * @param email the email to be checked for correctness.
   * @return If the emailID is in correct format or not.
   */
  static public boolean isEmail(String email) throws IllegalArgumentException {
    return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
  }


  /**
   * Function to check if the customer who is writting the review for the movie has attended the
   * movie in the last 7 days.
   *
   * @param date       The date the review is posted on.
   * @param customerID The customerID of the customer who posts the review.
   * @param movieID    The movieID whose review the customer has posted.
   * @return true if the review is within 7 days of latest attendence or else false.
   * @throws SQLException   if unable to find attendence in the table.
   * @throws ParseException if unable to parse String of date.
   */

  static public boolean isReviewDateCorrect(Timestamp date, String customerID, String movieID)
          throws SQLException, ParseException {
    String protocol = "jdbc:derby:";
    String dbName = "iRate";
    String connStr = protocol + dbName + ";create=true";

    Properties props = new Properties(); // connection properties
    // providing a user name and password is optional in the embedded
    // and derbyclient frameworks
    props.put("user", "user1");
    props.put("password", "user1");

    try (

            Connection conn = DriverManager.getConnection(connStr, props);

            // statement is channel for sending commands thru connection
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "select AttendanceDate "
                            + "from Attendance "
                            + "WHERE (MOVIEID = '" + movieID
                            + "' AND CUSTOMERID = '"
                            + customerID + "')"
                            + "order by AttendanceDate desc")) {

      if (!rs.next()) {
        return false;
      }
      String dateOfAttendanceString = rs.getString(1);
      //dateOfAttendanceString = dateOfAttendanceString.substring(0,9);
      dateOfAttendanceString = dateOfAttendanceString.replace('-', '/');
      String reviewDateString = date.toString().replace('-', '/');
      Date reviewDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
              .parse(reviewDateString);
      Date dateOfAttendance = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
              .parse(dateOfAttendanceString);

      long difference = reviewDate.getTime() - dateOfAttendance.getTime();
      conn.close();
      return !(difference > 6048 * Math.pow(10, 5) && difference >= 0);
    }

  }


  /**
   * Function to find if the customer has endorsed the same movie within the last 24 hours.
   *
   * @param customerID of the custumer who is endorsing the review.
   * @param reviewID   The reviewID of the review the customer is endorsing.
   * @param date       The date on which the customer is endorsing the reviw.
   * @return true if the endorsement is not within 1 day of the custumer endorsing the review of the
   * same movie else false.
   * @throws SQLException   if unable to execute SQL query for finding customer.
   * @throws ParseException if unable to parse String of date.
   */
  static public boolean hasEndorsedSameMovie(String customerID, String reviewID, Timestamp date)
          throws SQLException, ParseException {
    String protocol = "jdbc:derby:";
    String dbName = "iRate";
    String connStr = protocol + dbName + ";create=true";

    Properties props = new Properties(); // connection properties
    // providing a user name and password is optional in the embedded
    // and derbyclient frameworks
    props.put("user", "user1");
    props.put("password", "user1");
    String movieID = "";

    try {
      Connection conn = DriverManager.getConnection(connStr, props);

      // statement is channel for sending commands thru connection
      Statement stmt = conn.createStatement();

      ResultSet rs = stmt.executeQuery(
              "select MOVIEID "
                      + "from REVIEW "
                      + "WHERE (REVIEWID = '" + reviewID
                      + "')"
      );
      rs.next();
      movieID = rs.getString(1);
    } catch (Exception e) {
      return false;
    }

    try {
      Connection conn = DriverManager.getConnection(connStr, props);

      // statement is channel for sending commands thru connection
      Statement stmt = conn.createStatement();

// Getting all reviewIDs for all endorsements by the customer.
      ResultSet rs = stmt.executeQuery(
              "select REVIEWID "
                      + "from ENDORSEMENT "
                      + "WHERE "
                      + "CUSTOMERID = '"
                      + customerID
                      + "'");

      boolean flag = true;
      while (rs.next()) {
        String reviewIDString = rs.getString(1);
        Statement stmt2 = conn.createStatement();

        //getting the movieIDs for all the reviews from the above query.
        ResultSet rs1 = stmt2.executeQuery(
                "SELECT MOVIEID "
                        + "from REVIEW "
                        + "where REVIEWID = '"
                        + reviewID
                        + "'"
        );
        rs1.next();
        String previousMovieID = rs1.getString(1);
        if (!previousMovieID.equals(movieID)) {
          continue;
        } else {

          // if any of the review returns false then falg will be false.
          flag = flag && isDateWithinOneDay(customerID, reviewIDString, date);

        }
      }
      return flag;

    } catch (Exception e) {
      return false;
    }

  }

  /**
   * Helper function to find if date of the review is within one day of the endorsement. This method
   * is required because there can be multiple reviews for same movie the customer may have
   * endorsed.
   *
   * @param customerID Of the customer who is endorsing the review..
   * @param reviewID   Of the review the customer is going to endorse.
   * @param date       On which the customer is endorsing the review.
   * @return true if the endorsement is after 24 hours of
   */
  static public boolean isDateWithinOneDay(String customerID, String reviewID, Timestamp date)
          throws SQLException, ParseException {
    String protocol = "jdbc:derby:";
    String dbName = "iRate";
    String connStr = protocol + dbName + ";create=true";

    Properties props = new Properties(); // connection properties
    // providing a user name and password is optional in the embedded
    // and derbyclient frameworks
    props.put("user", "user1");
    props.put("password", "user1");
    try (

            Connection conn = DriverManager.getConnection(connStr, props);

            // statement is channel for sending commands thru connection
            Statement stmt = conn.createStatement();

            //Finding the Date of endorsement of the given reviewID and CustomerID.
            ResultSet rs = stmt.executeQuery(
                    "select DATEOFENDORSEMENT "
                            + "from ENDORSEMENT "
                            + "WHERE (REVIEWID = '" + reviewID
                            + "' AND CUSTOMERID = '"
                            + customerID + "')"
            )
    ) {
      if (!rs.next()) {
        return true;
      }
      String pastDateOfEndorsementString = rs.getString(1);
      pastDateOfEndorsementString = pastDateOfEndorsementString.replace('-', '/');

      String currentDateOfEndorsementString = date.toString().replace('-', '/');
      Date currentDateOfEndorsement = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
              .parse(currentDateOfEndorsementString);
      Date pastDateOfEndorsement = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
              .parse(pastDateOfEndorsementString);

      long difference = currentDateOfEndorsement.getTime() - pastDateOfEndorsement.getTime();
      conn.close();
      return difference > 864 * Math.pow(10, 5);

    }
  }

  /**
   * Function to find if the customer his trying to endorse his own movie or not.
   *
   * @param customerId of the customer who is endorsing the review.
   * @param reviewID   of the review the customer is trying to endorse.
   * @return true if the customer is not endorsing his own movie.
   * @throws SQLException if unable to execute the SQL query for finding costumerID form review
   *                      table.
   */
  static public boolean isCustomerDifferentThanEndorser(String customerId, String reviewID)
          throws SQLException {
    String protocol = "jdbc:derby:";
    String dbName = "iRate";
    String connStr = protocol + dbName + ";create=true";

    Properties props = new Properties(); // connection properties
    // providing a user name and password is optional in the embedded
    // and derbyclient frameworks
    props.put("user", "user1");
    props.put("password", "user1");

    try (Connection conn = DriverManager.getConnection(connStr, props);

         // statement is channel for sending commands thru connection
         Statement stmt = conn.createStatement();

//Quering for the customerID of the person who wrote the review.
         ResultSet rs = stmt.executeQuery(
                 "select CustomerID "
                         + "from Review "
                         + "WHERE (REVIEWID = '" + reviewID + "')")) {

      rs.next();
      String reviewer = rs.getString(1);

      conn.close();
      return !customerId.equals(reviewer);

    }


  }


}
