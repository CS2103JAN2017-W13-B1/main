package utask.staging.ui.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.DeadlineTask;
import utask.model.task.EventTask;
import utask.model.task.Frequency;
import utask.model.task.IsCompleted;
import utask.model.task.Name;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Timestamp;

//DEMO CLASS
//CHECKSTYLE.OFF: LineLength
public class TypicalTaskBuilder {
    public static ObservableList<ReadOnlyTask> due() {
        ObservableList<ReadOnlyTask> tasks = FXCollections.observableArrayList();

        try {
            DeadlineTask f = new DeadlineTask(new Name("Update John on project specs"), new Deadline("010317"), Frequency.getEmptyFrequency(), new UniqueTagList("Work", "Urgent"), new IsCompleted("y"));
            tasks.add(f);
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static ObservableList<ReadOnlyTask> today() {
        ObservableList<ReadOnlyTask> tasks = FXCollections.observableArrayList();

        try {
            DeadlineTask f = new DeadlineTask(new Name("Help Alice with MSWord"), new Deadline("010417"), Frequency.getEmptyFrequency(), new UniqueTagList("Asap"), new IsCompleted("y"));
            EventTask e = new EventTask(new Name("Dinner with friends"), new Deadline("010417"), new Timestamp("1730 to 2200"), new Frequency("Every Monday"), new UniqueTagList("Life", "NonWork"), new IsCompleted("y"));
            tasks.add(f);
            tasks.add(e);
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }

        return tasks;
    }

//    public static ObservableList<ReadOnlyTask> todos() {
//        ObservableList<ReadOnlyTask> tasks = FXCollections.observableArrayList();
//
//        try {
//            FloatingTask f = new FloatingTask(new Name("Take Medicine"), new Frequency("-"), new UniqueTagList("daily"));
//            tasks.add(f);
//            f = new FloatingTask(new Name("Buy Icecream"), new Frequency("-"), new UniqueTagList());
//            tasks.add(f);
//            f = new FloatingTask(new Name("Buy more pens"), new Frequency("-"), new UniqueTagList("Important"));
//            tasks.add(f);
//            f = new FloatingTask(new Name("Buy fruits"), new Frequency("-"), new UniqueTagList());
//            tasks.add(f);
//        } catch (IllegalValueException e) {
//            e.printStackTrace();
//        }
//
//        return tasks;
//    }
}