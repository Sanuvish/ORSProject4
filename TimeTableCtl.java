package in.co.sunrays.proj4.controller;

import in.co.sunrays.proj4.bean.*;

import in.co.sunrays.proj4.exception.*;
import in.co.sunrays.proj4.model.CourseModel;
import in.co.sunrays.proj4.model.RoleModel;
import in.co.sunrays.proj4.model.SubjectModel;
import in.co.sunrays.proj4.model.TimeTableModel;
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
 * TimeTableCtl functionality Controller. Performs operation for add, update and get
 * TimeTable
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "TimeTableCtl", urlPatterns = { "/ctl/TimeTableCtl" })
public class TimeTableCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TimeTableCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		SubjectModel model = new SubjectModel();
		try {
			
			List l = model.list();
			request.setAttribute("SubjectList", l);
		} catch (ApplicationException e) {
			e.printStackTrace();
			log.error(e);
		}
		CourseModel cmodel=new CourseModel();
		try{
			List lis=cmodel.list();
			request.setAttribute("CourseList", lis);
		}
			catch (ApplicationException e){
			e.printStackTrace();	
			}
		
	}	/**
	 * Contains validation logics
	 */

	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("TimeTableCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("Courseid"))) {
			request.setAttribute("Courseid", PropertyReader.getValue("error.require", "Course Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("semester"))) {
			request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("Date"))
				&& !DataValidator.isInteger(request.getParameter("time"))) {
			request.setAttribute("Date", PropertyReader.getValue("error.require", "Exam Date is Require"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("time"))
				&& !DataValidator.isInteger(request.getParameter("time"))) {
			request.setAttribute("time", PropertyReader.getValue("error.require", "Exam Time is Require"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("Courseid"))
				&& !DataValidator.isInteger(request.getParameter("Courseid"))) {
			request.setAttribute("Courseid", PropertyReader.getValue("error.require", "Course name  is Require"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("Subjectid"))
				&& !DataValidator.isInteger(request.getParameter("Subjectid"))) {
			request.setAttribute("Subjectid", PropertyReader.getValue("error.require", "Subject Name is Require"));
			pass = false;
		}
		log.debug("TimeTableCtl Method validate Ended");

		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("TimeTableCtl Method populatebean Started");

		TimeTableBean bean = new TimeTableBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setSubjectid(DataUtility.getLong(request.getParameter("Subjectid")));
		bean.setCourseid(DataUtility.getLong(request.getParameter("Courseid")));
		bean.setDate(DataUtility.getDate(request.getParameter("Date")));

		bean.setSemester(DataUtility.getString(request.getParameter("semester")));

		bean.setTime(DataUtility.getString(request.getParameter("time")));
		bean.setSubjectName(DataUtility.getString(request.getParameter("time")));

		populateDTO(bean, request);

		log.debug("TimeTableCtl Method populatebean Ended");

		return bean;
	}

	/**
	 * Contains DIsplay logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimeTableCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model

		TimeTableModel model = new TimeTableModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			TimeTableBean bean;
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
		log.debug("TimeTableCtl Method doGet Ended");
	}

	/**
	 * Contains Submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimeTableCtl Method doPost Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		TimeTableModel model = new TimeTableModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op)|| OP_UPDATE.equalsIgnoreCase(op)){
			TimeTableBean bean = (TimeTableBean) populateBean(request);

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
				ServletUtility.setErrorMessage("time table name is already exits", request);
				ServletUtility.forward(getView(), request, response);
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			TimeTableBean bean = (TimeTableBean) populateBean(request);
			System.out.println("in try");
			try {
				model.delete(bean);
				ServletUtility.redirect(ORSView.TIME_TABLE_LIST_CTL, request, response);
				System.out.println("in try");
				return;
			} catch (ApplicationException e) {
				System.out.println("in catch");
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TIME_TABLE_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);

		log.debug("SubjectCtl Method doPost Ended");
	}


	@Override
	protected String getView() {
		return ORSView.TIME_TABLE_VIEW;
	}

}
