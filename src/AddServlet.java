import javax.annotation.Resource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {
    private static final long serialVersionUID = 5L;
    @Resource(name="jdbc/Master")
    private DataSource dataSource;


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	System.out.println("got it!");
        String id = request.getParameter("movieid");
        String title = request.getParameter("movietitle");
        String number = request.getParameter("number");
        
        ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("ShoppingCart");
        if (cart == null){
            cart = new ShoppingCart();
        }
        MovieInCart itemToAdd = new MovieInCart(id,title);        
        if(number != null) {
        	System.out.println("n in the input" + number);
        	cart.setNumber(itemToAdd, Integer.parseInt(number));
        }        
        else {cart.addItem(itemToAdd);}      
        request.getSession().setAttribute("ShoppingCart", cart);
        System.out.println(cart.getItems());        
    }
}

