//@@author A0138493W

package utask.commons.comparators;

import java.text.ParseException;
import java.util.Comparator;

import utask.model.task.ReadOnlyTask;

/**
 * Compares the ReadOnlyTask in ascending dates.
 */
public class EarliestFirstComparator implements Comparator<ReadOnlyTask> {

    @Override
    public int compare(ReadOnlyTask o1, ReadOnlyTask o2) {
        int preTime = 0;
        if (!o1.getDeadline().isEmpty()) {
            try {
                preTime = (int) (o1.getDeadline().getDate().getTime() / 1000);
            } catch (ParseException e) {
                assert false : "Should never come to this catch";
            }
        }

        int nextTime = 0;
        if (!o2.getDeadline().isEmpty()) {
            try {
                nextTime = (int) (o2.getDeadline().getDate().getTime() / 1000);
            } catch (ParseException e) {
                assert false : "Should never come to this catch";
            }
        }

        return preTime - nextTime;
    }
}
