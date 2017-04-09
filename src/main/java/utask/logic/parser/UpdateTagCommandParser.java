package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static utask.logic.parser.CliSyntax.PREFIX_NAME;
import static utask.logic.parser.CliSyntax.PREFIX_TAGCOLOR;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.Command;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.UpdateTagCommand;
import utask.model.tag.EditTagDescriptor;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;

//@@ author A0138423J
public class UpdateTagCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditCommand and returns an EditCommand object for execution.
     */
    public Command parse(String args) {
        assert args != null;
        ArgumentTokenizer argsTokenizer = prepareArgumentTokenizer(args);
        String oldTaskKey = argsTokenizer.getPreamble().get();
        Tag toBeEdited = null;
        EditTagDescriptor editTagDescriptor = new EditTagDescriptor();

        if ("".equals(oldTaskKey) || oldTaskKey == null) {
            return new IncorrectCommand(MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        try {
            toBeEdited = new Tag(new TagName(oldTaskKey),
                    new TagColorIndex(""));
            editTagDescriptor = setEditTagDescriptor(argsTokenizer);
            Tag latestTag = createdLatestTag(toBeEdited, editTagDescriptor);
            return new UpdateTagCommand(toBeEdited, latestTag);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Parses the given {@code args} of arguments in the context of the
     * UpdateCommand and returns an prepared ArgumentTokenizer
     */
    private ArgumentTokenizer prepareArgumentTokenizer(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(PREFIX_NAME,
                PREFIX_TAGCOLOR);
        argsTokenizer.tokenize(args);
        return argsTokenizer;
    }

    /**
     * Parses each attribute found in {@code argsTokenizer} into
     * {@code EditTagDescriptor}.
     */
    private EditTagDescriptor setEditTagDescriptor(
            ArgumentTokenizer argsTokenizer) throws IllegalValueException {
        EditTagDescriptor editTagDescriptor = new EditTagDescriptor();
        editTagDescriptor.setTagName(
                ParserUtil.parseTagName(argsTokenizer.getValue(PREFIX_NAME)));
        editTagDescriptor.setTagColor(ParserUtil
                .parseColorIndex(argsTokenizer.getValue(PREFIX_TAGCOLOR)));
        return editTagDescriptor;
    }

    /**
     * Create latest tag by taking inputs from {@code Tag} and
     * {@code EditTagDescriptor}.
     */
    private Tag createdLatestTag(Tag toBeEdited, EditTagDescriptor editedTag) {
        assert toBeEdited != null;
        assert editedTag != null;
        Tag tempTag = new Tag();
        tempTag.setTagName(updateOrRetainTagName(toBeEdited, editedTag));
        tempTag.setTagColorIndex(
                updateOrRetainTagColorIndex(toBeEdited, editedTag));
        return tempTag;
    }

    /**
     * If {@code editedTag} has new name, return it.
     * Else return {@code toBeEdited} old name.
     */
    private TagName updateOrRetainTagName(Tag toBeEdited,
            EditTagDescriptor editedTag) {
        if (editedTag.getTagName().isPresent()) {
            return editedTag.getTagName().get();
        } else {
            return toBeEdited.getTagName();
        }
    }

    /**
     * If {@code editedTag} has new color, return it.
     * Else return {@code toBeEdited} old name.
     */
    private TagColorIndex updateOrRetainTagColorIndex(Tag toBeEdited,
            EditTagDescriptor editedTag) {
        if (editedTag.getTagColor().isPresent()) {
            return editedTag.getTagColor().get();
        } else {
            return toBeEdited.getTagColorIndex();
        }
    }
}
