package EditingServlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import defPackage.Classroom;
import defPackage.DBConnection;

/**
 * Servlet implementation class AddNewSectionLeaderServlet
 */
@WebServlet("/AddNewSectionLeaderServlet")
public class AddNewSectionLeaderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNewSectionLeaderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String surname = request.getParameter("surname");
		
		
		DBConnection  connection = (DBConnection)request.getServletContext().getAttribute("connection");
		
		//temporary function to be sure that before adding someone to students table, they will be added to persons
		connection.addPerson(name, surname, email);
	
		String classroomId = request.getParameter(Classroom.ID_ATTRIBUTE_NAME);
		
		Classroom currentClassroom = connection.getClassroom(classroomId);
		if(currentClassroom.classroomAddSectionLeader(email)) {
			RequestDispatcher view = request.getRequestDispatcher("edit.jsp?"+EditStatusConstants.STATUS +"="
					+ EditStatusConstants.ADD_NEW_SECTION_LEADER_ACC);	
						 
			view.forward(request, response);  
			System.out.println("Added Section Leader: " + name + " " + surname + " " + email + " to class with id: " + classroomId);
		}
		else {
			RequestDispatcher view = request.getRequestDispatcher("edit.jsp?"+EditStatusConstants.STATUS +"="
					+ EditStatusConstants.ADD_NEW_SECTION_LEADER_REJ);	
						 
			view.forward(request, response);  
			System.out.println("Person Already Existed IN This Classroom: " 
			+ name + " " + surname + " " + email + "    class with id: " + classroomId);
		}
	}

}