package utask.model.task;

import java.text.ParseException;
import java.util.Date;

//@@author A0138423J
public class DeadlineStub extends AbsDeadline {

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Date getDate() throws ParseException {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
