import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleStarServlet", urlPatterns = "/single-star")
public class SingleStarServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json"); // Response mime type

		// Retrieve parameter id from url request.
		String name = request.getParameter("starName");

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		try {
			Connection database = dataSource.getConnection();

			String query = "select stars.name, any_value(stars.birthYear) as birthyear, group_concat(movies.title) as starredMovies from stars ";
			query += " join stars_in_movies on stars.id = stars_in_movies.starId";
			query += " join movies on movies.id = stars_in_movies.movieId";
			query += " group by stars.name";
			query += " having stars.name LIKE '" + name + "'";

			Statement statement = database.createStatement();
			
			//ResultSet rs = statement.executeQuery(query);
			ResultSet rs = statement.executeQuery(query);

			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of rs
			while (rs.next()) {

				String starName = rs.getString("name");
				String starDOB = rs.getString("birthYear");
				String starredMovies = rs.getString("starredMovies");

				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("starName", starName);
				jsonObject.addProperty("starDOB", starDOB);
				jsonObject.addProperty("starredMovies", starredMovies);
				
				jsonArray.add(jsonObject);
			}
			
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

			rs.close();
			statement.close();
			database.close();
		} catch (Exception e) {
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
		}
		out.close();

	}

}
