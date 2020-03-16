import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RegisterServlet extends HttpServlet {

    int result;

    public Register_validation register_validation = new Register_validation();
    public User user = new User();

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
        String ResgisterSafe = request.getParameter("ResgisterSafe");

        System.out.println("注册账号：" + ResgisterAccount + ",注册昵称：" + ResgisterUsername + ",注册密码" + ResgisterPassword + ",注册年龄" + ResgisterAge + ",安全问题" + ResgisterSafe);
        result = register_validation.re_validation(ResgisterAccount, ResgisterUsername, ResgisterPassword, ResgisterAge, ResgisterSafe);
        if (result == 1){
            user.getuser(ResgisterAccount); //获取用户实例
        }

        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        JSONObject lan1 = new JSONObject();
        if (result == 1){
            lan1.put("result", "注册成功");
            lan1.put("id", user.id);
            lan1.put("username", user.username);
            lan1.put("age", user.age);
        }else {
            lan1.put("result", "注册失败");
            lan1.put("id", 0);
            lan1.put("username", "无");
            lan1.put("age", 0);
        }

        jsonArray.add(lan1);

        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);

    }

}
