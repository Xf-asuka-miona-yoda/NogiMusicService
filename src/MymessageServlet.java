import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.SystemUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MymessageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userid = request.getParameter("userid");
        System.out.println("用户id" + userid);
        List<Mymessage> mymessageList = new ArrayList<>();
        getdycomment(userid,mymessageList);
        getcocomment(userid,mymessageList);
        getreply(userid,mymessageList);

        Collections.sort(mymessageList, new Comparator<Mymessage>() { //按照消息时间进行排序
            @Override
            public int compare(Mymessage o1, Mymessage o2) {
                int i = o1.getTime().compareTo(o2.getTime());
                if (i > 0){   //o1的时间为较新的消息
                    return 1;
                }else if (i < 0){ //o2较新
                    return -1;
                }
                return 0;  //相等
            }
        });

        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < mymessageList.size(); i++ ){
            JSONObject lan1 = new JSONObject();
            lan1.put("type",mymessageList.get(i).getType());
            lan1.put("objectid", mymessageList.get(i).getObjectid());
            lan1.put("objectcontent", mymessageList.get(i).getObjectcontent());
            lan1.put("objecttime", mymessageList.get(i).getObjecttime());
            lan1.put("userid", mymessageList.get(i).getUserid());
            lan1.put("username", mymessageList.get(i).getUsername());
            lan1.put("content",mymessageList.get(i).getContent());
            lan1.put("time", mymessageList.get(i).getTime());
            jsonArray.add(lan1);
        }

        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);
        jsonArray.clear();
        mymessageList.clear();

    }

    public void getdycomment(String id, List<Mymessage> mymessageList){ //获取动态评论
        int userid = Integer.parseInt(id);

        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql = "select * from dynamic where userid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1,userid); //先查询用户发了什么动态
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int dyid = rs.getInt("id");
                String content = rs.getString("content"); //动态内容

                String year = rs.getString("year");
                String month = rs.getString("month");
                String day = rs.getString("day");
                String hour = rs.getString("hour");
                String minute = rs.getString("minute");
                String second = rs.getString("second");

                String time = year + "-" + month + "-" + day + "  " + hour + ":" + minute + ":" + second;

                String sql_dy = "select * from dycomment where dynamicid = ? and userid != ?"; //根据之前查到的动态id查找非自己的评论
                PreparedStatement ps1 = conn.prepareStatement(sql_dy);
                ps1.setObject(1,dyid);
                ps1.setObject(2,userid);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()){//到这一步说明有人评论了动态可以创建消息
                    int comment_userid = rs1.getInt("userid"); //获取评论人的id

                    Mymessage mymessage = new Mymessage();
                    mymessage.setType("dycomment"); //类型为动态评论
                    mymessage.setObjectid(String.valueOf(dyid)); //动态的id
                    mymessage.setObjecttime(time);//设置对象时间
                    mymessage.setObjectcontent(content); //设置对象内容

                    mymessage.setUserid(String.valueOf(comment_userid)); //设置评论人id

                    String sql_user = "select username from users where ID = ?";
                    PreparedStatement ps2 = conn.prepareStatement(sql_user);//根据id查找评论人用户名
                    ps2.setObject(1,comment_userid);
                    ResultSet resultSet = ps2.executeQuery();
                    while (resultSet.next()){
                        String name = resultSet.getString("username");
                        mymessage.setUsername(name); //设置评论人的用户名

                    }
                    resultSet.close();

                    String comment = rs1.getString("content"); //获取评论内容
                    mymessage.setContent(comment); //设置消息内容

                    String year1 = rs1.getString("year");
                    String month1 = rs1.getString("month");
                    String day1 = rs1.getString("day");
                    String hour1 = rs1.getString("hour");
                    String minute1 = rs1.getString("minute");
                    String second1 = rs1.getString("second");

                    String time1 = year1 + "-" + month1 + "-" + day1 + "  " + hour1 + ":" + minute1+ ":" + second1;

                    mymessage.setTime(time1); //设置消息时间
                    mymessageList.add(mymessage);
                }
                rs1.close();
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
    }

    public void getcocomment(String id, List<Mymessage> mymessageList){ //获取评论的评论
        int userid = Integer.parseInt(id);

        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql = "select * from dycomment where userid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1,userid); //先查询用户发了什么评论
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int coid = rs.getInt("id");
                String content = rs.getString("content"); //评论内容

                String year = rs.getString("year");
                String month = rs.getString("month");
                String day = rs.getString("day");
                String hour = rs.getString("hour");
                String minute = rs.getString("minute");
                String second = rs.getString("second");

                String time = year + "-" + month + "-" + day + "  " + hour + ":" + minute + ":" + second;

                String sql_dy = "select * from cocomment where commentid = ? and userid != ?"; //根据之前查到的评论id查找非自己的评论
                PreparedStatement ps1 = conn.prepareStatement(sql_dy);
                ps1.setObject(1,coid);
                ps1.setObject(2,userid);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()){//到这一步说明有人评论了评论可以创建消息
                    int comment_userid = rs1.getInt("userid"); //获取评论人的id

                    Mymessage mymessage = new Mymessage();
                    mymessage.setType("cocomment"); //类型为动态评论
                    mymessage.setObjectid(String.valueOf(coid)); //评论的id
                    mymessage.setObjecttime(time);//设置对象时间
                    mymessage.setObjectcontent(content); //设置对象内容

                    mymessage.setUserid(String.valueOf(comment_userid)); //设置评论人id

                    String sql_user = "select username from users where ID = ?";
                    PreparedStatement ps2 = conn.prepareStatement(sql_user);//根据id查找评论人用户名
                    ps2.setObject(1,comment_userid);
                    ResultSet resultSet = ps2.executeQuery();
                    while (resultSet.next()){
                        String name = resultSet.getString("username");
                        mymessage.setUsername(name); //设置评论人的用户名

                    }
                    resultSet.close();

                    String comment = rs1.getString("content"); //获取评论内容
                    mymessage.setContent(comment); //设置消息内容

                    String year1 = rs1.getString("year");
                    String month1 = rs1.getString("month");
                    String day1 = rs1.getString("day");
                    String hour1 = rs1.getString("hour");
                    String minute1 = rs1.getString("minute");
                    String second1 = rs1.getString("second");

                    String time1 = year1 + "-" + month1 + "-" + day1 + "  " + hour1 + ":" + minute1+ ":" + second1;

                    mymessage.setTime(time1); //设置消息时间
                    mymessageList.add(mymessage);
                }
                rs1.close();
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
    }

    public void getreply(String id, List<Mymessage> mymessageList){ //获取回复信息
        int userid = Integer.parseInt(id);

        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql = "select * from reply where replyid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1,userid); //先查询用户被回复了吗
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int dycommentid = rs.getInt("dycommentid"); //在哪一条动态评论下回复的
                int commitid = rs.getInt("commitid");    //回复人的id
                String content = rs.getString("content"); //回复的内容

                Mymessage mymessage = new Mymessage();
                mymessage.setType("reply"); //类型为回复评论
                mymessage.setContent(content); // 设置回复的内容
                mymessage.setUserid(String.valueOf(commitid)); //设置回复人的id

                String sql_user = "select username from users where ID = ?";
                PreparedStatement ps2 = conn.prepareStatement(sql_user);//根据id查找回复人的用户名
                ps2.setObject(1,commitid);
                ResultSet resultSet = ps2.executeQuery();
                while (resultSet.next()){
                    String name = resultSet.getString("username");
                    mymessage.setUsername(name); //设置回复人的用户名

                }
                resultSet.close();

                String sql_dy = "select * from dycomment where id = ?";
                PreparedStatement ps1 = conn.prepareStatement(sql_dy);
                ps1.setObject(1,dycommentid); //先查询回复是在哪一条动态评论下面
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()){
                    int dyid = rs1.getInt("id");
                    String comment = rs1.getString("content"); //获取评论内容
                    mymessage.setObjectcontent(comment); //设置对象内容

                    String year1 = rs1.getString("year");
                    String month1 = rs1.getString("month");
                    String day1 = rs1.getString("day");
                    String hour1 = rs1.getString("hour");
                    String minute1 = rs1.getString("minute");
                    String second1 = rs1.getString("second");

                    String time1 = year1 + "-" + month1 + "-" + day1 + "  " + hour1 + ":" + minute1+ ":" + second1;

                    mymessage.setObjecttime(time1); //对象时间
                    mymessage.setObjectid(String.valueOf(dyid)); //对象id
                }
                rs1.close();

                String sql_time = "select * from cocomment where userid = ? and content = ?";
                PreparedStatement ps_time = conn.prepareStatement(sql_time);
                ps_time.setObject(1,commitid);
                ps_time.setObject(2,content);
                ResultSet rs_time = ps_time.executeQuery();
                while (rs_time.next()){
                    String year = rs_time.getString("year");
                    String month = rs_time.getString("month");
                    String day = rs_time.getString("day");
                    String hour = rs_time.getString("hour");
                    String minute = rs_time.getString("minute");
                    String second = rs_time.getString("second");

                    String time = year + "-" + month + "-" + day + "  " + hour + ":" + minute+ ":" + second;

                    mymessage.setTime(time);
                }
                rs_time.close();
                mymessageList.add(mymessage);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
    }
}
