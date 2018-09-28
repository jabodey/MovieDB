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

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {
	/*
    console.log("handleResult: populating star info from resultData");
    // populate the star info h3
    //find the empty h3 body by id "star_info"
    let starInfoElement = jQuery("#star_info");

    // append two html <p> created to the h3 body, which will refresh the page
    starInfoElement.append("<p>Star Name: " + resultData[0]["star_name"] + "</p>" +
        "<p>Date Of Birth: " + resultData[0]["star_dob"] + "</p>");
	
    console.log("handleResult: populating movie table from resultData");
    */

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");
    
    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < Math.min(10, resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + "<button type='button' class='btn btn-outline-primary' id = '" + resultData[i]["movieId"] + "' name='" + resultData[i]["movieTitle"] +"'>Add to Cart</button>" + resultData[i]["movieId"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieTitle"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieYear"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieDirector"] + "</th>";
        rowHTML += "<th>";
        var genre = resultData[i]["genreNames"];
        var genreArray = genre.split(',');
        for(var z = 0; z < genreArray.length; z++)
        	{
        	if(z == (genreArray.length-1))
        		{
        		rowHTML += "<a href='movie_list.html?limit=10&offset=0&genre=" + genreArray[z] + "'>" + genreArray[z] + "</a>";
        		break;
        		}	
        	rowHTML += "<a href='movie_list.html?limit=10&offset=0&genre=" + genreArray[z] + "'>" + genreArray[z] + "</a>, ";
        	}
        rowHTML += "</th>";
        rowHTML += "<th>";
        var star = resultData[i]["starNames"];
        var starArray = star.split(',');
        for(var x = 0; x < starArray.length; x++)
        	{
        	if(x == (starArray.length-1))
        		{
        		rowHTML += "<a href='single-star.html?starName=" + starArray[x] + "'>" + starArray[x] + "</a>";
        		break;
        		}	
        	rowHTML += "<a href='single-star.html?starName=" + starArray[x] + "'>" + starArray[x] + "</a>, ";
        	}
        rowHTML += "</th>";
        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
    
    //jQuery("#movie_table_body").on("click", ".btn", addToCart(this.id,this.name));
    $(document).on("click", '.btn', function(event) {
    	addToCart(event, this.id,this.name);
    	});
}

let movieId = getParameterByName('id');
let movie = getParameterByName('movie');

console.log(movieId);

//Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "SingleMovieServlet?id=" +movieId + "&movie=" + movie, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
