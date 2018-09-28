/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */


/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
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

//if click the remove button
function addToCart(event, m_id, m_name,value) {
    console.log("add a movie");
    //event.preventDefault();
    jQuery.ajax({
    	dataType: "json",
    	method: "GET",
        url: "AddServlet?movieid=" + m_id + "&movietitle=" + m_name + "number=" + value  
    });
    jQuery("input").serialize();
}

function remove(event, m_id, m_name) {
    console.log("remove a movie");
    //event.preventDefault();
    jQuery.ajax({
    	dataType: "json",
    	method: "GET",
        url: "RemoveServlet?id=" + m_id + "&name=" + m_name
    });
    jQuery(".btn").serialize();
}

function handleResult(resultData) {
	
    let movieTableBodyElement = jQuery("#cart_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < Math.min(10, resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["id"] + "</th>";
        rowHTML += "<th>" + resultData[i]["title"] + "</th>";
        rowHTML += "<th><from><input placeholder='" + resultData[i]["quantity"] + "' id='" + resultData[i]["id"] + "' name='" + resultData[i]["title"] + "'></input></th>";
        rowHTML += "<th>";
        rowHTML += "<a href='view_cart.html?increment="+resultData[i]["title"] +"'>+</a>";
        rowHTML += "</th>";
        rowHTML += "<th>";
		rowHTML += "<a href='view_cart.html?decrement="+resultData[i]["title"] +"'>-</a>";
		rowHTML += "</th>";
        //rowHTML += "<th>" + resultData[i]["starredMovies"] + "</th>";
        rowHTML += "<button class='btn btn-outline-primary' id='" + resultData[i]["id"] + "' name='" + resultData[i]["title"] + "'>Remove</button>";
        rowHTML += "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
    
    $(document).on("input porpertychange", 'input', function(event) {
    	addToCart(event, this.id,this.name,this.value);
    	});
    
    $(document).on("click", '.btn-outline-primary', function(event) {
    	remove(event, this.id,this.name);
    	jQuery("#"+this.id).attr('placeholder', '0');
    	jQuery("#"+this.id).value = 0;
    	});
}



jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "ShowCartServlet", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});