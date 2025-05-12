import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.ArrayList;


public class OlympicsProject {

    

    public static void main(String args[]){

        Scanner scanner = new Scanner(System.in);
        int choice;
        Database olympicsDatabase = new Database();
        String input;
        while (true) {
            displayMenu();
            try {
                System.out.print("Select an option (1-22): ");
                input = scanner.nextLine();
                input = input.trim();
                choice = Integer.parseInt(input);  // Getting user input

                if (choice == 22) {
                    System.out.println("\033[31mExiting the menu. Goodbye!\033[0m");
                    break;  // Exit the loop
                } else if (choice == 21)
                {
                    helpMenu();  
                } else if (choice >= 1 && choice <= 20) {
                    executeQuery(choice,olympicsDatabase);  // Execute the corresponding query
                } else {
                    System.out.println("\033[33mInvalid option. Please select a number between 1 and 22.\033[0m");
                }
            } catch (NumberFormatException e) {
                System.out.println("\033[31mInvalid input. Please enter a number.\033[0m");
            }
        }

        scanner.close();  // Close the scanner
     //example use case
     //iterate through the resultSet
     //see the BestSportForCountry method while Loop.

}

public static void displayMenu() {
    System.out.println("\n      ⚽🏀🏅************ OLYMPIC ANALYTICS MENU ************🏅🏀⚽");

    System.out.println();

    System.out.println("\033[38;5;21m1. List all countries\033[0m");  
    System.out.println("\033[38;5;27m2. List all athletes and their details representing a given country\033[0m");
    System.out.println("\033[38;5;33m3. List all sports\033[0m");
    System.out.println("\033[38;5;39m4. List all coaches\033[0m");
    System.out.println("\033[38;5;45m5. Count coaches by country\033[0m");
    System.out.println("\033[38;5;51m6. Order countries by total medals won and show GDP\033[0m");
    System.out.println("\033[38;5;50m7. List top ten athletes by medal count\033[0m");
    System.out.println("\033[38;5;49m8. Medal count by country for a specific sport\033[0m");
    System.out.println("\033[38;5;48m9. Count athletes by country\033[0m");
    System.out.println("\033[38;5;47m10. Preferred sport for a country\033[0m");
    System.out.println("\033[38;5;46m11. List team IDs and their associated sports\033[0m");
    System.out.println("\033[38;5;82m12. Get team info for a specific team ID\033[0m");
    System.out.println("\033[38;5;118m13. Average age of athletes and medallists for a specific sport\033[0m");
    System.out.println("\033[38;5;190m14. Get event IDs for a specific sport\033[0m"); 
    System.out.println("\033[38;5;191m15. Get podium results for a specific event ID\033[0m");
    System.out.println("\033[38;5;192m16. Get medallists for a specific sport\033[0m");
    System.out.println("\033[38;5;193m17. Best performing sport for a specific country\033[0m");
    System.out.println("\033[38;5;194m18. List countries that participated in all sports\033[0m");

    System.out.println("\033[38;5;195m19. Populate Database\033[0m");
    System.out.println("\033[38;5;195m20. Depopulate Database\033[0m");
    System.out.println("\033[38;5;195m21. Help Menu\033[0m");
    System.out.println("\033[38;5;196m22. Exit\033[0m");
    System.out.println();
}



public static void helpMenu() {
    System.out.println("\n--- Help Menu ---");
    System.out.println("Choose an option by entering the corresponding number.");
    System.out.println("For queries requiring input, you will be prompted for the specific value (e.g., sport name, country, or team ID).");
    System.out.println("Results are based on predefined SQL queries for Olympic data analysis.");
}

public static boolean isValidInteger(String input) {
    try {
        Integer.parseInt(input);  
        return true;               
    } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number!");
        return false;            
    }
}



