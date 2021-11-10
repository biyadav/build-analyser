package com.test.persistence.implementations;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.test.persistence.dto.EventDto;
import com.test.persistence.interfaces.PersistenceMgr;
import com.test.utils.ApplicationException;
import com.test.utils.Constants;

public class PersistenceMgrImpl implements PersistenceMgr {

	public static final Logger LOGGER  = Logger.getLogger(PersistenceMgrImpl.class);
	@Override
	public void writeRecords(List<EventDto> events) {
		
		LOGGER.debug(" going to commit  "+events);	
		Connection conn = null;
		Statement stmt = null;
		try {
	        conn = getHSQLConnection();
	        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	                                    ResultSet.CONCUR_UPDATABLE);
	        conn.setAutoCommit(false);
	       
	       /* ResultSet  rs = stmt.executeQuery("SELECT COUNT(*) AS ROW_COUNT FROM BUILD_REPORT ");
	       
	        while(rs.next()) {
	            System.out.println("The count is " + rs.getInt("ROW_COUNT"));
	         }*/
	        
	        
	       
	        for(EventDto event: events){
	        
	        	System.out.println();
		        stmt.addBatch(
		        		new StringBuilder(Constants.BATCH_INSERT_STMT)
		        		.append("'")
		        		.append(event.getId())
		        		.append("'")
		                .append(" , ")
		                .append(event.getDuration())
		                .append(" , ")
		                .append("'")
		                .append(event.getHost())
		                .append("'")
		                .append(" , ")
		                .append("'")
		                .append(event.getType())
		                .append("'")
		                .append(" , ")
		                .append("'")
		                .append(event.getDuration()>4 ? Constants.ALERT_TRUE:Constants.ALERT_FALSE)
		                .append("'")
		                .append(" )")
		                .toString());
	        }
	      
	        int[] updateCounts = stmt.executeBatch();
            LOGGER.debug("updateCounts "+Arrays.toString(updateCounts));
	        conn.commit();
	        org.hsqldb.DatabaseManager.closeDatabases(0);
	    }
	    catch(BatchUpdateException ex) {
	    	LOGGER.error("Error inserting  current chunk into database ", ex);
	        throw new ApplicationException("Batch Insert Failed", ex);
	    }
	    catch(SQLException ex) {
	    	LOGGER.error("Error updating database ", ex);
	    	throw new ApplicationException("Batch Insert Failed", ex);
	    }
		catch(Exception ex) {
	    	LOGGER.error("Error updating database ", ex);
	    	throw new ApplicationException("Batch Insert Failed", ex);
	    }
	    finally {
	        close(conn, stmt);
	        
	    }

	}

	private void close(Connection conn, Statement stmt) {
		try {
			if(null != conn){
			 stmt.close();
			}
			if(null != stmt){
			 conn.close();
			}
			
		} catch (SQLException e) {
			LOGGER.error("closing connection/statement failed ", e);
			throw new ApplicationException("Closing resource Failed", e);
		}
	}
	
	 private static Connection getHSQLConnection() throws Exception {
		    Class.forName("org.hsqldb.jdbcDriver");
		    LOGGER.info("Driver Loaded Successfully.");
		    String url = "jdbc:hsqldb:file:data/demodb";
		    return DriverManager.getConnection(url, "sa", "");
		  }
	 
	 public  void createTable() {
		   
		    Statement stmt = null;
		    Connection con = null;
			try {
				con = getHSQLConnection();
				stmt = con.createStatement();
				stmt.execute(Constants.CREATE_TABLE_STMT);
				con.commit();
			} catch (Exception e) {
				
			   if(e.getMessage().contains("Table already exists")){
				   LOGGER.info("Table alreday Exists not created again ");
			   }
			   else{
				   LOGGER.error("Error creating Table ", e);
				   throw new ApplicationException("Table creation failed ", e);
			   }
			   
			}
			finally{
				close(con,stmt);
			}
		   
		}

}
