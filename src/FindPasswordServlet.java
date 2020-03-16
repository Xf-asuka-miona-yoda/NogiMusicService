import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class FindPasswordServlet extends HttpServlet {

    int result;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String FindAccount = request.getParameter("FindAccount");
        String NewPassword = request.getParameter("NewPassword");
        String FindSafe = request.getParameter("FindSafe");

        System.out.println(FindAccount +  NewPassword + FindSafe);
        result = findaccount(FindAccount, NewPassword, FindSafe);
        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        JSONObject lan1 = new JSONObject();
        if (result == 1){
            lan1.put("code", "ok");
            jsonArray.add(lan1);
        }else if (result == 0){
            lan1.put("code", "fail");
            jsonArray.add(lan1);
        }
        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);

    }

    public int findaccount(String account, String pass, String safe){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql = "select * from users where account ='"+account+"' and  safe = '"+safe+"'";
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
                //rs为空时执行的内容...
                System.out.println("爬");
                result = 0;

            } else {
                //rs不为空时执行的内容...
                System.out.println("核实成功");
                String sql_re = "update users set password = ? where account = ? and safe = ?";
                PreparedStatement ps = conn.prepareStatement(sql_re);
                ps.setObject(1,pass);
                ps.setObject(2,account);
                ps.setObject(3,safe);

                int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
                //System.out.println(len);
                if (len > 0){
                    System.out.println("更新成功");
                    result = 1;
                }else {
                    System.out.println("更新失败");
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
