/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log("status:" +resultDataJson["status"]);
    //console.log("gstatus:" +resultDataJson["gstatus"]);
    // If login success, redirect to index.html page
    //if (resultDataJson["employee"] == null && resultDataJson["status"] === "success" && resultDataJson["gstatus"] === "success") {
    if (resultDataJson["employee"] == null && resultDataJson["status"] === "success") {
        window.location.replace("index.html");
    }
    //else if(resultDataJson["employee"] != null && resultDataJson["status"] === "success" && resultDataJson["gstatus"] === "success"){
    else if(resultDataJson["employee"] != null && resultDataJson["status"] === "success"){
    	window.location.replace("_dashboard.html")
    }
    // If login fail, display error message on <div> with id "login_error_message"
    else {
    	console.log("errors!")
        if(resultDataJson["message"] != null){
        	console.log("show error message:");
            console.log(resultDataJson["message"]);
        	jQuery("#login_error_message").text(resultDataJson["message"]);
        }
    	/*
        if(resultDataJson["gmessage"] != null)
        	jQuery("#g_error_message").text(resultDataJson["gmessage"]);
        	*/
    }

}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();

    jQuery.post(
        "api/login",
        // Serialize the login form to the data sent by POST request
        jQuery("#login_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString));

}

// Bind the submit action of the form to a handler function
jQuery("#login_form").submit((event) => submitLoginForm(event));

