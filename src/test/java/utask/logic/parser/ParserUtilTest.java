//@@author A0138493W
package utask.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class ParserUtilTest {

    @Test
    public void parse_index() {
        Optional<Integer> index = ParserUtil.parseIndex(" ");
        assertFalse (index.isPresent());

        index = ParserUtil.parseIndex("-1");
        assertFalse (index.isPresent());

        index = ParserUtil.parseIndex("16");
        assertTrue (index.isPresent());

    }

    @Test
    public void parse_multiple_indexes() {
        Optional<List<Integer>> indexList = ParserUtil.parseMultiIndex(" ");
        assertFalse (indexList.isPresent());

        indexList = ParserUtil.parseMultiIndex("-1, 2");
        assertFalse (indexList.isPresent());

        indexList = ParserUtil.parseMultiIndex("1, 2 to c");
        assertFalse (indexList.isPresent());

        indexList = ParserUtil.parseMultiIndex("1, 1, 1 to 3, 4");
        ArrayList<Integer> expectedList = new ArrayList<Integer>();
        expectedList.add(4);
        expectedList.add(3);
        expectedList.add(2);
        expectedList.add(1);
        assertTrue (indexList.isPresent());
        assertEquals (expectedList, indexList.get());
    }

    @Test
    public void is_path_valid() {
        if (System.getProperty("os.name").contains("Windows")) {
            assertTrue (ParserUtil.isPathValid("C:\\path\\to\\destination"));
        } else {
            assertTrue (ParserUtil.isPathValid("/Users/James/Desktop"));
        }
    }

}
