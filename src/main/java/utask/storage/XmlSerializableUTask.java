package utask.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utask.commons.core.UnmodifiableObservableList;
import utask.commons.exceptions.IllegalValueException;
import utask.model.ReadOnlyUTask;
import utask.model.tag.Tag;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;

/**
 * An Immutable UTask that is serializable to XML format
 */
@XmlRootElement(name = "utask")
public class XmlSerializableUTask implements ReadOnlyUTask {

    @XmlElement
    private List<XmlAdaptedTask> task;
    @XmlElement
    private List<XmlAdaptedTag> tag;

    /**
     * Creates an empty XmlSerializableUTask.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableUTask() {
        task = new ArrayList<>();
        tag = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableUTask(ReadOnlyUTask src) {
        this();
        task.addAll(src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
        tag.addAll(src.getTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
    }

    @Override
    public ObservableList<ReadOnlyTask> getTaskList() {
        final ObservableList<Task> tasks = this.task.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return new UnmodifiableObservableList<>(tasks);
    }

    @Override
    public ObservableList<Tag> getTagList() {
        final ObservableList<Tag> tags = this.tag.stream().map(t -> {
            try {
                return t.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return new UnmodifiableObservableList<>(tags);
    }

}
