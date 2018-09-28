

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
 * Servlet implementation class SingleMovieServlet
 */
@WebServlet("/SingleMovieServlet")
public class SingleMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 7L;
	@Resource(name="jdbc/moviedb")
	private DataSource dataSource;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json"); // Response mime type
		
		try {
			Connection database = dataSource.getConnection();
			PrintWriter out = response.getWriter();
			
			System.out.println("CONNECTED");
						
			String id = request.getParameter("id");
			String movie = request.getParameter("movie");
			//String year = request.getParameter("year");
			//String director = request.getParameter("director");
			//String name = request.getParameter("starName");

			String query = "";
			
			if (!id.equals("null") && !id.isEmpty())
			{
			query += "select movies.id, movies.title, movies.year, movies.director, ";
			query += "group_concat(distinct(stars.name)) as starNames, group_concat(distinct(genres.name)) as genreNames, ratings.rating from movies ";
			query += "join stars_in_movies on movies.id = stars_in_movies.movieId ";
			query += "join stars on stars.id = starId ";
			query += "join ratings on movies.id = ratings.movieId ";
			query += "join genres_in_movies on movies.id = genres_in_movies.movieId ";
			query += "join genres on genres_in_movies.genreId = genres.id where ";
			query += "movies.id = '" + id + "'";
			}
			
			else if(!movie.equals("null") && !movie.isEmpty())
			{
			query += "select movies.id, movies.title, movies.year, movies.director, ";
			query += "group_concat(distinct(stars.name)) as starNames, group_concat(distinct(genres.name)) as genreNames, ratings.rating from movies ";
			query += "join stars_in_movies on movies.id = stars_in_movies.movieId ";
			query += "join stars on stars.id = starId ";				
			query += "join ratings on movies.id = ratings.movieId ";
			query += "join genres_in_movies on movies.id = genres_in_movies.movieId ";
			query += "join genres on genres_in_movies.genreId = genres.id where ";
			query += "movies.title = '" + movie + "'";

			}
			System.out.println(query);
						
			Statement statement = database.createStatement();
			
			ResultSet rs = statement.executeQuery(query);	

			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of rs
			while (rs.next()) {

				String movieId = rs.getString("id");
				String movieTitle = rs.getString("title");
				String movieYear = rs.getString("year");
				String movieDirector = rs.getString("director");
				String genreNames = rs.getString("genreNames");
				String starNames = rs.getString("starNames");
				String rating = rs.getString("rating");

				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("movieId", movieId);
				jsonObject.addProperty("movieTitle", movieTitle);
				jsonObject.addProperty("movieYear", movieYear);
				jsonObject.addProperty("movieDirector", movieDirector);
				jsonObject.addProperty("genreNames", genreNames);
				jsonObject.addProperty("starNames", starNames);
				jsonObject.addProperty("rating", rating);
				
				jsonArray.add(jsonObject);
				}
						
			    // write JSON string to output
			out.write(jsonArray.toString());
			    // set response status to 200 (OK)
			response.setStatus(200);

			rs.close();
			statement.close();
			database.close();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}