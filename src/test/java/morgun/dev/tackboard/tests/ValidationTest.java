package morgun.dev.tackboard.tests;

import org.junit.Test;

import static morgun.dev.tackboard.Validation.headerIsValid;
import static junit.framework.TestCase.*;
import static morgun.dev.tackboard.Validation.loginPasswordIsValid;
import static morgun.dev.tackboard.Validation.textIsValid;

/**
 * @author Vitaliy Morgun
 * @version 1.0
 * @since 04.11.2015
 */
public class ValidationTest {

    @Test
    public void validationTest(){
        String shortHeader = "<>";
        assertEquals(false,headerIsValid(shortHeader));
        assertEquals(false,textIsValid(shortHeader));
        assertEquals(false,loginPasswordIsValid(shortHeader));
    }
}
