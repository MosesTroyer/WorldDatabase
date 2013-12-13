/*******************************************
 * Class: UserApp
 * Description: Handles the input from the trans file and passes relevant info
 * to the DBAccess class
 * Project: WorldDataBase
 * Author: Moses Troyer
 * For Dr. Kaminski's 3310 Data and File Structures, WMU
 *******************************************/

package worlddatabase;

import java.io.*;
import java.sql.*;

public class UserApp {

    public static void main(String[] args) throws IOException {
        DeleteFile("log.txt"); //starts clean log
        
        Logger log = new Logger();
        DBAccess db = new DBAccess();
        
        FileReader trans = new FileReader("WorldTrans.txt");
        BufferedReader inFile = new BufferedReader(trans);
        
        String url = "jdbc:mysql://localhost:3306/world";
        String user = "root";
        String password = "";
        
        String line;
        
        log.writeln("Connecting to MySQL...");

        try
        {
            //Create a connection to the database
            Connection conn = DriverManager.getConnection(url, user, password);
            log.writeln("OK, the DB Connection is OPENED\n");
            
            while((line = inFile.readLine()) != null){
                
                if(line.charAt(0) == 'S'){ //select
                    db.RetrieveData(conn, line);
                    log.writeln();
                } else { //everything else 
                    db.ChangeData(conn, line);
                    log.writeln();
                }
                
            } //end while

            conn.close();
        }
        catch (Exception ex)
        {
            log.writeln("\r\nERROR, DB Connection didn't work - no trans done");
            log.writeln(ex.toString());
            log.writeln("ERROR, DB Connection didn't work - no trans done");
        }
        
        log.writeln("EXITING PROGRAM");
            
        log.close();
    } //end main method
    
    //************************PUBLIC METHODS************************//
    
    //from Dr. Kaminski, World Data Project 1
    private static boolean DeleteFile(String fileName) {
        boolean delete = false;
        File f = new File(fileName);
        if (f.exists()) {
            delete = f.delete();
        }
        return delete;
    } 

} //end main class
