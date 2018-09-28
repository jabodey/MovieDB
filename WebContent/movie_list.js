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


//if click the add to cart button
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
    for (let i = 0; i < Math.min(parseInt(resultData[0]["limit"]), resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th><button type='button' class='btn btn-outline-primary' id = '" + resultData[i]["movieId"] + "' name='" + resultData[i]["movieTitle"] +"'>Add to Cart</button>" + resultData[i]["movieId"] + "</th>";
        rowHTML += "<th><a href='single-movie.html?id=" + resultData[i]["movieId"]; 
        rowHTML += "'>" + resultData[i]["movieTitle"] + "</a></th>";
        rowHTML += "<th>" + resultData[i]["movieYear"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieDirector"] + "</th>";
        rowHTML += "<th>";
        var genreArray = resultData[i]["genreNames"].split(',');
        for(var z = 0; z < genreArray.length; z++)
        	{
        	if(z == (genreArray.length-1))
        		{
        		rowHTML += genreArray[z];
        		break;
        		}	
        	rowHTML += genreArray[z] + ", ";
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
        let sortByTitle = jQuery("#sorting");
        
        var sortByTitleLink = "Sort by: <a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + offset;
        sortByTitleLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        sortByTitleLink += "&letter=" + letter + "&order=asc_title'>Ascending Title</a>, ";
        sortByTitleLink += "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + offset;
        sortByTitleLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        sortByTitleLink += "&letter=" + letter + "&order=desc_title'>Descending Title</a>, ";
        sortByTitleLink += "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + offset;
        sortByTitleLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        sortByTitleLink += "&letter=" + letter + "&order=asc_rating'>Ascending Rating</a>, ";
        sortByTitleLink += "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + offset;
        sortByTitleLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        sortByTitleLink += "&letter=" + letter + "&order=desc_rating'>Descending Rating</a>";
        sortByTitle.append(sortByTitleLink);
        
        let moviesPerPage = jQuery("#listNum");
        var moviesPerPageLink = "Movies Per Page: <a href='movie_list.html?title=" + title + "&year=" + year + "&limit=10&offset=" + offset;
        moviesPerPageLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        moviesPerPageLink += "&letter=" + letter + "&order=" + order + "'>10</a> ";
        moviesPerPageLink +=  "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=20&offset=" + offset;
        moviesPerPageLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        moviesPerPageLink += "&letter=" + letter + "&order=" + order + "'>20</a> ";
        moviesPerPageLink +=  "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=30&offset=" + offset;
        moviesPerPageLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        moviesPerPageLink += "&letter=" + letter + "&order=" + order + "'>30</a> ";
        moviesPerPageLink +=  "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=40&offset=" + offset;
        moviesPerPageLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        moviesPerPageLink += "&letter=" + letter + "&order=" + order + "'>40</a> ";
        moviesPerPageLink +=  "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=50&offset=" + offset;
        moviesPerPageLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        moviesPerPageLink += "&letter=" + letter + "&order=" + order + "'>50</a>";
  
        console.log(listNum);
        moviesPerPage.append(moviesPerPageLink);
        
        let next = jQuery("#next");
        var nextInt = parseInt(offset) + parseInt(limit);
        var nextLink = "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + nextInt;
        nextLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        nextLink += "&letter=" + letter + "&order=" + order + "'>Next Page</a> ";
        next.append(nextLink);
        
        let prev = jQuery("#prev");
        var offsetInt = parseInt(offset);
        if((offsetInt - parseInt(limit)) < 0)
        	offsetInt = 0;
        else
        	offsetInt = offsetInt - parseInt(limit);
        prevLink = "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + offsetInt;
        prevLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        prevLink += "&letter=" + letter + "&order=" + order + "'>Previous Page</a> ";
        prev.append(prevLink);
        
        $(document).on("click", '.btn', function(event) {
        	addToCart(event, this.id,this.name);
        	});
     	
}

console.log("looking for the movies...")
let title = getParameterByName('title');
let year = getParameterByName('year');
let director = getParameterByName('director');
let starName = getParameterByName('starName');
let genre = getParameterByName('genre');
let genreId = getParameterByName('genreId');
let letter = getParameterByName('letter'); 
let order = getParameterByName('order');
let limit = getParameterByName('limit');
let offset = getParameterByName('offset');

console.log(limit);

        
//Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
	
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "MovieListServlet?title=" +title +"&year=" +year +"&director=" +director +"&starName=" +starName +"&genre=" +genre + "&genreId=" +genreId + "&letter=" +letter + "&order=" +order + "&limit=" +limit + "&offset=" +offset, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
//$(document).on("click", ".btn", addToCart(this.id,this.name));








