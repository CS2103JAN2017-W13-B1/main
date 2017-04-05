package utask.logic.commands;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.logging.Logger;

import utask.commons.core.LogsCenter;
import utask.commons.exceptions.IllegalValueException;
import utask.commons.util.CollectionUtil;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.tag.Tag;

//@@ author A0138423J
public class UpdateTagCommand  extends Command implements ReversibleCommand {
    private final Logger logger = LogsCenter.getLogger(UpdateTagCommand.class);
    public static final String COMMAND_WORD = "updatetag";

    public static final String COMMAND_FORMAT = "NAME [/name NAME] [/color COLOR]";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a custom tag in uTask. "
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD
            + " Urgent /name VIP /colour blue";

    public static final String MESSAGE_SUCCESS = "Tag created: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG = "This tag already exists in uTask";

    protected Tag toBeEdited, editedTag;
    /**
     * Creates an UpdateTagCommand using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public UpdateTagCommand(Tag toBeEdited, Tag editedTag)
            throws IllegalValueException {
        assert !CollectionUtil.isAnyNull(toBeEdited, editedTag);
        this.toBeEdited = toBeEdited;
        this.editedTag = editedTag;
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            model.updateTag(toBeEdited, editedTag);
            model.addUndoCommand(this);
        } catch (IllegalArgumentException ive) {
            throw new CommandException(String
                    .format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        } catch (Exception e) {
            throw new CommandException(MESSAGE_DUPLICATE_TAG);
        }
        logger.fine(String.format(MESSAGE_SUCCESS, editedTag));
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedTag));
    }

    @Override
    public void undo() throws Exception {
        assert model != null;
        model.updateTag(editedTag, toBeEdited);
    }

    @Override
    public void redo() throws Exception {
        assert model != null;
        model.updateTag(toBeEdited, editedTag);
    }

}
