import java.sql.* ;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.sql.* ;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Query{
    Connection con;
    Statement statement;

    Query(){
        try {
            DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
        } catch (Exception cnfe){
            System.out.println("Class not found");
        }

        int sqlCode=0;      // Variable to hold SQLCODE
        String sqlState="00000";  // Variable to hold SQLSTATE
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        try{
            con = DriverManager.getConnection (url,"cs421g45", "[0mP421-McGill-45") ;
            statement = con.createStatement ( ) ;}
        catch(SQLException e){
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }
    }

    public void close(){
        try{
        statement.close();
        con.close();}
        catch(Exception e){
            System.out.println(e);
        }
    }
    public boolean checkGuest(String name, String spc){
        String spec = "";
        if (spc.equals("P")){ spec = "performer";}
        else if (spc.equals("V")){spec = "vendor";}
        else if (spc.equals("S")){spec = "speaker";}
        String query = "Select * from guests where gname = '" + name + "' AND speciality = '"  + spec + "';";
        //String query = "Select * from guests where gname = 'Brian Ross' and speciality = 'vendor'";
        try{java.sql.ResultSet rs = statement.executeQuery ( query );
            while(rs.next()){
                return true;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return  false;
    }

    public boolean createGuest(String name, String spc, String phonenum, String country){
        String spec = "";
        if (spc.equals("P")){ spec = "performer";}
        else if (spc.equals("V")){spec = "vendor";}
        else if (spc.equals("S")){spec = "speaker";}
        String query = "Insert into guests values('"
                + name + "','" + phonenum + "','" + country + "','" + spec + "');";
        //String query = "Select * from guests where gname = 'Brian Ross' and speciality = 'vendor'";
        try{
            statement.executeUpdate( query );
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean checkPanelType(String spc, String name){
        String spec = "";
        String tablename = "";
        if (spc.equals("P")){ spec = "performer"; tablename = "shows";}
        else if (spc.equals("V")){spec = "vendor"; tablename = "market";}
        else if (spc.equals("S")){spec = "speaker"; tablename = "panel";}


        String query = "Select * from "
                + tablename + " where actname = '" + name + "';";
        //String query = "Select * from guests where gname = 'Brian Ross' and speciality = 'vendor'";
        try{java.sql.ResultSet rs = statement.executeQuery ( query );
            while(rs.next()){
                return true;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return  false;
    }

    public boolean checkActivityExists(String name){
        String query = "Select * from activity where actname = '" + name + "';";
        //String query = "Select * from guests where gname = 'Brian Ross' and speciality = 'vendor'";
        try{java.sql.ResultSet rs = statement.executeQuery ( query );
            while(rs.next()){
                return true;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return  false;
    }

    public String newActivity(String spc){
        String spec = "";
        String tablename = "";
        if (spc.equals("P")){ spec = "performer"; tablename = "shows";}
        else if (spc.equals("V")){spec = "vendor"; tablename = "market";}
        else if (spc.equals("S")){spec = "speaker"; tablename = "panel";}

        System.out.println("Please give new name");
        Scanner console = new Scanner(System.in);
        String input = console.nextLine();
        boolean exists = checkActivityExists(input);
        while(exists){
            System.out.println("Activity name taken please give new");
            input = console.nextLine();
            exists = checkActivityExists(input);
        }
        System.out.println("Give num of staff: Must be int");
        String numstaff = console.nextLine();
        System.out.println("Give expected number of attendance: Must be int");
        String expnum = console.nextLine();
        System.out.println("Give duration in form HH:MM");
        String dur = console.nextLine();
        System.out.println("Give agelim if any: Must be int");
        String agelim = console.nextLine();

        String query = "Insert into activity values("
                + expnum + "," + agelim + ",'" + input + "'," + numstaff + ",'" + dur + "');";
        //String query = "Select * from guests where gname = 'Brian Ross' and speciality = 'vendor'";
        String q2 = "Insert into " + tablename+ " values("
                + expnum + "," + agelim + ",'" + input + "'," + numstaff + ",'" + dur + "');";

        try{
            statement.executeUpdate( query );
            statement.executeUpdate( q2 );
            return input;
        }
        catch(Exception e){
            System.out.println("Error in creating new activity");
            System.out.println(e);
            return null;
        }
    }

    public boolean createGA(String gname, String name, String spc){
        String spec = "";
        String tablename = "";
        if (spc.equalsIgnoreCase("P")){ spec = "performer"; tablename = "performers";}
        else if (spc.equalsIgnoreCase("V")){spec = "vendor"; tablename = "vendors";}
        else if (spc.equalsIgnoreCase("S")){spec = "speaker"; tablename = "speakers";}

        String query = "Insert into " + tablename + " values('"
                + gname + "','" + name + "','" + spec + "');";
        //String query = "Select * from guests where gname = 'Brian Ross' and speciality = 'vendor'";
        try{
            statement.executeUpdate( query );
        }
        catch(Exception e){
            System.out.println("Error in GA");
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean exists(String query){

        try{java.sql.ResultSet rs = statement.executeQuery ( query );
            while(rs.next()){
                return true;
            }
        }
        catch(Exception e){
            System.out.println("Exists");
            System.out.println(query);
            System.out.println(e);
        }
        return  false;
    }

    public boolean display(String query){
        try{
            java.sql.ResultSet rs = statement.executeQuery ( query );
            ResultSetMetaData rsmd = rs.getMetaData();
            int cnum = rsmd.getColumnCount();
            while(rs.next()){
                for(int i = 1; i <= cnum; i++){
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
        return  true;

    }

    public boolean alterTable(String query){
        try{
            statement.executeUpdate(query);
        }
        catch(Exception e){
            System.out.println("Alter Table");
            System.out.println(query);
            System.out.println(e);
            return false;
        }
        return  true;

    }

    public boolean schedStaff(String newstart, String newend, int numstaff, int curnumstaff, String roomid, String actname, String date) {
        int count = 0;
        String[] staffarray = new String[numstaff];
        try{
            Statement staffcon = con.createStatement();
            ResultSet staffresults = staffcon.executeQuery("select sid from staff st "
                + "WHERE NOT EXISTS(select * from schedule sc where st.sid = sc.sid AND sdate = '"
                + date + "' AND ((actstart < '" + newend + "') AND (actstart > '"
                + newstart + "')) OR ((actstart < '" + newstart + "') AND (actend > '" + newstart + "')));");

            while (staffresults.next() && count < (numstaff - curnumstaff)) {
                String sid = staffresults.getString(1);
                if (alterTable("insert into schedule values("
                    + sid + ",'" + roomid + "','" + date + "','"
                    + actname + "','" + newstart + "','"
                    + newend + "');")) {
                    staffarray[count] = sid;
                    count++;
                }
            }
            if (count == (numstaff - curnumstaff)) {
                return true;
            }
            else {
                for (int x = 0; x < count; x++) {
                    alterTable("delete from schedule where sdate = '"
                        + date + "' AND actname = '"
                        + actname + "' AND sid = "
                        + staffarray[x] + ";");
                }
            }
        }
        catch(Exception e){
            System.out.println("Error in SchedStaff");
            System.out.println(e);
            return false;
        }
        return false;
    }

    //Query 1
    public void insertGA(){
        String input;
        System.out.println("Give the name of the guest");
        Scanner console = new Scanner(System.in);
        String gname = console.nextLine();
        System.out.println("Give specialty. P = performer, V = vendor, S = speaker");
        String spec = console.nextLine();
        Boolean check = spec.equals("P") || spec.equals("V") || spec.equals("S");
        while(!check){
            System.out.println("Please give speciality Format: P = performer, V = vendor, S = speaker");
            spec = console.nextLine();
            check = spec.equals("P") || spec.equals("V") || spec.equals("S");
        }
        Boolean exists = checkGuest(gname, spec);

        if(!exists){
            System.out.println("Guest does not exist. Would you like to create? Y/N");
            input = console.nextLine();
            while(!(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"))){
                System.out.println("Format: Y or N");
                input = console.nextLine();
            }
            if(input.equalsIgnoreCase("N")){return;}
            System.out.println("Please give a phonenumber or just enter if none");
            String phonenum = console.nextLine();
            System.out.println("Please give a Country or just enter if none");
            String country = console.nextLine();
            check = createGuest(gname, spec, phonenum, country);
            if(!check){return;}
        }

        //
        System.out.println("Give name of activity assigning to guest;");
        String name = console.nextLine();
        check = checkPanelType(spec, name);
        while(!check){
            System.out.println("Either panel doesn't exist or is of wrong type. Give a new name or blank to create new panel");
            name = console.nextLine();
            if(name.equalsIgnoreCase("")){
                name = newActivity(spec);
                if(name == null){
                    System.out.println("Error in creation, try again");
                    check = false;
                }
                else{
                    break;
                }
            }
            else{
                check = checkActivityExists(name);
            }
        }

        //Create input in one of three other tables
        createGA(gname, name, spec);

    }

    //Query 2
    public boolean limitMaxHours(){
        System.out.println("Please give new value for max hours");
        Scanner console = new Scanner(System.in);
        String input;
        int x = 0;
        boolean isint = false;

        while(!isint){
            input = console.nextLine();
            try {
                x = Integer.parseInt(input);
                isint = true;
            }
            catch (Exception e) {
                System.out.println("Please Enter Integer");
                isint = false;
            }
        }
        String query = "Select * from staff where hours > " + x + ";";
        //String query = "Select * from guests where gname = 'Brian Ross' and speciality = 'vendor'";
        try{
            Statement stat2 = con.createStatement();
            java.sql.ResultSet rs = statement.executeQuery ( query );
            while(rs.next()){
                int id = rs.getInt(1);
                query = "update staff set hours = " + x + " where sid = '" + id + "';";
                stat2.executeUpdate(query);
                stat2.close();
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return  false;
    }

    //Query 3
    public boolean alter(){
        display("Select sname from sponsor;");
        Scanner console = new Scanner(System.in);
        System.out.println("Please Give Name of Sponsor");
        String input = console.nextLine();
        while(!exists("select * from sponsor where sname = '" + input + "';")){
            System.out.println("Please Give Name of Sponsor");
            input = console.nextLine();
        }
        String sname = input;

        System.out.println("Do you want to update total contribution (rather than individual)?: Y/N");
        input = console.nextLine();
        while(!(input.equalsIgnoreCase("Y")|| input.equalsIgnoreCase("N"))){
            System.out.println("Do you want to update total contribution (in contrast to individual)?: Y/N");
            input = console.nextLine();
        }

        String newbalance = "0";
        //Total Contribution
        if(input.equalsIgnoreCase("Y")){

            boolean check = false;
            int newbal;
            while(!check){
                System.out.println("Give Integer For New Total Contribution");
                input = console.nextLine();
                try{
                    newbal = Integer.parseInt(input);
                    if(newbal <= 0){
                        System.out.println("Your Contribution Must Be Positive");
                    }
                    else{
                        check = true;
                        newbalance = input;
                    }
                }
                catch(NumberFormatException e){
                    System.out.println("Not an integer");
                    check = false;
                }
            }
            //Check if new balance is less than
            if(exists("select sum(amount) from sponsorships where sname ='" + sname + "' HAVING sum(amount) > " + newbalance + ";")){
                System.out.println("This new balance is lower than individual balances and thus cannot be accepted");
            }
            else{
                if(alterTable("update sponsor set contrib = " + newbalance + " where sname = '" + sname + "';")){return true;}
                return false;
            }

        }

        //Individual Contribution
        else{
            display("Select * from sponsorships where sname = '" + sname +"';");
            System.out.println("Which activity should be selected?");
            input = console.nextLine();
            while(!exists("select from sponsorships where sname ='" + sname + "' AND actname = '" + input + "';")){
                System.out.println("Pls give activity name");
                input = console.nextLine();
            }
            String actname = input;
            System.out.println("Pls give new amount");


            boolean check = false;
            int newbal;
            while(!check){
                System.out.println("Give Integer For New Total Contribution");
                input = console.nextLine();
                try{
                    newbal = Integer.parseInt(input);
                    if(newbal <= 0){
                        System.out.println("Your Contribution Must Be Positive");
                    }
                    else{
                        check = true;
                        newbalance = input;
                    }
                }
                catch(NumberFormatException e){
                    System.out.println("Not an integer");
                    check = false;
                }
            }
            newbalance = input;

            alterTable("update sponsorships set amount = "
                    + newbalance + " where actname = '" + actname + "' AND sname = '" + sname + "';");

            String query = "select sum(amount) from sponsorships where sname = '" + sname + "';";
            try{
                ResultSet rs = statement.executeQuery(query);
                int x = 0;
                while(rs.next()){
                x = rs.getInt(1);}
                query = "select * from sponsor where sname = '" + sname + "' AND contrib < " + x + ";";
                if(exists(query)){
                    alterTable("update sponsor set contrib = " + x + "where sname = '" + sname + "';");
                    System.out.println("Total contribution has also been updated");
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        return true;
    }

    //Query 4
    public boolean scheduleAttendee(){
        Scanner console = new Scanner(System.in);
        String input;
        System.out.println("Please give your username.");
        input = console.nextLine();
        while(!exists("Select * from attendee where username = '" + input + "';")){
            System.out.println("Please enter a valid username");
            input = console.nextLine();
        }
        String username = input;
        boolean cont = true;
        while(cont){
            display("Select * from activity");
            System.out.println("Give the name of an activity you'd like to register for:");
            input = console.nextLine();
            while (!(exists("Select * from activity where actname = '" + input + "';"))){
                System.out.println("Please give a valid activity name.");
                input = console.nextLine();
            }
            String actname = input;
            if(!exists("select * from schedule where actname = '" + actname + "';")){
                System.out.println("Sorry this activity has not been scheduled yet.");}
            else if(display("Select sdate, actstart from schedule where actname = '" + actname + "';")){
                System.out.println("Please select a date for this activity. Please give in the form YYYY-DD-MM");
                input = console.nextLine();
                while(!exists("Select from schedule where actname = '" + actname + "' AND sdate = '" + input + "';")){
                    System.out.println("Please select a date for this activity. Please give in the form YYYY-DD-MM");
                    input = console.nextLine();
                }
                String date = input;
                if(exists("select * from activity, attendee where username = '"
                        + username + "' AND actname = '" + actname + "' AND age < agelim;")){
                    System.out.println("Sorry, the user is too young to take part in this activity");
                }
                else{
                    try {
                        ResultSet rs = statement.executeQuery("Select actstart from schedule where actname = '" +
                                actname + "' AND sdate = '" + date + "';");
                        rs.next();
                        String actstart = rs.getString(1);
                        if(alterTable("INSERT INTO attendance values ('"+ username + "' ,'" + date + "' ,'" + actname + "')")){
                            System.out.println("Register Successful");
                        }
                    }
                    catch(Exception e){
                        System.out.println("Exception getting starttime");
                    }
                }


            }
            else{
                System.out.println("Sorry there was an error and you could not be scheduled.");
            }


            System.out.println("Do you want to register for another activity?");
            input = console.nextLine();
            while (!(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"))) {
                System.out.println("Do you want to register for another activity? Y/N");
                input = console.nextLine();
            }
            if (input.equalsIgnoreCase("y")) {
                cont = true;
            }
            else{
                cont = false;}
        }

        display("Select * from attendance where username = '" + username + "' Order by sdate");
        return true;

    }

    //Query 5
    public boolean scheduleActivity(){

        //Assign value of actname
        System.out.println("Give name of activity to schedule");
        Scanner console = new Scanner(System.in);
        String input = console.nextLine();
        while(!exists("Select * from schedactivity where actname = '" + input + "';")){
            System.out.println("Give name of activity to schedule");
            input = console.nextLine();
        }
        String actname = input;

        //Assign Date
        display("Select sdate from schedactivity where actname = '" + actname + "';");
        System.out.println("Please give a date. Format: YYYY-MM-DD");
        input = console.nextLine();
        while(!exists("Select * from schedactivity where actname = '" + actname + "' AND sdate = '" + input + "';")){
            System.out.println("Please give a date. Format: YYYY-MM-DD");
            input = console.nextLine();
        }
        String date = input;

        String duration;
        int numhours;
        int expnum;
        int numstaff;
        int curnumstaff = 0;

        String roomid;
        String newstart;
        String newend;

        try{

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select * from activity where actname = '" + actname + "';");
            rs.next();

            duration = rs.getString(5);
            numstaff = rs.getInt(4);
            expnum = rs.getInt(1);

            if(exists("select * from schedule where actname = '" + actname +"' and sdate = '" + date +  "';")) {
                rs = st.executeQuery("select count(*) from schedule where actname = '"
                        + actname + "' AND sdate = '" + date + "';");
                if(rs.next()){
                    curnumstaff = rs.getInt(1);
                }
            }
            st.close();
            if(curnumstaff >= numstaff){System.out.println("Enough staff has been scheduled."); return true;}

        }catch(Exception e){
            System.out.println("There was an error in getting activity info.");
            return false;
        }

        try{
            numhours = Integer.parseInt(duration.substring(0,1));
        }catch(Exception e){
            System.out.println("Can't convert");
            return  false;
        }

        //If there is minutes as
        if(!duration.substring(3).equalsIgnoreCase("0") || !duration.substring(4).equalsIgnoreCase("0")){
            numhours += 1;
        }

        if(curnumstaff == 0) {
            try {

                Statement roomsched = con.createStatement();
                ResultSet rs = roomsched.executeQuery("Select roomno from room where capacity >= " + expnum + " order by capacity;");

                while (rs.next()) {
                    roomid = rs.getString(1);

                    for (int i = 9; i <= (22 - numhours); i++) {
                        if(i < 10){newstart = "0" + i + ":00:00";}
                        else {newstart = i + ":00:00";}
                        newend = i + numhours + ":00:00";
                        if (!exists("Select * from schedule where roomno = '" + roomid + "' AND sdate = '"
                                + date + "' AND ((actstart - '"
                                + duration + "' < '" + newstart + "') AND (actstart > '"
                                + newstart + "')) OR ((actstart < '" + newstart + "') AND (actend > '" + newstart + "'));")) {
                            if (schedStaff(newstart, newend, numstaff, curnumstaff, roomid, actname, date)) {
                                roomsched.close();
                                System.out.println("Registration successful");
                                return true;
                            }
                        }
                    }
                }
                System.out.println("Sorry, there is no way to schedule the activity with the required number of staff");
                roomsched.close();
                return false;
            } catch (Exception e) {
                System.out.println("Exception at end");
                return false;
            }
        }
        else{
            try {
                Statement st = con.createStatement();
                ResultSet r = st.executeQuery("select actstart, actend, roomno from schedule where actname = '"
                        + actname + "' AND sdate = '" + date + "';");
                if(r.next()){
                    roomid = r.getString(3);
                    newstart = r.getString(1);
                    newend = r.getString(2);
                }
                else{
                    System.out.println("There is no currentinfo");
                    st.close();
                    return false;
                }
                st.close();
            }catch(Exception e){
                System.out.println("Error getting current info");
                System.out.println(e);
                return false;
            }
            if(schedStaff(newstart, newend, numstaff, curnumstaff, roomid, actname, date)){
                System.out.println("Scheduling was successful");
                return true;
            }
            else{
                System.out.println("Was unable to add more staff to the existing activity.\n Maybe try and adjust required num of staff or reschedule.");
                return false;
            }

        }

    }

    public static void main(String[] args){
        Query q = new Query();
        String input;
        Scanner console = new Scanner(System.in);
        Boolean cont = true;
        while(cont){
            System.out.println("Please select the operation you'd like to perform");
            System.out.println("1: Create New Guest And Activity Or Assign Activity To Existing Guest");
            System.out.println("2: Change Max Hour Limit For Staff");
            System.out.println("3: Alter Total Or Individual Contribution Of Sponsor");
            System.out.println("4: Select Activities To Attend For Guest");
            System.out.println("5: Assign Room And Staff To Scheduled Activity");
            System.out.println("Q: Quit");
            input = console.nextLine();
            if(input.equalsIgnoreCase("1")){q.insertGA();}
            else if(input.equalsIgnoreCase("2")){ q.limitMaxHours();}
            else if(input.equalsIgnoreCase("3")){ q.alter();}
            else if(input.equalsIgnoreCase("4")){ q.scheduleAttendee();}
            else if(input.equalsIgnoreCase("5")){ q.scheduleActivity();}
            else if(input.equalsIgnoreCase("Q")){
                cont = false;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
        q.close();

    }
}
