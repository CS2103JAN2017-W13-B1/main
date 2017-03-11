package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.utask.model.task.Timestamp;

public class TimestampTest {

    @Test
    public void isValidEmail() {
        // blank timestamp
        assertFalse(Timestamp.isValidTimestamp("")); // empty string
        assertFalse(Timestamp.isValidTimestamp(" ")); // spaces only

        // missing parts
//        assertFalse(Timestamp.isValidTimestamp("@webmail.com")); // missing local part
//        assertFalse(Timestamp.isValidTimestamp("peterjackwebmail.com")); // missing '@' symbol
//        assertFalse(Timestamp.isValidTimestamp("peterjack@")); // missing domain name

        // invalid parts
//        assertFalse(Timestamp.isValidTimestamp("-@webmail.com")); // invalid local part
//        assertFalse(Timestamp.isValidTimestamp("peterjack@-")); // invalid domain name
//        assertFalse(Timestamp.isValidTimestamp("peter jack@webmail.com")); // spaces in local part
//        assertFalse(Timestamp.isValidTimestamp("peterjack@web mail.com")); // spaces in domain name
//        assertFalse(Timestamp.isValidTimestamp("peterjack@@webmail.com")); // double '@' symbol
//        assertFalse(Timestamp.isValidTimestamp("peter@jack@webmail.com")); // '@' symbol in local part
//        assertFalse(Timestamp.isValidTimestamp("peterjack@webmail@com")); // '@' symbol in domain name

        // valid timestamp
        assertTrue(Timestamp.isValidTimestamp("from 0000 to 1200"));
//        assertTrue(Timestamp.isValidTimestamp("PeterJack_1190@WEB.Mail.com"));
//        assertTrue(Timestamp.isValidTimestamp("a@b"));  // minimal
//        assertTrue(Timestamp.isValidTimestamp("test@localhost"));   // alphabets only
//        assertTrue(Timestamp.isValidTimestamp("123@145"));  // numeric local part and domain name
//        assertTrue(Timestamp.isValidTimestamp("a1@sg50.org"));  // mixture of alphanumeric and dot characters
//        assertTrue(Timestamp.isValidTimestamp("_user_@_do_main_.com_"));    // underscores
//        assertTrue(Timestamp.isValidTimestamp("peter_jack@a_very_long_domain_AVLD.com"));   // long domain name
//        assertTrue(Timestamp.isValidTimestamp("if.you.dream.it_you.can.do.it@youth_email.com"));    // long local part
    }
}
