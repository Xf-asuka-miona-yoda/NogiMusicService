import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class CollectionServlet extends HttpServlet {

    Connection conn = null;
    Statement stmt = null;
    Statement stmt1 = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        String userid = request.getParameter("userid");
        String musicid = request.getParameter("musicid");
        response.setCharacterEncoding("UTF-8");
        if (method.equals("addorcancel")){
            int i = addorcancel(userid,musicid);
            JSONArray jsonArray = new JSONArray();
            JSONObject lan1 = new JSONObject();
            if (i == 1){
                lan1.put("result", "收藏成功");
            }else if (i == 0){
                lan1.put("result", "取消收藏");
            }

            jsonArray.add(lan1);

            PrintWriter writer = response.getWriter();
            System.out.println(jsonArray);
            writer.print(jsonArray);
        }
    }

    public int addorcancel(String userid, String musicid){
        int result = -1;
        int realuserid = Integer.parseInt(userid);
        int realmusicid = Integer.parseInt(musicid);

        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            String sql = "select * from collection where musicid ='"+realmusicid+"' and  userid = '"+realuserid+"'";
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
                //rs为空时执行的内容...
                System.out.println("无当前收藏信息");
                String sql_re = "insert into collection(musicid,userid) VALUES(?,?)";
                PreparedStatement ps = conn.prepareStatement(sql_re);
                ps.setObject(1,realmusicid);
                ps.setObject(2,realuserid);

                int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
                //System.out.println(len);
                if (len > 0){
                    System.out.println("添加成功");
                    result = 1;
                }else {
                    System.out.println("添加失败");
                }
                ps.close();

            } else {
                //rs不为空时执行的内容...
                System.out.println("已有当前收藏信息");
                String sql_re = "DELETE from collection where musicid = ? and userid = ?";
                PreparedStatement ps = conn.prepareStatement(sql_re);
                ps.setObject(1,realmusicid);
                ps.setObject(2,realuserid);

                int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
                //System.out.println(len);
                if (len > 0){
                    System.out.println("删除成功");
                    result = 0;
                }else {
                    System.out.println("删除失败");
                }
                ps.close();

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
        return result;
    }

}
