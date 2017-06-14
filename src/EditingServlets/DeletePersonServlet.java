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
 * Servlet implementation class DeletePersonServlet
 */
@WebServlet("/DeletePersonServlet")
public class DeletePersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeletePersonServlet() {
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
		
		String email = request.getParameter("email");

		DBConnection  connection = (DBConnection)request.getServletContext().getAttribute("connection");
		
		
		String classroomId = request.getParameter(Classroom.ID_ATTRIBUTE_NAME);
		
		Classroom classroom = connection.getClassroom(classroomId);
		
		if(classroom.classroomDeleteLecturer(email) || classroom.classroomDeleteSectionLeader(email)
				|| classroom.classroomDeleteSeminarist(email) || classroom.classroomDeleteStudent(email)){
			
			RequestDispatcher view = request.getRequestDispatcher("edit.jsp?"+EditStatusConstants.STATUS +"="
					+ EditStatusConstants.DEL_PERSON_ACC);	
						 
		view.forward(request, response);   
		
		System.out.println("deleted person: " + email + " class with id: " + classroomId);
		}	else {
			RequestDispatcher view = request.getRequestDispatcher("edit.jsp?"+EditStatusConstants.STATUS +"="
						+ EditStatusConstants.DEL_PERSON_REJ);	
							 
			view.forward(request, response);   
			
			System.out.println("Didn't Existed IN This Classroom: "  + email + "    class with id: " + classroomId);
		}
			
		
		
		
	}

}