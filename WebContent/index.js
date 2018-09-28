function handleGenreResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let table = jQuery("#genreTable");

    for(let i = 0; i < resultData.length; i++){
    	if(i % 4 == 0){
    		let rowHTML = "<tr>";
    		for (let j = 0; j < 4 && i+j < resultData.length; j++) {
	            rowHTML +=
	                "<td>" +
	                // Add a link to single-star.html with id passed with GET url parameter
	                '<a href="movie_list.html?genre=' + resultData[i+j]['genre'] + '&limit=10&offset=0">' 
	                + resultData[i+j]["genre"] +     // display star_name for the link text
	                '</a>' +
	                "</td>";
    		}
    		rowHTML += "</tr>";
    		table.append(rowHTML);
    		console.log(rowHTML);
    	}
    }
    	
    	let table2 = jQuery ('#letterTable');

    	var letterArray = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');
    	var numberArray = '1234567890'.split('');
    	let rowHTML = "<tr>";
    	for(let i = 0; i < letterArray.length; i++){
    				if(i % 13 == 0)
    					{
    					rowHTML += "</tr><tr>";
    					}
    				rowHTML += "<td><a href='movie_list.html?letter=" + letterArray[i] + "&limit=10&offset=0'>";
    				rowHTML += letterArray[i] + "</a></td>";
    			}
    			rowHTML += "</tr><tr>";
    			for(let i = 0; i < numberArray.length; i++)
    				{
    				rowHTML += "<td><a href='movie_list.html?letter=" + numberArray[i] + "&limit=10&offset=0'>";
    				rowHTML += numberArray[i] + "</a></td>";
    				}
    			table2.append(rowHTML);
    }


// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/genres", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleGenreResult(resultData) 
    		// Setting callback function to handle data returned successfully by the StarsServlet
});


function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	console.log("sending AJAX request to backend Java Servlet")
	
	// TODO: if you want to check past query results first, you can do it here
	
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
	jQuery.ajax({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "Suggestion?query=" + escape(query),
		"success": function(data) {
			// pass the data, query, and doneCallback function into the success handler
			handleLookupAjaxSuccess(data, query, doneCallback) 
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	})
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	
	// parse the string into JSON
	var jsonData = JSON.parse(data);
	console.log(jsonData)
	
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"])
	var url = suggestion["data"]["category"] + "-hero" + "?id=" + suggestion["data"]["heroID"]
	console.log(url)
}


/*
 * This statement binds the autocomplete library with the input box element and 
 *   sets necessary parameters of the library.
 * 
 * The library documentation can be find here: 
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 * 
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set the groupby name in the response json data field
    groupBy: "category",
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
}

// bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the handler function
		handleNormalSearch($('#autocomplete').val())
	}
})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button

