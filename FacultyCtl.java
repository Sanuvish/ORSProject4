package in.co.sunrays.proj4.controller;

import in.co.sunrays.proj4.bean.*;

import in.co.sunrays.proj4.exception.*;
import in.co.sunrays.proj4.model.FacultyModel;
import in.co.sunrays.proj4.model.MarksheetModel;
import in.co.sunrays.proj4.model.StudentModel;
import in.co.sunrays.proj4.model.UserModel;
import in.co.sunrays.proj4.util.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * FacultyCtl functionality Controller. Performs operation for add, update,
 * delete and get FacultyCtl
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "FacultyCtl", urlPatterns = { "/ctl/FacultyCtl" })
public class FacultyCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(FacultyCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("FacultyCtl Method validate Started");

		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("FirstName"))) {
			request.setAttribute("FirstName", PropertyReader.getValue("error.require", "FirstName"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("LastName"))) {
			request.setAttribute("LastName", PropertyReader.getValue("error.require", "LastName"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("Email"))) {
			request.setAttribute("Email", PropertyReader.getValue("error.require", "Email"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("Address"))) {
			request.setAttribute("Address", PropertyReader.getValue("error.require", "Address"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("Dob"))) {
			request.setAttribute("Dob", PropertyReader.getValue("error.require", "Dob"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "gender"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("MobileNo"))) {
			request.setAttribute("MobileNo", PropertyReader.getValue("error.require", "MobileNo"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("qualification"))) {
			request.setAttribute("qualification", PropertyReader.getValue("error.require", "qualification"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("experience"))) {
			request.setAttribute("experience", PropertyReader.getValue("error.require", "experience"));
			pass = false;
		}
		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("FacultyCtl Method populatebean Started");

		FacultyBean bean = new FacultyBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFirstName(DataUtility.getString(request.getParameter("FirstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("LastName")));
		bean.setEmail(DataUtility.getString(request.getParameter("Email")));
		bean.setAddress(DataUtility.getString(request.getParameter("Address")));
		bean.setDob(DataUtility.getDate(request.getParameter("Dob")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("MobileNo")));
		bean.setQualification(DataUtility.getString(request.getParameter("qualification")));
		bean.setExperience(DataUtility.getString(request.getParameter("experience")));
		populateDTO(bean, request);

		System.out.println("Population done");

		log.debug("FacultyCtl Method populatebean Ended");

		return bean;
	}

	/**
	 * Contains Display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("FacultyCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		FacultyModel model = new FacultyModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			FacultyBean bean;
			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("faculltCl Method doGet Ended");
	}

	/**
	 * Contains Submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("FacultyCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		FacultyModel model = new FacultyModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			FacultyBean bean = (FacultyBean) populateBean(request);

			try {

				if (id > 0) {

					model.update(bean);
					ServletUtility.setSuccessMessage("Data is successfully updated", request);

				} else {
					long pk = model.add(bean);
					ServletUtility.setSuccessMessage("Data is successfully saved", request);
					bean.setId(pk);
				}
				// ServletUtility.setBean(bean, request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setErrorMessage("Faculty name is already exits", request);
				ServletUtility.forward(getView(), request, response);
				e.printStackTrace();
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			FacultyBean bean = (FacultyBean) populateBean(request);
			System.out.println("in try");
			try {
				model.delete(bean);
				ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
				System.out.println("in try");
				return;
			} catch (ApplicationException e) {
				System.out.println("in catch");
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);

		log.debug("FacultyCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.FACULTY_VIEW;
	}

}
