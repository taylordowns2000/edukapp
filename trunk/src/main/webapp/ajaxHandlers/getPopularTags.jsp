<%@ page
	import="java.sql.*,java.io.*,org.apache.wookie.connector.framework.*,uk.ac.edukapp.util.*,uk.ac.edukapp.model.*,uk.ac.edukapp.service.*,java.util.*,javax.persistence.*,java.sql.Timestamp,javax.persistence.EntityManager,javax.persistence.EntityManagerFactory"%>
<%

response.setHeader("Content-type","application/json");
	PrintWriter pw = response.getWriter();


String host = "localhost";
String port = "3306";
String service = "edukapptests";

String url = "jdbc:mysql://" + host + ":" + port + "/" + service;
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection(url, "edukapptests", "edukapptests"); 

PreparedStatement ps = con.prepareStatement(
    "SELECT t.id AS id,t.tagtext AS tagtext,count(*) AS freq "+
	"FROM widgetprofiles_tags wt INNER JOIN tags t ON (t.id=wt.tag_id) "+
	"GROUP BY wt.tag_id "+
	"ORDER BY freq DESC "+
	"LIMIT 0,10");
ResultSet rs = ps.executeQuery();

int counter = 0;
pw.append("[");
while (rs.next()){
  if (counter!=0){pw.append(",");}
  pw.append("{");
  pw.append("\"");pw.append("id");pw.append("\"");
  pw.append(":");
  pw.append("\"");pw.append(rs.getString("id"));pw.append("\"");
  
  pw.append(",");
  
  pw.append("\"");pw.append("tagtext");pw.append("\"");
  pw.append(":");
  pw.append("\"");pw.append(rs.getString("tagtext"));pw.append("\"");
  
  pw.append(",");
  
  pw.append("\"");pw.append("freq");pw.append("\"");
  pw.append(":");
  pw.append("\"");pw.append(rs.getString("freq"));pw.append("\"");
  pw.append("}");
  counter++;
}
pw.append("]");
con.close();
%>