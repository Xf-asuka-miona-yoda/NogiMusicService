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

public class AllsingerServlet extends HttpServlet {
    List<Singer> singers = new ArrayList<>();

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String code = request.getParameter("code");
        if (code.equals("200")){
            getAllsinger();
        }

        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < singers.size(); i++ ){
            JSONObject lan1 = new JSONObject();
            lan1.put("singerid", singers.get(i).getSingerid());
            lan1.put("singername", singers.get(i).getSingername());
            lan1.put("singerpicurl", singers.get(i).getSingerpicurl());
            lan1.put("singer_in", singers.get(i).getSinger_in());
            jsonArray.add(lan1);
        }

        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);
        jsonArray.clear();
        singers.clear();

    }

    public void getAllsinger(){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql = "select * from singer";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                Singer singer = new Singer();
                int singerid = rs.getInt("singerid");
                singer.setSingerid(singerid);
                String singername = rs.getString("singername");
                singer.setSingername(singername);
                String singerintroduce = rs.getString("singerintroduce");
                singer.setSinger_in(singerintroduce);
                String singerpic = rs.getString("singerpic");
                singer.setSingerpicurl(singerpic);
                singers.add(singer);
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
