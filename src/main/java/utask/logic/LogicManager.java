package utask.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import utask.commons.core.ComponentManager;
import utask.commons.core.LogsCenter;
import utask.logic.commands.Command;
import utask.logic.commands.CommandResult;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.parser.Parser;
import utask.model.AliasCommandMap;
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
        AliasCommandMap.getInstance().setAliasCommandMap(model.getUserPrefs().getAliasMap());
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
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }

    //@@author A0139996A
    @Override
    public ObservableList<ReadOnlyTask> getDueFilteredTaskList() {
        return model.getDueFilteredTaskList();
    }

    @Override
    public ObservableList<ReadOnlyTask> getTodayFilteredTaskList() {
        return model.getTodayFilteredTaskList();
    }

    @Override
    public ObservableList<ReadOnlyTask> getTomorrowFilteredTaskList() {
        return model.getTomorrowFilteredTaskList();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFutureFilteredTaskList() {
        return model.getFutureFilteredTaskList();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFloatingFilteredTaskList() {
        return model.getFloatingFilteredTaskList();
    }

    //TODO: too many of this?
    @Override
    public void setIfFindOverlayShowing(boolean isShowing) {
        model.setIfFindOverlayShowing(isShowing);
    }
}
