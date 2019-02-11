package in.co.sunrays.proj4.model;

import in.co.sunrays.proj4.bean.*;

import in.co.sunrays.proj4.exception.ApplicationException;
import in.co.sunrays.proj4.exception.DatabaseException;
import in.co.sunrays.proj4.exception.DuplicateRecordException;

import in.co.sunrays.proj4.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * JDBC Implementation of Marksheet Model
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */
/**
 *
 *
 */
public class TimeTableModel {

	Logger log = Logger.getLogger(TimeTableModel.class);

	public Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			System.out.println("Connection Succesfully Establish");

			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from time_table");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {
			log.error(e);
			throw new DatabaseException("Exception in Marksheet getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}

	/**
	 * Adds a TimeTable
	 * 
	 * @param bean
	 * @throws Exception
	 * 
	 */

	public long add(TimeTableBean bean) throws Exception {
		log.debug("Model add Started");
		Connection conn = null;

		// get College Name
	
		CourseModel comodel=new CourseModel();
		CourseBean cbean=comodel.findByPK(bean.getCourseid());
		bean.setCourseName(cbean.getName());
		
	//get subject name
		
		
		SubjectModel cModel = new SubjectModel();
		SubjectBean Sudentbean = cModel.findByPK(bean.getSubjectid());
		bean.setSubjectName(Sudentbean.getsName());
		bean.setSubjectid(Sudentbean.getId());
		
		int pk = 0;
		TimeTableBean duplicateName = findByPK(bean.getId());
		if (duplicateName != null) {
			throw new DuplicateRecordException("id already exists");
		}

		try { 
		conn = JDBCDataSource.getConnection();
		pk = nextPK();
		
		// Get auto-generated next primary key
		System.out.println(pk + " in ModelJDBC");
		conn.setAutoCommit(false); // Begin transaction
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO time_table VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		pstmt.setLong(1, pk);
		pstmt.setLong(2, bean.getCourseid());
		pstmt.setString(3, bean.getCourseName());
		pstmt.setLong(4, bean.getSubjectid());
		pstmt.setString(5, bean.getSubjectName());
		pstmt.setString(6, bean.getSemester());
		pstmt.setString(7, bean.getTime());
		pstmt.setDate(8, new java.sql.Date(bean.getDate().getTime()));
		pstmt.setString(9, bean.getCreatedBy());
		pstmt.setString(10, bean.getModifiedBy());
		pstmt.setTimestamp(11, bean.getCreatedDatetime());
		pstmt.setTimestamp(12, bean.getModifiedDatetime());
		
		
		pstmt.executeUpdate();
		conn.commit(); // End transaction
		pstmt.close();
		
		  } catch (Exception e) 
		{ log.error("Database Exception..", e);
		  
		  try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace();
		  throw new ApplicationException("Exception : add rollback exception "
		  + ex.getMessage()); } throw new ApplicationException(
		  "Exception : Exception in add time table"); } finally {
		  JDBCDataSource.closeConnection(conn); }
		
		log.debug("Model add End");
		return pk;
	}

	/**
	 * Deletes a TimeTable
	 * 
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(TimeTableBean bean) throws ApplicationException {

		log.debug("Model delete Started");

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM time_table WHERE ID=?");
			pstmt.setLong(1, bean.getId());
			System.out.println("Deleted MarkSheet");
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {
			log.error(e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				log.error(ex);
				throw new ApplicationException("Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in delete marksheet");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model delete End");
	}

	/**
	
	 * findbypk a TimeTable
	 * 
	 * @param bean
	 * @throws DatabaseException
	 */

	public TimeTableBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");

