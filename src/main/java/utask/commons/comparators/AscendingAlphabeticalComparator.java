//@@author A0138493W

package utask.commons.comparators;

import java.util.Comparator;

import utask.model.task.ReadOnlyTask;

/**
 * Compares the ReadOnlyTask in ascending lexicographical order.
 */
public class AscendingAlphabeticalComparator implements Comparator<ReadOnlyTask> {

    @Override
    public int compare(ReadOnlyTask o1, ReadOnlyTask o2) {
        return o1.getName().toString().compareTo(o2.getName().toString());
    }

}
