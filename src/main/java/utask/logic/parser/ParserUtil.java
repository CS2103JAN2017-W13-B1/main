package utask.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import utask.commons.exceptions.IllegalValueException;
import utask.commons.util.StringUtil;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.Status;
import utask.model.task.Timestamp;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes
 */
public class ParserUtil {

    private static final Pattern INDEX_ARGS_FORMAT = Pattern
            .compile("(?<targetIndex>.+)");
    private static final Pattern MULTI_INDEX_ARGS_FORMAT = Pattern
            .compile("^(((?=[^0])\\d+)|((?=[^0])(\\d+))\\sto\\s((?=[^0])(\\d+)))"
                    + "+(,*\\s*((?=[^0])\\d+|((?=[^0])(\\d+))\\sto\\s((?=[^0])(\\d+))))*$");
    private static final Pattern WIN_PATH_FORMAT =
            Pattern.compile("([a-zA-Z]:)?(\\\\[a-zA-Z0-9 _.-]+)+\\\\?");
    private static final Pattern MAC_PATH_FORMAT =
            Pattern.compile("^(/Users/)((?!-)[a-zA-Z0-9-]+(?<!-))(/((?!-)[a-zA-Z0-9-]+(?<!-)))*$");
    private static final Pattern SORT_IN_FIND_FORMAT =
            Pattern.compile("(?<columnAlphabet>[a-f])\\s*(?<orderBy>(asc|dsc))?");

    /**
     * Returns the specified index in the {@code command} if it is a positive
     * unsigned integer Returns an {@code Optional.empty()} otherwise.
     */
    public static Optional<Integer> parseIndex(String command) {
        final Matcher matcher = INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if (!StringUtil.isUnsignedInteger(index)) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    //@@author A0138493W
    /**
     * Returns the indexes as a list in the {@code command} if it every index positive
     * unsigned integer Returns an {@code Optional.empty()} otherwise.
     */
    public static Optional<List<Integer>> parseMultiIndex(String command) {
        if (!isValidIndex(command.trim())) {
            return Optional.empty();
        }
        ArrayList<Integer> intIndexList = extractNumberFromInput(command);
        return Optional.of(getReversedSortedList(intIndexList));
    }

    /**
     * Returns ArrayList which contains not repeated input numbers
     */
    private static ArrayList<Integer> extractNumberFromInput(String command) {
        String[] splittedStringIndexes = command.trim().replace(" ", "").split(",");
        ArrayList<Integer> intIndexList = new ArrayList<Integer>();
        for (String index : splittedStringIndexes) {
            if (index.contains("to")) {
                String[] range = index.split("to");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (int i = start; i <= end; i++) {
                    if (!intIndexList.contains(i)) {
                        intIndexList.add(i);
                    }
                }
            } else {
                if (!intIndexList.contains(Integer.parseInt(index))) {
                    intIndexList.add(Integer.parseInt(index));
                }
            }
        }
        return intIndexList;
    }

    /**
     * Returns a sorted reversed ArrayList
     */
    private static ArrayList<Integer> getReversedSortedList(ArrayList<Integer> intIndexList) {
        Collections.sort(intIndexList);
        Collections.reverse(intIndexList);
        return intIndexList;
    }

    /**
     * Returns true if a given string is a valid input string.
     */
    private static boolean isValidIndex(String command) {
        assert command != null;
        final Matcher matcher = MULTI_INDEX_ARGS_FORMAT.matcher(command.trim());
        return matcher.matches();
    }

    /**
     * Returns true if a given string is a valid path.
     */
    public static boolean isPathValid(String command) {
        assert command != null;
        String os = System.getProperty("os.name");
        Matcher pathathMatcher = null;
        if (os.contains("Windows")) {
            pathathMatcher = WIN_PATH_FORMAT.matcher(command.trim());
        } else {
            pathathMatcher = MAC_PATH_FORMAT.matcher(command.trim());
        }
        return pathathMatcher.matches();
    }

    //author

    /**
     * Returns a new Set populated by all elements in the given list of strings
     * Returns an empty set if the given {@code Optional} is empty, or if the
     * list contained in the {@code Optional} is empty
     */
    public static Set<String> toSet(Optional<List<String>> list) {
        List<String> elements = list.orElse(Collections.emptyList());
        return new HashSet<>(elements);
    }

    //@@author A0138423J
    /**
     * Splits a preamble string into ordered fields.
     *
     * @return A list of size {@code numFields} where the ith element is the ith
     *         field value if specified in the input, {@code Optional.empty()}
     *         otherwise.
     */
    public static List<Optional<String>> splitPreamble(String preamble,
            int numFields) {
        return Arrays
                .stream(Arrays.copyOf(preamble.split("\\s+", numFields),
                        numFields))
                .map(Optional::ofNullable).collect(Collectors.toList());
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if
     * {@code name} is present.
     */
    public static Optional<Name> parseName(Optional<String> name)
            throws IllegalValueException {
        assert name != null;
        return name.isPresent() ? Optional.of(new Name(name.get()))
                : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> deadline} into an
     * {@code Optional<Deadline>} if {@code deadline} is present.
     */
    public static Optional<Deadline> parseDeadline(Optional<String> deadline)
            throws IllegalValueException {
        assert deadline != null;
        return deadline.isPresent() ? Optional.of(new Deadline(deadline.get()))
                : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> frequency} into an
     * {@code Optional<Frequency>} if {@code frequency} is present.
     */
    public static Optional<Frequency> parseFrequency(Optional<String> frequency)
            throws IllegalValueException {
        assert frequency != null;
        return frequency.isPresent()
                ? Optional.of(new Frequency(frequency.get()))
                : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> timestamp} into an
     * {@code Optional<Timestamp>} if {@code timestamp} is present.
     */
    public static Optional<Timestamp> parseTimestamp(Optional<String> timestamp)
            throws IllegalValueException {
        assert timestamp != null;
        return timestamp.isPresent()
                ? Optional.of(new Timestamp(timestamp.get()))
                : Optional.empty();
    }

    /**
     * Parses {@code Collection<String> tags} into an {@code UniqueTagList}.
     */
    public static UniqueTagList parseTags(Collection<String> tags)
            throws IllegalValueException {
        assert tags != null;
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        return new UniqueTagList(tagSet);
    }

    /**
     * Parses a {@code Optional<String> status} into an
     * {@code Optional<Status>} if {@code status} is present.
     */
    public static Optional<Status> parseStatus(
            Optional<String> status) throws IllegalValueException {
        assert status != null;
        return status.isPresent()
                ? Optional.of(new Status(status.get()))
                : Optional.empty();
    }

    //@@author A0139996A
    public static String parseColumnAlphabetOfSortInFind(String command) {
        assert command != null && !command.isEmpty();

        Matcher matcher = SORT_IN_FIND_FORMAT.matcher(command);

        String column = "";

        if (matcher.matches()) {
            column = matcher.group("columnAlphabet");
        }

        return column;
    }

    public static String parseOrderByOfSortInFind(String command) throws IllegalValueException {
        assert command != null && !command.isEmpty();

        Matcher matcher = SORT_IN_FIND_FORMAT.matcher(command);

        String orderBy = "";

        if (matcher.matches()) {
            orderBy = matcher.group("orderBy");

            //Since orderBy is optional, matcher::group may return a null ptr.
            if (orderBy == null) {
                orderBy = "";
            }
        } else {
            throw new IllegalValueException("Sort order must be ASC or DSC");
        }

        return orderBy;
    }
}
