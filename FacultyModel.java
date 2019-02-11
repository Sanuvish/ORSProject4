package in.co.sunrays.proj4.model;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import in.co.sunrays.proj4.exception.*;
import org.apache.log4j.Logger;

import in.co.sunrays.proj4.bean.*;

import in.co.sunrays.proj4.exception.DatabaseException;

import in.co.sunrays.proj4.util.*;

/**
 * JDBC Implementation of FacultyModel
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */
public class FacultyModel {
	private static Logger log = Logger.getLogger(StudentModel.class);

	/**
	 * Find next PK of Student
	 * 
	 * @throws DatabaseException
	 */
	public Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM st_faculty");
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
	 * add a Faculty
	 * 
	 * @param bean
	 * @throws DatabaseException
	 * 
	 */
	public long add(FacultyBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		Connection conn = null;
		int pk = 0;
		FacultyBean duplicatafaculty = findByName(bean.getFirstName());
		// Check if updated faculty already exist
		if (duplicatafaculty != null && duplicatafaculty.getId() != bean.getId()) {
			throw new DuplicateRecordException("Faculty already exists");
		}
		FacultyBean existbean = findByEmailId(bean.getEmail());

		if (existbean != null) {
			throw new DuplicateRecordException("Faculty Id already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			// Get auto-generated next primary key
			System.out.println(pk + " in ModelJDBC");
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn
					.prepareStatement("INSERT INTO ST_FACULTY VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(5, bean.getMobileNo());
			pstmt.setString(6, bean.getEmail());
			pstmt.setString(7, bean.getQualification());
			pstmt.setString(8, bean.getGender());
			pstmt.setString(9, bean.getAddress());
			pstmt.setString(10, bean.getExperience());
			pstmt.setString(11, bean.getCreatedBy());
			pstmt.setString(12, bean.getModifiedBy());
			pstmt.setTimestamp(13, bean.getCreatedDatetime());
			pstmt.setTimestamp(14, bean.getModifiedDatetime());
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
	 * delete a Faculty
	 * 
	 * @param bean
	 * @throws DatabaseException
	 * 
	 */
	public void delete(FacultyBean bean) throws ApplicationException {
		log.debug("Model delete Started");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_FACULTY WHERE ID=?");
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
			throw new ApplicationException("Exception : Exception in delete faculty");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model delete Started");
	}

	/**
	 * find by Email a Faculty
	 * 
	 * @param bean
	 * @throws DatabaseException
	 * 
	 */

	public FacultyBean findByEmailId(String Email) throws ApplicationException {
		log.debug("Model findBy Email Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE EMAIL=?");
		FacultyBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, Email);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setMobileNo(rs.getString(5));
				bean.setEmail(rs.getString(6));
				bean.setQualification(rs.getString(7));
				bean.setGender(rs.getString(8));
				bean.setAddress(rs.getString(9));
				bean.setExperience(rs.getString(10));
				bean.setCreatedBy(rs.getString(11));
				bean.setModifiedBy(rs.getString(12));
				bean.setCreatedDatetime(rs.getTimestamp(13));
				bean.setModifiedDatetime(rs.getTimestamp(14));

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by Email");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findBy Email End");
		return bean;
	}

	/**
	 * findbypk a Faculty
	 * 
	 * @param bean
	 * @throws DatabaseException
	 * 
	 */
	public FacultyBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE ID=?");
		FacultyBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setMobileNo(rs.getString(5));
				bean.setEmail(rs.getString(6));
				bean.setQualification(rs.getString(7));
				bean.setGender(rs.getString(8));
				bean.setAddress(rs.getString(9));
				bean.setExperience(rs.getString(10));
				bean.setCreatedBy(rs.getString(11));
				bean.setModifiedBy(rs.getString(12));
				bean.setCreatedDatetime(rs.getTimestamp(13));
				bean.setModifiedDatetime(rs.getTimestamp(14));
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting faculty by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;
	}

	/**
	 * update a Faculty
	 * 
	 * @param bean
	 * @throws DatabaseException
	 * 
	 */
	public void update(FacultyBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;

		FacultyBean duplicatanamae = findByPK(bean.getId());
		FacultyBean duplicatafaculty = findByName(bean.getFirstName());
		// Check if updated faculty already exist
		if (duplicatafaculty != null && duplicatafaculty.getId() != bean.getId()) {
			throw new DuplicateRecordException("Faculty already exists");
		}
		// Check if updated email no already exist
		if (duplicatanamae != null && duplicatanamae.getEmail() != bean.getEmail()) {
			throw new DuplicateRecordException("Id is already exist");
		}
		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE ST_FACULTY SET FIRST_NAME=?,LAST_NAME=?,DOB=?,MOBILE_NO=?,EMAIL=?,QUALIFICATION=?,GENEDER=?,ADDRESS=?,EXPERIENCE=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");

			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(4, bean.getMobileNo());
			pstmt.setString(5, bean.getEmail());
			pstmt.setString(6, bean.getQualification());
			pstmt.setString(7, bean.getGender());
			pstmt.setString(8, bean.getAddress());
			pstmt.setString(9, bean.getExperience());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDatetime());
			pstmt.setTimestamp(13, bean.getModifiedDatetime());
			pstmt.setLong(14, bean.getId());
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
			throw new ApplicationException("Exception in updating faculty ");
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

	public List search(FacultyBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search faculty with pagination
	 * 
	 * @return list : List of faculty
	 * @param bean
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * 
	 * @throws DatabaseException
	 */

	public List search(FacultyBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append(" AND FIRST_NAME like '" + bean.getFirstName() + "%'");
			}
			if (bean.getLastName() != null && bean.getLastName().length() > 0) {
				sql.append(" AND LAST_NAME like '" + bean.getLastName() + "%'");
			}
			if (bean.getDob() != null && bean.getDob().getDate() > 0) {
				sql.append(" AND DOB = " + bean.getDob());
			}
			if (bean.getMobileNo() != null && bean.getMobileNo().length() > 0) {
				sql.append(" AND MOBILE_NO like '" + bean.getMobileNo() + "%'");
			}
			if (bean.getEmail() != null && bean.getEmail().length() > 0) {
				sql.append(" AND EMAIL like '" + bean.getEmail() + "%'");
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
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setMobileNo(rs.getString(5));
				bean.setEmail(rs.getString(6));
				bean.setQualification(rs.getString(7));
				bean.setGender(rs.getString(8));
				bean.setAddress(rs.getString(9));
				bean.setExperience(rs.getString(10));
				bean.setCreatedBy(rs.getString(11));
				bean.setModifiedBy(rs.getString(12));
				bean.setCreatedDatetime(rs.getTimestamp(13));
				bean.setModifiedDatetime(rs.getTimestamp(14));
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
	 * Get List of faculty with pagination
	 * 
	 * @return list : List of faculty
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from ST_FACULTY");
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
				FacultyBean bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setMobileNo(rs.getString(5));
				bean.setEmail(rs.getString(6));
				bean.setQualification(rs.getString(7));
				bean.setGender(rs.getString(8));
				bean.setAddress(rs.getString(9));
				bean.setExperience(rs.getString(10));
				bean.setCreatedBy(rs.getString(11));
				bean.setModifiedBy(rs.getString(12));
				bean.setCreatedDatetime(rs.getTimestamp(13));
				bean.setModifiedDatetime(rs.getTimestamp(14));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model list End");
		return list;

	}

	/**
	 * findByName a Faculty
	 * 
	 * @param bean
	 * @throws DatabaseException
	 * 
	 */

	public FacultyBean findByName(String name) throws ApplicationException {
		log.debug("Model findBy EmailId Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE FIRST_NAME=?");
		FacultyBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setMobileNo(rs.getString(5));
				bean.setEmail(rs.getString(6));
				bean.setQualification(rs.getString(7));
				bean.setGender(rs.getString(8));
				bean.setAddress(rs.getString(9));
				bean.setExperience(rs.getString(10));
				bean.setCreatedBy(rs.getString(11));
				bean.setModifiedBy(rs.getString(12));
				bean.setCreatedDatetime(rs.getTimestamp(13));

				bean.setModifiedDatetime(rs.getTimestamp(14));
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting faculty by emailId");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findBy EmailId End");
		return bean;
	}

}
