package utask.staging.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.DeadlineTask;
import utask.model.task.EventTask;
import utask.model.task.FloatingTask;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Timestamp;


//CHECKSTYLE.OFF: LineLength
public class TypicalTaskBuilder {
    public static ObservableList<ReadOnlyTask> due() {
        ObservableList<ReadOnlyTask> tasks = FXCollections.observableArrayList();

        try {
            DeadlineTask f = new DeadlineTask(new Name("Clear emails"), new Deadline("010117"), new Frequency("123"), new UniqueTagList("a", "k"));
            tasks.add(f);
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static ObservableList<ReadOnlyTask> today() {
        ObservableList<ReadOnlyTask> tasks = FXCollections.observableArrayList();

        try {
            DeadlineTask f = new DeadlineTask(new Name("Email Alice"), new Deadline("010417"), new Frequency("-"), new UniqueTagList("asap"));
            EventTask e = new EventTask(new Name("Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur"), new Deadline("050317"), new Timestamp("0000 to 2359"), new Frequency("-"), new UniqueTagList("important"));
            tasks.add(f);
            tasks.add(e);
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public static ObservableList<ReadOnlyTask> todos() {
        ObservableList<ReadOnlyTask> tasks = FXCollections.observableArrayList();

        try {
            FloatingTask f = new FloatingTask(new Name("Take Medicine"), new Frequency("-"), new UniqueTagList("daily"));
            tasks.add(f);
            f = new FloatingTask(new Name("Buy Icecream"), new Frequency("-"), new UniqueTagList());
            tasks.add(f);
            f = new FloatingTask(new Name("Buy more pens"), new Frequency("-"), new UniqueTagList("Important"));
            tasks.add(f);
            f = new FloatingTask(new Name("Buy fruits"), new Frequency("-"), new UniqueTagList());
            tasks.add(f);
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }

        return tasks;
    }
}
