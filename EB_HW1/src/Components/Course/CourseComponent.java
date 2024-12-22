/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in Myungji University
 */
package Components.Course;
import java.io.*;
import java.util.*;
public class CourseComponent { 
    protected ArrayList<Course> vCourse; // 과목 목록
    private List<String> enrolledStudents = new ArrayList<>(); // 수강 신청 데이터 저장
    public CourseComponent(String sCourseFileName) throws FileNotFoundException, IOException { 	
        BufferedReader bufferedReader  = new BufferedReader(new FileReader(sCourseFileName));       
        this.vCourse  = new ArrayList<Course>();
        while (bufferedReader.ready()) {
            String courseInfo = bufferedReader.readLine();
            if(!courseInfo.equals("")) this.vCourse.add(new Course(courseInfo));
        }    
        bufferedReader.close();
    }
    public ArrayList<Course> getCourseList() {
        return this.vCourse;
    }
    public boolean isRegisteredCourse(String courseId) {
        for (Course c : this.vCourse) {
            if (c.match(courseId)) return true;
        }
        return false;
    }
    public boolean deletedCourse(String courseId) {
        for (int i = 0; i < vCourse.size(); i++) {
            Course course = vCourse.get(i);
            if (course.match(courseId)) {
                vCourse.remove(i); 
                return true; 
            }
        }
        return false; 
    }
    public Course getCourse(String courseId) {
        for (Course c : this.vCourse) {
            if (c.match(courseId)) return c;
        }
        return null;
    }
    public boolean enrollStudent(String studentId, String courseId) {
        String enrollmentRecord = studentId + "-" + courseId;
        if (!enrolledStudents.contains(enrollmentRecord)) {
            enrolledStudents.add(enrollmentRecord);
            return true; 
        }
        return false; 
    }
    public List<String> getEnrolledStudents() {
        return new ArrayList<>(enrolledStudents); 
    }
        public String getEnrollmentStatus(String studentId) {
        	StringBuilder status = new StringBuilder("Enrollment Status for Student ID: " + studentId + "\n");
        	boolean found = false;
        	for (String enrollment : enrolledStudents) {
        		if (enrollment.startsWith(studentId + "-")) {
        			status.append(enrollment.split("-")[1]).append("\n");
        			found = true;
        }
    }
        	if (!found) { 
        		status.append("No courses enrolled."); 
        	} 
        	return status.toString(); }
    }