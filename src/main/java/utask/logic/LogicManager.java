package utask.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import utask.commons.core.ComponentManager;
import utask.commons.core.LogsCenter;
import utask.logic.commands.Command;
import utask.logic.commands.CommandResult;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.parser.Parser;
import utask.model.Model;
import utask.model.task.ReadOnlyTask;
import utask.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        return command.execute();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredPersonList() {
        return model.getFilteredTaskList();
    }
}
