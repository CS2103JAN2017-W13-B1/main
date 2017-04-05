package utask.logic.commands;

import java.util.logging.Logger;

import utask.commons.core.LogsCenter;

//import utask.commons.core.EventsCenter;
//import utask.commons.events.ui.ShowTaskOfInterestEvent;

import utask.commons.exceptions.IllegalValueException;
import utask.commons.util.CollectionUtil;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;
// @@ author A0138423J
public class CreateTagCommand extends Command  implements ReversibleCommand {
    private final Logger logger = LogsCenter.getLogger(CreateTagCommand.class);
    public static final String COMMAND_WORD = "createtag";

    public static final String COMMAND_FORMAT = "NAME [/color COLOR]";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a custom tag in uTask. "
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD
            + " Urgent /colour blue";

    public static final String MESSAGE_SUCCESS = "New tag created: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG = "This tag already exists in uTask";

    protected Tag toAdd;
    protected TagName tagName;
    protected TagColorIndex tagColorIndex;
    /**
     * Creates an CreateTagCommand using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public CreateTagCommand(String tagName, String tagColorIndex)
            throws IllegalValueException {
        assert !CollectionUtil.isAnyNull(tagName, tagColorIndex);
        this.tagName = new TagName(tagName);
        this.tagColorIndex = new TagColorIndex(tagColorIndex);
        this.toAdd = new Tag(this.tagName, this.tagColorIndex);
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            model.addTag(toAdd);
            model.addUndoCommand(this);
        } catch (Exception e) {
            throw new CommandException(MESSAGE_DUPLICATE_TAG);
        }
        logger.fine(String.format(MESSAGE_SUCCESS, toAdd));
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public void undo() throws Exception {
        assert model != null;
        model.deleteTag(toAdd);
    }

    @Override
    public void redo() throws Exception {
        assert model != null;
        model.addTag(toAdd);
    }

}
