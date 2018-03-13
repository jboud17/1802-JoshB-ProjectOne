package com.revature.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.revature.beans.User;
import com.revature.util.ConnFactory;

public class LoginServlet extends HttpServlet {
	
	static int userId;
	static String username;
	static String password;
	static String firstName;
	static String lastName;
	static String email;
	static int roleId;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		PrintWriter pw = resp.getWriter();
		resp.setContentType("text/html");
		req.getRequestDispatcher("login.html").include(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HttpSession session = req.getSession();
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		
		String formUsername = req.getParameter("username");
		String formPassword = req.getParameter("password");
		String user = "";
		
		try {
			// Create ConnFactory object
			ConnFactory cf = new ConnFactory();
			
			// Create connection
			Connection conn = cf.getConnection();
			
			// Create get user query
			String sqlGet = "SELECT * FROM ers_users WHERE u_username = ?";
			
			// Create PreparedStatement object
			PreparedStatement ps = conn.prepareStatement(sqlGet);
			
			// Set username value in statement
			ps.setString(1, formUsername);
			
			// Create ResultSet object
			ResultSet rs = ps.executeQuery();
			
			// Perform checks and redirects
			if(rs.next()) {
				
				userId = rs.getInt("u_id");
				username = rs.getString("u_username");
				password = rs.getString("u_password");
				roleId = rs.getInt("ur_id");
				
				if(formPassword.equals(password)) {
					if(roleId == 1) {
						
						firstName = rs.getString("u_firstname");
						lastName = rs.getString("u_lastname");
						email = rs.getString("u_email");
						
						// pass user values into string to be parsed
						user += userId + ":" + username + ":" + password + ":" + firstName + ":" + lastName + ":" + email + ":" + roleId; 
						// set session attribute and redirect to admin-dashboard
						session.setAttribute("username", username);
						resp.sendRedirect("admin-dashboard");
						
					} else {
						resp.sendRedirect("employee-dashboard");
					}
				} else {
					resp.sendRedirect("login");
				}
				
			} else {
				resp.sendRedirect("login");
//				req.setAttribute("error-msg", "Error! Wrong login info.");
//				req.getRequestDispatcher("/login.html").forward(req, resp);;
			}
			
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

}

