import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginServlet extends HttpServlet {

    public Login_validation login_validation = new Login_validation();
    public User user = new User();
    public LoginServlet() {
        super();
    }
    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String loginAccount = request.getParameter("loginAccount");
        String loginPassword = request.getParameter("loginPassword");

        int result = login_validation.validation(loginAccount, loginPassword);

        System.out.println("登录账号：" + loginAccount + ",登陆密码：" + loginPassword + ",登录结果" + result);
        if (result == 1){
            user.getuser(loginAccount); //获取用户实例
        }

        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        JSONObject lan1 = new JSONObject();
        if (result == 1){
            lan1.put("result", "登录成功");
            lan1.put("id", user.id);
            lan1.put("username", user.username);
            lan1.put("age", user.age);
        }else {
            lan1.put("result", "登录失败");
            lan1.put("id", 0);
            lan1.put("username", "无");
            lan1.put("age", 0);
        }

        jsonArray.add(lan1);



        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);

    }

    public void init() throws ServletException {
    }

}
