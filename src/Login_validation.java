import java.sql.*;

public class Login_validation {
    int result = 0;

    public int validation(String input_account, String input_password){
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

            while(rs.next()) {
                String password = rs.getString("password");
                System.out.println("登录的账号密码: " + password);
                System.out.println("客户端输入的密码: " + input_password);
                if (password.equals(input_password)){
                    result = 1;
                } else {
                    result = 0;
                }
                System.out.println("登录结果: " + result);
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

    public static void main(String[] args){
        Login_validation login_validation = new Login_validation();
        login_validation.validation("admin", "123456");
    }
}
