//@@author A0138493W

package utask.commons.comparators;

import java.util.Comparator;

import utask.model.task.ReadOnlyTask;

/**
 * Compares the ReadOnlyTask in descending lexicographical order.
 */
public class DescendingAlphabeticalComparator implements Comparator<ReadOnlyTask> {

    @Override
    public int compare(ReadOnlyTask o1, ReadOnlyTask o2) {
        return o2.getName().toString().compareTo(o1.getName().toString());
    }

}
