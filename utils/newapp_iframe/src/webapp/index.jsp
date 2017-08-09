<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Enumeration"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<style type="text/css">
html,head,body,iframe {
	margin: 0 0 0 0;
	padding: 0;
	border-width: 0px 0px 0px 0px;
}
html,body {
	height: 100%;
}
</style>
<title><%=request.getAttribute("TITLE")%></title>
</head>
<body>
	<iframe src="<%=request.getAttribute("URL")%>" style="padding: 0px; width: 100%; height: 100%;" seamless></iframe>
</body>
</html>
