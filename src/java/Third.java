/**
 * The Third class handles data provided by the Contact Form.
 * @author Yusef
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import com.sendgrid.*;
import java.sql.*;

public class Third extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. The purpose is to send an e-mail once the Contact Form is filled
     * out and to direct the page to a list of e-mails once done. 
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        //Get session credentials
        HttpSession session = request.getSession(false);
        String username = (String)(session.getAttribute("username"));
        String password = (String)(session.getAttribute("password"));        
        
        //Store the input data from the contact form
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        
        try {
            //Ensure that no field is left empty
            if(name != null && !name.isEmpty() 
                    && email != null && !email.isEmpty() 
                    && message != null && !message.isEmpty()){
                
                //Create class object and use to send mail via sendgrid
                Third sender = new Third();
                sender.sendMail(name, email, message, username, password);
                RequestDispatcher rd = request.getRequestDispatcher("emails.jsp");
                rd.forward(request,response);
                
            } else {
                //Send user back to the form page if contact form fields are left blank
                RequestDispatcher rd = request.getRequestDispatcher("form.jsp");
                rd.include(request,response);
                }
        } finally {
            out.close();
        }
    }
    
    /**
     * Sends an e-mail message to the provided e-mail address and stores the 
     * e-mail in a mySQL database.
     *
     * @param name the person to send the email to/the subject header
     * @param email the email address to which the mail is directed
     * @param message the content of the email 
     * @param username the login user
     * @param password the login password
     * @throws IOException if an I/O error occurs
     */
    public void sendMail(String name, String email, String message, 
            String username, String password) throws IOException{ 
        
        //Set email parameters
        Email from = new Email("jane.contactform@gmail.com");
        from.setName("Jane");
        String subject = name;
        Email to = new Email(email);
        to.setName(name);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);
        
        //Create MySQL connection
        String API_Key = "";
        try{
            String myDriver = "com.mysql.jdbc.Driver";
            String myURL = "jdbc:mysql://contactformdb.csw5ig1hapkg.us-west-1.rds.amazonaws.com:3306/ContactFormDB?zeroDateTimeBehavior=convertToNull";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myURL, username, password);
            Statement stmt = conn.createStatement();

            //Retrieve SendGrid API Key from database
            String query = "SELECT * FROM api_keys WHERE service='sendgrid'";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                API_Key = rs.getString("key");
            }
            rs.close();
            
            //Send mail
            SendGrid sg = new SendGrid(API_Key);
            Request request = new Request();
            String details = "None";
            try {
                request.method = Method.POST;
                request.endpoint = "mail/send";
                request.body = mail.build();
                Response response = sg.api(request);
                details = response.statusCode + ' ' + response.body + ' '
                        + response.headers;
              } catch (IOException ex) {
                throw ex;
              }
            
            //Insert mail into database
            query = " INSERT into emails (email_field, subject_field, message)" 
                    + " values (?, ?, ?)";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, email);
            preparedStmt.setString(2, subject);
            preparedStmt.setString(3, message);
            preparedStmt.execute();
            conn.close();
            
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
