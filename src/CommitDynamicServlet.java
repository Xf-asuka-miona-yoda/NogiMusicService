import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommitDynamicServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userid = request.getParameter("userid");
        String content = request.getParameter("content");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("day");
        String hour = request.getParameter("hour");
        String minute = request.getParameter("minute");
        String second = request.getParameter("second");

        System.out.println("发来的动态是:" + userid+content+year+month+day+hour+minute+second);

        int result = -1;
        result = getresult(userid,content,year,month,day,hour,minute,second);
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

    public int getresult(String userid,String content,String year,String month,String day,String hour,String minute,String second){
        int result = 0;
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql_re = "insert into dynamic(userid,content,year,month,day,hour,minute,second,zhuanfa,pinglun,dianzan) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setObject(1,userid);
            ps.setObject(2,content);
            ps.setObject(3,year);
            ps.setObject(4,month);
            ps.setObject(5,day);
            ps.setObject(6,hour);
            ps.setObject(7,minute);
            ps.setObject(8,second);
            ps.setObject(9,0);
            ps.setObject(10,0);
            ps.setObject(11,0);
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
