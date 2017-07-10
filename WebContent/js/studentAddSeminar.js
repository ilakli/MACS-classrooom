String.prototype.replaceAll = function(search, replacement) {
	var target = this;
	return target.replace(new RegExp(search, 'g'), replacement);
};

$(document).ready(function() { 
	
	$(".studentAddSeminarButton").on('click', function() {
	
		
		
		var str = $(this).parent().parent().html();
		
		
		
		str = str.substr(str.indexOf("<"));
		
		str = str.substr(0,str.indexOf("<inp"));
		
		
		

		str = str.replaceAll('<span>', '');

		str = str.replaceAll('</span>', ' ');

	
		
		var servlet = $(this).next().val();
		
		var seminarN = $(this).next().next().val();
		
		var classroomId = $("#classroom-id").val();
	
		
		
		
		$(this).parent().parent().find("span").remove();
		
		
		
		$.ajax({
			url : servlet,
			type: 'POST',
			data: {
				studentEmail: str,
				seminarN: seminarN,
				classroomID: classroomId
			},
			success : function(result) {
				location.reload();
			}
		});

	});
});