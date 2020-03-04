import java.sql.*;

public class Register_validation {
    int result = -1; //为0表示账号冲突，为1注册成功

    public int re_validation(String input_account, String input_username, String input_password, String input_age){
        int re_age = Integer.parseInt(input_age);
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql = "select password from Users where account='"+input_account+"'";
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
                //rs为空时执行的内容...
                System.out.println("可以给你插一下");
                String sql_re = "insert into users(account,password,username,age) VALUES(?,?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql_re);
                ps.setObject(1,input_account);
                ps.setObject(2,input_password);
                ps.setObject(3,input_username);
                ps.setObject(4,re_age);

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
                System.out.println("给爷爬");
                result = 0;
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
        return result;
    }
}
