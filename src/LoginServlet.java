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

        System.out.println("登录账号："+loginAccount+",登陆密码："+loginPassword+",登录结果"+"result");
        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        JSONObject lan1 = new JSONObject();
        lan1.put("name", "王小二");
        lan1.put("password", "17");
        jsonArray.add(lan1);

        JSONObject lan2 = new JSONObject();
        lan2.put("name", "王小三");
        lan2.put("password", "17");
        jsonArray.add(lan2);

        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);

    }

    public void init() throws ServletException {
    }

}
