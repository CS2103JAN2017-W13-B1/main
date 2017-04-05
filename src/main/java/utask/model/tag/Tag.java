package utask.model.tag;

import java.util.Objects;

import utask.commons.util.CollectionUtil;
// @@author A0138423J
/**
 * Represents a Tag in the address book. Guarantees: immutable; name is valid as
 * declared in {@link #isValidTagName(String)}
 */
public class Tag {

    protected TagName tagname;
    protected TagColorIndex tagcolorindex;
    protected Integer tagCount;

    /**
     * Every field must be present and not null.
     */
    public Tag(TagName tagName, TagColorIndex tagColorIndex) {
        assert !CollectionUtil.isAnyNull(tagName, tagColorIndex);

        this.tagname = tagName;
        this.tagcolorindex = tagColorIndex;
        tagCount = 0;
    }

    public Tag(Tag tag) {
        assert tag != null;

        this.tagname = tag.getTagname();
        this.tagcolorindex = tag.getTagcolorindex();
        tagCount = 0;
    }

    public TagName getTagname() {
        return tagname;
    }

    public void setTagname(TagName tagname) {
        this.tagname = tagname;
    }

    public TagColorIndex getTagcolorindex() {
        return tagcolorindex;
    }

    public void setTagcolorindex(TagColorIndex tagcolorindex) {
        this.tagcolorindex = tagcolorindex;
    }

    public Integer getTagCount() {
        return tagCount;
    }

    public void setTagCount(Integer tagCount) {
        this.tagCount = tagCount;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && this.isSameStateAs((Tag) other));
    }

    private boolean isSameStateAs(Tag other) {
        return other.getTagname().equals(this.getTagname());
//                        && other.getTagcolorindex().equals(this.getTagcolorindex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagname);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagname.toString() + ']' + '[' + tagcolorindex.toString() + ']';
    }

}
