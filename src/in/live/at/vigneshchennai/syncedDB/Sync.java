package in.live.at.vigneshchennai.syncedDB;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Sync
 */
@WebServlet("/Sync")
public class Sync extends HttpServlet {
	private final static Logger log = Logger.getLogger(Sync.class.getName());
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Sync() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}


	/*
	 * Error Code
	 * 	0 = Success
	 *  1 = Error in posted data
	 *  2 = Error in Server
	 *   
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
			
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SimpleJson sj = new SimpleJson();
		PrintStream out = new PrintStream(response.getOutputStream());
		try {
			response.setContentType("text/json");
			
			
			JSTransObject jsto = new JSTransObject();
			
			
			try {
				jsto.setVersion(Integer.parseInt(request.getParameter("version")));
				jsto.setData(request.getParameter("data"));
				jsto.setStore(request.getParameter("store"));
				jsto.setAction(Integer.parseInt(request.getParameter("action")));
			} catch(NumberFormatException nfe) {
				log.log(Level.SEVERE, "Error in received data.", nfe);
				sj.put("status", 1);
				sj.put("description", nfe.getMessage());
				out.println(sj);
				return;
			}
			
			NonAcidDBConn nadc = new NonAcidDBConn();
			nadc.executeUpdate("insert into SYNCEDDB (version, action, store, data) values (" +
					"\""+jsto.getVersion()+"\"" + "," +
					"\""+jsto.getAction()+"\"" + "," +
					"\""+jsto.getStore()+"\"" + "," +
					"\""+jsto.getData()+"\"" +
					")");
			int lastId = 0;
			ResultSet rs = nadc.executeQuery("SELECT LAST_INSERT_ID()");
			if(rs.next()) {
				lastId = rs.getInt(1);
			}
			if(lastId == 0) {
				sj.put("status", 2);
				sj.put("description", "Unexception error: unable to get the latest Generated ID");
				out.println(sj);
				return;
			}
			sj.put("status", 0);
			sj.put("id", lastId);
			out.println(sj);
			
			nadc.closeConnection();
		} catch (InstantiationException e) {
			log.log(Level.SEVERE, "Error in initialization.", e);
			sj.put("status", 2);
			sj.put("description", e.getMessage());
			out.println(sj);
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Database error occured.", e);
			sj.put("status", 2);
			sj.put("description", e.getMessage());
			out.println(sj);
		}
	}
}
