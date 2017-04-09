package utask.logic.commands;

import java.util.logging.Logger;

import utask.commons.core.EventsCenter;
import utask.commons.core.LogsCenter;
import utask.commons.events.ui.ShowTagColorDialogEvent;
import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;
// @@ author A0138423J
public class DeleteTagCommand extends Command implements ReversibleCommand {
    private final Logger logger = LogsCenter.getLogger(DeleteTagCommand.class);
    public static final String COMMAND_WORD = "deletetag";

    public static final String COMMAND_FORMAT = "NAME";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes an existing tag in uTask. " + "Parameters: "
            + COMMAND_FORMAT + "\n" + "Example: " + COMMAND_WORD
            + " Urgent";

    public static final String MESSAGE_SUCCESS = "Tag deleted: %1$s";
    public static final String MESSAGE_MISSING_TAG = "This tag doesn't exist in uTask";

    protected Tag toRemove;
    protected TagName tagName;
    protected TagColorIndex tagColorIndex;
    private String tagToRemove;

    /**
     * Creates an DeleteTagCommand using raw values.
     *
     * @throws IllegalValueException
     *             if any of the raw values are invalid
     */
    public DeleteTagCommand(String tagName) throws IllegalValueException {
        assert tagName != null;
        this.tagName = new TagName(tagName);
        tagToRemove = tagName;
        this.tagColorIndex = new TagColorIndex("");
        this.toRemove = new Tag(this.tagName, this.tagColorIndex);
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            model.deleteTag(toRemove);
            model.addUndoCommand(this);
            EventsCenter.getInstance().post(new ShowTagColorDialogEvent(model.getTags()));
        } catch (Exception e) {
            throw new CommandException(MESSAGE_MISSING_TAG);
        }
        logger.fine(String.format(MESSAGE_SUCCESS, tagToRemove));
        return new CommandResult(String.format(MESSAGE_SUCCESS, tagToRemove));
    }

    @Override
    public void undo() throws Exception {
        assert model != null;
        model.addTag(toRemove);
    }

    @Override
    public void redo() throws Exception {
        assert model != null;
        model.deleteTag(toRemove);
    }

}
