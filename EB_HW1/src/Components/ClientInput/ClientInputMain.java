/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */
package Components.ClientInput;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import Framework.Event;
import Framework.EventId;
import Framework.RMIEventBus;
public class ClientInputMain {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("** ClientInputMain(ID:" + componentId + ") is successfully registered. \n");		
		boolean done = false;
		while (!done) {
			writeMenu();
			try {
				switch (new BufferedReader(new InputStreamReader(System.in)).readLine().trim()) {
				case "1":
					eventBus.sendEvent(new Event(EventId.ListStudents, null));
					printLogSend(EventId.ListStudents);
					break;
				case "2":
					eventBus.sendEvent(new Event(EventId.ListCourses, null));
					printLogSend(EventId.ListCourses);
					break;
				case "3":
					eventBus.sendEvent(new Event(EventId.RegisterStudents, makeStudentInfo()));
					printLogSend(EventId.RegisterStudents);
					break;
				case "4":
					eventBus.sendEvent(new Event(EventId.RegisterCourses, makeCourseInfo()));
					printLogSend(EventId.RegisterCourses);
					break;
				case "5":
					eventBus.sendEvent(new Event(EventId.DeleteStudents,makeStudentInfo()));
					printLogSend(EventId.DeleteStudents);
					break;
				case "6":
					eventBus.sendEvent(new Event(EventId.DeleteCourses,makeCourseInfo()));
					printLogSend(EventId.DeleteCourses);
					break;
				case "7":
					eventBus.sendEvent(new Event(EventId.RegisterEnrollment,enrollmentInfo()));
					printLogSend(EventId.RegisterEnrollment);
					break;	
				case "8":
					eventBus.sendEvent(new Event(EventId.CheckEnrollment,checkenrollmentInfo()));
					printLogSend(EventId.CheckEnrollment);
					break;	
				case "0":
					eventBus.sendEvent(new Event(EventId.QuitTheSystem, "Quit the system!!!"));
					printLogSend(EventId.QuitTheSystem);
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static String checkenrollmentInfo() throws IOException {
		String userInput = "";
		 System.out.println("\nEnter Student ID and press return (Ex. 20131234)>> ");
		 userInput = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		 return userInput;
}
	private static String enrollmentInfo() throws IOException {
		String userInput = "";
		 System.out.println("\nEnter Student ID and press return (Ex. 20131234)>> ");
		 userInput = new BufferedReader(new InputStreamReader(System.in)).readLine().trim(); // 학생 ID 입력
		 System.out.println("\nEnter Course ID and press return (Ex. 12345)>> ");
		 userInput += " " + new BufferedReader(new InputStreamReader(System.in)).readLine().trim(); // 과목 ID 입력
		 System.out.println("\n** Enrollment Info: " + userInput + "\n");
		 return userInput; 	   
	}
	private static String makeStudentInfo() throws IOException {
		String userInput = "";
		System.out.println("\nEnter student ID and press return (Ex. 20131234)>> ");
		userInput = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		System.out.println("\nEnter family name and press return (Ex. Hong)>> ");
		userInput += " " + new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		System.out.println("\nEnter first name and press return (Ex. Gildong)>> ");
		userInput += " " + new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		System.out.println("\nEnter department and press return (Ex. CS)>> ");
		userInput += " " + new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		System.out.println(
				"\nEnter a list of IDs (put a space between two different IDs) of the completed courses and press return >> ");
		System.out.println("(Ex. 17651 17652 17653 17654)");
		userInput += " " + new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		System.out.println("\n ** Message: " + userInput + "\n");
		return userInput;
	}
	private static String makeCourseInfo() throws IOException {
		String userInput = "";
		System.out.println("\nEnter course ID and press return (Ex. 12345)>> ");
		userInput = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		System.out.println("\nEnter the family name of the instructor and press return (Ex. Hong)>> ");
		userInput += " " + new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		System.out.println(
				"\nEnter the name of the course ( substitute a space with ab underbar(_) ) and press return (Ex. C++_Programming)>> ");
		userInput += " " + new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		System.out.println(
				"\nEnter a list of IDs (put a space between two different IDs) of prerequisite courses and press return >> ");
		System.out.println("(Ex. 12345 17651)");
		userInput += " " + new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		System.out.println("\n ** Message: " + userInput + "\n");
		return userInput;
	}
	@SuppressWarnings("unused") //사용하지 않는 코드 또는 불필요한 코드가 있을 경우 경고를 억제시켜준다.
	private static String setStudentId() throws IOException {
		System.out.println("\nEnter student ID and press return (Ex. 20131234)>> ");
		return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
	}
	@SuppressWarnings("unused")
	private static String setCourseId() throws IOException {
		System.out.println("\nEnter course ID and press return (Ex. 12345)>> ");
		return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
	}
	private static void writeMenu() {
		System.out.println("1. List Students");
		System.out.println("2. List Courses");
		System.out.println("3. Register a new Student");
		System.out.println("4. Register a new Course");
		System.out.println("5. Delete Student List");
		System.out.println("6. Delete Course List");
		System.out.println("7. Course Enrollment");
		System.out.println("8. Check Enrollment");
		System.out.println("0. Quit the system");
		System.out.print("\n Choose No.: ");
	}
	private static void printLogSend(EventId eventId) {
		System.out.println("\n** Sending an event(ID:" + eventId + ")\n");
	}
}