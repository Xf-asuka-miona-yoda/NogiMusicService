import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DyCommentServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String dynamicid = request.getParameter("dynamicid");
        System.out.println("查询的动态id:" + dynamicid);
        List<Comment> commentList = new ArrayList<>();
        getcomments(commentList, dynamicid);
        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < commentList.size(); i++ ){
            JSONObject lan1 = new JSONObject();
            lan1.put("id",commentList.get(i).getId());
            lan1.put("userid", commentList.get(i).getUserid());
            lan1.put("username", commentList.get(i).getUsername());
            lan1.put("content", commentList.get(i).getContent());
            lan1.put("year", commentList.get(i).getYear());
            lan1.put("month", commentList.get(i).getMonth());
            lan1.put("date", commentList.get(i).getDay());
            lan1.put("hour", commentList.get(i).getHour());
            lan1.put("minute", commentList.get(i).getMinute());
            lan1.put("second", commentList.get(i).getSecond());
            jsonArray.add(lan1);
        }

        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);
        jsonArray.clear();
        commentList.clear();
    }

    public void getcomments(List<Comment> commentList, String dynamicid){
        int realdynamicid = Integer.parseInt(dynamicid);
        Connection conn = null;
        Statement stmt = null;
        Statement stmt1 = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            String sql = "select * from dycomment where dynamicid='"+realdynamicid+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                Comment comment = new Comment();
                // 通过字段检索
                int id = rs.getInt("id");
                comment.setId(String.valueOf(id));
                int userid = rs.getInt("userid");
                comment.setUserid(String.valueOf(userid));
                String sql_user = "select username from users where ID ='"+userid+"'";
                ResultSet rs1 = stmt1.executeQuery(sql_user);
                while(rs1.next()){
                    String username = rs1.getString("username");
                    System.out.println("用户: " + username);
                    comment.setUsername(username);
                }
                rs1.close();
                String content = rs.getString("content");
                comment.setContent(content);
                String year = rs.getString("year");
                comment.setYear(year);
                String month = rs.getString("month");
                comment.setMonth(month);
                String day = rs.getString("day");
                comment.setDay(day);
                String hour = rs.getString("hour");
                comment.setHour(hour);
                String minute = rs.getString("minute");
                comment.setMinute(minute);
                String second = rs.getString("second");
                comment.setSecond(second);
                commentList.add(0,comment);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException var22) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var21) {
                var21.printStackTrace();
            }

        }
        System.out.println("数据库操作完成");
    }
}

