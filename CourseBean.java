package in.co.sunrays.proj4.bean;

/**
 * Course bean JavaBean encapsulates Course attributes
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class CourseBean extends BaseBean {
	/**
	 * course name
	 */
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	/**
	 * course Description
	 */
	private String description;
	/**
	 * course duration
	 */
	private String duration;
	
	public String getKey() {
        return id + "";
    }

    public String getValue() {
        return name;
    }

}
