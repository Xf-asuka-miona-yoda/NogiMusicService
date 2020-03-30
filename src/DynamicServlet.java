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

public class DynamicServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        String userid = request.getParameter("userid");
        System.out.println("查询的方式:" + method + "用户id" + userid);

        List<Dynamic> dynamicList = new ArrayList<>();
        if(method.equals("all")){ //查询全部动态
            getalldynamic(dynamicList);
            response.setCharacterEncoding("UTF-8");
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < dynamicList.size(); i++ ){
                JSONObject lan1 = new JSONObject();
                lan1.put("dyid", dynamicList.get(i).getDyid());
                lan1.put("userid", dynamicList.get(i).getUserid());
                lan1.put("username", dynamicList.get(i).getUsername());
                lan1.put("content", dynamicList.get(i).getContent());
                lan1.put("year", dynamicList.get(i).getYear());
                lan1.put("month", dynamicList.get(i).getMonth());
                lan1.put("date", dynamicList.get(i).getDate());
                lan1.put("hour", dynamicList.get(i).getHour());
                lan1.put("minute", dynamicList.get(i).getMinute());
                lan1.put("second", dynamicList.get(i).getSecond());
                lan1.put("zhuanfa", dynamicList.get(i).getZhuanfa());
                lan1.put("pinglun", dynamicList.get(i).getPinglun());
                lan1.put("dianzan", dynamicList.get(i).getDianzan());
                jsonArray.add(lan1);
            }

            PrintWriter writer = response.getWriter();
            System.out.println(jsonArray);
            writer.print(jsonArray);
            jsonArray.clear();
            dynamicList.clear();
        }else if (method.equals("dianzan")){
           int rs = dianzan(userid);
           response.setCharacterEncoding("UTF-8");
           JSONArray jsonArray = new JSONArray();
           JSONObject lan1 = new JSONObject();
           if (rs == 1){
               lan1.put("result", "点赞成功");
           }else if (rs == 0){
               lan1.put("result", "点赞失败");
           }

           jsonArray.add(lan1);

           PrintWriter writer = response.getWriter();
           System.out.println(jsonArray);
           writer.print(jsonArray);

        }else if (method.equals("pinglun")){
            int rs = pinglun(userid);
            response.setCharacterEncoding("UTF-8");
            JSONArray jsonArray = new JSONArray();
            JSONObject lan1 = new JSONObject();
            if (rs == 1){
                lan1.put("result", "评论成功");
            }else if (rs == 0){
                lan1.put("result", "评论失败");
            }

            jsonArray.add(lan1);

            PrintWriter writer = response.getWriter();
            System.out.println(jsonArray);
            writer.print(jsonArray);
        }else if (method.equals("one")){
            getone(dynamicList,userid);
            response.setCharacterEncoding("UTF-8");
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < dynamicList.size(); i++ ){
                JSONObject lan1 = new JSONObject();
                lan1.put("dyid", dynamicList.get(i).getDyid());
                lan1.put("userid", dynamicList.get(i).getUserid());
                lan1.put("username", dynamicList.get(i).getUsername());
                lan1.put("content", dynamicList.get(i).getContent());
                lan1.put("year", dynamicList.get(i).getYear());
                lan1.put("month", dynamicList.get(i).getMonth());
                lan1.put("date", dynamicList.get(i).getDate());
                lan1.put("hour", dynamicList.get(i).getHour());
                lan1.put("minute", dynamicList.get(i).getMinute());
                lan1.put("second", dynamicList.get(i).getSecond());
                lan1.put("zhuanfa", dynamicList.get(i).getZhuanfa());
                lan1.put("pinglun", dynamicList.get(i).getPinglun());
                lan1.put("dianzan", dynamicList.get(i).getDianzan());
                jsonArray.add(lan1);
            }

            PrintWriter writer = response.getWriter();
            System.out.println(jsonArray);
            writer.print(jsonArray);
            jsonArray.clear();
            dynamicList.clear();
        }



    }

    public void getalldynamic(List<Dynamic> dynamicList){
        Connection conn = null;
        Statement stmt = null;
        Statement stmt1 = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            String sql = "select * from dynamic";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                Dynamic dynamic = new Dynamic();
                // 通过字段检索
                int id = rs.getInt("id");
                dynamic.setDyid(String.valueOf(id));
                int userid = rs.getInt("userid");
                dynamic.setUserid(String.valueOf(userid));
                String sql_user = "select username from users where ID ='"+userid+"'";
                ResultSet rs1 = stmt1.executeQuery(sql_user);
                while(rs1.next()){
                    String username = rs1.getString("username");
                    System.out.println("用户: " + username);
                    dynamic.setUsername(username);
                }
                rs1.close();
                String content = rs.getString("content");
                dynamic.setContent(content);
                String year = rs.getString("year");
                dynamic.setYear(year);
                String month = rs.getString("month");
                dynamic.setMonth(month);
                String day = rs.getString("day");
                dynamic.setDate(day);
                String hour = rs.getString("hour");
                dynamic.setHour(hour);
                String minute = rs.getString("minute");
                dynamic.setMinute(minute);
                String second = rs.getString("second");
                dynamic.setSecond(second);
                int zhuanfa = rs.getInt("zhuanfa");
                dynamic.setZhuanfa(String.valueOf(zhuanfa));
                int pinglun = rs.getInt("pinglun");
                dynamic.setPinglun(String.valueOf(pinglun));
                int dianzan = rs.getInt("dianzan");
                dynamic.setDianzan(String.valueOf(dianzan));
                dynamicList.add(0,dynamic);
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

    public void getone(List<Dynamic> dynamicList, String id){
        Connection conn = null;
        Statement stmt = null;
        Statement stmt1 = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            String sql = "select * from dynamic where userid = '"+id+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                Dynamic dynamic = new Dynamic();
                // 通过字段检索
                int dyid = rs.getInt("id");
                dynamic.setDyid(String.valueOf(dyid));
                int userid = rs.getInt("userid");
                dynamic.setUserid(String.valueOf(userid));
                String sql_user = "select username from users where ID ='"+userid+"'";
                ResultSet rs1 = stmt1.executeQuery(sql_user);
                while(rs1.next()){
                    String username = rs1.getString("username");
                    System.out.println("用户: " + username);
                    dynamic.setUsername(username);
                }
                rs1.close();
                String content = rs.getString("content");
                dynamic.setContent(content);
                String year = rs.getString("year");
                dynamic.setYear(year);
                String month = rs.getString("month");
                dynamic.setMonth(month);
                String day = rs.getString("day");
                dynamic.setDate(day);
                String hour = rs.getString("hour");
                dynamic.setHour(hour);
                String minute = rs.getString("minute");
                dynamic.setMinute(minute);
                String second = rs.getString("second");
                dynamic.setSecond(second);
                int zhuanfa = rs.getInt("zhuanfa");
                dynamic.setZhuanfa(String.valueOf(zhuanfa));
                int pinglun = rs.getInt("pinglun");
                dynamic.setPinglun(String.valueOf(pinglun));
                int dianzan = rs.getInt("dianzan");
                dynamic.setDianzan(String.valueOf(dianzan));
                dynamicList.add(0,dynamic);
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


    public int dianzan(String id){
        int realdyid = Integer.parseInt(id);
        int result = 0;
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql_re = "update dynamic set dianzan = dianzan + 1 where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setObject(1,realdyid);
            int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
            //System.out.println(len);
            if (len > 0){
                System.out.println("点赞成功");
                result = 1;
            }else {
                System.out.println("点赞失败");
            }
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
        return result;
    }

    public int pinglun(String id){
        int realdyid = Integer.parseInt(id);
        int result = 0;
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql_re = "update dynamic set pinglun = pinglun + 1 where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql_re);
            ps.setObject(1,realdyid);
            int len = ps.executeUpdate(); //()中不需要加入sql的对象参数
            //System.out.println(len);
            if (len > 0){
                System.out.println("点赞成功");
                result = 1;
            }else {
                System.out.println("点赞失败");
            }
            ps.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
        return result;
    }
}
