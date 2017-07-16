package defPackage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

import database.AllConnections;
import database.DriveDB;

import javax.servlet.http.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class MyDrive {

	private static String CLIENT_ID = "548672842662-t9cr8fb2l6288ikja6367v4ck3drlk3j.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "EMXr3ltR8h3UaHOZxeqKWs0k";
	private Drive service;
	private AllConnections allConnections;
	private JsonBatchCallback<Permission> callback;

	public MyDrive() throws IOException {

		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET,
				Arrays.asList(new String[] { DriveScopes.DRIVE }))
				.setAccessType("online").setApprovalPrompt("auto").build();

		Credential credential = new AuthorizationCodeInstalledApp(
	            flow, new LocalServerReceiver()).authorize("ipopk15@freeuni.edu.ge");
		
		// Create a new authorized API client
		Drive service = new Drive.Builder(httpTransport, jsonFactory,
				credential).build();

		this.service = service;
		
		allConnections = new AllConnections();
		
		callback = new JsonBatchCallback<Permission>() {
		    @Override
		    public void onFailure(GoogleJsonError e,
		                          HttpHeaders responseHeaders)
		            throws IOException {
		        // Handle error
		        System.err.println(e.getMessage());
		    }

		    @Override
		    public void onSuccess(Permission permission,
		                          HttpHeaders responseHeaders)
		            throws IOException {
		        System.out.println("Permission ID: " + permission.getId());
		    }
		};
	}

	public Drive getDrive() {
		return service;
	}
	
	public String createFolder (String folderName) {
		File fileMetaData = new File();
		fileMetaData.setName(folderName);
		fileMetaData.setMimeType("application/vnd.google-apps.folder");
		
		File folder = null;
		try {
			folder = service.files().create(fileMetaData)
					.setFields("id")
					.execute();
			Permission userPermission = new Permission().setType("anyone").setRole("writer");
			BatchRequest batch = service.batch();
			service.permissions().create(folder.getId(), userPermission).queue(batch, callback);
			batch.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return folder != null ? folder.getId() : "";
	}
	
	public String createFolder (String folderName, String parentFolderId) {
		
		File fileMetaData = new File();
		fileMetaData.setName(folderName);
		fileMetaData.setMimeType("application/vnd.google-apps.folder");
		fileMetaData.setParents(Collections.singletonList(parentFolderId));
		
		File folder = null;
		try {
			folder = service.files().create(fileMetaData)
					.setFields("id, parents")
					.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return folder != null ? folder.getId() : "";
	}

	public void uploadFile (String fileName, java.io.File fl, String fileType, String folderId) {
		try {
			
			String mimeType = fileType;
			
			File file = new File();
			file.setName(fileName);
			file.setMimeType(mimeType);
			file.setParents(Collections.singletonList(folderId));

			java.io.File fileToUpload = fl;
			FileContent fileToUploadContent = new FileContent(mimeType, fileToUpload);
			File fileInFolder = service.files().create(file, fileToUploadContent)
						.setFields("id, parents")
						.execute();

			Permission userPermission = new Permission().setType("anyone").setRole("writer");
			BatchRequest batch = service.batch();
			service.permissions().create(fileInFolder.getId(), userPermission).queue(batch, callback);
			batch.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getAssignmentFolderId (String classroomId) {
		String classroomFolder = allConnections.driveDB.getClassroomFolder(classroomId);
		String assignmentFolderId = "";
		try {
			FileList fl = service.files().list().setQ(String.format("'%s' in parents", classroomFolder)).execute();
			for (File f: fl.getFiles()) {
				if (f.getName().equals("Assignments")) {
					assignmentFolderId = f.getId();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return assignmentFolderId;
	}
	
	public ArrayList<String> getHtmlForStudentUploads (String classroomId, String assignmentName, String studentEmail) {
		
		ArrayList<String> result = new ArrayList<String>();
		String studentsAssignmentsFolderId = getStudentsAssignmentsFolderId(classroomId);
		
		try {
			FileList fl = service.files().list().setQ(String.format("'%s' in parents", studentsAssignmentsFolderId)).execute();
			String assignmentFolderId = "";
			for (File f: fl.getFiles()) {
				if (f.getName().equals(assignmentName)) {
					assignmentFolderId = f.getId();
					break;
				}
			}
			if (assignmentFolderId.equals("")) {
				return result;
			}
			int atIndex = studentEmail.indexOf("@");
			if (atIndex == -1) {
				atIndex = studentEmail.length();
			}
			String mailPrefix = studentEmail.substring(0, atIndex);
			fl = service.files().list().setQ(String.format("'%s' in parents", assignmentFolderId)).execute();
			
			String studentFolderId = "";
			for (File f: fl.getFiles()) {
				if (f.getName().equals(mailPrefix)) {
					studentFolderId = f.getId();
					break;
				}
			}
			if (studentFolderId.equals("")) {
				return result;
			}
			
			fl = service.files().list().setQ(String.format("'%s' in parents", studentFolderId)).execute();
			for (File f: fl.getFiles()) {
				result.add(String.format("<a href=https://drive.google.com/open?id=%s> %s </a>\n", f.getId(), f.getName()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String getStudentsAssignmentsFolderId (String classroomId) {
		String classroomFolderId = allConnections.driveDB.getClassroomFolder(classroomId);
		String folderId = "";
		try {
			FileList fl = service.files().list().setQ(String.format("'%s' in parents", classroomFolderId)).execute();
			for (File f: fl.getFiles()) {
				if (f.getName().equals("Students Assignments")) {
					folderId = f.getId();
					break;
				}
			}
		} catch (IOException e) {
		}
		return folderId;
	}

	public void uploadAssignment(String studentEmail, java.io.File fileToUpload, String fileType, String classroomId, String assignmentTitle) {
		String studentsAssignmentFolder = getStudentsAssignmentsFolderId(classroomId);
		int atIndex = studentEmail.indexOf("@");
		if (atIndex == -1) atIndex = studentEmail.length();
		String studentEmailPrefix = studentEmail.substring(0, atIndex);
		try {
			FileList fl = service.files().list().setQ(String.format("'%s' in parents", studentsAssignmentFolder)).execute();
			String currentAssignmentFolderId = "";
			for (File f: fl.getFiles()) {
				if (f.getName().equals(assignmentTitle)) {
					currentAssignmentFolderId = f.getId();
					break;
				}
			}
			if (currentAssignmentFolderId.equals("")) {
				currentAssignmentFolderId = createFolder(assignmentTitle, studentsAssignmentFolder);
			}
			
			fl = service.files().list().setQ(String.format("'%s' in parents", currentAssignmentFolderId)).execute();
			String studentFolderId = "";
			for (File f: fl.getFiles()) {
				if (f.getName().equals(studentEmailPrefix)) {
					studentFolderId = f.getId();
					break;
				}
			}
			if (studentFolderId.equals("")) {
				studentFolderId = createFolder(studentEmailPrefix, currentAssignmentFolderId);
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String currentTime = dateFormat.format(date);
			
			uploadFile(studentEmailPrefix + " " + currentTime, fileToUpload, fileType, studentFolderId);

		} catch (IOException e) {
		}
	
	}
	
	public String getAssignmentFileId (String classroomId, String fileName) {
		String assignmentFileId = "";
		String classroomFolderId = allConnections.driveDB.getClassroomFolder(classroomId);
		
		try {
			FileList fl = service.files().list().setQ(String.format("'%s' in parents", classroomFolderId)).execute();
			String assignmentFolderId = "";
			for (File f: fl.getFiles()) {
				if (f.getName().equals("Assignments")) {
					assignmentFolderId = f.getId();
					break;
				}
			}
			
			if (assignmentFolderId.equals("")) {
				return "";
			}
			
			fl = service.files().list().setQ(String.format("'%s' in parents", assignmentFolderId)).execute();
			
			for (File f: fl.getFiles()) {
				if (f.getName().equals(fileName)) {
					assignmentFileId = f.getId();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return assignmentFileId;
	}
	
	public String findMaterialId (String classroomId, String categoryName, String materialName) {
		String materialId = "";
		String categoryFolderId = allConnections.driveDB.getCategoryFolder(classroomId, categoryName);
		
		try {
			FileList fl = service.files().list().setQ(String.format("'%s' in parents", categoryFolderId)).execute();
			for (File f: fl.getFiles()) {
				if (f.getName().equals(materialName)) {
					materialId = f.getId();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return materialId;
	}
	
	public static void main(String[] args) throws IOException {
		MyDrive drv = new MyDrive();
		drv.createFolder("rachiriginda");
//		drv.uploadFile("ragaca", "C:/Users/PC/Desktop/Pj8iWKG.jpg", "0BzefYzRpjMBPQkpVLXQtS3FDbGc");
//		FileList fl = drv.service.files().list()
//				.setQ("'0BzefYzRpjMBPQkpVLXQtS3FDbGc' in parents")
//				.execute();
//		for (File f: fl.getFiles()) {
//			System.out.println(f.getName() + " " + f.getId());
//		}
	}
}
