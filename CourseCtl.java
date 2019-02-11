package in.co.sunrays.proj4.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import in.co.sunrays.proj4.bean.*;

import in.co.sunrays.proj4.exception.*;
import in.co.sunrays.proj4.model.CourseModel;
import in.co.sunrays.proj4.model.UserModel;
import in.co.sunrays.proj4.util.*;

import java.io.IOException;
/**
 * Course functionality Controller. Performs operation for add, update, delete
 * and get Course
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "CourseCtl", urlPatterns = { "/ctl/CourseCtl" })

public class CourseCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CourseCtl.class);

	protected boolean validate(HttpServletRequest request) {
		log.debug("Course method Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("Duration"))) {
			request.setAttribute("Duration", PropertyReader.getValue("error.require", "Duration"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("Discription"))) {
			request.setAttribute("Discription", PropertyReader.getValue("error.require", "Discription"));
			pass = false;
		}

		log.debug("CourseCtl Method validate Ended");

		return pass;
	}

	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("Course Method populatebean Started");

		CourseBean bean = new CourseBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDuration(DataUtility.getString(request.getParameter("Duration")));
		bean.setDescription(DataUtility.getString(request.getParameter("Discription")));

		populateDTO(bean, request);

		log.debug("Course Method populatebean Ended");

		return bean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("RoleCtl Method doGet Started");

		System.out.println("In do get");

		String op = DataUtility.getString(request.getParameter("operation"));

		// get model
		CourseModel model = new CourseModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			CourseBean bean;
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
		log.debug("RoleCtl Method doGetEnded");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CourseCtl Method doGet Started");

		System.out.println("In do get");

		String op = DataUtility.getString(request.getParameter("operation"));

		// get model
		CourseModel model = new CourseModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)|| OP_UPDATE.equalsIgnoreCase(op)){

			CourseBean bean = (CourseBean) populateBean(request);

			try {
				if (id > 0) {
					model.update(bean);
					ServletUtility.setSuccessMessage("Data is successfully updated", request);
				} else {
					long pk = model.add(bean);
					ServletUtility.setSuccessMessage("Data is successfully", request);
					bean.setId(pk);
				}

			//	ServletUtility.setBean(bean, request);
				

			} catch (ApplicationException e) {
				log.error(e);
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setErrorMessage("Course name is already exits", request);
				ServletUtility.forward(getView(), request, response);
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			CourseBean bean = (CourseBean) populateBean(request);
			try {
				model.delete(bean);
				ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
			return;

		}

		ServletUtility.forward(getView(), request, response);

		log.debug("RoleCtl Method doPOst Ended");
	}

	@Override
	protected String getView() {
		return ORSView.COURSE_VIEW;
	}

}
