package morgun.dev.tackboard.tests;

import morgun.dev.tackboard.Advert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static morgun.dev.tackboard.DBaseHandling.*;

/**
 * @author Vitaliy Morgun
 * @version 1.0
 * @since 04.11.2015
 */
public class DBaseConnectionTest {

    @Test
    public void getUserTest() {
        assertEquals("error", getUser("йцукенгшщзхъ", "фывапролджэячс").getLog());
    }

    @Test
    public void getAdsTest() {
        assertEquals(0, getAds(0).size());
    }

    @Test
    public void getAdvertByIdTest() {
        Advert testAdvert = new Advert();
        testAdvert.setAuthor("not found");
        assertEquals(testAdvert.getAuthor(), getAdvertById(0).getAuthor());
    }

    @Test
    public void getLastAddedAdvertTest() {
        assertEquals(Advert.class, getLastAddedAdvert().getClass());
    }

    @Test
    public void updateAdvertTest() {
        Advert advert1 = getLastAddedAdvert();
        Advert advert2 = getLastAddedAdvert();
        updateAdvert(advert2);
        assertEquals(advert1, advert2);
    }

    @Test
    public void adsSizeTest() {
        assertEquals(0, adsSize("йцукен", "фывапр"));
    }
}
