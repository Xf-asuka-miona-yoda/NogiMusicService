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

public class SearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        String content = request.getParameter("content");
        System.out.println(method);
        JSONArray jsonArray = new JSONArray();

        List<HotSearch> hotSearches = new ArrayList<>();
        List<Music> musicList = new ArrayList<>();

        if (method.equals("hotsearch")){
            gethotsearch(hotSearches);
            for (int i = 0; i < hotSearches.size(); i++ ){
                JSONObject lan1 = new JSONObject();
                lan1.put("content", hotSearches.get(i).getContent());
                lan1.put("num", hotSearches.get(i).getNum());
                jsonArray.add(lan1);

            }
        }else if (method.equals("result")){
            addsearch(content);
            getresultbysinger(content,musicList);
            getresult(content,musicList);
            if (musicList.size() > 0){
                for (int i = 0; i < musicList.size();i++){
                    JSONObject lan1 = new JSONObject();
                    lan1.put("musicid", musicList.get(i).musicid);
                    lan1.put("musicname", musicList.get(i).musicname);
                    lan1.put("singer", musicList.get(i).singer);
                    lan1.put("musicurl", musicList.get(i).musicurl);
                    lan1.put("musicpic", musicList.get(i).musicpic);
                    jsonArray.add(lan1);
                }
            }else {
                JSONObject lan1 = new JSONObject();
                lan1.put("musicid", -1);
                lan1.put("musicname", "无");
                lan1.put("singer", "无");
                lan1.put("musicurl", "无");
                lan1.put("musicpic", "无");
                jsonArray.add(lan1);
            }
        }

        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);
        jsonArray.clear();
        hotSearches.clear();
    }

    public void gethotsearch(List<HotSearch> hotSearches){
        int i = 0;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql = "select * from search order by searchnum DESC ";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                HotSearch hotSearch = new HotSearch();
                String content = rs.getString("searchcontent");
                hotSearch.setContent(content);
                int num = rs.getInt("searchnum");
                hotSearch.setNum(String.valueOf(num));
                hotSearches.add(hotSearch);
                i++;
                if (i == 19){
                    break;
                }
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



    public void addsearch(String content) {

        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            //stmt1 = conn.createStatement();
            String sql = "select * from search where searchcontent ='" + content + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
                //rs为空时执行的内容...
                System.out.println("无当前搜素信息");
                String sql_re = "insert into search(searchcontent,searchnum) VALUES(?,?)";
                PreparedStatement ps = conn.prepareStatement(sql_re);
                ps.setObject(1, content);
                ps.setObject(2, 1);

                int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
                //System.out.println(len);
                if (len > 0) {
                    System.out.println("添加成功");
                } else {
                    System.out.println("添加失败");
                }
                ps.close();

            } else {
                //rs不为空时执行的内容...
                System.out.println("已有当前搜素信息");
                String sql_re = "update search set searchnum = searchnum + 1 where searchcontent = ?";
                PreparedStatement ps = conn.prepareStatement(sql_re);
                ps.setObject(1, content);
                int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
                //System.out.println(len);
                if (len > 0) {
                    System.out.println("成功");

                } else {
                    System.out.println("失败");
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
    }


    public void getresult(String content, List<Music> musicList){
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            String sql_re = "select musicid from music where musicname like ?";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setString(1,"%" + content +"%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int musicid = rs.getInt("musicid");
                Music music = new Music();
                music.getMusicbyid(musicid);
                musicList.add(music);
            }
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
    }

    public void getresultbysinger(String content, List<Music> musicList){
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            String sql_re = "select singerid from singer where singername like ?";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setString(1,"%" + content +"%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int singerid = rs.getInt("singerid");
                String sql_music = "select musicid from music where singerid = ?";
                PreparedStatement ps1 = conn.prepareStatement(sql_music);
                ps1.setInt(1,singerid);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()){
                    int musicid = rs1.getInt("musicid");
                    Music music = new Music();
                    music.getMusicbyid(musicid);
                    musicList.add(music);
                }
                ps1.close();
            }
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
    }


    class HotSearch{
        private String content;
        private String num;

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
