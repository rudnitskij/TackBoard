package morgun.dev.tackboard;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The class is used to store static methods
 * of processing MySQL database and extracting/updating/removing
 * entities in the database
 *
 * @author Vitaliy Morgun
 * @version 1.0
 * @see DBaseHandling#getUser(String, String)
 * @see DBaseHandling#getAds(int, String...)
 * @see DBaseHandling#getAdvertById(int)
 * @see DBaseHandling#getLastAddedAdvert()
 * @see DBaseHandling#addUser(String, String)
 * @see DBaseHandling#addAdvert(Calendar, String, String, String, String, String)
 * @see DBaseHandling#updateAdvert(Advert)
 * @see DBaseHandling#removeAdvert(int)
 * @see DBaseHandling#adsSize(String...)
 * @since 01.11.2015
 */
public class DBaseHandling {
    static EntityManagerFactory emf;
    static EntityManager em;
    static String sqlQuery;
    static Query nq;

    /**
     * @param log  value of {@link java.lang.String String} class corresponding with the login of {@link morgun.dev.tackboard.User User}
     * @param pass value of {@link java.lang.String String} class corresponding with the password of {@link morgun.dev.tackboard.User User}
     * @return result - object of {@link morgun.dev.tackboard.User User} class in accordance with acquired values of log and pass or
     * user with <u>log</u>=<b>error</b>  in case of absence of the user with given <u>log</u> and <u>pass</u> or in case of exception thrown
     */
    public static User getUser(String log, String pass) {
        User result = null;
        try {
            emf = Persistence.createEntityManagerFactory("Tackboard");
            em = emf.createEntityManager();
            sqlQuery = "SELECT user_id from users where log='" + log + "' and pass='" + pass + "';";
            em.getTransaction().begin();
            nq = em.createNativeQuery(sqlQuery);
            result = em.find(User.class, nq.getSingleResult());
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new User("error");
            return result;
        } finally {
            em.close();
            emf.close();
        }
        return result;
    }


