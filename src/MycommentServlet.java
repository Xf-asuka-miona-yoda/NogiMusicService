import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class MycommentServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        String userid = request.getParameter("userid");
        String objectid = request.getParameter("objectid");
        String content = request.getParameter("content");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("day");
        String hour = request.getParameter("hour");
        String minute = request.getParameter("minute");
        String second = request.getParameter("second");

        System.out.println("发来的评论是:" + userid+objectid+content+year+month+day+hour+minute+second);

        int result = -1;
        if (method.equals("music")){
            result = getresult(objectid,userid,content,year,month,day,hour,minute,second);
        }else if (method.equals("dynamic")){
            result = getdyresult(objectid,userid,content,year,month,day,hour,minute,second);
        }else if (method.equals("comment")){
            result = getcoresult(objectid,userid,content,year,month,day,hour,minute,second);
        }

        if (result == 1){
            response.setCharacterEncoding("UTF-8");
            JSONArray jsonArray = new JSONArray();
            JSONObject lan1 = new JSONObject();
            lan1.put("code", "success");
            jsonArray.add(lan1);

            PrintWriter writer = response.getWriter();
            System.out.println(jsonArray);
            writer.print(jsonArray);
            jsonArray.clear();
        }else {
            response.setCharacterEncoding("UTF-8");
            JSONArray jsonArray = new JSONArray();
            JSONObject lan1 = new JSONObject();
            lan1.put("code", "failed");
            jsonArray.add(lan1);

            PrintWriter writer = response.getWriter();
            System.out.println(jsonArray);
            writer.print(jsonArray);
            jsonArray.clear();
        }

    }

    public int getresult(String musicid,String userid,String content,String year,String month,String day,String hour,String minute,String second){
        int result = 0;
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql_re = "insert into comment(musicid,userid,content,year,month,day,hour,minute,second) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setObject(1,musicid);
            ps.setObject(2,userid);
            ps.setObject(3,content);
            ps.setObject(4,year);
            ps.setObject(5,month);
            ps.setObject(6,day);
            ps.setObject(7,hour);
            ps.setObject(8,minute);
            ps.setObject(9,second);
            int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
            //System.out.println(len);
            if (len > 0){
                System.out.println("添加成功");
                result = 1;
            }else {
                System.out.println("添加失败");
            }
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
        return result;
    }

    public int getdyresult(String dynamicid,String userid,String content,String year,String month,String day,String hour,String minute,String second){
        int result = 0;
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql_re = "insert into dycomment(dynamicid,userid,content,year,month,day,hour,minute,second) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setObject(1,dynamicid);
            ps.setObject(2,userid);
            ps.setObject(3,content);
            ps.setObject(4,year);
            ps.setObject(5,month);
            ps.setObject(6,day);
            ps.setObject(7,hour);
            ps.setObject(8,minute);
            ps.setObject(9,second);
            int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
            //System.out.println(len);
            if (len > 0){
                System.out.println("添加成功");
                result = 1;
            }else {
                System.out.println("添加失败");
            }
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
        return result;
    }

    public int getcoresult(String dynamicid,String userid,String content,String year,String month,String day,String hour,String minute,String second){
        int result = 0;
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql_re = "insert into cocomment(commentid,userid,content,year,month,day,hour,minute,second) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setObject(1,dynamicid);
            ps.setObject(2,userid);
            ps.setObject(3,content);
            ps.setObject(4,year);
            ps.setObject(5,month);
            ps.setObject(6,day);
            ps.setObject(7,hour);
            ps.setObject(8,minute);
            ps.setObject(9,second);
            int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
            //System.out.println(len);
            if (len > 0){
                System.out.println("添加成功");
                result = 1;
            }else {
                System.out.println("添加失败");
            }
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
        return result;
    }
}
