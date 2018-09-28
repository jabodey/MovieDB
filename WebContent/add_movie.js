/**
 * 
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
/*
function addToCart(event, m_id, m_name) {
    console.log("adding to shopping cart");
    //event.preventDefault();
    jQuery.ajax({
    	dataType: "json",
    	method: "GET",
        url: "AddServlet?movieid=" + m_id + "&movietitle=" + m_name
    });
    jQuery(".addToCart").serialize();
}
*/
/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function printResult() {
	
    let addLink = jQuery("#addMovieLink");
    
    // Concatenate the html tags with resultData jsonObject to create table rows
        let rowHTML = "<a href='single-movie.html?id=" + movieMID + "&movie=" + movieTitle + "'> Movie Link </a>";
        
        addLink.append(rowHTML);
    }
  

let message = getParameterByName('message');
if(message != null)
	{
	alert(message);
	}
/*
let movieTitle = getParameterByName('movieTitle');
let movieYear = getParameterByName('movieYear');
let movieDirector = getParameterByName('movieDirector');
let starName = getParameterByName('starName');
let genreName = getParameterByName('genreName');

console.log(movieId);

//Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "AddMovieServlet?movieTitle=" +movieTitle + "&movieYear=" + movieYear + "&movieDirector=" + movieDirector + "&starName=" + starName + "&genreName=" +genreName, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
*/