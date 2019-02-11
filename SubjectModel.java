package in.co.sunrays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import in.co.sunrays.proj4.bean.*;

import in.co.sunrays.proj4.exception.*;
import in.co.sunrays.proj4.util.JDBCDataSource;

/**
 * JDBC Implementation of subject Model
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */
/**
 * 
 *
 */
public class SubjectModel {
	private static Logger log = Logger.getLogger(SubjectModel.class);
	/**
	 * Find next PK of subject
	 * 
	 * @throws DatabaseExceptionO
	 */
	public Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM  st_subject");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}
	 /**
     * Add a subject
     * 
     * @param bean
     * @throws DatabaseException
     * 
     */
	public long add(SubjectBean bean) throws Exception , DuplicateRecordException{
		log.debug("Model add Started");
		Connection conn = null;
		int pk = 0;
	
		SubjectBean  duplicatafaculty = findByName(bean.getsName());
		// Check if updated Role already exist
		if (duplicatafaculty != null && duplicatafaculty.getId() != bean.getId()) {
			throw new DuplicateRecordException("subject already exists");
		}		
		CourseModel cModel = new CourseModel();
		CourseBean collegeBean = cModel.findByPK(bean.getCourseId());
		bean.setCourseName(collegeBean.getName());
		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			// Get auto-generated next primary key
			System.out.println(pk + " in ModelJDBC");
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO st_subject VALUES(?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setLong(2, bean.getCourseId());
			pstmt.setString(3, bean.getCourseName());
			pstmt.setString(4, bean.getsName());
			pstmt.setString(5, bean.getDiscription());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add Faculty");
			
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;
	}
	 /**
     * delete a Student
     * 
     * @param bean
     * @throws DatabaseException
     * 
     */
	public void delete(SubjectBean bean) throws ApplicationException {
		log.debug("Model delete Started");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_subject WHERE ID=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Role");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model delete Started");
	}
	 /**
     * findbyname a Student
     * 
     * @param bean
     * @throws DatabaseException
     * 
     */
	public SubjectBean findByName(String name) throws ApplicationException {
		log.debug("Model findBy name Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM st_subject WHERE SUB_NAME=?");
		SubjectBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setsName(rs.getString(4));
				bean.setDiscription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting pk by name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findBy name End");
		return bean;
	}
	 

	/**
	 * @param pk
	 * @return
	 * @throws ApplicationException
	 */
	public SubjectBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM st_subject WHERE ID=?");
		SubjectBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setsName(rs.getString(4));
				bean.setDiscription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;
	}
	 /**
     * update a Student
     * 
     * @param bean
     * @throws DatabaseException
     * 
     */
	/**
	 * @param bean
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public void update(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;
		CourseModel cModel = new CourseModel();
		CourseBean collegeBean = cModel.findByPK(bean.getCourseId());
		bean.setCourseName(collegeBean.getName());
		SubjectBean  duplicatafaculty = findByName(bean.getsName());
		// Check if updated Role already exist
		if (duplicatafaculty != null && duplicatafaculty.getId() != bean.getId()) {
			throw new DuplicateRecordException("subject already exits already exists");
		}		
		try {
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE st_subject SET  COURSE_ID=?,COURSE_NAME=?,SUB_NAME=?,DISCRIPTION=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");

			pstmt.setLong(1, bean.getCourseId());
			pstmt.setString(2, bean.getCourseName());
			pstmt.setString(3, bean.getsName());
			pstmt.setString(4, bean.getDiscription());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());
			pstmt.setLong(9, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating subject ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}

	/**
	 * Search Student
	 * 
	 * @param bean
	 *            : Search Parameters
	 * @throws DatabaseException
	 */

	public List search(SubjectBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search Student with pagination
	 * 
	 * @return list : List of Students
	 * @param bean
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * 
	 * @throws DatabaseException
	 */

	public List search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM st_subject WHERE 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND ID = " + bean.getId());
			}
			if (bean.getCourseId()> 0) {
				sql.append(" AND COURSE_ID like " + bean.getCourseId());
			}

			if (bean.getsName() != null && bean.getsName().length() > 0) {
				sql.append(" AND SUB_NAME like '" + bean.getsName() + "%'");
			}
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append(" AND COURSE_NAME like '" + bean.getCourseName() + "%'");
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
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setsName(rs.getString(4));
				bean.setDiscription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model search End");
		return list;
	}

	/**
	 * Get List of Student
	 * 
	 * @return list : List of Student
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Student with pagination
	 * 
	 * @return list : List of Student
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_subject");
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
				SubjectBean bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setsName(rs.getString(4));
				bean.setDiscription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of subject");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model list End");
		return list;

	}
}
