import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class HistoryServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String musicid = request.getParameter("musicid");
        String userid = request.getParameter("userid");
        System.out.println("查询的方式:" + musicid + "用户id" + userid);
        addhistory(musicid,userid);
        updateuser(musicid,userid);
    }

    public void addhistory(String musicid, String userid){
        int realmusicid = Integer.parseInt(musicid);
        int realuserid = Integer.parseInt(userid);

        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql_re = "insert into musichistory(musicid,userid) values (?,?)";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setObject(1,realmusicid);
            ps.setObject(2,realuserid);
            int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
            //System.out.println(len);
            if (len > 0){
                System.out.println("添加成功");
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
    }

    public void updateuser(String musicid, String userid){
        int realmusicid = Integer.parseInt(musicid);
        int realuserid = Integer.parseInt(userid);
        String sql_re = "";
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql = "select * from music where musicid = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1,realmusicid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String type = resultSet.getString("musictype");
                if (type.equals("华语流行")){
                    sql_re = "update users set 华语流行 = 华语流行 + 1 where ID = ?";
                }else if (type.equals("古风")){
                    sql_re = "update users set 古风 = 古风 + 1 where ID = ?";
                }else if (type.equals("日语流行")){
                    sql_re = "update users set 日语流行 = 日语流行 + 1 where ID = ?";
                }else if (type.equals("ACG")){
                    sql_re = "update users set ACG = ACG + 1 where ID = ?";
                }else if (type.equals("民谣")){
                    sql_re = "update users set 民谣 = 民谣 + 1 where ID = ?";
                }else if (type.equals("RAP")){
                    sql_re = "update users set RAP = RAP + 1 where ID = ?";
                }else if (type.equals("怀旧")){
                    sql_re = "update users set 怀旧 = 怀旧 + 1 where ID = ?";
                }else if (type.equals("儿歌")){
                    sql_re = "update users set 儿歌 = 儿歌 + 1 where ID = ?";
                }
                PreparedStatement ps = conn.prepareStatement(sql_re);
                ps.setObject(1,realuserid);
                int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
                //System.out.println(len);
                if (len > 0){
                    System.out.println("更新成功");
                }else {
                    System.out.println("更新失败");
                }
                ps.close();
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
    }
}
