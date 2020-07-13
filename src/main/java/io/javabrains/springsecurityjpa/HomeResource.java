package io.javabrains.springsecurityjpa;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@RestController
public class HomeResource {
    //This should not allow multiple queries to prevent an SQL injection
    private static final String SQL_SERVER_URL = "jdbc:mysql://localhost:3306/springsecurity?allowMultiQueries=true";
    private static final String SQL_USER_LOGIN = "root";
    private static final String SQL_PASS_LOGIN = "";

    @GetMapping("/")
    public String home() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return ("<h1>Welcome, " + username + "</h1>");
    }

    //For the catalog
    public String formatHTMLStringFromResultSet(ResultSet Results, String data) throws SQLException{
        while (Results.next()){
            data += "<p><center>ID: " + Results.getInt("id") + "</center><br>"
                    + "<center>Active: " + Results.getBoolean("active") + "</center><br>"
                    + "<center>Campaign Name: " + Results.getString("name") + "</center><br>"
                    +  "<center>Campaign Type: " + Results.getString("type_label") + "</center><br>"
                    + "<center>Number of users who are assigned to this Campaign: " + Results.getInt("number_assigned") + "</center><br>"
                    + "<center>Number of users who have completed this Campaign: " + Results.getInt("number_completed") + "</center><br><br></p>";
        }
        return data;
    }

    @GetMapping("/catalog")
    public String catalog() throws SQLException{
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.campaigns WHERE active = true";
        ResultSet Results = stmt.executeQuery(sqlCommand);
        String data = formatHTMLStringFromResultSet(Results, "");
        Results.close();
        con.close();
        return data;
    }

    @GetMapping("/catalog/food")
    public String catalog_food() throws SQLException{
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.campaigns WHERE type_label = \"food\" AND active = true";
        ResultSet Results = stmt.executeQuery(sqlCommand);
        String data = formatHTMLStringFromResultSet(Results, "");
        Results.close();
        con.close();
        return data;
    }

    @GetMapping("/catalog/tech")
    public String catalog_tech() throws SQLException{
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.campaigns WHERE type_label = \"tech\" AND active = true";
        ResultSet Results = stmt.executeQuery(sqlCommand);
        String data = formatHTMLStringFromResultSet(Results, "");
        Results.close();
        con.close();
        return data;
    }

    @GetMapping("/catalog/goods")
    public String catalog_goods() throws SQLException{
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.campaigns WHERE type_label = \"goods\";";
        ResultSet Results = stmt.executeQuery(sqlCommand);
        String data = formatHTMLStringFromResultSet(Results, "");
        Results.close();
        con.close();
        return data;
    }

    @GetMapping("/user")
    public String user() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return ("<h1>Your username is: " + username + "</h1>");
    }

    @GetMapping("/user/current_projects")
    public String user_current_projects() throws SQLException{

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.user WHERE user_name = \"" + username + "\"";
        ResultSet Results = stmt.executeQuery(sqlCommand);
        int[] campaignIDs = new int[3]; //Because at most, a user can have 3 campaigns taken on at one time
        System.out.println(username + "attempted to view their current projects\n");
        if (Results.next()){
            campaignIDs[0] = Results.getInt("advert_one");
            campaignIDs[1] = Results.getInt("advert_two");
            campaignIDs[2] = Results.getInt("advert_three");
        }
        sqlCommand =  "SELECT * FROM springsecurity.campaigns WHERE id = " + campaignIDs[0] + " OR id = " +
                campaignIDs[1] + " OR id = " + campaignIDs[2] + ";";
        Results = stmt.executeQuery(sqlCommand);
        String data = formatHTMLStringFromResultSet(Results, "");
        Results.close();
        con.close();
        return data;
    }

    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>");
    }

    // Vulnerable to an XSS injection:
    // http://localhost:8080/admin/display_user?userID=%3Cscript%3Ealert(%27XSS%20Success!%27)%3C/script%3E
    @GetMapping("/admin/display_user")
    public String display_user(@RequestParam("userID") String userID) throws IOException {
        try {
            Double.parseDouble(userID);
        } catch(NumberFormatException e){
            return ("<h1>Invalid format for user ID.</h1>");
        }
        return ("<h1>Showing info about user with ID: " + userID + "</h1>");
    }

    @GetMapping("/admin/view_projects")
    public String view_projects() throws SQLException {
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.campaigns WHERE active = true";
        ResultSet Results = stmt.executeQuery(sqlCommand);
        String data = formatHTMLStringFromResultSet(Results, "");
        Results.close();
        con.close();
        return data;
    }

    //Assigns a campaign to a user - this is not for creating a whole new ad campaign
    @PostMapping("/admin/add_campaign")
    public String add_campaign(@RequestParam("userID") String userID, @RequestParam("projectID") String projectID) throws SQLException {
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.user WHERE id = " + userID;
        ResultSet Results = stmt.executeQuery(sqlCommand);
        int[] campaignIDs = new int[3];
        if (Results.next()){
            campaignIDs[0] = Results.getInt("advert_one");
            campaignIDs[1] = Results.getInt("advert_two");
            campaignIDs[2] = Results.getInt("advert_three");
        }
        if (campaignIDs[0] == 0){
            sqlCommand = "UPDATE user SET advert_one = " + projectID + " WHERE id = " + userID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_assigned = number_assigned + 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            return "<h1>Added campaign with projectID " + projectID + " to userID " + userID + " in slot one</h1>";
        }
        else if (campaignIDs[1] == 0){
            sqlCommand = "UPDATE user SET advert_two = " + projectID + " WHERE id = " + userID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_assigned = number_assigned + 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            return "<h1>Added campaign with projectID " + projectID + " to userID " + userID + " in slot two</h1>";
        }
        else if (campaignIDs[2] == 0){
            sqlCommand = "UPDATE user SET advert_three = " + projectID + " WHERE id = " + userID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_assigned = number_assigned + 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            return "<h1>Added campaign with projectID " + projectID + " to userID " + userID + " in slot three</h1>";
        }
        else {
            return "<h1>Unable to add Campaign: You already have three active campaigns.</h1>";
        }
    }

    @PostMapping("/admin/remove_campaign")
    public String remove_campaign(@RequestParam("userID") String userID, @RequestParam("projectID") String projectID) throws SQLException {
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.user WHERE id = " + userID;
        ResultSet Results = stmt.executeQuery(sqlCommand);
        int[] campaignIDs = new int[3];
        if (Results.next()){
            campaignIDs[0] = Results.getInt("advert_one");
            campaignIDs[1] = Results.getInt("advert_two");
            campaignIDs[2] = Results.getInt("advert_three");
        }
        if (campaignIDs[0] == Integer.parseInt(projectID)){
            sqlCommand = "UPDATE user SET advert_one = 0 WHERE id = " + userID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_assigned = number_assigned - 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            //Shift down
            if (campaignIDs[1] != 0 || campaignIDs[2] != 0){
                sqlCommand = "UPDATE user SET advert_one = advert_two WHERE id = " + userID;
                stmt.execute(sqlCommand);
                sqlCommand = "UPDATE user SET advert_two = advert_three WHERE id = " + userID;
                stmt.execute(sqlCommand);
                sqlCommand = "UPDATE user SET advert_three = 0 WHERE id = " + userID;
                stmt.execute(sqlCommand);
            }
            return "<h1>Removed campaign with projectID " + projectID + " to userID " + userID + " in slot one</h1>";
        }
        else if (campaignIDs[1] == Integer.parseInt(projectID)){
            sqlCommand = "UPDATE user SET advert_two = 0 WHERE id = " + userID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_assigned = number_assigned - 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            if (campaignIDs[2] != 0){
                sqlCommand = "UPDATE user SET advert_two = advert_three WHERE id = " + userID;
                stmt.execute(sqlCommand);
                sqlCommand = "UPDATE user SET advert_three = 0 WHERE id = " + userID;
                stmt.execute(sqlCommand);
            }
            return "<h1>Removed campaign with projectID " + projectID + " to userID " + userID + " in slot two</h1>";
        }
        else if (campaignIDs[2] == Integer.parseInt(projectID)){
            sqlCommand = "UPDATE user SET advert_three = 0 WHERE id = " + userID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_assigned = number_assigned - 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            return "<h1>Removed campaign with projectID " + projectID + " to userID " + userID + " in slot three</h1>";
        }
        else {
            return "<h1>Unable to remove Campaign: No Campaign found with that ID. (Or you have no active campaigns.)</h1>";
        }
    }

    @PostMapping("/admin/complete_campaign")
    public String complete_campaign(@RequestParam("userID") String userID, @RequestParam("projectID") String projectID) throws SQLException {
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.user WHERE id = " + userID;
        ResultSet Results = stmt.executeQuery(sqlCommand);
        int[] campaignIDs = new int[3];
        if (Results.next()){
            campaignIDs[0] = Results.getInt("advert_one");
            campaignIDs[1] = Results.getInt("advert_two");
            campaignIDs[2] = Results.getInt("advert_three");
        }
        if (campaignIDs[0] == Integer.parseInt(projectID)){
            sqlCommand = "UPDATE user SET advert_one = 0 WHERE id = " + userID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_assigned = number_assigned - 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_completed = number_completed + 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            //Shift down
            if (campaignIDs[1] != 0 || campaignIDs[2] != 0){
                sqlCommand = "UPDATE user SET advert_one = advert_two WHERE id = " + userID;
                stmt.execute(sqlCommand);
                sqlCommand = "UPDATE user SET advert_two = advert_three WHERE id = " + userID;
                stmt.execute(sqlCommand);
                sqlCommand = "UPDATE user SET advert_three = 0 WHERE id = " + userID;
                stmt.execute(sqlCommand);
            }
            return "<h1>Completed campaign with projectID " + projectID + " to userID " + userID + " in slot one</h1>";
        }
        else if (campaignIDs[1] == Integer.parseInt(projectID)){
            sqlCommand = "UPDATE user SET advert_two = 0 WHERE id = " + userID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_assigned = number_assigned - 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_completed = number_completed + 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            if (campaignIDs[2] != 0){
                sqlCommand = "UPDATE user SET advert_two = advert_three WHERE id = " + userID;
                stmt.execute(sqlCommand);
                sqlCommand = "UPDATE user SET advert_three = 0 WHERE id = " + userID;
                stmt.execute(sqlCommand);
            }
            return "<h1>Completed campaign with projectID " + projectID + " to userID " + userID + " in slot two</h1>";
        }
        else if (campaignIDs[2] == Integer.parseInt(projectID)){
            sqlCommand = "UPDATE user SET advert_three = 0 WHERE id = " + userID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_assigned = number_assigned - 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            sqlCommand = "UPDATE campaigns SET number_completed = number_completed + 1 WHERE id = " + projectID;
            stmt.execute(sqlCommand);
            return "<h1>Completed campaign with projectID " + projectID + " to userID " + userID + " in slot three</h1>";
        }
        else {
            return "<h1>Unable to complete Campaign: No Campaign found with that ID.</h1>";
        }
    }

    @GetMapping("/business")
    public String business() {
        return ("<h1>Welcome Business</h1>");
    }

    //This is for adding in a campaign as an admin
    //This is vulnerable to a SQL injection below
    //http://localhost:8080/admin/new_campaign?name=mytestcamp&type=food%27%29%3B+INSERT+INTO+campaigns+%28%60name%60%2C+%60type_label%60%29+VALUES+%28%27thisisaninjection%27%2C+%27food
    //TODO change this to POST Method
    @GetMapping("/admin/new_campaign")
    public String new_campaign(@RequestParam("name") String name, @RequestParam("type") String type) throws SQLException {
        String campaignToAdd = "";
        try{
            Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
            Statement stmt = con.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            campaignToAdd = "INSERT INTO campaigns (`name`, `type_label`) VALUES ('" + name + "', '" + type + "')";
            stmt.execute(campaignToAdd);
            return ("<h1> Successfully added the " + type + " Campaign: " + name + ".</h1>");
        } catch (Exception e){
            System.out.println("Illegal: " + campaignToAdd);
            return ("<h1>Illegal string: " + campaignToAdd + "</h1>");
        }
    }

    @PostMapping("/business/new_campaign")
    public String new_campaign(@RequestParam("type") String type) throws SQLException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String campaignToAdd = "INSERT INTO campaigns (`name`, `type_label`) VALUES ('" + username + "', '" + type + "')";
        stmt.execute(campaignToAdd);
        return ("<h1> Successfully added the " + type + " Campaign: " + username + ".</h1>");
    }

    @GetMapping("/business/view_completed")
    public String view_completed() throws SQLException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.campaigns WHERE name = \"" + username + "\";";
        ResultSet Results = stmt.executeQuery(sqlCommand);
        int numberCompleted = 0;
        if (Results.next()){
            numberCompleted = Results.getInt("number_completed");
        }
        return ("<h1>A total of " + numberCompleted + " users have completed your campaign.</h1>");
    }

    @GetMapping("/business/view_in_progress")
    public String view_in_progress() throws SQLException{
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Connection con = DriverManager.getConnection(SQL_SERVER_URL, SQL_USER_LOGIN, SQL_PASS_LOGIN);
        Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sqlCommand = "SELECT * FROM springsecurity.campaigns WHERE name = \"" + username + "\";";
        ResultSet Results = stmt.executeQuery(sqlCommand);
        int numberAssigned = 0;
        if (Results.next()){
            numberAssigned = Results.getInt("number_assigned");
        }
        return ("<h1>A total of " + numberAssigned + " users are currently promoting your campaign.</h1>");
    }

}