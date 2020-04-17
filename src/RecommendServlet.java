import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RecommendServlet extends HttpServlet {



    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userid = request.getParameter("userid");
        System.out.println("查询的用户是" + userid);

        List<Music> musicList = new ArrayList<>();

        Recommend recommend = new Recommend(Integer.parseInt(userid));
        recommend.getadjacent();  //获取邻用户
        recommend.adjacentgetdata(); //计算邻用户的得分情况
        recommend.showadjacent();  //输出看看
        recommend.calculatesimilarity(); //计算邻用户的相似度并排序
        recommend.showadjacent(); //再看看
        recommend.getrecommendmusic(); //拿到推荐歌曲
        recommend.getmusic(musicList);

        response.setCharacterEncoding("UTF-8");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < musicList.size(); i++){
            JSONObject lan1 = new JSONObject();
            lan1.put("musicid", musicList.get(i).musicid);
            lan1.put("musicname", musicList.get(i).musicname);
            lan1.put("singer", musicList.get(i).singer);
            lan1.put("musicurl", musicList.get(i).musicurl);
            lan1.put("musicpic", musicList.get(i).musicpic);
            jsonArray.add(lan1);
        }


        PrintWriter writer = response.getWriter();
        System.out.println(jsonArray);
        writer.print(jsonArray);
        musicList.clear();
        jsonArray.clear();
    }
}
