package morgun.dev.tackboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static morgun.dev.tackboard.DBaseHandling.*;
import static morgun.dev.tackboard.Validation.*;

/**
 * Spring controller class for processing requests to the server and mapping them to respective jsp-pages
 *
 * @author Vitaliy Morgun
 * @version 1.0
 * @since 01.11.2015
 */
@Controller
public class BoardController {
    /**
     * stands for the absolute path of the project folder, required for the proper working of the entire application
     */
    @Autowired
    private String projectPath;
    /**
     * keeps info of users' authorization, key stands for the session ID, value for login of the user ('anonymous' if user hasn't logged in)
     */
    public static Map<String, String> userMap = new HashMap<>();
    /**
     * required for correct depicting of page with ads and different picture for every single ad. The key stands for ID of the advertisement, the value -
     * for picture file path ending
     */
    public static Map<Integer, String> picturesMap = new HashMap<>();

    @RequestMapping({"/index", "/"})
    public ModelAndView index(HttpServletRequest request) {
        try {
            if (userMap.get(request.getSession().getId()).equals("anonymous")) {
                return new ModelAndView("home");
            } else {
                return new ModelAndView("ads");
            }
        } catch (Exception ex) {
            userMap.put(request.getSession().getId(), "anonymous");
            request.getSession().setAttribute("projectpath", projectPath);
            return new ModelAndView("home");

        }
    }

