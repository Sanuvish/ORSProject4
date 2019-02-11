
package in.co.sunrays.proj4.controller;

import in.co.sunrays.proj4.bean.*;

import in.co.sunrays.proj4.exception.ApplicationException;

import in.co.sunrays.proj4.model.FacultyModel;
import in.co.sunrays.proj4.model.RoleModel;
import in.co.sunrays.proj4.util.*;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
/**
 * Faculty List functionality Controller. Performs operation for list, search
 * and delete operations of Marksheet
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */

/**
 * Servlet implementation class MarksheetlistCtl
 */
@WebServlet(name = "FacultyListCtl", urlPatterns = { "/ctl/FacultyListCtl" })
public class FacultyListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(FacultyListCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		FacultyBean bean = new FacultyBean();
		bean.setId(DataUtility.getLong(request.getParameter("FacultyId")));
		bean.setFirstName(DataUtility.getString(request.getParameter("FirstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("LastName")));
		bean.setEmail(DataUtility.getString(request.getParameter("Email")));
		return bean;
	}

	/**
	 * Contains Display logics
	 */
	 protected void preload(HttpServletRequest request) {
	        log.debug("TimeTableListCtl preload start");
	        FacultyModel  Model1 = new FacultyModel();
	        try {
	            List FacultyList = Model1.list();
	            request.setAttribute("FacultyList", FacultyList);
	        } catch (ApplicationException e) {
	            e.printStackTrace();
	        }
	 }
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list = null;
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        FacultyBean bean = (FacultyBean) populateBean(request);
        String op = DataUtility.getString(request.getParameter("operation"));
       
        String[] ids = request.getParameterValues("ids");
        FacultyModel model = new FacultyModel();
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
        log.debug("FacultyListCtl doPOst End");
    }
	/**
	 * Contains submit logics
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("FacultyListCtl doPost Start");

		List list = null;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
        FacultyBean bean = (FacultyBean) populateBean(request);
        String op = DataUtility.getString(request.getParameter("operation"));
      
        String[] ids = request.getParameterValues("ids");
        FacultyModel model = new FacultyModel();
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
                ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                	FacultyBean deletebean = new FacultyBean();
                    
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getInt(id));
                        model.delete(deletebean);
                        
                        ServletUtility.setSuccessMessage("Data Successfully Deleted ", request);
                    
                    }
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
                return;
            }else if (OP_BACK.equalsIgnoreCase(op)) {
                list = model.search(bean, pageNo, pageSize);
                ServletUtility.setList(list, request);
                ServletUtility.setPageNo(pageNo, request);
                ServletUtility.setPageSize(pageSize, request);
                ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);

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
        log.debug("FacultyListCtl doGet End");
    }

	@Override
	protected String getView() {
		return ORSView.FACULTY_LIST_VIEW;
	}
}
