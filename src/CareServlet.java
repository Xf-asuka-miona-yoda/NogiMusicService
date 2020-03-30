import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class CareServlet extends HttpServlet {




    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        String userid = request.getParameter("userid");
        String myid = request.getParameter("myid");
        response.setCharacterEncoding("UTF-8");
        if (method.equals("addorcancel")){
            int i = addorcancel(userid,myid);
            JSONArray jsonArray = new JSONArray();
            JSONObject lan1 = new JSONObject();
            if (i == 1){
                lan1.put("result", "关注成功");
            }else if (i == 0){
                lan1.put("result", "取消关注");
            }

            jsonArray.add(lan1);

            PrintWriter writer = response.getWriter();
            System.out.println(jsonArray);
            writer.print(jsonArray);
        } else if (method.equals("check")){
            int i = getcare(userid,myid);
            JSONArray jsonArray = new JSONArray();
            JSONObject lan1 = new JSONObject();
            if (i == 1){
                lan1.put("result", "已关注");
            }else if (i == 0){
                lan1.put("result", "没关注");
            }

            jsonArray.add(lan1);

            PrintWriter writer = response.getWriter();
            System.out.println(jsonArray);
            writer.print(jsonArray);
        }


    }





    public int addorcancel(String userid, String myid){
        int result = -1;
        int realuserid = Integer.parseInt(userid);
        int realmyid = Integer.parseInt(myid);
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            //stmt1 = conn.createStatement();
            String sql = "select * from care where idolid ='"+realuserid+"' and  fanid = '"+realmyid+"'";
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
                //rs为空时执行的内容...
                System.out.println("无当前收藏信息");
                String sql_re = "insert into care(idolid,fanid) VALUES(?,?)";
                PreparedStatement ps = conn.prepareStatement(sql_re);
                ps.setObject(1,realuserid);
                ps.setObject(2,realmyid);

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
                String sql_re = "DELETE from care where idolid = ? and fanid = ?";
                PreparedStatement ps = conn.prepareStatement(sql_re);
                ps.setObject(1,realuserid);
                ps.setObject(2,realmyid);

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

    public int getcare(String userid, String myid){
        int result = -1;
        int realuserid = Integer.parseInt(userid);
        int realmyid = Integer.parseInt(myid);
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            //stmt1 = conn.createStatement();
            String sql = "select * from care where idolid ='"+realuserid+"' and  fanid = '"+realmyid+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                result = 0;
            } else {
                //rs不为空时执行的内容...
                result = 1;
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