    @RequestMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request) {
        if (userMap.get(request.getSession().getId()).equals("anonymous")) {
            return new ModelAndView("login");
        } else return ReadAds(1, request);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ModelAndView userValidation(String log, String pass, HttpServletRequest request) {
        if (loginPasswordIsValid(log) && loginPasswordIsValid(pass)) {
            User loggingIn = getUser(log, pass);
            if (loggingIn.getLog().equals("error")) {
                request.getSession().setAttribute("errortype", "wrongpassword");
                return new ModelAndView("login");
            }
            userMap.put(request.getSession().getId(), loggingIn.getLog());
            request.getSession().setAttribute("adsPage", 1);
            request.getSession().setAttribute("url", request.getServletPath());
            request.getSession().setAttribute("searchfield", null);
            request.getSession().setAttribute("searchvalue", null);
            return new ModelAndView("ads");
        } else {
            request.getSession().setAttribute("errortype", "invalidpassword");
            return new ModelAndView("login");
        }
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    public ModelAndView userValidated(@RequestParam(value = "adsPage", defaultValue = "1") int page, HttpServletRequest request) {
        if (!userMap.get(request.getSession().getId()).equals("anonymous")) {
            return ReadAds(page, request);
        } else {
            return loginPage(request);
        }
    }


    @RequestMapping("/register")
    public ModelAndView Register() {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public ModelAndView AddUser(String log, String pass, HttpServletRequest request) {
        if (loginPasswordIsValid(log) && loginPasswordIsValid(pass)) {
            boolean registered = addUser(log, pass);
            request.getSession().setAttribute("registered", String.valueOf(registered));
            request.getSession().setAttribute("NewUser", log);
            return new ModelAndView("addUser");
        } else {
            request.getSession().setAttribute("errortype", "invalidpassword");
            return new ModelAndView("register");
        }
    }

    @RequestMapping("/fulladvert")
    public ModelAndView ShowFullAdvert(int advertID, HttpServletRequest request) {
        request.getSession().setAttribute("advertID", advertID);
        return new ModelAndView("fullAdvert");
    }

    @RequestMapping("/new_advert")
    public ModelAndView NewAdvert(HttpServletRequest request) {
        request.getSession().setAttribute("url", "no_url");
        return new ModelAndView("new_advert");
    }

    @RequestMapping(value = "/send_advert", method = RequestMethod.POST)
    public ModelAndView SendAdvert(@RequestParam("adsPicture") MultipartFile adsPicture, @RequestParam("topic") String topic,
                                   @RequestParam("header") String header, @RequestParam("text") String text,
                                   HttpServletRequest request) throws IOException {

        if (!headerIsValid(header)) {
            request.getSession().setAttribute("errortype", "invalidheader");
            return new ModelAndView("new_advert");
        } else if (!textIsValid(text)) {
            request.getSession().setAttribute("errortype", "invalidtext");
            return new ModelAndView("new_advert");
        } else {
            File receivedPicture;
            String date;
            Calendar issueMoment = Calendar.getInstance();
            String author = userMap.get(request.getSession().getId());
            date = String.format("%tF", new Date());
            File todaysDir = new File(projectPath + "src\\main\\webapp\\resources\\images\\" + date);
            todaysDir.mkdir();
            receivedPicture = new File(todaysDir.getAbsolutePath() + "\\" + adsPicture.getOriginalFilename());
            try {
                adsPicture.transferTo(receivedPicture);
            } catch (IOException e) {
            }
            request.getSession().setAttribute("added", (addAdvert(issueMoment, author, topic, header, text, date +
                    "/" + adsPicture.getOriginalFilename())) ? "true" : "false");
            request.getSession().setAttribute("pictureName", (receivedPicture.getAbsolutePath().endsWith("/")) ?
                    "no_image.jpg" : date + "\\" + adsPicture.getOriginalFilename());
            return new ModelAndView("full_advert_to_add");
        }
    }


    @RequestMapping("/pic")
    public ModelAndView pic(@RequestParam("picturelink") String pictureLink) {
        return new ModelAndView("\\resources\\images\\" + pictureLink);
    }

    @RequestMapping("/edit")
    public ModelAndView editAdvert(HttpServletRequest request) {
        request.getSession().setAttribute("url", request.getServletPath());
        return new ModelAndView("new_advert");
    }

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    public ModelAndView acceptChanges(@RequestParam("adsPicture") MultipartFile adsPicture, @RequestParam("topic") String topic,
                                      @RequestParam("header") String header, @RequestParam("text") String text,
                                      HttpServletRequest request) {
        Advert updatedAdvert;
        if (!headerIsValid(header)) {
            request.getSession().setAttribute("errortype", "invalidheader");
            return new ModelAndView("new_advert");
        } else if (!textIsValid(text)) {
            request.getSession().setAttribute("errortype", "invalidtext");
            return new ModelAndView("new_advert");
        } else {
            String oldPictureName;
            File newPicture, newPictureFolder, oldPicture;
            String date, pictureLink;
            if (adsPicture.getSize() > 0) {
                oldPictureName = request.getSession().getAttribute("pictureName").toString();
                date = String.format("%tF", new Date());
                newPictureFolder = new File(projectPath + "src\\main\\webapp\\resources\\images\\" + date);
                newPicture = new File(newPictureFolder.getAbsolutePath() + "\\" + adsPicture.getOriginalFilename());
                try {
                    adsPicture.transferTo(newPicture);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                oldPicture = new File(projectPath + "src\\main\\webapp\\resources\\images\\" + oldPictureName);
                if (!oldPictureName.endsWith("no_image.jpg")) {
                    oldPicture.delete();
                }
                pictureLink = date + "/" + adsPicture.getOriginalFilename();
            } else {
                pictureLink = request.getSession().getAttribute("pictureName").toString();
            }
            int ID = (Integer) request.getSession().getAttribute("advertID");
            updatedAdvert = new Advert(topic, Calendar.getInstance(), header, text, pictureLink, userMap.get(request.getSession().getId()));
            updatedAdvert.setId(ID);
            updateAdvert(updatedAdvert);
            request.getSession().setAttribute("advertID", updatedAdvert.getId());
            return new ModelAndView("fullAdvert");
        }
    }

    @RequestMapping("/remove")
    public ModelAndView removeAdd(HttpServletRequest request) {
        Advert advert = (Advert) request.getSession().getAttribute("advert");
        removeAdvert(advert.getId());
        return new ModelAndView("ads");
    }

    @RequestMapping("/logout")
    public ModelAndView logoutController(HttpServletRequest request) {
        userMap.put(request.getSession().getId(), "anonymous");
        request.getSession().setAttribute("searchfield", null);
        request.getSession().setAttribute("searchvalue", null);
        request.getSession().setAttribute("url", request.getServletPath());
        return new ModelAndView("ads");
    }

    @RequestMapping("/ads")
    public ModelAndView ReadAds(@RequestParam(value = "adsPage", defaultValue = "1") int adsPage, HttpServletRequest request) {
        request.getSession().setAttribute("searchfield", null);
        request.getSession().setAttribute("searchvalue", null);
        request.getSession().setAttribute("adsPage", adsPage);
        request.getSession().setAttribute("url", request.getServletPath());
        return new ModelAndView("ads");
    }

    @RequestMapping(value = "/ads/{author}")
    public ModelAndView filterAds(@PathVariable("author") String author,
                                  @RequestParam(value = "adsPage", defaultValue = "1") int adsPage,
                                  HttpServletRequest request) {
        request.getSession().setAttribute("searchfield", "author");
        request.getSession().setAttribute("searchvalue", author);
        request.getSession().setAttribute("adsPage", adsPage);
        request.getSession().setAttribute("url", request.getServletPath());
        return new ModelAndView("ads");
    }

    @RequestMapping(value = "/authorsearch/{author}", method = RequestMethod.GET)
    public ModelAndView filterAdsByAuthorGet(@PathVariable("author") String author,
                                             @RequestParam(value = "adsPage", defaultValue = "1") int adsPage,
                                             HttpServletRequest request) {
        request.getSession().setAttribute("searchfield", "author");
        request.getSession().setAttribute("searchvalue", author);
        request.getSession().setAttribute("adsPage", adsPage);
        request.getSession().setAttribute("url", request.getServletPath());
        return new ModelAndView("ads");
    }


    @RequestMapping(value = "/authorsearch", method = RequestMethod.POST)
    public ModelAndView filterAdsByAuthorPost(@RequestParam("author") String author,
                                              HttpServletRequest request) {
        request.getSession().setAttribute("searchfield", "author");
        request.getSession().setAttribute("searchvalue", author);
        request.getSession().setAttribute("adsPage", 1);
        request.getSession().setAttribute("url", request.getServletPath());
        return new ModelAndView("ads");
    }

    @RequestMapping(value = "/topicsearch", method = RequestMethod.POST)
    public ModelAndView filterAdsByTopic(@RequestParam("topic") String topic,
                                         HttpServletRequest request) {
        request.getSession().setAttribute("searchfield", "topic");
        request.getSession().setAttribute("searchvalue", topic);
        request.getSession().setAttribute("adsPage", 1);
        request.getSession().setAttribute("url", request.getServletPath());
        return new ModelAndView("ads");
    }

    @RequestMapping(value = "/topicsearch/{topic}", method = RequestMethod.GET)
    public ModelAndView filterAdsByTopicGet(@PathVariable("topic") String topic,
                                            @RequestParam(value = "adsPage", defaultValue = "1") int adsPage,
                                            HttpServletRequest request) {
        request.getSession().setAttribute("searchfield", "topic");
        request.getSession().setAttribute("searchvalue", topic);
        request.getSession().setAttribute("adsPage", adsPage);
        request.getSession().setAttribute("url", request.getServletPath());
        return new ModelAndView("ads");
    }
}
