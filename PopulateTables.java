/*PopulateTables.java
 * 
 * Description: Script used to populate the databases tables.
 *              the popolate function is public so that it
 *              can also be called from the database client
 *              with PopulateTables.populate(Connection n).
 *              ? placeholders are used where there may be 
 *              ' inside the input.
 * 
 *              Indexes used to get attributes are the same ones
 *              you would use if you were just downloading the dataset
 *              from the source except for the athletes dataset since 
 *              some of the unused columns were deleted using excel
 */

 import java.io.BufferedReader;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.FileReader;
 import java.io.IOException;
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import java.util.Properties;
 
 public class PopulateTables {
 
     // Main is only used to populate the tables outside the user interface
     public static void main(String[] args) {
 
         Properties prop = new Properties();
         String fileName = "auth.cfg";
         try {
             FileInputStream configFile = new FileInputStream(fileName);
             prop.load(configFile);
             configFile.close();
         } catch (FileNotFoundException ex) {
             System.out.println("Could not find config file.");
             System.exit(1);
         } catch (IOException ex) {
             System.out.println("Error reading config file.");
             System.exit(1);
         }
         String username = (prop.getProperty("username"));
         String password = (prop.getProperty("password"));
 
         if (username == null || password == null){
             System.out.println("Username or password not provided.");
             System.exit(1);
         }
 
         String connectionUrl =
                 "jdbc:sqlserver://server;"
                 + "database=database_name;"
                 + "user=" + username + ";"
                 + "password="+ password +";"
                 + "encrypt=false;"
                 + "trustServerCertificate=false;"
                 + "loginTimeout=30;";
 
         try (Connection connection = DriverManager.getConnection(connectionUrl);
                 Statement statement = connection.createStatement();) {
 
             populate(connection);
 
         }
         catch (SQLException e) {
             e.printStackTrace();
         }
     }
 
     public static void populate(Connection connection) {
         System.out.println("Creating Tables");
         createTables(connection);
 
         System.out.println("Populating Tables: Country");
         populateCountry(connection);
 
         System.out.println("Populating Tables: Events");
         populateEvents(connection);
 
         System.out.println("Populating Tables: Athlete, Participate");
         populateAthlete(connection);
 
         System.out.println("Populating Tables: Coach");
         populateCoach(connection);
 
         System.out.println("Populating Tables: Teams, BelongTo, Train");
         populateTeams(connection);
 
         System.out.println("Populating Tables: Medals, Win");
         populateMedals(connection);
 
         System.out.println("Populating Complete");
     }
 
     //Method used to create the database tables
     private static void createTables(Connection connection) {
         try {
            connection.setAutoCommit(false);
 
             String sql = "use cs3380; " +
             "drop table if exists Win; " +
             "drop table if exists BelongTo; " +
             "drop table if exists Train; " +
             "drop table if exists Participate; " + 
             "drop table if exists Medals; " +
             "drop table if exists Teams; " +
             "drop table if exists Coach; " + 
             "drop table if exists Athlete; " +
             "drop table if exists Events; " +
             "drop table if exists Country; " ;
             
 
             PreparedStatement statement = connection.prepareStatement(sql);
             statement.execute();
 
              
             sql = "create table Country(" +
             "   countryCode varchar(3) primary key, " +
             "   name varchar(max) not null, " +
             "   medalCount numeric default 0, " +
             "   gdp numeric); ";
 
             statement = connection.prepareStatement(sql);
             statement.execute();
 
             sql = "create table Events(" +
             "   eventID integer primary key IDENTITY(1,1), " +
             "   sport varchar(max) not null, " +
             "   event varchar(max) not null); ";
 
             statement = connection.prepareStatement(sql);
             statement.execute();
 
             sql = "create table Athlete(" +
             "   athleteID integer primary key, " +
             "   countryCode varchar(3) references Country(countryCode), " +
             "   name varchar(max) not null, " +
             "   gender varchar(max) not null, " +
             "   birthYear numeric not null); ";
 
             statement = connection.prepareStatement(sql);
             statement.execute();
             
             sql = "create table Coach(" +
             "   coachID integer primary key, " +
             "   countryCode varchar(3) references Country(countryCode), " +
             "   name varchar(max) not null, " +
             "   position varchar(max) not null, " +
             "   gender varchar(max) not null, " +
             "   birthYear numeric not null); ";
 
             statement = connection.prepareStatement(sql);
             statement.execute();
             
             sql = "create table Teams(" +
             "   teamID integer primary key, " +
             "   teamCode varchar(max) not null," +
             "   countryCode varchar(3) references Country(countryCode), " +
             "   eventID integer references Events(eventID), " +
             "   gender varchar(max) not null); ";
 
             statement = connection.prepareStatement(sql);
             statement.execute();
             
             sql = "create table Medals(" +
             "   medalID integer primary key, " +
             "   eventID integer references Events(eventID), " +
             "   medalType varchar(max) not null); ";
 
             statement = connection.prepareStatement(sql);
             statement.execute();
 
              
             sql = "create table Participate(" +
             "   eventID integer references Events(eventID), " +
             "   athleteID integer references Athlete(athleteID), " +
             "   primary key (eventID, athleteID)); ";
             
             statement = connection.prepareStatement(sql);
             statement.execute();
 
             sql = "create table Train(" +
             "   teamID integer references Teams(teamID), " +
             "   coachID integer references Coach(coachID), " +
             "   primary key (teamID, coachID)); ";
 
             statement = connection.prepareStatement(sql);
             statement.execute();
 
             sql = "create table BelongTo(" +
             "   teamID integer references Teams(teamID), " +
             "   athleteID integer references Athlete(athleteID), " +
             "   primary key (teamID, athleteID)); ";
 
             statement = connection.prepareStatement(sql);
             statement.execute();
             
             sql = "create table Win(" +
             "   medalID integer references Medals(medalID), " +
             "   athleteID integer references Athlete(athleteID), " +
             "   primary key (medalID, athleteID)); ";
 
             statement = connection.prepareStatement(sql);
             statement.execute();
         
            connection.setAutoCommit(true);
 
         } catch (SQLException e) {
             System.out.println("Transaction failed");
             try{
                 connection.rollback();
             }
             catch(SQLException e2) {
                 System.out.println("Failed to rollback");
             }
         }
     }
 
     private static void populateCountry(Connection connection) {
         BufferedReader input;
         String[] tokens;
         String line;
         String sql;
         PreparedStatement statement;
         
         try {
             input = new BufferedReader(new FileReader("nocs.csv"));			
             line = input.readLine();
             line = input.readLine();
 
             while (line != null) {
                 tokens = line.split(",");
                 
                 try {
                     // Prepared statement used for country name since a couple countries have single ' in their names
                     if (tokens.length > 1) {
                         tokens[1] = format(tokens[1]);
                         sql = "insert into Country(countryCode, name) values('" + tokens[0] + "',?);";
 
                         statement = connection.prepareStatement(sql);
                         statement.setString(1, tokens[1]);
                         statement.executeUpdate();
                     }
                 } catch(SQLException e) {
                     e.printStackTrace();
                 }
                 
                 line = input.readLine();
             }	
                 
             input.close();
 
             input = new BufferedReader(new FileReader("GDP.csv"));
             line = input.readLine();
             line = input.readLine();
 
             double gdp = 0;
             while (line != null) {
                 tokens = line.split(",");
                 
                 try {
                     // GDPs start from the 1960s so we want make sure there is some recent data
                     // and then we use the most recent year
                     if (tokens.length > 20 && tokens[tokens.length - 1].length() > 0) {
                         tokens[0] = format(tokens[0]);
 
                         gdp = Double.parseDouble(tokens[tokens.length - 1]);
 
                         sql = "update Country set gdp = " + gdp +
                         " where name like ?;";
 
                         statement = connection.prepareStatement(sql);
                         statement.setString(1, tokens[0]);
                         statement.executeUpdate();
                     }
                 } catch(SQLException e) {
                     e.printStackTrace();
                 }
                 
                 line = input.readLine();
             }	
             
             input.close();
 
             input = new BufferedReader(new FileReader("medals_total.csv"));
             line = input.readLine();
             line = input.readLine();
 
             while (line != null) {
 
                 tokens = line.split(",");
                 
                 try {
                     if (tokens.length > 1) {
                     sql = "update Country set medalCount = " + tokens[tokens.length - 1] +
                     " where countryCode = '"+ tokens[0] + "';";
 
                     statement = connection.prepareStatement(sql);
                     statement.executeUpdate();
                     }
                 } catch(SQLException e) {
                     e.printStackTrace();
                 }
                 
                 line = input.readLine();
             }
 
             input.close();
             
             updateGDP(connection);
 
         } catch (IOException ioe) {
             System.out.println(ioe.getMessage());
         }
     }
 
     private static void populateEvents(Connection connection) {
         BufferedReader input;
         String[] tokens;
         String line;
         String sql;
         PreparedStatement statement;
         
         try {
             input = new BufferedReader(new FileReader("events.csv"));			
             line = input.readLine();
             line = input.readLine();
 
             while (line != null) {
                 tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                 
                 try {
                     if (tokens.length > 2) {
                         tokens[0] = format(tokens[0]);
                         tokens[2] = format(tokens[2]);
 
                         sql = "insert into Events(sport, event) values(?,?);";
 
                         statement = connection.prepareStatement(sql);
                         statement.setString(1, tokens[2]);
                         statement.setString(2, tokens[0]);
                         statement.executeUpdate();
                     }
 
                 } catch(SQLException e) {
                     e.printStackTrace();
                 }
                 
                 line = input.readLine();
             }	
                 
             input.close();
             
         } catch (IOException ioe) {
             System.out.println(ioe.getMessage());
         }
     }
 
     private static void populateAthlete(Connection connection) {
         BufferedReader input;
         String[] tokens;
         String[] multi;
         String line;
         String sql;
         PreparedStatement statement;
         int code;
         int year;
         
         try {
             input = new BufferedReader(new FileReader("athletes.csv"));			
             line = input.readLine();
             line = input.readLine();
 
             while (line != null) {
                 tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                 
                 try {
                     if (tokens.length > 7) {
                         code = Integer.parseInt(tokens[0]);
 
                         multi = tokens[tokens.length-1].split("-");
                         year = Integer.parseInt(multi[0]);
 
                         sql = "insert into Athlete(athleteID, countryCode, name, gender, birthYear)" + 
                         "values(" + code + ",'" + tokens[5] + "',?,'" + tokens[3] + "'," + year + ");";
 
                         statement = connection.prepareStatement(sql);
 
                         statement.setString(1, tokens[1]);
 
                         statement.executeUpdate();
 
                         // Athletes dataset also includes the athletes events
                         populateParticipate(connection, code, tokens[6], tokens[7]);
                     }
 
                 } catch(SQLException e) {
                     e.printStackTrace();
                 }
                 
                 line = input.readLine();
             }	
                 
             input.close();
             
         } catch (IOException ioe) {
             System.out.println(ioe.getMessage());
         }
     }
 
     private static void populateCoach(Connection connection) {
         BufferedReader input;
         String[] tokens;
         String[] temp;
         String line;
         String sql;
         PreparedStatement statement;
         int code;
         int year;
         
         try {
             input = new BufferedReader(new FileReader("coaches.csv"));			
             line = input.readLine();
             line = input.readLine();
 
             while (line != null) {
                 tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                 
                 try {
                     if (tokens.length > 7) {
                         code = Integer.parseInt(tokens[0]);
 
                         if (tokens[tokens.length-1].length() > 0) {
                             temp = tokens[tokens.length-1].split("-");
                             year = Integer.parseInt(temp[0]);
                         } else {
                             year = 0;
                         }
 
                         sql = "insert into Coach(coachID, countryCode, position, name, gender, birthYear)" + 
                         "values("+ code +",'"+ tokens[6] +"','"+ tokens[4] +"',?,'"+ tokens[3] +"',"+ year +");";
 
                         statement = connection.prepareStatement(sql);
 
                         statement.setString(1, tokens[2]);
 
                         statement.executeUpdate();
                     }
 
                 } catch(SQLException e) {
                     e.printStackTrace();
                 }
                 
                 line = input.readLine();
             }	
                 
             input.close();
             
         } catch (IOException ioe) {
             System.out.println(ioe.getMessage());
         }
     }
 
     private static void populateTeams(Connection connection) {
         BufferedReader input;
         String[] tokens;
         String gender;
         String line;
         String sql;
         PreparedStatement statement;
         ResultSet results;
         int eventID = 0; 
         int teamID = 1;
 
         try {
             input = new BufferedReader(new FileReader("teams.csv"));			
             line = input.readLine();
             line = input.readLine();
 
             while (line != null) {
                 tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                 
                 try {
                     connection.setAutoCommit(true);
 
                     if (tokens.length > 15 && tokens[1].equals("True")) {
 
                         if (tokens[12].length() > 0) {
                             if (tokens[3].equals("M")) {
                                 gender = "Male";
                             } else if (tokens[3].equals("W")) {
                                 gender = "Female";
                             } else {
                                 gender = "Mixed";
                             }
                             
                             sql = "Select eventID from Events where sport like ? and event like ?;";
 
                             statement = connection.prepareStatement(sql);
                             statement.setString(1, tokens[7]);
                             statement.setString(2, tokens[9]);
 
                             results = statement.executeQuery();
 
                             while (results.next()) {
                                 eventID = results.getInt("eventID");
                             }
 
                             if (eventID != 0) {
                                 sql = "insert into Teams(teamID, teamCode, countryCode, eventID, gender)" + 
                                 "values("+ teamID +",'"+ tokens[0] +"','"+ tokens[4] +"',"+ eventID +",'"+ gender +"');";
         
                                 statement = connection.prepareStatement(sql);
         
                                 statement.executeUpdate();
 
                                 // We also need to populate the relational table between athletes and teams,
                                 // and coaches and teams if the team has coaches
                                 populateBelongTo(connection, teamID, tokens[12]);
 
                                 if (tokens[14].length() > 0) {
                                     populateTrain(connection, teamID, tokens[14]);
                                 }
 
                                 teamID++;
                             }
                         }
                     }
                     eventID = 0;
                     connection.setAutoCommit(true);
  
                 } catch (SQLException e) {
                     System.out.println("Transaction failed");
                     try{
                         connection.rollback();
                     }
                     catch(SQLException e2) {
                         System.out.println("Failed to rollback");
                     }
                 }
                 
                 line = input.readLine();
             }	
                 
             input.close();
             
         } catch (IOException ioe) {
             System.out.println(ioe.getMessage());
         }
     }
 
     private static void populateMedals(Connection connection) {
         BufferedReader input;
         String[] tokens;
         String line;
         String sql;
         ResultSet results;
         PreparedStatement statement;
         int medalID = 1;
         int eventID;
         
         try {
             input = new BufferedReader(new FileReader("medals.csv"));			
             line = input.readLine();
             line = input.readLine();
 
             while (line != null) {
                 tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                 
                 try {
                     if (tokens.length > 10) {
                         tokens[5] = format(tokens[5]);
                         tokens[6] = format(tokens[6]);
 
                         sql = "Select eventID from Events where sport like ? and event like ?;";
                         
 
                         statement = connection.prepareStatement(sql);
                         statement.setString(1, tokens[5]);
                         statement.setString(2, tokens[6]);
 
                         results = statement.executeQuery();
 
                         while (results.next()) {
                             eventID = results.getInt("eventID");
 
                             sql = "insert into Medals(medalID, eventID, medalType) values("+ medalID +","+ eventID +",'"+ tokens[0] +"');";
 
                             statement = connection.prepareStatement(sql);
 
                             statement.executeUpdate();
 
                             // We need to populate the the table that gives allows us to know who won each medal
                             populateWin(connection, medalID, tokens[8]);
 
                             medalID++;
                         }
                     }
                 } catch(SQLException e) {
                     e.printStackTrace();
                 }
                 
                 line = input.readLine();
             }	
                 
             input.close();
             
         } catch (IOException ioe) {
             System.out.println(ioe.getMessage());
         }
     }
 
     private static void populateParticipate(Connection connection, int athleteID, String sports, String events) {
         String[] tokSport = sports.split(",");
         String[] tokEvent;
         String temp;
         int eventID;
 
         if (events.charAt(0) == '\"') {
             events = events.substring(1, events.length()-1);
         } 
 
         tokEvent = events.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
 
         for (int i = 0; i < tokSport.length; i++) {
             tokSport[i] = format(tokSport[i]);
         }
 
         for (int i = 0; i < tokEvent.length; i++) {
             tokEvent[i] = format(tokEvent[i]);
         }
 
         //Used to deal with the only two events that have commas
         for (int i = 0; i < tokEvent.length - 1; i++) {
             temp = tokEvent[i] + tokEvent[i+1];
 
             if (i < tokEvent.length - 1 && temp.equals("Women's 10000m")) {
                 tokEvent[i] = "Women's 10,000m";
 
             } else if (i < tokEvent.length - 1 && temp.equals("Men's 10000m")) {
                 tokEvent[i] = "Men's 10,000m";
             }
         }
 
         try {
             connection.setAutoCommit(false);
  
             PreparedStatement statement;
             ResultSet results;
             String sql;
 
             for (int i = 0; i < tokSport.length; i++) {
                 for (int k = 0; k < tokEvent.length; k++) {
                     sql = "Select eventID from Events where sport like ? and event like ?;";
 
                     statement = connection.prepareStatement(sql);
                     statement.setString(1, tokSport[i]);
                     statement.setString(2, tokEvent[k]);
 
                     results = statement.executeQuery();
 
                     while (results.next()) {
                         eventID = results.getInt("eventID");
 
                         sql = "insert into Participate(eventID, athleteID) values("+ eventID +","+ athleteID +");";
 
                         statement = connection.prepareStatement(sql);
 
                         statement.executeUpdate();
                     }
                 }
             }
          
             connection.setAutoCommit(true);
  
         } catch (SQLException e) {
             System.out.println("Transaction failed");
             try{
                 connection.rollback();
             }
             catch(SQLException e2) {
                 System.out.println("Failed to rollback");
             }
         }
     }
 
     private static void populateTrain(Connection connection, int teamID, String coaches) {
         String[] tokCoaches = coaches.split(",");
         int coachID = 0;
 
         for (int i = 0; i < tokCoaches.length; i++) {
             tokCoaches[i] = format(tokCoaches[i]);
         }
 
         try {
             PreparedStatement statement;
             String sql;
 
             for (int i = 0; i < tokCoaches.length; i++) {
                 coachID = Integer.parseInt(tokCoaches[i]);
                 
                 sql = "insert into Train(teamID, coachID) values("+ teamID +","+ coachID +");";
 
                 statement = connection.prepareStatement(sql);
 
                 statement.executeUpdate();
             }
         } catch (SQLException e) {
             //Some coaches listed on teams were not in the coaches dataset and 
             //there is not eneough info to add them
         }
     }
 
     private static void populateBelongTo(Connection connection, int teamID, String athletes) {
         String[] tokAthlete = athletes.split(",");
         int athleteID = 0;
 
         for (int i = 0; i < tokAthlete.length; i++) {
             tokAthlete[i] = format(tokAthlete[i]);
         }
 
         try { 
             PreparedStatement statement;
             String sql;
 
             for (int i = 0; i < tokAthlete.length; i++) {
                 athleteID = Integer.parseInt(tokAthlete[i]);
                     
                 sql = "insert into BelongTo(teamID, athleteID) values("+ teamID +","+ athleteID +");";
 
                 statement = connection.prepareStatement(sql);
 
                 statement.executeUpdate();
             }
         } catch (SQLException e) {
             //Very few athletes listed on teams were not in the athletes dataset and 
             //there is not eneough info to add them
         }
     }
 
     private static void populateWin(Connection connection, int medalID, String winnerID) {
         PreparedStatement statement;
         ResultSet results;
         String sql;
         int id;
 
         // Medals can be one by athletes or teams, if it is a team we need
         // to get the id's of those athletes before inserting into the table
         if (Character.isDigit(winnerID.charAt(0))) {
             try {
                 id = Integer.parseInt(winnerID);
                 sql = "insert into Win(medalID, athleteID) values("+ medalID +","+ id +");";
 
                     statement = connection.prepareStatement(sql);
 
                     statement.executeUpdate();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
 
         } else {
             try {
                 sql = "select athleteID from Teams left join BelongTo " +
                 "on Teams.teamID = BelongTo.teamID " +
                 "where Teams.teamCode like '"+ winnerID +"'";
                 
                 statement = connection.prepareStatement(sql);
                 results = statement.executeQuery();
 
                 while (results.next()) {
                     id = results.getInt("athleteID");
 
                     sql = "insert into Win(medalID, athleteID) values("+ medalID +","+ id +");";
 
                     statement = connection.prepareStatement(sql);
 
                     statement.executeUpdate();
                 }
                 
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     }
 
 
     // Used to add gdps for some of the larger countries that were missed in 
     // the gdp dataset, these numbers come from the same data but due to format 
     // differences not all countries got matched up with their GDPs
     private static void updateGDP(Connection connection) {
         try {
             String sql = "update Country set gdp = 3070667732359 where countryCode like 'GBR'; " +
             
             "update Country set gdp = 388544468140 where countryCode like 'IRI'; " +
             
             "update Country set gdp = 10930644915 where countryCode like 'KGZ'; " +
 
             "update Country set gdp = 11159274040 where countryCode like 'SYR'; " +
 
             "update Country set gdp = 905987824096 where countryCode like 'TUR'; " +
 
             "update Country set gdp = 359838598806 where countryCode like 'HKG'; " +
 
             "update Country set gdp = 791610000000 where countryCode like 'TPE'; " +
 
             "update Country set gdp = 14420947884 where countryCode like 'MDA'; " +
 
             "update Country set gdp = 2065027556 where countryCode like 'LCA'; " +
 
             "update Country set gdp = 115468803972 where countryCode like 'SVK'; " +
 
             "update Country set gdp = 11159274040 where countryCode like 'SYR'; " +
 
             "update Country set gdp = 4204000000 where countryCode like 'ISV'; " +
 
             "update Country set gdp = 961563259 where countryCode like 'SKN'; " +
 
             "update Country set gdp = 546680341 where countryCode like 'STP'; " +
 
             "update Country set gdp = 507534921715 where countryCode like 'UAE'; " +
 
             "update Country set gdp = 29600000000 where countryCode like 'PRK'; " +
 
             "update Country set gdp = 948558503 where countryCode like 'VIN'; " +
 
             "update Country set gdp = 24527507288 where countryCode like 'BIH'; " +
 
             "update Country set gdp = 2382618615 where countryCode like 'CAF'; " +
 
             "update Country set gdp = 58065953573 where countryCode like 'COD'; ";
 
             PreparedStatement statement = connection.prepareStatement(sql);
             statement.execute();
 
         } catch(SQLException e) {
             e.printStackTrace();
         }
     }
 
 
     // Function used to remove unwanted chars from the start and end of our input
     private static String format(String input) {
         String n = input;
 
         while (n.length() > 0 && !Character.isLetter(n.charAt(0)) &&
         !Character.isDigit(n.charAt(0))) {
             n = n.substring(1);
         }
 
         while (n.length() > 0 && !Character.isLetter(n.charAt(n.length()-1)) &&
         !Character.isDigit(n.charAt(n.length()-1))){
             n = n.substring(0,n.length()-1);
         }
         
         return n;
     }
 }