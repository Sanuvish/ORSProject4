package in.co.sunrays.proj4.controller;

import in.co.sunrays.proj4.bean.*;

import in.co.sunrays.proj4.exception.*;
import in.co.sunrays.proj4.model.CourseModel;
import in.co.sunrays.proj4.model.MarksheetModel;
import in.co.sunrays.proj4.model.RoleModel;
import in.co.sunrays.proj4.model.StudentModel;
import in.co.sunrays.proj4.model.UserModel;
import in.co.sunrays.proj4.util.*;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Marksheet List functionality Controller. Performs operation for list, search
 * and delete operations of Marksheet
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */

/**
 * Servlet implementation class MarksheetlistCtl
 */
@WebServlet(name = "MarksheetListCtl", urlPatterns = { "/ctl/MarksheetListCtl" })
public class MarksheetListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(MarksheetListCtl.class);

	/**
	 * Contains search logics
	 */

	protected void preload(HttpServletRequest request) {
		log.debug("TimeTableListCtl preload start");
		StudentModel Model1 = new StudentModel();
		try {
			List StudentList = Model1.list();
			request.setAttribute("StudentList", StudentList);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		MarksheetBean bean = new MarksheetBean();
		bean.setStudentId(DataUtility.getLong(request.getParameter("StudentId")));
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

		bean.setName(DataUtility.getString(request.getParameter("NAME")));

		return bean;
	}

	/**
	 * Contains Display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		MarksheetBean bean = (MarksheetBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");
		MarksheetModel model = new MarksheetModel();
		try {
			list = model.search(bean, pageNo, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				System.out.println("No found");
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("SubjectListCtl doPOst End");
	}

	/**
	 * Contains submit logics
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("SubjectListCtl doPost Start");

		List list = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		MarksheetBean bean = (MarksheetBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");
		MarksheetModel model = new MarksheetModel();
		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.MARKSHEET_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					MarksheetBean deletebean = new MarksheetBean();

					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean);

						ServletUtility.setSuccessMessage("Data Successfully Deleted ", request);

					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, request, response);
				return;
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				list = model.search(bean, pageNo, pageSize);
				ServletUtility.setList(list, request);
				ServletUtility.setPageNo(pageNo, request);
				ServletUtility.setPageSize(pageSize, request);
				ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, request, response);

				return;
			}

			list = model.search(bean, pageNo, pageSize);
			ServletUtility.setList(list, request);
			ServletUtility.setBean(bean, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("TimeTableListCtl doGet End");
	}

	@Override
	protected String getView() {
		return ORSView.MARKSHEET_LIST_VIEW;
	}

}
