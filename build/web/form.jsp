<%-- 
    Document   : form
    Created on : Nov 6, 2016, 5:16:13 PM
    Author     : Yusef
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Form</title>
    </head>
    <body>
        <h1>Contact Form</h1>
        <form action="Third" method="post">
            
            Name:<input type="text" name="name" required="true" /><br/>
            Email:<input type="email" name="email" required="true" /><br/>
            Message:<textarea name="message" style="width:250px;height:150px;" required="true"></textarea><br/>
            <input type="submit" value="Send"/><input type="reset">
            
        </form>
        <a href="emails.jsp">List of Emails</a>
    </body>
</html>
