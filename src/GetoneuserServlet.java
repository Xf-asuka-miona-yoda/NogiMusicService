import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetoneuserServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userid = request.getParameter("userid");
        System.out.println( "用户id" + userid);

        User user = new User();
        user.getuserbyid(userid);
        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        JSONObject lan1 = new JSONObject();

        lan1.put("id", user.id);
        lan1.put("username", user.username);
        lan1.put("age", user.age);

        jsonArray.add(lan1);

        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);
    }
}
