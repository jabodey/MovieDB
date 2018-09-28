

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class AddMovieServlet
 */
@WebServlet("/AddMovieServlet")
public class AddMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name="jdbc/Master")
    DataSource dataSource;    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			/*
			 Context initCtx;
			try {
				initCtx = new InitialContext();
	         Context envCtx = (Context) initCtx.lookup("java:comp/env");
	            if (envCtx == null)
	                System.out.println("envCtx is NULL");
	            // Look up our data source
	            dataSource = (DataSource) envCtx.lookup("jdbc/Master");
			} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
			Connection database = dataSource.getConnection();
			PrintWriter out = response.getWriter();
			String movieTitle = request.getParameter("movieTitle");
			String movieYear = request.getParameter("movieYear");
			String movieDirector = request.getParameter("movieDirector");
			String starName = request.getParameter("starName");
			String genreName = request.getParameter("genreName");
			
			String query = "select max(id) from stars";
			String starMID = "";
			Statement statement = database.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next())
			{
				starMID = resultSet.getString("max(id)");
			}
			String[] splitMax = starMID.split("(?<=\\D)(?=\\d)");
			System.out.println(splitMax[0]);
			System.out.println(splitMax[1]);
			int maxInt = Integer.parseInt(splitMax[1]);
			maxInt++;
			splitMax[1] = Integer.toString(maxInt);
			starMID = String.join("", splitMax[0], splitMax[1]);
			
			query = "select max(id) from movies";
			String movieMID = "";
			statement = database.createStatement();
			resultSet = statement.executeQuery(query);
			while(resultSet.next())
			{
				movieMID = resultSet.getString("max(id)");
			}
			splitMax = movieMID.split("(?<=\\D)(?=\\d)");
			System.out.println(splitMax[0]);
			System.out.println(splitMax[1]);
			maxInt = Integer.parseInt(splitMax[1]);
			maxInt++;
			splitMax[1] = Integer.toString(maxInt);
			movieMID = String.join("", splitMax[0], splitMax[1]);
			
			statement.close();
			
			String movieMessage = "";
			String starMessage = "";
			String genreMessage = "";
			System.out.println(starMID + "STARMIDDDDD");
			System.out.println(movieMID + "MOVIEMIDDDD");
			query = "call addMovie(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			CallableStatement  cs = database.prepareCall(query);
			cs.setString(1, movieTitle);
			cs.setString(2, movieYear);
			cs.setString(3, movieDirector);
			cs.setString(4, starName);
			cs.setString(5, genreName);
			cs.setString(6, movieMID);
			cs.setString(7, starMID);
			cs.setString(8, movieMessage);
			cs.setString(9, starMessage);
			cs.setString(10, genreMessage);
			cs.executeQuery();
			starMessage = cs.getString("starMessage");
			System.out.println(starMessage);
			genreMessage = cs.getString("genreMessage");
			System.out.println(genreMessage);
			movieMessage = cs.getString("movieMessage");
			System.out.println(movieMessage);
			if(!movieMessage.equals("Movie already exists, no changes to database made."))
			{
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("movieTitle", movieTitle);
				jsonObject.addProperty("movieYear", movieYear);
				jsonObject.addProperty("movieDirector", movieDirector);
				jsonObject.addProperty("movieMID", movieMID);
				out.write(jsonObject.toString());
				response.sendRedirect("dashboard_movie.html?id="+movieMID+"&starName="+starName+"&genreName="+genreName);
			}
			else
			{
				response.sendRedirect("_dashboard.html?message="+movieMessage);

			}
			
			cs.close();
			resultSet.close();
			database.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
