package com.revature.servlet;

import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.revature.util.ConnFactory;
import java.util.Date;

import java.util.ArrayList;

public class ReimbursementsServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
        resp.setContentType("text/html");
        HttpSession session = req.getSession();
		
		if(session!=null && session.getAttribute("username") != null){
			
			req.getRequestDispatcher("reimbursements.html").forward(req, resp);
			
		} else {
			resp.sendRedirect("login");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HttpSession session = req.getSession();
		resp.setContentType("text/html");
		
		// Done
		String statusType = (String) session.getAttribute("status");
		
		Integer rStatus = Integer.parseInt(req.getParameter("status"));
		Integer rId = Integer.parseInt(req.getParameter("reimbursementId"));
		
		try {
			
			// Create ConnFactory object
			ConnFactory cf = new ConnFactory();
			
			// Create connection
			Connection conn = cf.getConnection();
			
			// Create get user query
			String sqlUpdate = "UPDATE ers_reimbursements SET rt_status = ? WHERE r_id = ?";
			
			// Create PreparedStatement object
			PreparedStatement ps = conn.prepareStatement(sqlUpdate);
			
			// Set username value in statement
			ps.setInt(1, rStatus);
			ps.setInt(2, rId);
			
			// Execute update
			int rowsUpdated = ps.executeUpdate();
			if(rowsUpdated > 0) {
				// Update successful
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect("reimbursements");
	}

}
