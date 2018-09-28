import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ShoppingCart 
{
    protected HashMap<MovieInCart, Integer> items;

    public ShoppingCart(){
        items = new HashMap<MovieInCart, Integer>();
    }

    public HashMap<MovieInCart, Integer> getItems(){
        return items;
    }

    public void addItem(MovieInCart newItem){
        if(items.containsKey(newItem)) {
        	items.put(newItem, items.get(newItem) + 1);
        }
        else
        	items.put(newItem, 1);
    }
    
    public void deMovie(MovieInCart item) {
    	if(items.get(item) != null && items.get(item) > 0)
    		items.put(item, items.get(item) - 1);
    }
    
    public void setNumber(MovieInCart item, Integer n){
        items.put(item, n);
    }
    
    public void removeMovie(MovieInCart item) {
    	System.out.println("item is " + item.toString() + "  " + item.getId());
    	items.put(item,0);
    	System.out.println(items.toString());
    }
    
    public JsonArray display() {
		JsonArray jsonArray = new JsonArray();
		System.out.println("disp" + items.toString());
		for(Map.Entry<MovieInCart, Integer> entry: items.entrySet()) {				
			MovieInCart item = entry.getKey();
			Integer quantity = new Integer(entry.getValue());
			JsonObject jsonObject = new JsonObject();
			System.out.println("disp" + item.getId() + item.getTitle() + quantity);
			jsonObject.addProperty("id", item.getId());
			jsonObject.addProperty("title", item.getTitle());
			jsonObject.addProperty("quantity", quantity);
			jsonArray.add(jsonObject);
		}
		System.out.println(jsonArray);
		return jsonArray;		
    }
}
