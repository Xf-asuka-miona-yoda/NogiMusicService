import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String ResgisterAccount = request.getParameter("ResgisterAccount");
        String ResgisterUsername = request.getParameter("ResgisterUsername");
        String ResgisterPassword = request.getParameter("ResgisterPassword");
        String ResgisterAge = request.getParameter("ResgisterAge");

        System.out.println("注册账号：" + ResgisterAccount + ",注册昵称：" + ResgisterUsername + ",注册密码" + ResgisterPassword);
    }
}
