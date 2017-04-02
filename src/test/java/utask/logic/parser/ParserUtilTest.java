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
        assertTrue (!index.isPresent());

        index = ParserUtil.parseIndex("%");
        assertTrue (!index.isPresent());

        index = ParserUtil.parseIndex("abc");
        assertTrue (!index.isPresent());

        index = ParserUtil.parseIndex("16");
        assertFalse (!index.isPresent());

    }

    @Test
    public void parse_multiple_indexes() {
        Optional<List<Integer>> indexList = ParserUtil.parseMultiIndex(" ");
        assertTrue (!indexList.isPresent());

        indexList = ParserUtil.parseMultiIndex(" , 1");
        assertTrue (!indexList.isPresent());

        indexList = ParserUtil.parseMultiIndex("1, a");
        assertTrue (!indexList.isPresent());

        indexList = ParserUtil.parseMultiIndex("1, 2 to c");
        assertTrue (!indexList.isPresent());

        indexList = ParserUtil.parseMultiIndex("1, 1");
        assertFalse (!indexList.isPresent());
        assertEquals ("1", indexList.get().get(0).toString());

        indexList = ParserUtil.parseMultiIndex(" 1, 1 to 3");
        assertFalse (!indexList.isPresent());
        ArrayList<Integer> expectedList = new ArrayList<Integer>();
        expectedList.add(3);
        expectedList.add(2);
        expectedList.add(1);
        assertEquals (expectedList, indexList.get());
    }
}
