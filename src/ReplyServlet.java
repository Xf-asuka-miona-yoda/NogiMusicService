import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReplyServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String replyid = request.getParameter("replyid");
        String commitid = request.getParameter("commitid");
        String content = request.getParameter("content");
        String dycommentid = request.getParameter("dycommentid");
        System.out.println("查询的方式:" + replyid + "用户id" + commitid + content);
        addreply(replyid,commitid,content,dycommentid);
    }

    public void addreply(String replyid, String commitid, String content, String dycommentid){
        int realreplyid = Integer.parseInt(replyid);
        int realcommitid = Integer.parseInt(commitid);
        int realdycommentid = Integer.parseInt(dycommentid);

        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql_re = "insert into reply(replyid,commitid,content,dycommentid) values (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setObject(1,realreplyid);
            ps.setObject(2,realcommitid);
            ps.setObject(3,content);
            ps.setObject(4,realdycommentid);
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
}
