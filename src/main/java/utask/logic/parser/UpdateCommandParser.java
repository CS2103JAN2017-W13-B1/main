package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static utask.commons.util.UpdateUtil.TO_BE_REMOVED;
import static utask.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static utask.logic.parser.CliSyntax.PREFIX_DONE;
import static utask.logic.parser.CliSyntax.PREFIX_FREQUENCY;
import static utask.logic.parser.CliSyntax.PREFIX_NAME;
import static utask.logic.parser.CliSyntax.PREFIX_TAG;
import static utask.logic.parser.CliSyntax.PREFIX_TIMESTAMP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.Command;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.UpdateCommand;
import utask.model.tag.UniqueTagList;
import utask.model.task.Attribute;
import utask.model.task.EditTaskDescriptor;

/**
 * Parses input arguments and creates a new EditCommand object
 */
// @@author A0138423J
public class UpdateCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditCommand and returns an EditCommand object for execution.
     */
    public Command parse(String args) {
        assert args != null;
        ArgumentTokenizer argsTokenizer = prepareArgumentTokenizer(args);
        Optional<Integer> index = getTaskIndex(argsTokenizer);
        if (!index.isPresent()) {
            return new IncorrectCommand(MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        EditTaskDescriptor editTaskDescriptor = new EditTaskDescriptor();
        ArrayList<Attribute> attributesToRemove = new ArrayList<Attribute>();
        try {
            editTaskDescriptor = setEditTaskDescriptor(argsTokenizer);
            attributesToRemove = getListOfAttributeToRemove(argsTokenizer);
        } catch (IllegalValueException ive) {
            System.out.println(ive.toString());
            return new IncorrectCommand(ive.getMessage());
        }

        if (!editTaskDescriptor.isAnyFieldEdited()) {
            return new IncorrectCommand(UpdateCommand.MESSAGE_NOT_EDITED);
        }

        return new UpdateCommand(index.get(), editTaskDescriptor,
                attributesToRemove);
    }

    /**
     * Parses the given {@code args} of arguments in the context of the
     * EditCommand and returns an prepared ArgumentTokenizer
     */
    private ArgumentTokenizer prepareArgumentTokenizer(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(PREFIX_NAME,
                PREFIX_DEADLINE, PREFIX_TIMESTAMP, PREFIX_FREQUENCY, PREFIX_TAG,
                PREFIX_DONE);
        argsTokenizer.tokenize(args);
        return argsTokenizer;
    }

    /**
     * Parses {@code argsTokenizer} into {@code Optional<Integer>}. If
     * {@code argsTokenizer} contain only one element which is an empty string,
     * it will be parsed into a {@code Optional<Integer>} containing zero
     * integer.
     */
    private Optional<Integer> getTaskIndex(ArgumentTokenizer argsTokenizer) {
        List<Optional<String>> preambleFields = ParserUtil
                .splitPreamble(argsTokenizer.getPreamble().orElse(""), 2);
        return preambleFields.get(0).flatMap(ParserUtil::parseIndex);
    }

    /**
     * Parses each attribute found in {@code argsTokenizer} into
     * {@code EditTaskDescriptor}.
     */
    private EditTaskDescriptor setEditTaskDescriptor(
            ArgumentTokenizer argsTokenizer) throws IllegalValueException {
        EditTaskDescriptor editTaskDescriptor = new EditTaskDescriptor();
        editTaskDescriptor.setName(
                ParserUtil.parseName(argsTokenizer.getValue(PREFIX_NAME)));
        editTaskDescriptor.setDeadline(ParserUtil
                .parseDeadline(argsTokenizer.getValue(PREFIX_DEADLINE)));
        editTaskDescriptor.setTimeStamp(ParserUtil
                .parseTimestamp(argsTokenizer.getValue(PREFIX_TIMESTAMP)));
        editTaskDescriptor.setTags(parseTagsForEdit(
                ParserUtil.toSet(argsTokenizer.getAllValues(PREFIX_TAG))));
        editTaskDescriptor.setFrequency(ParserUtil
                .parseFrequency(argsTokenizer.getValue(PREFIX_FREQUENCY)));
        editTaskDescriptor.setIsCompleted(
                ParserUtil.parseStatus(argsTokenizer.getValue(PREFIX_DONE)));
        return editTaskDescriptor;
    }

    /**
     * Parses each attribute found in {@code argsTokenizer} checking whether it
     * matches {@code TO_BE_REMOVED}. If any of the inputs matches, the matching
     * ENUM Attribute will be added to {@code ArrayList<Attribute>}.
     */
    private ArrayList<Attribute> getListOfAttributeToRemove(
            ArgumentTokenizer argsTokenizer) {
        ArrayList<Attribute> attributesToRemove = new ArrayList<Attribute>();
        if (argsTokenizer.tryGet(PREFIX_DEADLINE).equals(TO_BE_REMOVED)) {
            attributesToRemove.add(Attribute.DEADLINE);
        }
        if (argsTokenizer.tryGet(PREFIX_TIMESTAMP).equals(TO_BE_REMOVED)) {
            attributesToRemove.add(Attribute.TIMESTAMP);
        }
        if (argsTokenizer.tryGet(PREFIX_TAG).equals(TO_BE_REMOVED)) {
            attributesToRemove.add(Attribute.TAG);
        }
        if (argsTokenizer.tryGet(PREFIX_FREQUENCY).equals(TO_BE_REMOVED)) {
            attributesToRemove.add(Attribute.FREQUENCY);
        }
        return attributesToRemove;
    }

    /**
     * Parses {@code Collection<String> tags} into an
     * {@code Optional<UniqueTagList>} if {@code tags} is non-empty. If
     * {@code tags} contain only one element which is an empty string, it will
     * be parsed into a {@code Optional<UniqueTagList>} containing zero tags.
     */
    private Optional<UniqueTagList> parseTagsForEdit(Collection<String> tags)
            throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("")
                ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
