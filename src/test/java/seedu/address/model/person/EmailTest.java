package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.utask.model.task.Timestamp;

public class EmailTest {

    @Test
    public void isValidEmail() {
        // blank email
        assertFalse(Timestamp.isValidEmail("")); // empty string
        assertFalse(Timestamp.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Timestamp.isValidEmail("@webmail.com")); // missing local part
        assertFalse(Timestamp.isValidEmail("peterjackwebmail.com")); // missing '@' symbol
        assertFalse(Timestamp.isValidEmail("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Timestamp.isValidEmail("-@webmail.com")); // invalid local part
        assertFalse(Timestamp.isValidEmail("peterjack@-")); // invalid domain name
        assertFalse(Timestamp.isValidEmail("peter jack@webmail.com")); // spaces in local part
        assertFalse(Timestamp.isValidEmail("peterjack@web mail.com")); // spaces in domain name
        assertFalse(Timestamp.isValidEmail("peterjack@@webmail.com")); // double '@' symbol
        assertFalse(Timestamp.isValidEmail("peter@jack@webmail.com")); // '@' symbol in local part
        assertFalse(Timestamp.isValidEmail("peterjack@webmail@com")); // '@' symbol in domain name

        // valid email
        assertTrue(Timestamp.isValidEmail("PeterJack_1190@WEB.Mail.com"));
        assertTrue(Timestamp.isValidEmail("a@b"));  // minimal
        assertTrue(Timestamp.isValidEmail("test@localhost"));   // alphabets only
        assertTrue(Timestamp.isValidEmail("123@145"));  // numeric local part and domain name
        assertTrue(Timestamp.isValidEmail("a1@sg50.org"));  // mixture of alphanumeric and dot characters
        assertTrue(Timestamp.isValidEmail("_user_@_do_main_.com_"));    // underscores
        assertTrue(Timestamp.isValidEmail("peter_jack@a_very_long_domain_AVLD.com"));   // long domain name
        assertTrue(Timestamp.isValidEmail("if.you.dream.it_you.can.do.it@youth_email.com"));    // long local part
    }
}
