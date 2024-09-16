package com.app;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String userid = request.getParameter("userid");
        String password = request.getParameter("Password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "venkat");
            Statement st = conn.createStatement();
            String query = "SELECT * FROM user WHERE userid ='" + userid + "' AND password = '" + password + "'";
            ResultSet rs = st.executeQuery(query);

            out.println("<html><head>");
            out.println("<style>");
            out.println("body { display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; font-family: Arial, sans-serif; background-color: #f0f0f0; }");
            out.println(".message { text-align: center; padding: 20px; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); transition: transform 0.3s ease, opacity 0.3s ease; }");
            out.println(".success { background-color: #d4edda; color: #155724; transform: scale(1.1); }");
            out.println(".error { background-color: #f8d7da; color: #721c24; transform: scale(1.1); }");
            out.println(".logout-btn { margin-top: 20px; padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer; }");
            out.println("</style>");
            out.println("</head><body>");

            if (rs.next()) {
                // Successful login
                HttpSession session = request.getSession();
                session.setAttribute("userid", userid); // Store user info in session

                out.println("<div class='message success'>");
                out.println("<h1>" + userid + ": Welcome to the Home page</h1>");
                out.println("<h2>Login Successfully</h2>");
                // Add logout button
                out.println("<form action='LogoutServlet' method='post'>");
                out.println("<button type='submit' class='logout-btn'>Logout</button>");
                out.println("</form>");
                out.println("</div>");
            } else {
                // Failed login
                out.println("<div class='message error'>");
                out.println("<h1>" + userid + ": Please enter correct userid and password</h1>");
                out.println("<h2>Login Failed</h2>");
                out.println("</div>");
            }

            out.println("</body></html>");

            rs.close();
            st.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            out.println("<html><head><style>");
            out.println("body { display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; font-family: Arial, sans-serif; background-color: #f0f0f0; }");
            out.println(".message.error { background-color: #f8d7da; color: #721c24; padding: 20px; text-align: center; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); transform: scale(1.1); transition: transform 0.3s ease, opacity 0.3s ease; }");
            out.println("</style></head><body>");
            out.println("<div class='message error'><h2>Login Failed due to server error</h2></div>");
            out.println("</body></html>");

            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
