package in.co.sunrays.proj4.bean;

import java.util.Date;
/**
 * Faculty JavaBean encapsulates Faculty attributes
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
public class FacultyBean extends BaseBean{
	private String firstName;
	private String lastName;
	private Date dob;
	private String mobileNo;
	private String email;
	private String Qualification;
	private String gender;
	private String address;
	
	private String experience;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQualification() {
		return Qualification;
	}
	public void setQualification(String Qualification) {
		this.Qualification = Qualification;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	 public String getKey() {
	        return id + "";
	 }
	public String getValue() {
		// TODO Auto-generated method stub
		return firstName+lastName;
	}
	   

}
