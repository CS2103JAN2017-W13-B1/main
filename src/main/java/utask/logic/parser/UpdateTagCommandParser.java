package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static utask.logic.parser.CliSyntax.PREFIX_NAME;
import static utask.logic.parser.CliSyntax.PREFIX_TAGCOLOR;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.Command;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.UpdateTagCommand;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;

public class UpdateTagCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditCommand and returns an EditCommand object for execution.
     */
    public Command parse(String args) {
        assert args != null;
        ArgumentTokenizer argsTokenizer = prepareArgumentTokenizer(args);
        String oldTaskKey = argsTokenizer.getPreamble().get();
        Tag toBeEdited, editedTag = null;

        System.out.println("".equals(oldTaskKey));
        System.out.println(oldTaskKey == null);

        if ("".equals(oldTaskKey) || oldTaskKey == null) {
            return new IncorrectCommand(MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        try {
            System.out.println();
            System.out.println(oldTaskKey);
            System.out.println(argsTokenizer.tryGet(PREFIX_NAME));
            System.out.println(argsTokenizer.tryGet(PREFIX_TAGCOLOR));

            toBeEdited = new Tag(new TagName(oldTaskKey),
                    new TagColorIndex(""));
            editedTag = new Tag(
                    new TagName(argsTokenizer.tryGet(PREFIX_NAME)),
                    new TagColorIndex(argsTokenizer.tryGet(PREFIX_TAGCOLOR)));
            Tag latestFile = createdLatestFile(toBeEdited, editedTag);
            return new UpdateTagCommand(toBeEdited, latestFile);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Parses the given {@code args} of arguments in the context of the
     * EditCommand and returns an prepared ArgumentTokenizer
     */
    private ArgumentTokenizer prepareArgumentTokenizer(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(PREFIX_NAME,
                PREFIX_TAGCOLOR);
        argsTokenizer.tokenize(args);
        return argsTokenizer;
    }

    private Tag createdLatestFile(Tag toBeEdited, Tag editedTag) {
        if ("".equals(editedTag.getTagname())) {
            editedTag.setTagname(toBeEdited.getTagname());
        }
        if ("".equals(editedTag.getTagcolorindex())) {
            editedTag.setTagcolorindex(toBeEdited.getTagcolorindex());
        }
        return editedTag;
    }
}
