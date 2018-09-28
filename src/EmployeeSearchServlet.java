

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class EmployeeSearchServlet
 */
@WebServlet("/EmployeeSearchServlet")
public class EmployeeSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Connection database = dataSource.getConnection();
			PrintWriter out = response.getWriter();
			
			System.out.println("EMPLOYEE LINKED");
						
			String id = request.getParameter("id");
			String starName = request.getParameter("starName");
			String genreName = request.getParameter("genreName");
			//String year = request.getParameter("year");
			//String director = request.getParameter("director");
			//String name = request.getParameter("starName");

			String query = "";
			
			query += "select movies.id, movies.title, movies.year, movies.director, stars.name as starName, genres.name as genreName from movies ";
			query += "join stars_in_movies on movies.id = stars_in_movies.movieId ";
			query += "join stars on stars.id = stars_in_movies.starId ";
			query += "join genres_in_movies on movies.id = genres_in_movies.movieId ";
			query += "join genres on genres_in_movies.genreId = genres.id where ";
			query += "movies.id =? and genres.name=? and stars.name=?";
			
			System.out.println(query);
						
			PreparedStatement prepareStatement = database.prepareStatement(query);
			
			prepareStatement.setString(1, id);
			prepareStatement.setString(2, genreName);
			prepareStatement.setString(3, starName);
			
			ResultSet rs = prepareStatement.executeQuery();	
			
			System.out.println(prepareStatement);

			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of rs
			while (rs.next()) {

				String movieId = rs.getString("id");
				String movieTitle = rs.getString("title");
				String movieYear = rs.getString("year");
				String movieDirector = rs.getString("director");
				genreName = rs.getString("genreName");
				starName = rs.getString("starName");

				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("movieId", movieId);
				jsonObject.addProperty("movieTitle", movieTitle);
				jsonObject.addProperty("movieYear", movieYear);
				jsonObject.addProperty("movieDirector", movieDirector);
				jsonObject.addProperty("genreName", genreName);
				jsonObject.addProperty("starName", starName);
				
				jsonArray.add(jsonObject);
				}
			out.write(jsonArray.toString());
		    // set response status to 200 (OK)
		response.setStatus(200);

		rs.close();
		prepareStatement.close();
		database.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
