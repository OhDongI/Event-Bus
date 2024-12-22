/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */
package Components.Course;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import Components.Student.StudentMain;
import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;
public class CourseMain {
	public static void main(String[] args) throws FileNotFoundException, IOException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("CourseMain (ID:" + componentId + ") is successfully registered...");
		CourseComponent coursesList = new CourseComponent("Courses.txt");
		Event event = null;
		boolean done = false;
		while (!done) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			EventQueue eventQueue = eventBus.getEventQueue(componentId);
			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent();
				switch (event.getEventId()) {
				case ListCourses:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeCourseList(coursesList)));
					break;
				case RegisterCourses:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, registerCourse(coursesList, event.getMessage())));
					break;
				case DeleteCourses:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, deleteCourse(coursesList, event.getMessage())));
					break;
				case RegisterEnrollment:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, processEnrollment(coursesList, event.getMessage())));
					break;
				case CheckEnrollment:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeEnrollmentList(coursesList, event.getMessage())));
					break;
				case QuitTheSystem:
					printLogEvent("Get", event);
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			}
		}
	}
	private static String makeEnrollmentList(CourseComponent coursesList, String studentId) {
	    StringBuilder enrollmentList = new StringBuilder("Enrollment List for Student ID: " + studentId + "\n");
	    boolean found = false;

	    for (String enrollment : coursesList.getEnrolledStudents()) {
	        if (enrollment.startsWith(studentId + "-")) {
	            String courseId = enrollment.split("-")[1]; // Extract Course ID
	            enrollmentList.append(" - Course ID: ").append(courseId).append("\n");
	            found = true;
	        }
	    }
	    if (!found) {
	        enrollmentList.append("No courses enrolled.\n");
	    }
	    return enrollmentList.toString();
	}
	private static String processEnrollment(CourseComponent coursesList, String message) {
        String[] inputs = message.split(" ");
        String studentId = inputs[0];
        String courseId = inputs[1];
        System.out.println("[DEBUG] Processing enrollment for Student ID: " + studentId + ", Course ID: " + courseId);
        if (!StudentMain.isStudentExists(studentId)) {
            String error = "Error: Student (ID: " + studentId + ") does not exist.";
            System.out.println("[DEBUG] " + error);
            return error;
        }
        if (!coursesList.isRegisteredCourse(courseId)) {
            String error = "Error: Course (ID: " + courseId + ") does not exist.";
            System.out.println("[DEBUG] " + error);
            return error;
        }
        Course course = coursesList.getCourse(courseId);
        for (String prerequisite : course.getPrerequisiteCoursesList()) {
            if (!StudentMain.hasCompletedCourse(studentId, prerequisite)) {
                String error = "Error: Prerequisite (ID: " + prerequisite + ") not completed.";
                System.out.println("[DEBUG] " + error);
                return error;
            }
        }
        if (coursesList.enrollStudent(studentId, courseId)) {
            String success = "Student (ID: " + studentId + ") successfully enrolled in Course (ID: " + courseId + ").";
            System.out.println("[DEBUG] " + success);
            return success;
        } else {
            String error = "Error: Student (ID: " + studentId + ") is already enrolled in Course (ID: " + courseId + ").";
            System.out.println("[DEBUG] " + error);
            return error;
        }
    }
	private static String deleteCourse(CourseComponent CoursesList, String message) {
	    String CourseId = message.split(" ")[0];
	    if (CoursesList.deletedCourse(CourseId)) {
	        return "Course (ID: " + CourseId + ") is successfully deleted.";
	    } else {
	        return "Course (ID: " + CourseId + ") not found or already deleted.";
	    }
	}    
	private static String registerCourse(CourseComponent coursesList, String message) {
		Course course = new Course(message);
		if (!coursesList.isRegisteredCourse(course.courseId)) {
			coursesList.vCourse.add(course);
			return "This course is successfully added.";
		} else
			return "This course is already registered.";
	}
	private static String makeCourseList(CourseComponent coursesList) {
		String returnString = "";
		for (int j = 0; j < coursesList.vCourse.size(); j++) {
			returnString += coursesList.getCourseList().get(j).getString() + "\n";
		}
		return returnString;
	}
	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
