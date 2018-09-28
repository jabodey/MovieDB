function handlePayResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle payment response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    if (resultDataJson["status"] === "success") {
        window.location.replace("confirmation.html");
    }
    
    else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        jQuery("#login_error_message").text(resultDataJson["message"]);
    }
}

function submitBillingForm(formSubmitEvent) {
    console.log("submit billing form");
    formSubmitEvent.preventDefault();
    console.log("prevent");

    jQuery.post(
        "api/billing",
        // Serialize the login form to the data sent by POST request
        jQuery("#billing_form").serialize(),
        (resultDataString) => handlePayResult(resultDataString));

}

// Bind the submit action of the form to a handler function
jQuery("#billing_form").submit((event) => submitBillingForm(event));

