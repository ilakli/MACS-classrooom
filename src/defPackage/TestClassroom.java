package defPackage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import database.AllConnections;
import database.ClassroomDB;

@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class TestClassroom {
	
	private AllConnections db = new AllConnections();
	private MockClassroom oop ; 
	private MockClassroom pp;
	private MockClassroom meth;
	private AllConnections allConnections;
	@Before
	public void init(){
		//creating classes;
		this.allConnections = new AllConnections();
		oop = new MockClassroom ("OOP" , db.classroomDB.addClassroom("OOP", "1"),"1");
		pp = new MockClassroom ("Paradigms", db.classroomDB.addClassroom("Paradigms", "1"),"1");
		meth = new MockClassroom ("Methodologies", db.classroomDB.addClassroom("Methodologies", "1"),"1");
	}
	
	
	@Test
	public void test1init() {
		//Just add people into database so we can use them
		
		db.personDB.addPerson("irakli", "popkhadze", "ipopk15@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		db.personDB.addPerson("giorgi", "khosroshvili", "gkhos15@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		db.personDB.addPerson("shota", "gvinepadze", "s.gvinepadze@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		db.personDB.addPerson("nika", "begiashvili", "n.begiashvili@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		db.personDB.addPerson("giorgi", "cercvadze", "gitser15@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		db.personDB.addPerson("aleko", "cxovrebovi", "acxcx15@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		db.personDB.addPerson("mari", "berishvili", "mberi15@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		
		//check if class names are correct;
		assertEquals(oop.getClassroomName(), "OOP");
		
		assertEquals(pp.getClassroomName(), "Paradigms");
			
		assertEquals(meth.getClassroomName(), "Methodologies");
		
	}
	
	/**
	 * Testing add methods of classroom; 
	 */
	@Test
	public void test2AddingToClassrom() {
		
		assertTrue(allConnections.lecturerDB.addGlobalLecturer("s.gvinepadze@freeuni.edu.ge"));
		assertTrue(oop.classroomAddLecturer("s.gvinepadze@freeuni.edu.ge"));
		assertTrue(pp.classroomAddLecturer("s.gvinepadze@freeuni.edu.ge"));
		assertTrue(meth.classroomAddLecturer("s.gvinepadze@freeuni.edu.ge"));
		
		assertTrue(oop.classroomAddSeminarist("n.begiashvili@freeuni.edu.ge"));
		assertTrue(pp.classroomAddSeminarist("n.begiashvili@freeuni.edu.ge"));
		
		assertTrue(meth.classroomAddSectionLeader("ipopk15@freeuni.edu.ge"));
		assertTrue(meth.classroomAddSectionLeader("gkhos15@freeuni.edu.ge"));
		assertTrue(meth.classroomAddSectionLeader("acxcx15@freeuni.edu.ge"));
		
		assertFalse(meth.classroomAddSectionLeader("ipopk15@freeuni.edu.ge"));
		assertFalse(meth.classroomAddSectionLeader("gkhos15@freeuni.edu.ge"));
		
		assertFalse(pp.classroomAddLecturer("baqara@freeuni.edu.ge"));
		assertFalse(pp.classroomAddSeminarist("baqara@freeuni.edu.ge"));
		assertFalse(oop.classroomAddSectionLeader("baquna@freeuni.edu.ge"));
		assertFalse(meth.classroomAddStudent("xosrika@freeuni.edu.ge"));
	}
	
	/**
	 * Testing deleting and exist method in classroom;
	 */
	@Test
	public void test3PersonsDelete() {
		oop.classroomAddLecturer("s.gvinepadze@freeuni.edu.ge");
		pp.classroomAddLecturer("s.gvinepadze@freeuni.edu.ge");
		meth.classroomAddLecturer("s.gvinepadze@freeuni.edu.ge");
		oop.classroomAddSeminarist("n.begiashvili@freeuni.edu.ge");
		meth.classroomAddSectionLeader("ipopk15@freeuni.edu.ge");
		meth.classroomAddSectionLeader("gkhos15@freeuni.edu.ge");
		
		
		assertTrue(oop.classroomSeminaristExists("n.begiashvili@freeuni.edu.ge"));
		assertFalse(oop.classroomDeleteSeminarist("ipopk15@freeuni.edu.ge"));
		assertTrue(oop.classroomDeleteSeminarist("n.begiashvili@freeuni.edu.ge"));
		assertFalse(oop.classroomSeminaristExists("n.begiashvili@freeuni.edu.ge"));
		
		assertTrue(oop.classroomLecturerExists("s.gvinepadze@freeuni.edu.ge"));
		assertFalse(oop.classroomDeleteLecturer("n.begiashvili@freeuni.edu.ge"));
		assertTrue(oop.classroomDeleteLecturer("s.gvinepadze@freeuni.edu.ge"));
		assertFalse(oop.classroomLecturerExists("s.gvinepadze@freeuni.edu.ge"));
		assertTrue(pp.classroomLecturerExists("s.gvinepadze@freeuni.edu.ge"));
		
		assertTrue(meth.classroomSectionLeaderExists("ipopk15@freeuni.edu.ge"));
		assertTrue(meth.classroomDeleteSectionLeader("ipopk15@freeuni.edu.ge"));
		assertTrue(meth.classroomDeleteSectionLeader("gkhos15@freeuni.edu.ge"));
		assertFalse(meth.classroomSectionLeaderExists("gkhos15@freeuni.edu.ge"));
		assertFalse(meth.classroomSectionLeaderExists("ipopk15@freeuni.edu.ge"));
		
		assertFalse(pp.classroomDeleteSectionLeader("vigaca@freeuni.edu.ge"));

		db.personDB.addPerson("tpp", "tpp", "tpp@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		db.personDB.addPerson("kpp", "kpp", "kpp@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		db.personDB.addPerson("unnamed", "unnamed", "unnamed@freeuni.edu.ge","http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
		
		assertTrue(oop.classroomAddStudent("tpp@freeuni.edu.ge"));
		assertTrue(oop.classroomAddStudent("kpp@freeuni.edu.ge"));
		assertTrue(oop.classroomAddStudent("unnamed@freeuni.edu.ge"));
		
		assertTrue(oop.classroomStudentExists("tpp@freeuni.edu.ge"));
		assertTrue(oop.classroomStudentExists("kpp@freeuni.edu.ge"));
		assertTrue(oop.classroomStudentExists("unnamed@freeuni.edu.ge"));
		assertTrue(oop.classroomDeleteStudent("tpp@freeuni.edu.ge"));
		assertTrue(oop.classroomDeleteStudent("kpp@freeuni.edu.ge"));
		assertTrue(oop.classroomDeleteStudent("unnamed@freeuni.edu.ge"));
		assertFalse(oop.classroomStudentExists("tpp@freeuni.edu.ge"));
		assertFalse(oop.classroomStudentExists("kpp@freeuni.edu.ge"));
		assertFalse(oop.classroomStudentExists("unnamed@freeuni.edu.ge"));
	}
	
	/**
	 * Testing method of classroom
	 * which return persons list (like students, section leaders and so on);
	 */
	@Test
	public void test4ListGetters() {
		String currentClassroom = db.classroomDB.addClassroom("just to test", "1");
		MockClassroom testClass = new MockClassroom("just to test",currentClassroom,"1");
				
		//Testing students' list
		ArrayList <Person> realStudents = new ArrayList <Person>();
		for (char ch = 'a'; ch <= 'z'; ch++) {
			String name = String.valueOf(ch);
			String surname = String.valueOf(ch);
			String email = String.valueOf(ch) + "@freeuni.edu.ge";

			db.personDB.addPerson(name, surname, email,"http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
			db.studentDB.addStudent(email, currentClassroom);
			
			realStudents.add(new Person(name, surname, email, "3"));
		}
		
		List <Person> students = testClass.getClassroomStudents();
		assertEquals(students, realStudents);
		
		//Testing lecturers' list
		ArrayList <Person> realLecturers = new ArrayList <Person>(); 
		for (char ch = 'a'; ch <= 'z'; ch++) {
			String name = "a" + String.valueOf(ch);
			String surname = "a" + String.valueOf(ch);
			String email = "a" + String.valueOf(ch) + "@freeuni.edu.ge";

			db.personDB.addPerson(name, surname, email,"http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
			db.lecturerDB.addGlobalLecturer(email);
			db.lecturerDB.addLecturer(email, currentClassroom);
			
			realLecturers.add(new Person(name, surname, email, "2"));
		}
		List <Person> lecturers = testClass.getClassroomLecturers();
		assertEquals(lecturers, realLecturers);

		//Testing seminarists' list
		ArrayList <Person> realSeminarists = new ArrayList <Person>(); 
		for (char ch = 'a'; ch <= 'z'; ch++) {
			String name = "b" + String.valueOf(ch);
			String surname = "b" + String.valueOf(ch);
			String email = "b" + String.valueOf(ch) + "@freeuni.edu.ge";

			db.personDB.addPerson(name, surname, email,"http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
			db.seminaristDB.addSeminarist(email, currentClassroom);
			
			realSeminarists.add(new Person(name, surname, email, "2"));
		}
		List <Person> seminarists = testClass.getClassroomSeminarists();
		assertEquals(seminarists, realSeminarists);

		//Testing section leaders' list;
		ArrayList <Person> realSectionLeaders = new ArrayList <Person>(); 
		for (char ch = 'a'; ch <= 'z'; ch++) {
			String name = "c" + String.valueOf(ch);
			String surname = "c" + String.valueOf(ch);
			String email = "c" + String.valueOf(ch) + "@freeuni.edu.ge";

			db.personDB.addPerson(name, surname, email,"http://cdn.makeuseof.com/wp-content/uploads/2011/04/0-incognito-intro.jpg?x92042");
			db.sectionLeaderDB.addSectionLeader(email, currentClassroom);
			
			realSectionLeaders.add(new Person(name, surname, email, "2"));
		}
		List <Person> sectionLeaders = testClass.getClassroomSectionLeaders();
		assertEquals(sectionLeaders, realSectionLeaders);				
	}
		
	/**
	 * This method tests sections and seminars in the classroom
	 * checked: add/delete and list methods for this objects;
	 */
	@Test
	public void test5SectionAndSeminars(){
		String classID = db.classroomDB.addClassroom("testGroups", "1");
		MockClassroom mockClass = new MockClassroom("testGoups", classID,"1");
		
		assertTrue(mockClass.classroomAddSection());
		assertTrue(mockClass.classroomSectionExists(0));
		assertFalse(mockClass.classroomSectionExists(1));
		assertTrue(mockClass.classroomAddSection());
		assertTrue(mockClass.classroomSectionExists(1));
		assertTrue(mockClass.classroomDeleteSection());
		assertTrue(mockClass.classroomDeleteSection());
		assertFalse(mockClass.classroomDeleteSection());
		
		ArrayList<Section> sections = new ArrayList<Section>();
		
		sections.add(new Section(0, classID,allConnections));
		sections.add(new Section(1, classID,allConnections));
		sections.add(new Section(2, classID,allConnections));

		db.sectionDB.addSection(classID);
		db.sectionDB.addSection(classID);
		db.sectionDB.addSection(classID);
		
		assertEquals(mockClass.getClassroomSections(), sections);
		
		assertTrue(mockClass.classroomAddSeminar());
		assertTrue(mockClass.classroomSeminarExists(0));
		assertFalse(mockClass.classroomSeminarExists(1));
		assertTrue(mockClass.classroomAddSeminar());
		assertTrue(mockClass.classroomDeleteSeminar());
		assertTrue(mockClass.classroomDeleteSeminar());
		assertFalse(mockClass.classroomDeleteSeminar());
		
		ArrayList<Seminar> seminars = new ArrayList<Seminar>();
		
		seminars.add(new Seminar(0, classID,allConnections));
		seminars.add(new Seminar(1, classID,allConnections));
		seminars.add(new Seminar(2, classID,allConnections));
		
		db.seminarDB.addSeminar(classID);
		db.seminarDB.addSeminar(classID);
		db.seminarDB.addSeminar(classID);
		
		assertEquals(mockClass.getClassroomSeminars(),seminars);
	}
	
	/**
	 * This method tests adding people in the groups;
	 * If they work add methods can be used;
	 */
	@Test
	public void test6AddGroupsAndPeople(){
		String classID = db.classroomDB.addClassroom("testGroupsAndPeople", "1");
		MockClassroom mockClass = new MockClassroom("testGoupsAndPeople", classID,"1");
		
		mockClass.classroomAddSection();
		mockClass.classroomAddSection();
		
		mockClass.classroomAddSeminar();
		mockClass.classroomAddSeminar();
		
		mockClass.classroomAddLecturer("s.gvinepadze@freeuni.edu.ge");
		mockClass.classroomAddSectionLeader("gkhos15@freeuni.edu.ge");
		mockClass.classroomAddSectionLeader("ipopk15@freeuni.edu.ge");
		mockClass.classroomAddSeminarist("n.begiashvili@freeuni.edu.ge");
		mockClass.classroomAddStudent("gitser15@freeuni.edu.ge");

//		similar methods in section and seminar classes must be tested		
//		assertFalse(mockClass.classroomAddSeminaristToSeminar(123, "n.begiashvili@freeuni.edu.ge"));
//		assertFalse(mockClass.classroomAddSeminaristToSeminar(0, "vigaca@mail.re"));
//		assertFalse(mockClass.classroomAddSeminaristToSeminar(0, "gitser15@freeuni.edu.ge"));
//		assertFalse(mockClass.classroomAddSeminaristToSeminar(0, "s.gvinepadze@freeuni.edu.ge"));
//		assertTrue(mockClass.classroomAddSeminaristToSeminar(0, "n.begiashvili@freeuni.edu.ge"));
//		assertTrue(mockClass.classroomAddSeminaristToSeminar(1, "n.begiashvili@freeuni.edu.ge"));
//		
//		assertFalse(mockClass.classroomAddSectionLeaderToSection(123, "gkhos15@freeuni.edu.ge"));
//		assertFalse(mockClass.classroomAddSectionLeaderToSection(0, "vigaca@mail.re"));
//		assertFalse(mockClass.classroomAddSectionLeaderToSection(0, "gitser15@freeuni.edu.ge"));
//		assertFalse(mockClass.classroomAddSectionLeaderToSection(0, "s.gvinepadze@freeuni.edu.ge"));
//		assertTrue(mockClass.classroomAddSectionLeaderToSection(0, "gkhos15@freeuni.edu.ge"));
//		assertTrue(mockClass.classroomAddSectionLeaderToSection(1, "ipopk15@freeuni.edu.ge"));
//		
//		assertFalse(mockClass.classroomAddStudentToSection(0, "acxcx15@freeuni.edu.ge"));
//		assertFalse(mockClass.classroomAddStudentToSection(0, "ipopk15@freeuni.edu.ge"));
//		assertFalse(mockClass.classroomAddStudentToSection(0, "raraca@mail.ru"));
//		assertTrue(mockClass.classroomAddStudentToSection(0, "gitser15@freeuni.edu.ge"));
//		
//		assertFalse(mockClass.classroomAddStudentToSeminar(0, "acxcx15@freeuni.edu.ge"));
//		assertFalse(mockClass.classroomAddStudentToSeminar(0, "gkhos15@freeuni.edu.ge"));
//		assertFalse(mockClass.classroomAddStudentToSeminar(0, "raraca@mail.ru"));
//		assertTrue(mockClass.classroomAddStudentToSeminar(0, "gitser15@freeuni.edu.ge"));
	}
	
	
}
