/*******************************************
 * Class: DBAccess
 * Description: Handles all interaction with the database. Only has two methods,
 * retrieveData, which handles Selects, and changeData, which handles Inserts,
 * deletes, and updates.
 * Project: WorldDataBase
 * Author: Moses Troyer
 * For Dr. Kaminski's 3310 Data and File Structures, WMU
 *******************************************/

package worlddatabase;

import java.sql.*;
import java.io.*;

public class DBAccess {
    
    private Logger log = new Logger();
    private int numberOfCommands = 0;
    
    //************************CONSTRUCTOR************************//
    
    public DBAccess(){
        
    } //end constructor
    
    //************************PUBLIC METHODS************************//
    
    //for select statements. Retrieves the data needed and prints it out,
    //column by column with a " : " inbetween
    public void RetrieveData(Connection conn, String sql) throws IOException {
        int i;
        String line;
        
        try {
            //create a Statement object
            Statement stmt = conn.createStatement();
            //Send the statement to the DBMS
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            
            log.writeln("SQL (" + (++numberOfCommands) + "): "
                +sql + "\n");

            while (rs.next()) {
                for(i = 1;i<rsmd.getColumnCount() + 1;i++){
                    if((line = rs.getString(i)) != null)
                        log.write(line);
                    else log.write("null");
                            
                    if(i != rsmd.getColumnCount()) log.write(" : ");
                }
                log.writeln(); 
            }
            
            stmt.close();
        } catch (Exception ex) {
            log.writeln("ERROR, QUERY not done");
            log.writeln(ex.toString());
        }
        
    } //end RetrieveData
    
    //for DELETE, INSERT, and UPDATE statements. Updates will go straight into
    //the database without formatting, while Delete and Insert statements will
    //be created by the method.
    public void ChangeData(Connection conn, String sql) throws IOException {
        char c = sql.charAt(0);
        String[] line;
        String command = "";
       
        try {

            //create a Statement object
            Statement stmt = conn.createStatement();

            if(c == 'U'){ //if updating
                line = sql.split(" ");
                stmt.executeUpdate(sql);
                
                log.writeln("SQL (" + (++numberOfCommands) + "): "
                    + sql + "\n");
                
                log.writeln("OK, UPDATE of chosen " + line[1] + 
                        " done"); 
            } else {
                sql = sql.substring(2);
                
                line = sql.split("\\|");
                
                //setting the command to use
                if(c == 'I'){ //insert
                    command += "INSERT INTO " + line[0];
                    //some columns
                    if(line.length > 2) 
                        command += " (" + line[1] + ") VALUES (" +
                            line[2] + ")";    
                    //all columns   
                    else command += " VALUES (" + line[1] + ")"; 
                }
                else command += "DELETE FROM " + line[0] + //delete
                        " WHERE " + line[1] + " = " + line[2];
                
                stmt.executeUpdate(command);
                
                log.writeln("SQL (" + (++numberOfCommands) + "): "
                        + command + "\n");
                
                if(c == 'I') log.writeln("OK, INSERT was done");
                else log.writeln("OK, DELETE was done"); 
            } //end else
            
            stmt.close();
        } catch (Exception ex) {
            log.writeln("ERROR, change of data not done");
            log.writeln(ex.toString());
        }

    } //end ChangeData

} //end DBAccess class
