package in.co.sunrays.proj4.bean;
/**
 * Subject JavaBean encapsulates Subject attributes
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
public class SubjectBean extends BaseBean{
	private String sName;
	private Long CourseId;
	private String CourseName;
	
	public Long getCourseId() {
		return CourseId;
	}

	public void setCourseId(Long courseId) {
		CourseId = courseId;
	}

	public String getCourseName() {
		return CourseName;
	}

	public void setCourseName(String courseName) {
		CourseName = courseName;
	}

	private String Discription;
	public String getsName() {
		return sName;
	}

	public void setsName(String sname) {
		sName = sname;
	}

	public String getDiscription() {
		return Discription;
	}

	public void setDiscription(String discription) {
		Discription = discription;
	}

	public String getKey() {
		// TODO Auto-generated method stub
		 return id + "";
	}

	public String getValue() {
		// TODO Auto-generated method stub
		return sName;
	}
	
}