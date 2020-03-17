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

public class SingerMusicServlet extends HttpServlet {

    List<Music> musicList = new ArrayList<>();

    public String singername;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String singerid = request.getParameter("singerid");
        singername = request.getParameter("singername");
        System.out.println(singerid + singername);
        getsingermusic(singerid);

        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < musicList.size(); i++){
            JSONObject lan1 = new JSONObject();
            lan1.put("musicid", musicList.get(i).musicid);
            lan1.put("musicname", musicList.get(i).musicname);
            lan1.put("singer", musicList.get(i).singer);
            lan1.put("musicurl", musicList.get(i).musicurl);
            lan1.put("musicpic", musicList.get(i).musicpic);
            jsonArray.add(lan1);
        }


        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);
        musicList.clear();
        jsonArray.clear();
    }



    public void getsingermusic(String id){

        int realsingerid = Integer.parseInt(id);

        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql = "select * from music where singerid='"+realsingerid+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                Music music = new Music();
                // 通过字段检索
                int musicid = rs.getInt("musicid");
                music.setMusicid(String.valueOf(musicid));
                String name = rs.getString("musicname");
                music.setMusicname(name);
                String url = rs.getString("musicurl");
                music.setMusicurl(url);
                String pic = rs.getString("musicpic");
                music.setMusicpic(pic);

                music.setSinger(singername);
                musicList.add(music);
                // 输出数据
                System.out.println("歌曲id"+ musicid);
                System.out.print(", 歌曲名称: " + name);
                System.out.print(", 播放 URL: " + url);
                System.out.print(", 图片 URL: " + pic);
                System.out.print("\n");
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
