package in.co.sunrays.proj4.bean;

import java.util.Date;
/**
 * College JavaBean encapsulates College attributes
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
public class TimeTableBean extends BaseBean {
	private Long courseid;
	public Long getCourseid() {
		return courseid;
	}

	public void setCourseid(Long courseid) {
		this.courseid = courseid;
	}

	private String courseName;

	private long subjectid;
	private String Time;
	private Date Date;
	private String subjectName;
	private String semester;

	
	
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * @return the subjectid
	 */
	public long getSubjectid() {
		return subjectid;
	}

	/**
	 * @param subjectid the subjectid to set
	 */
	public void setSubjectid(long subjectid) {
		this.subjectid = subjectid;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return Time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		Time = time;
	}

	/**
	 * @return the dOE
	 */


	/**
	 * @return the subjectName
	 */
	public String getSubjectName() {
		return subjectName;
	}

	public Date getDate() {
		return Date;
	}

	public void setDate(Date date) {
		Date = date;
	}

	/**
	 * @param subjectName the subjectName to set
	 */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	/**
	 * @return the semester
	 */
	public String getSemester() {
		return semester;
	}

	/**
	 * @param semester the semester to set
	 */
	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getKey() {
		// TODO Auto-generated method stub
		 return id + "";
	}

	public String getValue() {
		// TODO Auto-generated method stub
		return subjectName;
	}

}
