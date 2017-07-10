<%@page import="defPackage.StudentAssignment"%>
<%@page import="database.PersonDB"%>
<%@page import="defPackage.Comment"%>
<%@page import="defPackage.Person"%>
<%@page import="defPackage.Section"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="defPackage.Seminar"%>
<%@page import="defPackage.Post"%>
<%@page import="java.util.List"%>
<%@page import="defPackage.Classroom"%>
<%@page import="defPackage.Assignment"%>
<%@page import="database.AllConnections"%>
<%@page import="WorkingServlets.DownloadServlet"%>
<%@page import="defPackage.Material"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/semantic-ui/2.2.10/semantic.min.css">
<script
	src="https://cdn.jsdelivr.net/semantic-ui/2.2.10/semantic.min.js"></script>
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<title>Assignments</title>
<style type="text/css">
.ui.menu {
	margin-top: 0;
}

.block.header {
	margin: 0;
}
.sign-out {
	float: right;
	margin-top: 0.8%;
	margin-right: 0.7%;
}
.sign-out {
	float: right;
	margin-top: 0.8%;
	margin-right: 0.7%;
}

.head-panel {
	display: block;
	margin: 0 !important;
	padding: 0 !important;
}

.head-text {
	display: inline-block;
	border: solid;
	padding: .78571429rem 1rem !important;
	!
	important;
}
</style>
</head>
<body>


	<%!private String generateAssignmentHTML(Assignment a) {
		
		String result = "<div class=\"panel panel-default\"> " + 
						" <div class=\"panel-body\"> " + 
						"<h1>" + a.getTitle() + "</h1>" + 
						"<p> " + a.getInstructions() + "</p>";
						
						if( a.getDeadline()!= null){
							result+="<p> Deadline:" + a.getDeadline() + "</p>";
						}
						
						if(a.getFileName() != null){
							result +=" <a href=\"DownloadServlet?" + DownloadServlet.DOWNLOAD_PARAMETER 
									+ "=" + a.getFileName() + "\">" + a.getFileName() + "</a></div>";
						}
						
						
						result+= " <div class=\"panel-footer\"></div> " 
								
						+ "</div>";
		
		return result;
	}%>
	
	<%!private String generateStudentHTML(Person p, String classroomID, Assignment a, AllConnections connector) {
		
		StudentAssignment studentAssignment = 
				connector.studentAssignmentDB.getStudentAssignment(classroomID, p.getPersonID(), a.getTitle());
		boolean isApproved = studentAssignment.getApproval();
		Integer grade = studentAssignment.getAssignmentGrade();
		
		String gradeCode;
		if (grade == null)
			gradeCode = "<div class=\"ui red horizontal label\">Not Graded</div>"; else
			gradeCode = "<div class=\"ui green horizontal label\">Graded</div>";
		
		String approvalCode;
		if (isApproved)
			approvalCode = "<div class=\"ui green horizontal label\">Approved</div>"; else
			approvalCode = "<div class=\"ui red horizontal label\">Not Yet Approved</div>";
		
		String hrefCode = " href = \"studentsOneAssignment.jsp?" + Classroom.ID_ATTRIBUTE_NAME + "=" + classroomID+ 
				"&studentEmail="+p.getEmail()+ "&assignmentTitle=" + a.getTitle() +"\"";
		
		String result = "<div class=\"item\">" +
						"<div class=\"right floated content\">" +
						gradeCode +
						approvalCode +
						"</div>" +
						"<img class=\"ui avatar image\" src = \"" + p.getPersonImgUrl() + "\">" +
						"<div class = \"content\">" +
						"<a class =\"header\"" + hrefCode + ">" + p.getName() + " " + p.getSurname() + "</a>" +
						"<div class = \"description\">" + p.getEmail() + "</div>" +
						"</div>" + 
						"</div>"; 
		
		return result;
	}%>

	<%
		String classroomID = request.getParameter(Classroom.ID_ATTRIBUTE_NAME);
		AllConnections connector = (AllConnections) request.getServletContext().getAttribute("connection");
		Classroom currentClassroom = connector.classroomDB.getClassroom(classroomID);
		
		
		Person currentPerson = (Person)request.getSession().getAttribute("currentPerson");
		boolean isAdmin = connector.personDB.isAdmin(currentPerson);
		boolean isStudent = currentClassroom.classroomStudentExists(currentPerson.getEmail());
		boolean isSectionLeader = currentClassroom.classroomSectionLeaderExists(currentPerson.getEmail());
		boolean isSeminarist = currentClassroom.classroomSeminaristExists(currentPerson.getEmail());
		boolean isLecturer = currentClassroom.classroomLecturerExists(currentPerson.getEmail());
		
		String assignmentTitle = request.getParameter("assignmentTitle");
		Assignment assignment = connector.assignmentDB.getAssignment(assignmentTitle, classroomID);
	
		for (Person p : currentClassroom.getClassroomStudents()){
			
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String deadlineWithReschedulings = "";
			if (assignment.getDeadline()!=null) deadlineWithReschedulings = format1.format(assignment.getDeadline());
			
			
			connector.studentAssignmentDB.addStudentAssignment(classroomID, p.getPersonID(), assignmentTitle,
					deadlineWithReschedulings );
		}
		
	%>

	<div class="ui block header head-panel">
	<a href="index.jsp">
		<h3 class="ui header head-text">Macs Classroom</h3>
	</a>
	  <a class="sign-out" href="DeleteSessionServlet" onclick="signOut();">Sign out</a>
	</div>
	<div class="ui menu">
		<a
			href=<%="stream.jsp?" + Classroom.ID_ATTRIBUTE_NAME + "=" + classroomID%>
			class="header item"> <%=currentClassroom.getClassroomName()%>
		</a> <a class="item"
			href=<%="stream.jsp?" + Classroom.ID_ATTRIBUTE_NAME + "=" + classroomID%>>Stream</a>


		<a class="item"
			href=<%="about.jsp?" + Classroom.ID_ATTRIBUTE_NAME + "=" + classroomID%>>About</a>

		<a class="active item"
			href=<%="assignments.jsp?" + Classroom.ID_ATTRIBUTE_NAME + "=" + classroomID%>>Assignments</a>

		<%
			if (isAdmin || isLecturer) {
		%>
		<a class="item"
			href=<%="settings.jsp?" + Classroom.ID_ATTRIBUTE_NAME + "=" + classroomID%>>Settings</a>
		<%
			}
		%>
		<div class="right menu">
			<a class="item"
				href=<%="people.jsp?" + Classroom.ID_ATTRIBUTE_NAME + "=" + classroomID%>>
				People </a>
			<div class="ui dropdown item">
				Groups <i class="dropdown icon"></i>
				<div class="menu">
					<a
						href=<%="sections.jsp?" + Classroom.ID_ATTRIBUTE_NAME + "=" + classroomID%>
						class="item"> Sections </a> <a
						href=<%="seminars.jsp?" + Classroom.ID_ATTRIBUTE_NAME + "=" + classroomID%>
						class="item"> Seminars </a>
				</div>
			</div>
		</div>
	</div>
	
	<!-- -------------------------------------------------------------------- -->
	<%
		String htmlCode = generateAssignmentHTML(assignment);
		out.println(htmlCode);
	%>
	
	<%if (isSectionLeader){%>
		
		<%Section section = connector.sectionDB.getSectionByLeader(currentPerson, classroomID);
		if (section != null) {
			List <Person> sectionStudents = section.getSectionStudents();
			
			out.println("<h2>Students:</h2>");
			out.println("<div class=\"ui relaxed list\">");
			for (Person p : sectionStudents) {
				out.println(generateStudentHTML(p, classroomID, assignment, connector));
			}
			out.println("</div>");
		} else {
			out.println("you have no section yet...");
		}%>
		
	<%}%>
	
	
	
	<%if (isSeminarist){%>
		
		<%Seminar seminar = connector.seminarDB.getSeminarBySeminarist(currentPerson, classroomID);
		if (seminar != null){
			List <Person> seminarStudents = seminar.getSeminarStudents();
			
			out.println("<h2>Students:</h2>");
			out.println("<div class=\"ui relaxed list\">");
			for (Person p : seminarStudents){
				out.println(generateStudentHTML(p, classroomID, assignment, connector));
			}
			out.println("</div>");
		} else {
			out.println("you have no seminar group yet...");
		}%>
	<%}%>
	
	<!-- -------------------------------------------------------------------- -->
	<script src='https://code.jquery.com/jquery-3.1.0.min.js'></script>
	<script type="text/javascript" src='js/posts.js'></script>
	<script type="text/javascript" src='js/comments.js' type="text/javascript"></script>



</body>
</html>