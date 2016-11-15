/**
 * The First class handles the request given by the login form/index page 
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
import java.util.Map;

public class First extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Determines if login information is correct.
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
        
        //Retrieve user-submitted username and password
        String uname = request.getParameter("uname");
        String pword = request.getParameter("pword");
        
        //Retrieve legitimate username and password from server
        String env_vars = System.getenv("JAVA_OPTS");
        String[] varArr = env_vars.split("(=)|( )");
        String username = "";
        String password = "";
        for(int i=0; i < varArr.length; i++){
            if(varArr[i].contains("USERNAME"))
                username = varArr[i+1];
            if(varArr[i].contains("PASSWORD"))
                password = varArr[i+1];
            username = username.replace("\"", "").trim();
            password = password.replace("\"", "").trim();        
        }
        
        //Determine if submitted username and password are correct. If so,
        //create session and direct data to Second.java. If not, direct page 
        //back to login form.
        try {
            if(uname.equals(username) && pword.equals(password)){
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("password", password);
                RequestDispatcher rd = request.getRequestDispatcher("Second");
                rd.forward(request,response);           
            } else {
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.include(request,response);
            }            
        } finally {
            out.close();
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
