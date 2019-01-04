import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Class to drop and create db, functions and tables.
 */
public class Project {

  public static void main(String[] args) throws SQLException {

    String protocol = "jdbc:derby:";
    String dbName = "iRate";
    String connStr = protocol + dbName + ";create=true";
    Properties props = new Properties(); // connection properties
    // providing a user name and password is optional in the embedded
    // and derbyclient frameworks
    props.put("user", "user1");
    props.put("password", "user1");
    try (
        // connect to the database using URL
        Connection conn = DriverManager.getConnection(connStr, props);

        // statement is channel for sending commands thru connection
        Statement stmt = conn.createStatement();
    ) {
      System.out.println("Connected to and created database " + dbName);

      // Foreign key constraints created by this program
      String dbconstraints[] = {
          "movieIDAttendance", "customerIDAttendance", "movieIDReview", "customerIDReview",
          "reviewIDEndorsement"
      };

//DROPPING the constraints created by this program so that it doesn't interfere while dropping of the tables.
      for (String tbl : dbconstraints) {
        try {
          stmt.executeUpdate("alter table " + tbl + " DROP CONSTRAINT " + tbl);
          System.out.println("Dropped constraint " + tbl);
        } catch (SQLException ex) {
          System.out.println("Did not drop constraint " + tbl);
        }
      }

// Functions used in the database.
      String dbFunctions[] = {
          "reviewDateCheck", "endorserCheck", "isSameDayEndorsement", "checkDuplicateEndorsement",
          "generateUuid", "isEmail", "isCustomerDifferentThanEndorser", "isReviewDateCorrect",
          "hasEndorsedSameMovie"
      };

//DROPPING the functions created by this program.
      for (String fn : dbFunctions) {
        try {
          stmt.executeUpdate("drop function " + fn);
          System.out.println("Dropped function " + fn);
        } catch (SQLException ex) {
          System.out.println("Did not drop function " + fn);
        }
      }

//List of tables to drop in the database.
      String dbTables[] = {
          "Attendance", "Endorsement", "Review", "Customer", "Movie"
      };

//Dropping the tables.
      for (String tbl : dbTables) {
        try {
          stmt.executeUpdate("drop table " + tbl);
          System.out.println("Dropped table " + tbl);
        } catch (SQLException ex) {
          System.out.println("Did not drop table " + tbl);
        }
      }

// Dropping the type UUID.
      try {
        stmt.executeUpdate("drop type uuid restrict ");
        System.out.println("Dropped uuid type");
      } catch (Exception e) {
        System.out.println("Did not drop uuid type");
      }

      //CREATION OF FUNCTION AND TYPE for UUID generation.
      try {
        String createUUIDType = "create type uuid"
            + " external name 'java.util.UUID'"
            + " language java";
        stmt.executeUpdate(createUUIDType);
        System.out.println("uuid type created");
      } catch (Exception e) {
        System.out.println("UUID type not created");
      }

// Creating function for generating UUID.
      try {
        String generateUUID = "create function generateUuid() returns varchar(64)\n"
            + "language java\n"
            + "parameter style java\n"
            + "external name 'MovieTheatre.generateUUID'";
        stmt.executeUpdate(generateUUID);
        System.out.println("Created generateUUID function");
      } catch (Exception e) {
        System.out.println("Did not create function generateUUID");
      }

// Creating function for checking if email is in correct format.
      try {
        String isEmail = "create function isEmail"
            + "(email varchar(64)"
            + ") returns boolean"
            + " PARAMETER STYLE JAVA"
            + " LANGUAGE JAVA"
            + " DETERMINISTIC"
            + " NO SQL"
            + " EXTERNAL NAME "
            + "	'MovieTheatre.isEmail'";
        stmt.executeUpdate(isEmail);
        System.out.println("isEmail created");
      } catch (Exception e) {
        System.out.println("isEmail not created");
      }

//Function for checking if the review date follows the constraints we want.
      try {
        String isReviewDateCorrect = "create function isReviewDateCorrect"
            + "(date TIMESTAMP,"
            + "customerID VARCHAR(64),"
            + "movieID VARCHAR(64)"
            + ") returns boolean"
            + " PARAMETER STYLE JAVA"
            + " LANGUAGE JAVA"
            + " DETERMINISTIC"
            + " NO SQL"
            + " EXTERNAL NAME "
            + "	'MovieTheatre.isReviewDateCorrect'";
        stmt.executeUpdate(isReviewDateCorrect);
        System.out.println("isReviewDateCorrect created");
      } catch (Exception e) {
        System.out.println("isReviewDateCorrect not created");
      }

// Function to check if the customer is not endorsing his own review.
      try {
        String isCustomerDifferentThanEndorser = "create function isCustomerDifferentThanEndorser"
            + "(customerID VARCHAR(64),"
            + "reviewID VARCHAR(64)"
            + ") returns boolean"
            + " PARAMETER STYLE JAVA"
            + " LANGUAGE JAVA"
            + " DETERMINISTIC"
            + " NO SQL"
            + " EXTERNAL NAME "
            + "	'MovieTheatre.isCustomerDifferentThanEndorser'";
        stmt.executeUpdate(isCustomerDifferentThanEndorser);
        System.out.println("iisCustomerDifferentThanEndorser created");
      } catch (Exception e) {
        System.out.println("isCustomerDifferentThanEndorser not created");
      }

//Function to check if the endorser has endorsed same moview within 1 day of endorsing the review of the same movie.
      try {
        String hasEndorsedSameMovie = "create function hasEndorsedSameMovie"
            + "(customerID VARCHAR(64),"
            + "reviewID VARCHAR(64),"
            + "dateValue TIMESTAMP "
            + ") returns boolean"
            + " PARAMETER STYLE JAVA"
            + " LANGUAGE JAVA"
            + " DETERMINISTIC"
            + " NO SQL"
            + " EXTERNAL NAME "
            + "	'MovieTheatre.hasEndorsedSameMovie'";
        stmt.executeUpdate(hasEndorsedSameMovie);
        System.out.println("hasEndorsedSameMovie created");
      } catch (Exception e) {
        System.out.println("hasEndorsedSameMovie not created");
      }

      // create the Customer table
      String createTable_Customer =
          "create table Customer ("
              + "  CustomerID varchar(64) not null ,"
              + "  first_Name varchar(32) not null,"
              + "  last_Name varchar(32),"
              + "  email varchar(64),"
              + "  date TIMESTAMP,"
              + "  primary key (CustomerID),"
              + "  check (isEmail(email))"
              + ")";
      stmt.executeUpdate(createTable_Customer);
      System.out.println("Created entity table Customer");

// create the Movie table.
      String createTable_Movie =
          "create table Movie ("
              + "  MovieID varchar(64) not null ,"
              + "  Title varchar(64) not null,"
              + "PRIMARY KEY (MovieID)"
              + ")";
      stmt.executeUpdate(createTable_Movie);
      System.out.println("Created entity table Movie");

// create the Attendence table.
      String createTable_Attendance =
          "create table Attendance ("
              + "  MovieID varchar(64) not null ,"
              + "  AttendanceDate TIMESTAMP not null,"
              + "  CustomerID varchar(64) not null ,"
              + "constraint movieIDAttendance foreign key(MovieID) references Movie(MovieID) ON DELETE CASCADE,"
              + "constraint customerIDAttendance foreign key(CustomerID) references Customer(CustomerID) ON DELETE CASCADE"
              + ")";
      stmt.executeUpdate(createTable_Attendance);
      System.out.println("Created entity table attendance");

// create the Review table.
      String createTable_Review =
          "create table Review ("
              + "  MovieID varchar(64) not null ,"
              + "  ReviewDate TIMESTAMP not null,"
              + "  CustomerID varchar(64) not null ,"
              + "  rating integer check (rating >= 0 and rating <= 5),"
              + "  review varchar(1000),"
              + "  ReviewID varchar(64) ,"
              + "primary key (ReviewID),"
              + "constraint movieIDReview foreign key(MovieID) references Movie(MovieID) ON DELETE CASCADE,"
              + "constraint customerIDReview foreign key(CustomerID) references Customer(CustomerID) ON DELETE CASCADE,"
              + "unique (CustomerID, MovieID), "
              + "check(isReviewDateCorrect(ReviewDate, CustomerID, MovieID))"
              + ")";
      stmt.executeUpdate(createTable_Review);
      System.out.println("Created entity table review");

// create the Endorsement table.
      String createTable_Endorsement =
          "create table Endorsement ("
              + "  CustomerID varchar(64) not null ,"
              + "  ReviewID varchar(64) not null ,"
              + "  DateOfEndorsement TIMESTAMP, "
              + "primary key (ReviewID, CustomerID),"
              + "foreign key (CustomerID) references CUSTOMER(CUSTOMERID) ON DELETE CASCADE,"
              + "constraint reviewIDEndorsement foreign key (ReviewID) references REVIEW(ReviewID) on delete cascade ,"
              + "check (isCustomerDifferentThanEndorser(CustomerID, ReviewID)),"
              + "check (hasEndorsedSameMovie(CustomerID, ReviewID, DateOfEndorsement))"
              + ")";
      stmt.executeUpdate(createTable_Endorsement);

    }
  }
}
