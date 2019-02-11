package in.co.sunrays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.proj4.bean.*;

import in.co.sunrays.proj4.exception.*;
import in.co.sunrays.proj4.model.CourseModel;
import in.co.sunrays.proj4.model.FacultyModel;
import in.co.sunrays.proj4.model.SubjectModel;
import in.co.sunrays.proj4.model.UserModel;
import in.co.sunrays.proj4.util.*;
/**
 * SubjectCtl functionality Controller. Performs operation for add, update and get
 * Subject
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@WebServlet(urlPatterns = { "/ctl/SubjectCtl" })
public class SubjectCtl extends BaseCtl {


	private static Logger log = Logger.getLogger(SubjectCtl.class);
	/**
	 * Contains preload method logics
	 */
	protected void preload(HttpServletRequest request) {
		CourseModel model = new CourseModel();
		try {
			List l = model.list();
			request.setAttribute("CourseList", l);
		} catch (ApplicationException e) {
			log.error(e);
		}
	}
	/**
	 * Contains validation logics
	 */
	protected boolean validate(HttpServletRequest request) {

		log.debug("SubjectCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("CourseId"))) {
			request.setAttribute("CourseId", PropertyReader.getValue("error.require", "Course Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("Name"))) {
			request.setAttribute("Name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("Discription"))) {
			request.setAttribute("Discription", PropertyReader.getValue("error.require", "Discription"));
			pass = false;
		}

		log.debug("SubjectCtl Method validate Ended");

		return pass;
	}

	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("SubjectCtl Method populatebean Started");

		SubjectBean bean = new SubjectBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setsName(DataUtility.getString(request.getParameter("Name")));

		bean.setCourseId(DataUtility.getLong(request.getParameter("CourseId")));

		bean.setDiscription(DataUtility.getString(request.getParameter("Discription")));

		populateDTO(bean, request);

		log.debug("SubjectCtl Method populatebean Ended");

		return bean;
	}
	/**
	 * Contains Display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("MarksheetCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		SubjectModel model = new SubjectModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			SubjectBean bean;
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
		log.debug("SubjectCtl Method doGet Ended");
	}

	/**
	 * Contains Submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("SubjectCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		SubjectModel model = new SubjectModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)|| OP_UPDATE.equalsIgnoreCase(op)){

			SubjectBean bean = (SubjectBean) populateBean(request);
		
			try {
				
				if (id > 0) {
					model.update(bean);
					ServletUtility.setSuccessMessage("Data is successfully updated", request);
				} else {
					long pk = model.add(bean);
					ServletUtility.setSuccessMessage("Data is successfully saved", request);
					bean.setId(pk);
					
				}
				//ServletUtility.setBean(bean, request);
			
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setErrorMessage("Subject name is already exits", request);
				ServletUtility.forward(getView(), request, response);
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			SubjectBean bean = (SubjectBean) populateBean(request);
			System.out.println("in try");
			try {
				model.delete(bean);
				ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
				System.out.println("in try");
				return;
			} catch (ApplicationException e) {
				System.out.println("in catch");
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);

		log.debug("SubjectCtl Method doPost Ended");
	}



	@Override
	protected String getView() {
		return ORSView.SUBJECT_VIEW;
	}
}
