import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 5L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String employee = request.getParameter("employee");
        
        System.out.println(username);
        System.out.println(password);
        
        //String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        
        /*
        Map<String, String[]> map = request.getParameterMap();
        for (String key: map.keySet()) {
            System.out.println(key);
            username = key;
            System.out.println(map.get(key)[0]);
            password = map.get(key)[0];
        }
        */
        
        JsonObject responseJsonObject = new JsonObject();
        /*
        // verify the recaptcha response
		try {      	
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);             
            responseJsonObject.addProperty("gstatus", "success");
            //response.getWriter().write(responseJsonObject.toString());
        }
        catch (Exception e) {
            responseJsonObject.addProperty("gstatus", "fail");
            responseJsonObject.addProperty("gmessage", e.getMessage());
            response.getWriter().write(responseJsonObject.toString());
            return;
        }  
        */
		try {
	     	Connection connection = dataSource.getConnection();
	     	//Statement statement = connection.createStatement();
			//String query = String.format("SELECT * from customers where email='%s'", username);
	     	String query = "";
	     	System.out.println("HELLOOOO" + employee);
	     	System.out.println(username);
	     	//employee = employee.toString();
	     	if(employee != null)
	     	{
	     		query += "SELECT * from employees where email=?";
	     		username = employee;
	     		System.out.println(query);
	     		
	     	}
	     	//else if(!username.equals("null") && !username.isEmpty())
	     	else
	     	{
	     		query += "SELECT * from customers where email=?";
	     		System.out.println(query);
	     	}
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println(resultSet);
	     	System.out.println("Login Servlet:" + username);	     	
	     	//ResultSet resultSet = statement.executeQuery(query);
	     	boolean success = false;
	        if (resultSet.next()) {
	            
	            String encryptedPassword = resultSet.getString("password");
	            System.out.println(encryptedPassword);
	            System.out.println(password);
				success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
				if(success == true) {
					if(employee != null)
					{
						System.out.println("IN EMPLOYEE");
		            	responseJsonObject.addProperty("employee", employee);
		            	request.getSession().setAttribute("employee", employee);
					}
					else
					{
					System.out.println("IN USER");
					request.getSession().setAttribute("user", new User(username));
		            String userId = resultSet.getString("id");
		            request.getSession().setAttribute("userId", userId);
		            System.out.println(userId);
					}
		            //JsonObject responseJsonObject = new JsonObject();
		            responseJsonObject.addProperty("status", "success");
		            responseJsonObject.addProperty("message", "success");	
		            response.getWriter().write(responseJsonObject.toString());
				}
				else {
					 responseJsonObject.addProperty("status", "fail");
			         responseJsonObject.addProperty("message", "invalid password!");	
			         response.getWriter().write(responseJsonObject.toString());
				}
	        } 
	        else {
	            // Login fail
	            //JsonObject responseJsonObject = new JsonObject();
	            responseJsonObject.addProperty("status", "fail");
	            responseJsonObject.addProperty("message", "invalid user information!");
	            System.out.println("all error info:" + responseJsonObject.toString());
	            response.getWriter().write(responseJsonObject.toString());
	        }
	    }
        catch(Exception e) {
			e.printStackTrace();
			return;
		}
    }
}

