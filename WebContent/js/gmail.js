function onSignIn(googleUser) {

	var profile = googleUser.getBasicProfile();

	var firstName = profile.getGivenName();
	var lastName = profile.getFamilyName();
	var img = profile.getImageUrl();
	var email = profile.getEmail();
	
	$.ajax({
		url : "LoginServlet",
		type: 'POST',
		data: {
			firstName: firstName,
			lastName: lastName,
			email: email,
			image: img
		},
		success : function(result) {
			window.location = "index.jsp"
		}
	});
};