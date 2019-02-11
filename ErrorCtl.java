package in.co.sunrays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.sunrays.proj4.util.ServletUtility;

/**
 * Error handling refers to the anticipation, detection, and resolution of programming, application, and communications errors. Specialized programs, called error handlers, are available for some applications. ...
 * In programming, a development error is one that can be prevented. Such an error can occur in syntax or logic.
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */


@WebServlet(name="ErrorCtl",urlPatterns={"/ErrorCtl"})
public class ErrorCtl extends BaseCtl
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletUtility.forward(getView(), request, response);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.ERROR_VIEW;
	}
	

}
