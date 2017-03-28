package utask.logic.commands.inteface;

public interface ReversibleCommand {
    void undo() throws Exception;
    void redo() throws Exception;
}
