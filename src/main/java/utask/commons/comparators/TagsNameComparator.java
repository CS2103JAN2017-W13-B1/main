//@@author A0138493W

package utask.commons.comparators;

import java.util.Comparator;

import utask.model.task.ReadOnlyTask;

/**
 * Compares the ReadOnlyTask in ascending lexicographical Tag order.
 */
public class TagsNameComparator implements Comparator<ReadOnlyTask> {

    @Override
    public int compare(ReadOnlyTask o1, ReadOnlyTask o2) {
        return o1.getTags().getFirstTagName().compareTo(o2.getTags().getFirstTagName());
    }

}
