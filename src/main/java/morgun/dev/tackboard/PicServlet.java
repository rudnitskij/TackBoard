package morgun.dev.tackboard;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import static morgun.dev.tackboard.BoardController.picturesMap;

/**
 * Servlet needed to transmit the pictures to jsp-pages
 */
public class PicServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int picNumber;
        String picName;
        File picture;
        byte[] bytes;
        InputStream in;
        try {
            picNumber = Integer.parseInt(request.getPathInfo().substring(1, request.getPathInfo().length()));
            picName = picturesMap.get(picNumber);
        } catch (Exception ex){
            picName = (String) request.getSession().getAttribute("pictureName");
        }

        String filePath = (String) request.getSession().getAttribute("projectpath");
        String path = filePath + "src\\main\\webapp\\resources\\images\\";
        response.setContentType("image/" + picName.substring(picName.lastIndexOf(".") + 1));
        int i = 0, offset;
        picture = new File(path + picName);
        try {
            in = new FileInputStream(picture);
        } catch (IOException ex){
            picture = new File(path + "ads_not_found.jpg");
            in = new FileInputStream(picture);
        }
        bytes = new byte[(int) picture.length()];
        while (i < bytes.length) {
            offset = in.read(bytes, i, bytes.length);
            i += offset;
        }
        in.close();
        OutputStream bis = response.getOutputStream();
        bis.write(bytes);
        bis.close();
    }
}
