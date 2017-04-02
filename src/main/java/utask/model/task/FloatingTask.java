package utask.model.task;

import utask.model.tag.UniqueTagList;

public class FloatingTask extends Task {

    public FloatingTask(Name name, Frequency frequency, UniqueTagList tags,
            IsCompleted isCompleted) {
        super(name, frequency, tags, isCompleted);
    }

    /**
     * Creates a copy of the given ReadOnlyTask.
     */
    public FloatingTask(ReadOnlyTask source) {
        this(source.getName(), source.getFrequency(), source.getTags(),
                source.getIsCompleted());
    }

    public void resetData(ReadOnlyTask replacement) {
        super.resetData(replacement);
    }
}
