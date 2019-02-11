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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
/**
 * Course List functionality Controller. Performs operation for list, search
 * and delete operations of College
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "CourseListCtl", urlPatterns = { "/ctl/CourseListCtl" })
public class CourseListCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(CourseListCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		CourseBean bean = new CourseBean();
		bean.setId(DataUtility.getLong(request.getParameter("CourseId")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		
		return bean;
	}

	/**
	 * Contains Display logics
	 */
	protected void preload(HttpServletRequest request) {
		log.debug("TimeTableListCtl preload start");
		CourseModel Model1 = new CourseModel();
		try {
			List CourseList = Model1.list();
			request.setAttribute("CourseList", CourseList);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		CourseBean bean = (CourseBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");
		CourseModel model = new CourseModel();
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
		CourseBean bean = (CourseBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");
		CourseModel model = new CourseModel();
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
				ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					CourseBean deletebean = new CourseBean();

					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean);

						ServletUtility.setSuccessMessage("Data Successfully Deleted ", request);

					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
				return;
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				list = model.search(bean, pageNo, pageSize);
				ServletUtility.setList(list, request);
				ServletUtility.setPageNo(pageNo, request);
				ServletUtility.setPageSize(pageSize, request);
				ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);

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
		return ORSView.COURSE_LIST_VIEW;
	}

}
