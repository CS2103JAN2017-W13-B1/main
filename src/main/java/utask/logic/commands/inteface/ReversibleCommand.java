package utask.logic.commands.inteface;

//@@author A0139996A
//Commands that can be undo or redo must implement this interface
public interface ReversibleCommand {
    void undo() throws Exception;
    void redo() throws Exception;
}
