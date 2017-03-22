package utask.logic.parser;

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
import utask.model.task.IsCompleted;
import utask.model.task.Name;
import utask.model.task.Timestamp;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes
 */
public class ParserUtil {

    private static final Pattern INDEX_ARGS_FORMAT = Pattern
            .compile("(?<targetIndex>.+)");

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

    /**
     * Returns a new Set populated by all elements in the given list of strings
     * Returns an empty set if the given {@code Optional} is empty, or if the
     * list contained in the {@code Optional} is empty
     */
    public static Set<String> toSet(Optional<List<String>> list) {
        List<String> elements = list.orElse(Collections.emptyList());
        return new HashSet<>(elements);
    }

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

    //@@author A0138423J
    /**
     * Parses a {@code Optional<String> iscompleted} into an
     * {@code Optional<IsCompleted>} if {@code iscompleted} is present.
     */
    public static Optional<IsCompleted> parseIsCompleted(
            Optional<String> iscompleted) throws IllegalValueException {
        assert iscompleted != null;
        return iscompleted.isPresent()
                ? Optional.of(new IsCompleted(iscompleted.get()))
                : Optional.empty();
    }
}
