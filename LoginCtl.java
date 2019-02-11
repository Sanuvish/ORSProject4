package in.co.sunrays.proj4.controller;

import in.co.sunrays.proj4.bean.BaseBean;
import in.co.sunrays.proj4.bean.RoleBean;
import in.co.sunrays.proj4.bean.UserBean;
import in.co.sunrays.proj4.exception.ApplicationException;
import in.co.sunrays.proj4.model.RoleModel;
import in.co.sunrays.proj4.model.UserModel;
import in.co.sunrays.proj4.util.DataUtility;
import in.co.sunrays.proj4.util.DataValidator;
import in.co.sunrays.proj4.util.PropertyReader;
import in.co.sunrays.proj4.util.ServletUtility;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * Login functionality Controller. Performs operation for Login
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */


@WebServlet(name="LoginCtl", urlPatterns={"/LoginCtl"})
public class LoginCtl extends BaseCtl
{
  private static final long serialVersionUID = 1L;
  public static final String OP_REGISTER = "Register";
  public static final String OP_SIGN_IN = "SignIn";
  public static final String OP_SIGN_UP = "SignUp";
  public static final String OP_LOG_OUT = "logout";
  private static Logger log = Logger.getLogger(LoginCtl.class);
  
  public LoginCtl() {}
  
  protected boolean validate(HttpServletRequest request) {
    log.debug("LoginCtl Method validate Started");
    System.out.println("validate login");
    boolean pass = true;
    
    String op = request.getParameter("operation");
    if (("SignUp".equals(op)) || ("logout".equals(op))) {
    	
      return pass;
    }
    
    String login = request.getParameter("login");
    
    if (DataValidator.isNull(login)) {
      request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
      pass = false;
    } else if (!DataValidator.isEmail(login)) {
      request.setAttribute("login", PropertyReader.getValue("error.email", "Login "));
      pass = false;
    }
    if (DataValidator.isNull(request.getParameter("password"))) {
      request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
      pass = false;
    }
    
    log.debug("LoginCtl Method validate Ended");
    
    return pass;
  }
  

  protected BaseBean populateBean(HttpServletRequest request)
 
  {
	  System.out.println("papulate bean login");
    log.debug("LoginCtl Method populatebean Started");
    
    UserBean bean = new UserBean();
    
    bean.setId(DataUtility.getLong(request.getParameter("id")));
    bean.setLogin(DataUtility.getString(request.getParameter("login")));
    bean.setPassword(DataUtility.getString(request.getParameter("password")));
    
    log.debug("LoginCtl Method populatebean Ended");

    return bean;
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    HttpSession session = request.getSession(true);
    log.debug(" Method doGet Started");
    
    String op = DataUtility.getString(request.getParameter("operation"));
    

    UserModel model = new UserModel();
    RoleModel role = new RoleModel();
    if ("logout".equals(op))
    {
      session = request.getSession();
      
      session.invalidate();
      ServletUtility.setSuccessMessage("Logout Scuccessfully ", request);
    
      }
    if (request.getSession().getAttribute("user") != null) {
      ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
    }
    
    ServletUtility.forward(getView(), request, response);
  }
  




  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
	  System.out.println("DOpost loginctl ");
    HttpSession session = request.getSession(true);
    log.debug(" Method doPost Started");
    
    String op = DataUtility.getString(request.getParameter("operation"));
    

    UserModel model = new UserModel();
    RoleModel role = new RoleModel();
    
    long id = DataUtility.getLong(request.getParameter("id"));
    System.out.println("dateutility loginctl ");
    if ("SignIn".equalsIgnoreCase(op))
    {
      UserBean bean = (UserBean)populateBean(request);
      System.out.println("papulatebean loginctl ");
      try
      {
        bean = model.authenticate(bean.getLogin(), bean.getPassword());
        System.out.println("authrratication loginctl ");
        if (bean != null) {
          session.setAttribute("user", bean);
          long rollId = bean.getRoleId();
          System.out.println("get role id loginctl ");
          RoleBean rolebean = role.findByPK(rollId);
          
          if (rolebean != null) {
            session.setAttribute("role", rolebean.getName());
          }
          String str = request.getParameter("uri");
          if (("null".equals(str)) || (str.trim().length() < 0)) {
            ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
          } else {
            ServletUtility.redirect(str, request, response);
            System.out.println("uri");
          }
          return;
        }
        
        bean = (UserBean)populateBean(request);
        System.out.println("papulatebean user loginctl ");
        ServletUtility.setBean(bean, request);
        ServletUtility.setErrorMessage("Invalid LoginId And Password", request);
      }
      catch (ApplicationException e)
      {
        log.error(e);
        ServletUtility.handleException(e, request, response);
        return;
      }
    }
    if ("SignUp".equalsIgnoreCase(op)) {
      ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
      return;
    }
    ServletUtility.forward(getView(), request, response);
    
    log.debug("UserCtl Method doPost Ended");
  }
  
  protected String getView()
  {
	  System.out.println("view loginctl");
    return ORSView.LOGIN_VIEW;
  }
}