		StringBuffer sql = new StringBuffer("SELECT * FROM time_table WHERE ID=?");
		TimeTableBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseid(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectid(rs.getLong(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setTime(rs.getString(7));
				bean.setDate(rs.getDate(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));

			}
			rs.close();
		} catch (Exception e) {
			log.error(e);
			throw new ApplicationException("Exception in getting marksheet by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;
	}

	/**
	 * Updates a TimeTable
	 * 
	 * @param bean
	 * @throws DatabaseException
	 */

	public void update(TimeTableBean bean) throws ApplicationException, DuplicateRecordException {

		log.debug("Model update Started");

		Connection conn = null;
		TimeTableBean beanExist = findByPK(bean.getId());

		// Check if updated Roll no already exist
		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Roll No is already exist");
		}

		// get Student Name
		SubjectModel sModel = new SubjectModel();
		CourseModel cmodel = new CourseModel();
		CourseBean cbean = cmodel.findByPK(bean.getCourseid());
		SubjectBean studentbean = sModel.findByPK(bean.getSubjectid());
		bean.setSubjectName(studentbean.getsName());
		bean.setCourseName(cbean.getName());
		try {
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE time_table SET CourseId=?,CourseName=?,SubjectId=?,SubjectName=?,semester=?,TIME=?,DATE=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
			
			pstmt.setLong(1, bean.getCourseid());
			pstmt.setString(2, bean.getCourseName());
			pstmt.setLong(3, bean.getSubjectid());
			pstmt.setString(4, bean.getSubjectName());
			pstmt.setString(5, bean.getSemester());
			pstmt.setString(6, bean.getTime());
			pstmt.setDate(7, new java.sql.Date(bean.getDate().getTime()));
			pstmt.setString(8, bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDatetime());
			pstmt.setTimestamp(11, bean.getModifiedDatetime());
			pstmt.setLong(12,bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			log.error(e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Update rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Marksheet ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model update End");

	}

	/**
	 * Searches TimeTable
	 * 
	 * @param bean
	 *            : Search Parameters
	 * @throws DatabaseException
	 */

	public List search(TimeTableBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Searches TimeTable with pagination
	 * 
	 * @return list : List of TimeTable
	 * @param bean
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * 
	 * @throws DatabaseException
	 */

	public List search(TimeTableBean bean, int pageNo, int pageSize) throws ApplicationException {

		log.debug("Model  search Started");

		StringBuffer sql = new StringBuffer("select * from time_table where true");

		if (bean != null) {
			System.out.println("service" + bean.getSubjectName());
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			
			if (bean.getSubjectid()> 0) {
				sql.append(" AND SubjectId like " + bean.getSubjectid());
			}
			if (bean.getCourseid()> 0) {
				sql.append(" AND CourseId like " + bean.getCourseid());
			}
			
			
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append(" AND CourseName like '" + bean.getCourseName() + "%'");
			}
			if (bean.getSubjectName() != null && bean.getSubjectName().length() > 0) {
				sql.append(" AND SubjectName like '" + bean.getSubjectName() + "%'");
			}

			if (bean.getSemester() != null && bean.getSemester().length() > 0) {
				sql.append(" AND semester like '" + bean.getSemester() + "%'");
			}
			if (bean.getDate() != null) {
				sql.append(" AND DATE like '" + DataUtility.getSearchDateString(bean.getDate()) + "%'");
			}

		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimeTableBean();
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseid(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectid(rs.getLong(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setTime(rs.getString(7));
				bean.setDate(rs.getDate(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error(e);
			throw new ApplicationException("Update rollback exception " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model  search End");
		return list;
	}

	/**
	 * Gets List of TimeTable
	 * 
	 * @return list : List of TimeTable
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * get List of TimeTable with pagination
	 * 
	 * @return list : List of TimeTable
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {

		log.debug("Model  list Started");

		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from time_table");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				TimeTableBean bean = new TimeTableBean();
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseid(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectid(rs.getLong(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setTime(rs.getString(7));
				bean.setDate(rs.getDate(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));

			}
			rs.close();
		} catch (Exception e) {
			log.error(e);
			throw new ApplicationException("Exception in getting list of Marksheet");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model  list End");
		return list;

	}/**
	 * findByName a TimeTable
	 * 
	 * @param bean
	 * @throws DatabaseException
	 */

	public TimeTableBean findByName(String name) throws ApplicationException {
		log.debug("Model findBy name Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM time_table WHERE NAME=?");
		TimeTableBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseid(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectid(rs.getLong(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setTime(rs.getString(7));
				bean.setDate(rs.getDate(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findBy name End");
		return bean;
	}

}
