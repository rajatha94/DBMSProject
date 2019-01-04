import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;
import java.sql.ResultSet;

public class Display {

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

      try {
        // query for contents of employee table
        String queryInfo =
            "select * "
                + "from CUSTOMER ";
        ResultSet rs = stmt.executeQuery(queryInfo);
        System.out.println("\nCustomer:");
        System.out.printf("%-40s  %-16s  %-16s %-16s  %-16s\n",
            "CustomerID", "First Name", "Last Name", "Email", "Date Joined");
        System.out.printf("%-40s  %-16s  %-16s %-16s  %-16s\n",
            "--------", "--------", "--------", "--------", "-------");
        while (rs.next()) {
          String customerIDString = rs.getString(1);
          String firstName = rs.getString(2);
          String lastName = rs.getString(3);
          String email = rs.getString(4);
          String time = rs.getTimestamp(5).toString();
          System.out.printf("%-40s  %-16s  %-16s %-16s  %-16s\n",
              customerIDString, firstName, lastName, email, time);
        }
        //rs.close();
      } catch (Exception e) {
        System.out.println("Could not display table view.");
      }

      try {
        // query for contents of employee table
        String queryInfo =
            "select * "
                + "from MOVIE ";
        ResultSet rs = stmt.executeQuery(queryInfo);
        System.out.println("\nMovie:");
        System.out.printf("%-40s  %-16s\n",
            "MovieID", "Title");
        System.out.printf("%-40s  %-16s\n",
            "--------", "--------");
        while (rs.next()) {
          String movieIDString = rs.getString(1);
          String title = rs.getString(2);
          System.out.printf("%-40s  %-16s\n",
              movieIDString, title);
        }
        //rs.close();
      } catch (Exception e) {
        System.out.println("Could not display table view.");
      }

      try {
        // query for contents of employee table
        String queryInfo =
            "select * "
                + "from ATTENDANCE ";
        ResultSet rs = stmt.executeQuery(queryInfo);
        System.out.println("\nAttendance:");
        System.out.printf("%-40s  %-32s  %-16s\n",
            "MovieID", "Attendance Date", "Customer ID");
        System.out.printf("%-40s  %-32s  %-16s\n",
            "--------", "--------", "--------");
        while (rs.next()) {
          String movieIDString1 = rs.getString(1);
          String attendanceDate = rs.getTimestamp(2).toString();
          String customerIDString = rs.getString(3);
          System.out.printf("%-40s  %-32s  %-16s\n",
              movieIDString1, attendanceDate, customerIDString);
        }
        //rs.close();
      } catch (Exception e) {
        System.out.println("Could not display table view.");
      }

      try {
        // query for contents of employee table
        String queryInfo =
            "select * "
                + "from REVIEW ";
        ResultSet rs = stmt.executeQuery(queryInfo);
        System.out.println("\nReview:");
        System.out.printf("%-40s  %-32s  %-40s %-16s  %-16s %-16s\n",
            "MovieID", "Review Date", "Customer ID", "Rating", "Review", "ReviewID");
        System.out.printf("%-40s  %-32s  %-40s %-16s  %-16s %-16s\n",
            "--------", "--------", "--------", "--------", "-------", "--------");
        while (rs.next()) {
          String movieIDString = rs.getString(1);
          String reviewDate = rs.getTimestamp(2).toString();
          String customerIDString = rs.getString(3);
          int rating = rs.getInt(4);
          String review = rs.getString(5);
          String reviewIDString = rs.getString(6);
          System.out.printf("%-40s  %-32s  %-40s %-16d  %-16s %-16s\n",
              movieIDString, reviewDate, customerIDString, rating, review, reviewIDString);
        }
        //rs.close();
      } catch (Exception e) {
        System.out.println("Could not display table view.");
      }

      try {
        // query for contents of employee table
        String queryInfo =
            "select * "
                + "from ENDORSEMENT ";
        ResultSet rs = stmt.executeQuery(queryInfo);
        System.out.println("\nEndorsement:");
        System.out.printf("%-40s  %-40s  %-16s \n",
            "CustomerID", "ReviewID", "Date of Endorsement");
        System.out.printf("%-40s  %-40s  %-16s \n",
            "--------", "--------", "--------");
        while (rs.next()) {
          String customerIDString = rs.getString(1);
          String reviewIDString = rs.getString(2);
          String dateOfEndorsement = rs.getTimestamp(3).toString();
          System.out.printf("%-40s  %-40s  %-16s \n",
              customerIDString, reviewIDString, dateOfEndorsement);
        }
        //rs.close();
      } catch (Exception e) {
        System.out.println("Could not display table view.");
      }


    }

  }
}
