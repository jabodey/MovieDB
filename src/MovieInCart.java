public class MovieInCart
{
    private String movieId;
    private String movieTitle;
    

    public MovieInCart()
    {
    }

    public MovieInCart(String aProductCode, String aTitle)
    {
        movieId = new String(aProductCode);        
        movieTitle = new String(aTitle);
    }


    @Override
    public int hashCode() {
    	return movieId.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o == this) return true;
        if (!(o instanceof MovieInCart) || o == null) {
            return false;
        }
        MovieInCart o1 = (MovieInCart) o;

        return o1.movieId.equals(movieId) && o1.movieTitle.equals(movieTitle);
    }
    
    public String getId() { return movieId; }

    public String getTitle() { return movieTitle; }

}