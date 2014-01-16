package in.live.at.vigneshchennai.syncedDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class NonAcidDBConn {
	public static final Logger log = Logger.getLogger(NonAcidDBConn.class.getName());
	private static final String dsName = "java:/SyncedDB";
	private static DataSource ds;
	private static boolean initSuccess;
	
	private Connection conn;
	
	static {
		try {
			log.info("Initializing DataSource " + dsName);
			InitialContext ic = new InitialContext();
			ds = (DataSource)ic.lookup(dsName);
			log.info("Datasource initialized.");
			log.info("Getting Connection to Datasource.");
			Connection conn = ds.getConnection();
			log.info("Connection acquired.");
			log.info("Creating required table SYNCEDDB if not found.");
			StringBuilder sql = new StringBuilder();
			sql.append("CREATE TABLE IF NOT EXISTS SYNCEDDB (" +
					"ID bigint auto_increment," +
					"version SMALLINT," +
					"store varchar(255)," +
					"action tinyint," +
					"data text)");
			log.info("Executing sql : " + sql);
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.execute();
			log.info("Executed Successfull.");
			log.info("SyncedDB initialized successfull.");
			initSuccess = true;
		} catch (Exception e) {
			initSuccess = false;
			log.log(Level.SEVERE, 
					"Error occured while initializing.", e);
		}
		
	}
	
	public NonAcidDBConn() throws InstantiationException, SQLException {
		if(!initSuccess) {
			String msg ="Cannot initialization connect as there is " +
					"error in datasource creation. Please check the error log";
			log.warning(msg);
			throw new InstantiationException("msg");
		}
		conn = ds.getConnection();
	}
	
	public void closeConnection() throws SQLException {
		if(conn != null && !conn.isClosed()) {
			conn.commit();
			conn.close();
		}
	}
	
	public int executeUpdate(String sql) throws InstantiationException, SQLException {
		if(!initSuccess) {
			String msg ="Cannot execute sql as there is " +
					"error in datasource creation. Please check the error log";
			log.warning(msg);
			throw new InstantiationException("msg");
		}
		PreparedStatement ps = conn.prepareStatement(sql);
		return ps.executeUpdate();
	}
	
	public ResultSet executeQuery(String sql) throws InstantiationException, SQLException {
		if(!initSuccess) {
			String msg ="Cannot execute sql as there is " +
					"error in datasource creation. Please check the error log";
			log.warning(msg);
			throw new InstantiationException("msg");
		}
		PreparedStatement ps = conn.prepareStatement(sql);
		return ps.executeQuery();
	}
	
}
