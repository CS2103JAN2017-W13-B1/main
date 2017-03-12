package seedu.utask.model.task;

import seedu.address.model.tag.UniqueTagList;

public class FloatingTask extends Task {

    public FloatingTask(Name name, Frequency frequency, UniqueTagList tags) {
        super(name, frequency, tags);

        fakeDeadline();
        fakeTimestamp();
    }

    /**
     * Creates a copy of the given ReadOnlyPerson.
     */
    public FloatingTask(ReadOnlyTask source) {
        this(source.getName(), source.getFrequency(), source.getTags());
    }

    @Override
    public Deadline getDeadline() {
        return _fakeDeadline;
    }

    @Override
    public Timestamp getTimestamp() {
        return _fakeTimestamp;
    }

    private Deadline _fakeDeadline;

    private void fakeDeadline() {
        try {
            Deadline d = new Deadline("101017");
            _fakeDeadline = d;
        } catch (Exception e) {

        }
    }

    private Timestamp _fakeTimestamp;

    private void fakeTimestamp() {
        try {
            Timestamp t = new Timestamp("from 0000 to 2359");
            _fakeTimestamp = t;
        } catch (Exception e) {

        }
    }
}