public static void executeQuery(int option,Database olympicsDatabase) {
    
    Scanner scanner = new Scanner(System.in);

    switch (option) {
        case 1:
            System.out.println("\033[32mListing all countries\033[0m");
            olympicsDatabase.getAllCountries();
            break;

        case 2:
            System.out.print("Enter the country name:");
            String AthleteCountry = scanner.nextLine();
            AthleteCountry = AthleteCountry.trim();
            
            System.out.println("\033[32mListing all athletes who represent " + AthleteCountry +" "+getOlympicFlag(AthleteCountry)+"\033[0m");
            olympicsDatabase.getAllAthletesInfo(AthleteCountry);
            break;

        case 3:
            System.out.println("\033[32mListing all sports\033[0m");
            olympicsDatabase.getAllSports();
            break;

        case 4:
            System.out.println("\033[32mListing all coaches\033[0m");
            olympicsDatabase.getAllCoaches();
            break;

        case 5:
            System.out.println("\033[32mCount coaches by country\033[0m");
            olympicsDatabase.coachesFromCountries();
            break;

        case 6:
            System.out.println("\033[32mOrdering countries by GDP and total medals won\033[0m");
            olympicsDatabase.countryMedalCountByGDP();
            break;

        case 7:
            System.out.println("\033[32mListing top ten athletes by medal count\033[0m");
            olympicsDatabase.TopTenAthletesByMedal();
            break;

        case 8:
            System.out.print("Enter the sport name: ");
            String sport = scanner.nextLine();
            sport = sport.trim();

            System.out.println("\033[32mQuerying medal count by country for the sport: " + sport + "\033[0m");
            olympicsDatabase.countryMedalsBySport(sport);
            break;

        case 9:
            System.out.println("\033[32mCounting athletes by country\033[0m");
            olympicsDatabase.countAthletesForCountries();
            break;

        case 10:
            System.out.print("Enter the country name: ");
            String country = scanner.nextLine();
            country = country.trim();

            System.out.println("\033[32mQuerying preferred sport for the country: " + country +" "+getOlympicFlag(country)+ "\033[0m");
            olympicsDatabase.countryPrefferedSport(country);
            break;

        case 11:
            System.out.println("\033[32mListing team IDs and their associated sports\033[0m");
            olympicsDatabase.getTeamIDwithSport();
            break;

        case 12:
            System.out.print("Enter the team ID: ");
            String teamID = scanner.nextLine();
            teamID = teamID.trim();
            if(isValidInteger(teamID))
            {
                int id = Integer.parseInt(teamID);
                System.out.println("\033[32mGetting team info for team ID: " + teamID + "\033[0m");
                olympicsDatabase.getTeamInfo(id);
            }

            break;

        case 13:
            System.out.print("Enter the sport name: ");
            String sportAvgAge = scanner.nextLine();
            sportAvgAge = sportAvgAge.trim();
            System.out.println("\033[32mQuerying average age of athletes and medallists in the sport: " + sportAvgAge + "\033[0m");
            olympicsDatabase.avgAgeOfAthletesAndMedallists(sportAvgAge);
            break;

        case 14:
            System.out.print("Enter the sport name: ");
            String sportForEvents = scanner.nextLine();
            sportForEvents = sportForEvents.trim();

            System.out.println("\033[32mGetting events for the sport: " + sportForEvents + "\033[0m");
            olympicsDatabase.getEventsIDs(sportForEvents);
            break;

        case 15:
            System.out.print("Enter the event ID: ");
            String eventID = scanner.nextLine();
            eventID = eventID.trim();
            if(isValidInteger(eventID))
            {
                int id = Integer.parseInt(eventID);
                System.out.println("\033[32mGetting podium for event ID: " + eventID + "\033[0m");
                olympicsDatabase.getPodium(id);
            }
            break;

        case 16:
            System.out.print("Enter the sport name: ");
            String sportForMedals = scanner.nextLine();
            sportForMedals = sportForMedals.trim();
            System.out.println("\033[32mGetting medallists for the sport: " + sportForMedals + "\033[0m");
            olympicsDatabase.getMedallists(sportForMedals);
            break;

        case 17:
            System.out.print("Enter the country name: ");
            String countryForBestSport = scanner.nextLine();
            countryForBestSport = countryForBestSport.trim();
            System.out.println("\033[32mQuerying best performing sport for the country: " + countryForBestSport + getOlympicFlag(countryForBestSport)+  "\033[0m");

            olympicsDatabase.BestSportForCountry(countryForBestSport);
            break;

        case 18:
            System.out.println("\033[32mListing countries that participated in all sports\033[0m");
            olympicsDatabase.getAllSportCountries();
            break;

        case 19:
            System.out.println("\033[32mPopulating Database\033[0m");
            olympicsDatabase.populateDatabase();
            break;

        case 20:
            System.out.println("\033[32mDepopulating Database\033[0m");
            olympicsDatabase.depopulateDatabase();
            break;

       
    }
}


