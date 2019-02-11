package in.co.sunrays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import in.co.sunrays.proj4.bean.BaseBean;
import in.co.sunrays.proj4.bean.TimeTableBean;
import in.co.sunrays.proj4.exception.ApplicationException;
import in.co.sunrays.proj4.model.CourseModel;
import in.co.sunrays.proj4.model.SubjectModel;
import in.co.sunrays.proj4.model.TimeTableModel;
import in.co.sunrays.proj4.util.DataUtility;
import in.co.sunrays.proj4.util.PropertyReader;
import in.co.sunrays.proj4.util.ServletUtility;


/**
 * Time Table List functionality Controller. Performs operation for list, search
 * and delete operations of User
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "TimeTableListCtl", urlPatterns = { "/ctl/TimeTableListCtl" })
public class TimeTableListCtl extends BaseCtl {
    private static Logger log = Logger.getLogger(TimeTableListCtl.class);

    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("TimeTableListCtl preload start");
        SubjectModel subjectModel = new SubjectModel();
        try {
            List subjectList = subjectModel.list();
            request.setAttribute("subjectList", subjectList);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        
        CourseModel courseModel = new CourseModel();
        try {
            List courseList = courseModel.list();
            request.setAttribute("courseList", courseList);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        log.debug("TimeTableListCtl preload End");
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        TimeTableBean bean = new TimeTableBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setSubjectid(DataUtility.getLong(request.getParameter("Subjectid")));
        bean.setCourseid(DataUtility.getLong(request.getParameter("Courseid")));
        bean.setDate(DataUtility.getDate(request.getParameter("Date")));
           
        populateDTO(bean, request);
        return bean;
    }

    /**
     * Contains Display logics
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("TimeTableListCtl doGet Start");
        List list = null;
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        TimeTableBean bean = (TimeTableBean) populateBean(request);
        String op = DataUtility.getString(request.getParameter("operation"));
       
        String[] ids = request.getParameterValues("ids");
        TimeTableModel model = new TimeTableModel();
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
        log.debug("TimeTableListCtl doPOst End");
    }

    /**
     * Contains Submit logics
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("TimeTableListCtl doPost Start");
        List list = null;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
        TimeTableBean bean = (TimeTableBean) populateBean(request);
        String op = DataUtility.getString(request.getParameter("operation"));
      
        String[] ids = request.getParameterValues("ids");
        TimeTableModel model = new TimeTableModel();
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
                ServletUtility.redirect(ORSView.TIME_TABLE_CTL, request, response);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    TimeTableBean deletebean = new TimeTableBean();
                    
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getInt(id));
                        model.delete(deletebean);
                        
                        ServletUtility.setSuccessMessage("Data Successfully Deleted ", request);
                    
                    }
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.TIME_TABLE_LIST_CTL, request, response);
                return;
            }else if (OP_BACK.equalsIgnoreCase(op)) {
                list = model.search(bean, pageNo, pageSize);
                ServletUtility.setList(list, request);
                ServletUtility.setPageNo(pageNo, request);
                ServletUtility.setPageSize(pageSize, request);
                ServletUtility.redirect(ORSView.TIME_TABLE_LIST_CTL, request, response);

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
        return ORSView.TIME_TABLE_LIST_VIEW;
    }

}

