package utask.logic.commands;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;
// @@ author A0138423J
public class DeleteTagCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "deletetag";

    public static final String COMMAND_FORMAT = "NAME";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes an existing tag in uTask. " + "Parameters: "
            + COMMAND_FORMAT + "\n" + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Tag deleted: %1$s";
    public static final String MESSAGE_MISSING_TAG = "This tag doesn't exist in uTask";

    protected Tag toRemove;
    protected TagName tagName;
    protected TagColorIndex tagColorIndex;

    /**
     * Creates an DeleteTagCommand using raw values.
     *
     * @throws IllegalValueException
     *             if any of the raw values are invalid
     */
    public DeleteTagCommand(String tagName) throws IllegalValueException {
        assert tagName != null;
        this.tagName = new TagName(tagName);
        this.tagColorIndex = new TagColorIndex("");
        this.toRemove = new Tag(this.tagName, this.tagColorIndex);
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            model.deleteTag(toRemove);
            model.addUndoCommand(this);

            return new CommandResult(String.format(MESSAGE_SUCCESS, toRemove));
        } catch (Exception e) {
            throw new CommandException(MESSAGE_MISSING_TAG);
        }
    }

    @Override
    public void undo() throws Exception {
        // TODO Add after deleteTag Command is done
    }

    @Override
    public void redo() throws Exception {
        // TODO Auto-generated method stub
    }

}
