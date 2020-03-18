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

public class NewMusicServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String rankingname = request.getParameter("rankingname");
        List<Music> newmusicranking = new ArrayList<>();
        if (rankingname.equals("新歌榜")){
            Connection conn = null;
            Statement stmt = null;
            try {
                Class.forName(JdbcUTil.JDBC_DRIVER);
                System.out.println("连接数据库...");
                conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
                System.out.println(" 实例化Statement对象...");
                stmt = conn.createStatement();
                String sql = "select * from newmusic";
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()) {
                    Music music = new Music();
                    // 通过字段检索
                    int musicid = rs.getInt("musicid");
                    music.setMusicid(String.valueOf(musicid));
                    music.getMusicbyid(musicid);
                    newmusicranking.add(music);
                    // 输出数据
                    System.out.println("歌曲id"+ musicid);
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

        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < newmusicranking.size(); i++){
            JSONObject lan1 = new JSONObject();
            lan1.put("musicid", newmusicranking.get(i).musicid);
            lan1.put("musicname", newmusicranking.get(i).musicname);
            lan1.put("singer", newmusicranking.get(i).singer);
            lan1.put("musicurl", newmusicranking.get(i).musicurl);
            lan1.put("musicpic", newmusicranking.get(i).musicpic);
            jsonArray.add(lan1);
        }


        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);
        newmusicranking.clear();
        jsonArray.clear();
    }
}