public static String getOlympicFlag(String country) {
    Map<String, String> countryFlags = new HashMap<>();

    countryFlags.put("united states", "\uD83C\uDDFA\uD83C\uDDF8");  
    countryFlags.put("united kingdom", "\uD83C\uDDEC\uD83C\uDDE7");  
    countryFlags.put("japan", "\uD83C\uDDEF\uD83C\uDDF5");
    countryFlags.put("germany", "\uD83C\uDDE9\uD83C\uDDEA"); 
    countryFlags.put("france", "\uD83C\uDDE8\uD83C\uDDF7"); 
    countryFlags.put("canada", "\uD83C\uDFF3\uD83C\uDDE8"); 
    countryFlags.put("china", "\uD83C\uDDE8\uD83C\uDDF3"); 
    countryFlags.put("brazil", "\uD83C\uDDE7\uD83C\uDDF7"); 
    countryFlags.put("india", "\uD83C\uDDEE\uD83C\uDDF3");
    countryFlags.put("spain", "\uD83C\uDDEA\uD83C\uDDF8");  
    countryFlags.put("italy", "\uD83C\uDDEE\uD83C\uDDF9"); 
    countryFlags.put("australia", "\uD83C\uDDE6\uD83C\uDDFA"); 
    countryFlags.put("south korea", "\uD83C\uDDF0\uD83C\uDDFC"); 
    countryFlags.put("mexico", "\uD83C\uDDF2\uD83C\uDDFD"); 
    countryFlags.put("russia", "\uD83C\uDDF7\uD83C\uDDFA"); 
    countryFlags.put("netherlands", "\uD83C\uDDF3\uD83C\uDDF1"); 
    countryFlags.put("argentina", "\uD83C\uDDE6\uD83C\uDDF7"); 
    countryFlags.put("sweden", "\uD83C\uDDF8\uD83C\uDDEA");
    countryFlags.put("switzerland", "\uD83C\uDDED\uD83C\uDDF8"); 
    countryFlags.put("norway", "\uD83C\uDDF3\uD83C\uDDF4");  
    countryFlags.put("poland", "\uD83C\uDDF5\uD83C\uDDF1"); 
    countryFlags.put("belgium", "\uD83C\uDDE7\uD83C\uDDEA"); 
    countryFlags.put("new zealand", "\uD83C\uDDF3\uD83C\uDDFF"); 
    countryFlags.put("denmark", "\uD83C\uDDE9\uD83C\uDDF0"); 
    countryFlags.put("finland", "\uD83C\uDDEB\uD83C\uDDEE");
    countryFlags.put("hungary", "\uD83C\uDDED\uD83C\uDDF7");
    countryFlags.put("south africa", "\uD83C\uDDF8\uD83C\uDDE6");
    countryFlags.put("saudi arabia", "\uD83C\uDDF8\uD83C\uDDE6");
    countryFlags.put("india", "\uD83C\uDDEE\uD83C\uDDF3");

    country = country.trim().toLowerCase();
    
    return countryFlags.getOrDefault(country, "");
}


  

}

class Database {
    private Connection connection;
    private Boolean Populated;
    

    public Database(){
        this.Populated = false;
        Properties prop = new Properties();
        String fileName = "auth.cfg";

        try 
        {
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } 
        catch (FileNotFoundException ex) 
        {
            System.out.println("Could not find config file.");
            System.exit(1);
        } 
        catch (IOException ex) 
        {
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
        
        try {
            connection = DriverManager.getConnection(connectionUrl);
        }catch (SQLException e) {
			e.printStackTrace(System.out);
		}

        this.checkIfPopulated();
    }

    public void printTable(ResultSet resultSet, String... columnNames) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println();

        String section="--------------------------------";
        String divider="+";

        if(resultSet.next())
        {
            for (int i = 0; i < columnCount; i++) {
                divider=divider+section+"+";
            }
    
            System.out.println(divider);
    
    
            for (String columnName : columnNames) 
            {
                System.out.printf("| %-30s ", columnName);
            }
    
            System.out.println("|");
            System.out.println(divider);

            for (int i = 1; i <= columnCount; i++) {
                String value = resultSet.getString(i);
                System.out.printf("| %-30s ", value != null ? value : "");
            }
            System.out.println("|");
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    System.out.printf("| %-30s ", value != null ? value : "");
                }
                System.out.println("|");
            }
    
