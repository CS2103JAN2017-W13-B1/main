package utask.model.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utask.commons.core.UnmodifiableObservableList;
import utask.commons.exceptions.DuplicateDataException;
import utask.commons.exceptions.IllegalValueException;
import utask.commons.util.CollectionUtil;

/**
 * A list of tags that enforces no nulls and uniqueness between its elements.
 *
 * Supports minimal set of list operations for the app's features.
 *
 * @see Tag#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTagList implements Iterable<Tag> {

    private final ObservableList<Tag> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty TagList.
     */
    public UniqueTagList() {}

    /**
     * Creates a UniqueTagList using given String tags.
     * Enforces no nulls or duplicates.
     */
    public UniqueTagList(String... tags) throws DuplicateTagException, IllegalValueException {
        final List<Tag> tagList = new ArrayList<Tag>();
        for (String tag : tags) {
            tagList.add(new Tag(new TagName(tag), new TagColorIndex("2")));
        }
        setTags(tagList);
    }

    /**
     * Creates a UniqueTagList using given tags.
     * Enforces no nulls or duplicates.
     */
    public UniqueTagList(Tag... tags) throws DuplicateTagException {
        assert !CollectionUtil.isAnyNull((Object[]) tags);
        final List<Tag> initialTags = Arrays.asList(tags);
        if (!CollectionUtil.elementsAreUnique(initialTags)) {
            throw new DuplicateTagException();
        }
        internalList.addAll(initialTags);
    }

    /**
     * Creates a UniqueTagList using given tags.
     * Enforces no null or duplicate elements.
     */
    public UniqueTagList(Collection<Tag> tags) throws DuplicateTagException {
        this();
        setTags(tags);
    }

    /**
     * Creates a UniqueTagList using given tags.
     * Enforces no nulls.
     */
    public UniqueTagList(Set<Tag> tags) {
        assert !CollectionUtil.isAnyNull(tags);
        internalList.addAll(tags);
    }

    /**
     * Creates a copy of the given list.
     * Insulates from changes in source.
     */
    public UniqueTagList(UniqueTagList source) {
        internalList.addAll(source.internalList); // insulate internal list from changes in argument
    }

    /**
     * Returns all tags in this list as a Set.
     * This set is mutable and change-insulated against the internal list.
     */
    public Set<Tag> toSet() {
        return new HashSet<>(internalList);
    }

    public String getAllTagNames() {
        String allTagNames = "";
        for (Tag tag:internalList) {
            allTagNames += tag.tagname + " ";
        }
        return allTagNames;
    }

    public String getFirstTagName() {
        if (internalList.isEmpty()) {
            return "z";
        } else {
            return internalList.get(0).toString();
        }
    }

    /**
     * Replaces the Tags in this list with those in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setTags(Collection<Tag> tags) throws DuplicateTagException {
        assert !CollectionUtil.isAnyNull(tags);
        if (!CollectionUtil.elementsAreUnique(tags)) {
            throw new DuplicateTagException();
        }
        internalList.setAll(tags);
    }

    /**
     * Ensures every tag in the argument list exists in this object.
     */
    public void mergeFrom(UniqueTagList from) {
        final Set<Tag> alreadyInside = this.toSet();
        from.internalList.stream()
                .filter(tag -> !alreadyInside.contains(tag))
                .forEach(internalList::add);
    }

    /**
     * Returns true if the list contains an equivalent Tag as the given argument.
     */
    public boolean contains(Tag toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    // @ author A0138423J
    /**
     * Adds a Tag to the list.
     *
     * @throws DuplicateTagException if the Tag to add is a duplicate of an existing Tag in the list.
     */
    public void add(Tag toAdd) throws DuplicateTagException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTagException();
        }
        internalList.add(toAdd);
    }

    public void delete(Tag toAdd) {
        assert toAdd != null;
        if (contains(toAdd)) {
            internalList.remove(internalList.indexOf(toAdd));
        } else {
            // TODO
            // throw error that tag not found/
        }
    }

    // @@ author
    @Override
    public Iterator<Tag> iterator() {
        return internalList.iterator();
    }

    public UnmodifiableObservableList<Tag> asObservableList() {
        return new UnmodifiableObservableList<>(internalList);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTagList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTagList) other).internalList));
    }

    public boolean equalsOrderInsensitive(UniqueTagList other) {
        return this == other || new HashSet<>(this.internalList).equals(new HashSet<>(other.internalList));
    }

    public List<Tag> getAsListTag() {
        ArrayList<Tag> listOfTags = new ArrayList<Tag>();
        for (Tag x : internalList) {
            listOfTags.add(x);
        }
        return listOfTags;
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateTagException extends DuplicateDataException {
        protected DuplicateTagException() {
            super("Operation would result in duplicate tags");
        }
    }

}
