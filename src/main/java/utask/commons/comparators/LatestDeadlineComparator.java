//@@author A0138493W

package utask.commons.comparators;

import java.util.Comparator;

import utask.model.task.ReadOnlyTask;

/**
 * Compares the ReadOnlyTask in descending dates.
 */
public class LatestDeadlineComparator implements Comparator<ReadOnlyTask> {

    @Override
    public int compare(ReadOnlyTask o1, ReadOnlyTask o2) {
        int preTime = 0;
        if (!o1.getDeadline().isEmpty()) {
            preTime = (int) (o1.getDeadline().getDate().getTime() / 1000);
        }

        int nextTime = 0;
        if (!o2.getDeadline().isEmpty()) {
            nextTime = (int) (o2.getDeadline().getDate().getTime() / 1000);
        }

        return nextTime - preTime;
    }
}