            System.out.println(divider);
        }
        else
        {
            System.out.println("***Invalid Input***");
        }
    }

    private void checkIfPopulated()
    {
        try
        {
            String sql = "SELECT count(*) as numTables from INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            int numTables = resultSet.getInt("numTables");
            if(numTables == 10)
            {
                System.out.println("The database is populated!");
                this.Populated = true;
            }
            else
            {
                System.out.println("***The database is not populated***");
                System.out.println("Please populate the database!");
                this.Populated = false;
            }

        }catch(SQLException e)
        {
            e.printStackTrace(System.out);
        }
    }

   
    public ResultSet getAllCountries()
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{
                String sql = "Select name from Country";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
    
                printTable(resultSet, "Country Name");
                return resultSet;
    
            }catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }
        }
        else
        {
                
            System.out.println("***ERROR! Please populate the database***");
        }

        return null;
    }
    
    public void getAllAthletesInfo(String country)
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;

            try{
                String sql = "SELECT Athlete.name, Athlete.gender, (YEAR(GETDATE()) - (birthYear)) AS age FROM Athlete JOIN "+
                "Country on Athlete.countryCode = Country.countryCode where Country.name = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,country);
                resultSet = statement.executeQuery();
    
                printTable(resultSet,"Name","Gender","Age");
    
            }
            
            catch(SQLException e){
                e.printStackTrace(System.out);
            }
    
        }
        else
        {
                
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    public ResultSet getAllSports()
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{
                String sql = "SELECT Distinct sport from Events";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
    
                printTable(resultSet,"Sport");
                return resultSet;
    
            }catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        else
        {
                
            System.out.println("***ERROR! Please populate the database***");
        }

        return null;
       
    }

    //returns name, position
    
    public void getAllCoaches()
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{
                String sql = "SELECT name,gender,position from Coach";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);

                printTable(resultSet,"Name","Gender","Position");

            }catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }
        }
        else
        {
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    //returns countryName, and the number of coaches that country has
    public void coachesFromCountries()
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{
                String sql = "Select Country.name as countryName,count(coachID) as coaches from Country "+
                "join Coach on Country.countryCode = Coach.countryCode GROUP BY Country.countryCode, Country.name ORDER BY coaches DESC";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
    
                printTable(resultSet,"Country","Coach Name");
    
    
            }catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }
        }
        else
        {   
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    //returns Country name, gdp, medalCount 
    public void countryMedalCountByGDP()
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;

            try{
                String sql = "SELECT name,gdp,medalCount from Country Order by medalCount DESC";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
    
                printTable(resultSet,"Name","GDP","Medal Count");
    
    
            }catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        else
        {   
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    //returns name,medalsWon for each athlete 
    public void TopTenAthletesByMedal()
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{
                String sql = "SELECT TOP 10 Athlete.name AS name, COUNT(Win.medalID) as medalsWon " +
                 "FROM Athlete " +
                 "LEFT JOIN Win ON Athlete.athleteID = Win.athleteID " +
                 "GROUP BY Athlete.athleteID, Athlete.name " +
                 "ORDER BY medalsWon DESC";
    
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
    
                printTable(resultSet,"Name","Medals Won");
    
    
            }catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        else
        { 
            System.out.println("***ERROR! Please populate the database***");
        }

    }

    //returns country name,MedalsWon 
    public void countryMedalsBySport(String sport){
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{
                String sql = "SELECT country.name," +
                "COUNT(Distinct Medals.medalID) AS medalsWon " +
                "FROM Country " +
                "JOIN Athlete ON Country.countryCode = Athlete.countryCode " +
                "LEFT JOIN Win ON Athlete.athleteId = Win.athleteId " +
                "LEFT JOIN Medals ON Win.medalId = Medals.medalId " +
                "LEFT JOIN Events ON Medals.eventId = Events.eventId " +
                "WHERE Events.sport = ? " +
                "GROUP BY country.name " +
                "ORDER BY medalsWon DESC";

                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, sport);
                resultSet = statement.executeQuery();
                printTable(resultSet,"Name","Medals Won");
            }catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        else
        {   
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    //returns name and numAthletes
    public void countAthletesForCountries()
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{
                String sql = "SELECT Country.name, " +
                "count(Athlete.athleteID) as numAthletes " +
                "FROM Country " +
                "JOIN Athlete ON Country.countryCode = Athlete.countryCode " +
                "GROUP BY Country.name " +
                "ORDER BY numAthletes DESC";

                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                printTable(resultSet,"Country","Number of Athletes");
            }catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        else
        {   
            System.out.println("***ERROR! Please populate the database***");
        }
    }


    //returns name,sport,numAthletes 
    public void countryPrefferedSport(String country)
    {   
        if(this.Populated)
        {
           
            ResultSet resultSet = null;
            try{
                String sql = "SELECT Country.name, Events.sport, count(Athlete.athleteID) as numAthletes from Country JOIN Athlete on Country.countryCode = Athlete.countryCode "+
                "JOIN participate on Athlete.athleteID = participate.athleteID "+
                "JOIN Events on participate.eventID = Events.eventID where Country.name = ? " +
                "GROUP BY Country.name,Events.sport ORDER BY numAthletes DESC";
                
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,country.trim());
                resultSet = statement.executeQuery();

                printTable(resultSet,"Country","Sport","Number of Athletes");

            }catch(SQLException e){
                e.printStackTrace(System.out);
            }  
        }  
        else
        {    
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    //returns teamID with the sport they play
    //since this query result gets very long, Maybe use range like 1-20 Archery?
    //use "teamID" and "sport" on the result.getString() functions
    public void getTeamIDwithSport()
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;

            try{
                String sql = "SELECT Teams.teamID, Events.sport "+
                                    "FROM Teams " + 
                                    "JOIN Events ON Teams.eventID = Events.eventID " +
                                    "GROUP BY Teams.teamID, Events.sport ORDER BY Teams.teamID ";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
    
                printTable(resultSet,"Team ID","Sport");
    
            }catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }
        }
        else
        {  
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    //returns the roster of a team,athletes and coaches, of the given teamID
    //use "sport","teamCode","position","countryCode","name","gender" when retrieving the values on resultSet.getString()
    public void getTeamInfo(int teamID)
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{

                String sql = " With roster as (SELECT 'Athlete' as position,countryCode,athleteID as personID,name,gender "+
                "FROM Athlete "+
                "UNION SELECT position,countryCode, coachID,name,gender FROM Coach), "+
                "rosterIDs as (SELECT teamID, athleteID as id "+
                "FROM BelongTo "+
                "UNION SELECT teamID, coachID as id "+
                "FROM Train) " +
                "SELECT sport,Teams.teamCode, roster.position, roster.countryCode, roster.name, roster.gender "+
                "FROM Teams LEFT JOIN rosterIDs on Teams.teamID = rosterIDs.teamID "+
                "LEFT JOIN roster on rosterIDs.id = roster.personID "+
                "LEFT JOIN Events on Teams.eventID = Events.eventID "+
                "WHERE Teams.teamID = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1,teamID);
                resultSet = statement.executeQuery();

                printTable(resultSet,"Sport","TeamCode","Position","Country Code","Name","Gender");

            }catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }
        }
        else
        {  
            System.out.println("***ERROR! Please populate the database***");
        }
     
    }

    //average age of all Athletes in that sport and average age of Medallists in that sport
    //use resultSet.getString("sport"),resultSet.getString("avgAge"),resultSet.getString("avgMedallistAge")
    public void  avgAgeOfAthletesAndMedallists(String sport)
    {
        if(this.Populated)
        {  
            
            ResultSet resultSet = null;
            try{
                String sql = "WITH Medallists as (SELECT AVG(YEAR(GETDATE()) - Athlete.birthYear) as avgMedallistAge from Athlete "+
                "JOIN Win on Athlete.athleteID = win.athleteID "+
                "JOIN Medals on Medals.medalID = Win.medalID "+
                "JOIN Events on Events.eventID = Medals.medalID where Events.sport = ?), "+
                "allAthletes as (SELECT AVG(YEAR(GETDATE()) - Athlete.birthYear)  as avgAge from Athlete JOIN participate on Athlete.athleteID = participate.athleteID "+
                "JOIN Events on Events.eventID = participate.eventID where Events.sport = ?), "+
                " EventSport as (SELECT DISTINCT sport from Events where Events.sport = ?) "+
                "Select EventSport.sport as sport, allAthletes.avgAge, Medallists.avgMedallistAge from EventSport,allAthletes,Medallists";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,sport);
                statement.setString(2,sport);
                statement.setString(3,sport);
    
    
                resultSet = statement.executeQuery();

                printTable(resultSet,"Sport","Average Age","Average Medallist Age");


            }catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }
        }
        else
        {    
            System.out.println("***ERROR! Please populate the database***");
        }
    }

   
    //user enters sport, and this method returns the events that issued medals for that specific sport.
    //returns eventID, event, sport. It is used for the getPoduim query that asks the user to input EventID.
    //use "eventID","event","sport" to get the results from the result set.
    //eventID should be displayed because we need it for the next query.
    public void getEventsIDs(String sport)
    {
        if(this.Populated)
        {
            
            ResultSet resultSet = null;
            try{
                String sql = "SELECT Events.eventID, Events.event, Events.sport from Events "+
                "where events.sport = ?";

                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,sport);
                resultSet = statement.executeQuery();

                printTable(resultSet,"Event ID","Event","Sport");

            }catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }    
        }
        else
        { 
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    //gets the poduim given an eventID. 
    //use resultSet.getString("name") 
    public void getPodium(int eventID)
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{
                String sql = "SELECT Athlete.name, Medals.medalType from Events JOIN Medals on Events.eventID = Medals.eventID "+
                "JOIN Win on Medals.medalID = Win.medalID "+
                "JOIN Athlete on Win.athleteID = Athlete.athleteID where Events.eventID = ?";
    
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, eventID);
                resultSet = statement.executeQuery();
    
                printTable(resultSet,"Name","Medal Type");
    
    
            }catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }
        }
        else
        { 
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    //gets all medallists given a sport.
    public void getMedallists(String sport)
    {
        if(this.Populated)
        {
           
            ResultSet resultSet = null;
            try{
                String sql = "SELECT Athlete.name, Medals.medalType from Athlete JOIN Win "+
                "on Athlete.athleteID = Win.athleteID JOIN "+
                "Medals on Medals.medalID = Win.medalID JOIN "+
                "Events on Medals.eventID = Events.eventID where Events.sport = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,sport);
                resultSet = statement.executeQuery();

                printTable(resultSet,"Name","Medal Type");

            }catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        else
        {   
            System.out.println("***ERROR! Please populate the database***");
        }
    }

    //best performing sport for a country.
    //returns the sport and MedalCount ordered by medalCount
    //use resultSet.getString("sport") and resultSet.getString("medalsWon")
    public void BestSportForCountry(String country)
    {
        if(this.Populated)
        {
            
            ResultSet resultSet = null;
        
            try{
                String sql = "SELECT Events.sport, count(Distinct Medals.medalID) as medalsWon from Country JOIN Athlete on Country.countryCode = Athlete.countryCode "+
                "JOIN Win on Athlete.athleteID = Win.athleteID JOIN "+
                "Medals on Medals.medalID = Win.medalID JOIN "+
                "Events on Events.eventID = Medals.eventID where country.name = ? "+
                "GROUP BY Events.sport ORDER By medalsWon DESC";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, country);
                resultSet = statement.executeQuery();


                printTable(resultSet,"Sport","Medals Won","");

            }catch(SQLException e){
                e.printStackTrace(System.out);
            }

        }
        else
        {     
            System.out.println("***ERROR! Please populate the database***");
        }
        
    }

    //returns the countries that participated in all sports
    //use resultSet.getString("name") to get the country names. 
    public void getAllSportCountries()
    {
        if(this.Populated)
        {
            ResultSet resultSet = null;
            try{
                String sql = "WITH sportsCount as (Select count(Distinct sport) as totalSports from Events) "+
                "SELECT Country.name FROM Country JOIN Athlete on Country.countryCode = Athlete.countryCode "+
                "JOIN Participate on Athlete.athleteID = Participate.athleteID "+
                "JOIN Events on Events.eventID = Participate.eventID "+
                "GROUP BY Country.countryCode,Country.name HAVING COUNT(DISTINCT Events.sport) = (SELECT totalSports FROM sportsCount)";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
    
                printTable(resultSet,"Name");
    
            }catch(SQLException e){
                e.printStackTrace(System.out);
            }
        }
        else
        {  
            System.out.println("***ERROR! Please populate the database***");
        }

    }

    public void depopulateDatabase()
    {
        if(this.Populated)
        {
            this.Populated = false;

            String[] sql = {"DROP Table BelongTo","DROP Table Participate","DROP Table Train","DROP Table Win","DROP Table Medals","DROP Table Teams",
                        "DROP Table Events","DROP Table Coach","DROP Table Athlete","DROP Table Country"};
        
            for(int i =0; i < sql.length; i ++)
            {
                Statement statement;
                try{
                    String currSql = sql[i];
                    statement = connection.createStatement();
                    statement.executeUpdate(currSql);
                }catch(SQLException e){
                    e.printStackTrace(System.out);
                }
            }
            
            System.out.println("\033[31mDepopulated the Database!\033[0m");
        }
        else
        {
            System.out.println("The Database is already Depopulated!");
        }
        

    }

    public void populateDatabase()
    {
        if(!this.Populated)
        {
            PopulateTables.populate(connection);
            this.Populated = true;
        }
        else
        {
            System.out.println("The Database is already populated!");
        }
    }
}