    /**
     * @param adsPage sequence number of ten ads to be returned by this method among all ads in the database
     * @param args    search criteria for selection from database. First {@link java.lang.String args[0]} contains search field to be selected,
     *                second {@link java.lang.String args[1]} contains the value of the field to be selected from database
     * @return {@link java.util.ArrayList result} list of selected entities in the form of {@link java.lang.Object} array, which fields contain
     * values of corresponding fields of database entity in sequence order. If the method doesn't obtain {@link java.lang.String args[0]} and
     * {@link java.lang.String args[1]} it would return the list of all entities from database. In case of any exception thrown the method returns an empty list
     */
    public static List<Object[]> getAds(int adsPage, String... args) {
        List<Object[]> result = new ArrayList<>();
        try {
            sqlQuery = "SELECT * FROM ads WHERE " + args[0] + " LIKE '%" + args[1] + "%' limit 10 offset " + (adsPage - 1) * 10;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException exception) {
            sqlQuery = "SELECT * FROM ads limit 10 offset " + (adsPage - 1) * 10;
        }
        try {
            emf = Persistence.createEntityManagerFactory("Tackboard");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            nq = em.createNativeQuery(sqlQuery);
            result = nq.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
        return result;
    }


    /**
     * @param ID sequence number of the {@link morgun.dev.tackboard.Advert advertisement} in the database
     * @return result - array of {@link java.lang.Object} class containing data from fields of the entity in the database
     * in corresponding sequence order which appears to be the {@link morgun.dev.tackboard.Advert advertisement} with the required ID.
     * In case of any exception returns the array of {@link java.lang.Object} class with "<i>not found</i>" words in the <b>author</b>
     * field (result[1])
     */
    public static Advert getAdvertById(int ID) {
        Advert result = null;
        try {
            emf = Persistence.createEntityManagerFactory("Tackboard");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            result = em.find(Advert.class, ID);
            if (result == null) {
                throw new Exception();
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            result = new Advert();
            result.setAuthor("not found");
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
        return result;
    }


    /**
     * The method is used for confirmation of newly added data or editing of the data if necessary
     *
     * @return result - object of {@link morgun.dev.tackboard.Advert} class which appears to be the last added entity in the database.
     * In case of any exception returns the object of {@link morgun.dev.tackboard.Advert} class with "<i>not found</i>" words in the {@link morgun.dev.tackboard.Advert#author}
     * field
     */
    public static Advert getLastAddedAdvert() {
        Advert result = new Advert();
        int maxID;
        try {
            emf = Persistence.createEntityManagerFactory("Tackboard");
            em = emf.createEntityManager();
            sqlQuery = "SELECT max(ad_id) FROM ads";
            em.getTransaction().begin();
            nq = em.createNativeQuery(sqlQuery);
            maxID = (Integer) nq.getSingleResult();
            result = em.find(Advert.class, maxID);
            em.getTransaction().commit();
        } catch (Exception ex) {
            result.setAuthor("not found");
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
        return result;
    }


    /**
     * @param log  {@link java.lang.String} class value standing for the login for the new user
     * @param pass {@link java.lang.String} class value standing for the password for the new user
     * @return {@link java.lang.Boolean#TRUE} value in case of successful registration of the new user in the database and
     * {@link java.lang.Boolean#FALSE} value in opposing cases and in case of the exception thrown as well
     */
    public static boolean addUser(String log, String pass) {
        try {
            User newUser = new User(log, pass);
            emf = Persistence.createEntityManagerFactory("Tackboard");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(newUser);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
        return false;
    }


    /**
     * @param issueMoment the moment of putting the new entity of {@link Advert} class into the database
     * @param author      login of the user added the new advertisement
     * @param topic       topic of the new advertisement
     * @param header      header of the new advertisement
     * @param text        content of the new advertisement
     * @param pictureLink relative path to the picture illustrating the new advertisement from /resources/images folder downwards
     * @return {@link java.lang.Boolean#TRUE} value in case of successful adding of the new advertisement in the database and
     * {@link java.lang.Boolean#FALSE} value in opposing cases and in case of the exception thrown as well
     */
    public static boolean addAdvert(Calendar issueMoment, String author, String topic, String header, String text, String pictureLink) {
        Advert ToAdd = new Advert();
        ToAdd.setIssueDate(issueMoment);
        ToAdd.setAuthor(author);
        ToAdd.setTopic(topic);
        ToAdd.setHeader(header);
        ToAdd.setText(text);
        ToAdd.setPictureLink((!pictureLink.endsWith("/")) ? pictureLink : "no_image.jpg");
        try {
            emf = Persistence.createEntityManagerFactory("Tackboard");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(ToAdd);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
        return false;
    }


    /**
     * Method is used for updating the entity in the database standing for the advertisement given as the parameter
     *
     * @param advert new {@link Advert} class object standing for the entity to be updated in the database
     */
    public static void updateAdvert(Advert advert) {
        try {
            emf = Persistence.createEntityManagerFactory("Tackboard");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(advert);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    /**
     * Method is used to remove the entity from the database
     *
     * @param i ID of the entity to be removed
     */
    public static void removeAdvert(int i) {
        Advert advert;
        try {
            emf = Persistence.createEntityManagerFactory("Tackboard");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            advert = em.find(Advert.class, i);
            em.remove(advert);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }


    /**
     * Method is used to obtain the number of ads in the database
     *
     * @param args {@link String} class objects standing for the search criterium and its value respectively
     * @return number of entities in the database corresponding with search conditions; in case of <b>args</b> absence returns number of all entities in the database.
     * Return 0 in case of any exception thrown
     */
    public static int adsSize(String... args) {
        int result = 0;
        try {
            sqlQuery = "SELECT COUNT(*) FROM ads WHERE " + args[0] + " LIKE '%" + args[1] + "%'";
        } catch (ArrayIndexOutOfBoundsException | NullPointerException exception) {
            sqlQuery = "SELECT count(*) FROM ads";
        }
        try {
            emf = Persistence.createEntityManagerFactory("Tackboard");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            nq = em.createNativeQuery(sqlQuery);
            result = Integer.valueOf(nq.getSingleResult().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
        return result;
    }


}
