package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static utask.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static utask.logic.parser.CliSyntax.PREFIX_FREQUENCY;
import static utask.logic.parser.CliSyntax.PREFIX_NAME;
import static utask.logic.parser.CliSyntax.PREFIX_TAG;
import static utask.logic.parser.CliSyntax.PREFIX_TIMESTAMP;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.Command;
import utask.logic.commands.EditCommand;
import utask.logic.commands.EditCommand.EditTaskDescriptor;
import utask.logic.commands.IncorrectCommand;
import utask.model.tag.UniqueTagList;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     */
    public Command parse(String args) {
        assert args != null;
        ArgumentTokenizer argsTokenizer =
                new ArgumentTokenizer(PREFIX_NAME, PREFIX_DEADLINE, PREFIX_TIMESTAMP, PREFIX_FREQUENCY, PREFIX_TAG);
        argsTokenizer.tokenize(args);
        List<Optional<String>> preambleFields = ParserUtil.splitPreamble(argsTokenizer.getPreamble().orElse(""), 2);

        Optional<Integer> index = preambleFields.get(0).flatMap(ParserUtil::parseIndex);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        EditTaskDescriptor editTaskDescriptor = new EditTaskDescriptor();
        try {
            editTaskDescriptor.setName(ParserUtil.parseName(argsTokenizer.getValue(PREFIX_NAME)));
            editTaskDescriptor.setDeadline(ParserUtil.parseDeadline(argsTokenizer.getValue(PREFIX_DEADLINE)));
            editTaskDescriptor.setTimeStamp(ParserUtil.parseTimestamp(argsTokenizer.getValue(PREFIX_TIMESTAMP)));
            editTaskDescriptor.setFrequency(ParserUtil.parseFrequency(argsTokenizer.getValue(PREFIX_FREQUENCY)));
            editTaskDescriptor.setTags(parseTagsForEdit(ParserUtil.toSet(argsTokenizer.getAllValues(PREFIX_TAG))));
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }

        if (!editTaskDescriptor.isAnyFieldEdited()) {
            return new IncorrectCommand(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index.get(), editTaskDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into an {@code Optional<UniqueTagList>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Optional<UniqueTagList>} containing zero tags.
     */
    private Optional<UniqueTagList> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}