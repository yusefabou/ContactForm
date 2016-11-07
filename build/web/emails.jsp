<%-- 
    Document   : emails
    Created on : Nov 7, 2016, 3:01:36 PM
    Author     : Yusef
--%>
<%@ page import="java.sql.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Email List</title>
    </head>
    <body>
        <h1>Email List</h1>
        <table border="1">
            <tr>
                <td>E-mail</td>
                <td>Subject</td>
                <td>Message</td>
            </tr>
            <% 
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String url="jdbc:mysql://contactformdb.csw5ig1hapkg.us-west-1.rds.amazonaws.com:3306/ContactFormDB?zeroDateTimeBehavior=convertToNull";
                String username="yusef";
                String password="abouremeleh";
                String query="SELECT * FROM emails";
                Connection conn=DriverManager.getConnection(url,username,password);
                Statement stmt=conn.createStatement();
                ResultSet rs=stmt.executeQuery(query);
                while(rs.next())
                {
                    %>
                    <tr>
                        <td><%=rs.getString("email_field") %></td>
                        <td><%=rs.getString("subject_field") %></td>
                        <td><%=rs.getString("message") %></td>             
                    </tr>
                    <%
                }
            %>
        </table>
        <%
            rs.close();
            stmt.close();
            conn.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        %>
        
    </body>
</html>
