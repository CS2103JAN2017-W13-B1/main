package utask.model.tag;

import java.util.Objects;

import utask.commons.util.CollectionUtil;
import utask.ui.helper.TagColorHelper.ColorType;

// @@author A0138423J
/**
 * Represents a Tag in the address book. Guarantees: immutable; name is valid as
 * declared in {@link #isValidTagName(String)}
 */
public class Tag {

    protected TagName tagName;
    protected TagColorIndex tagColorIndex;

    /**
     * Every field must be present and not null.
     */
    public Tag(TagName tagName, TagColorIndex tagColorIndex) {
        assert !CollectionUtil.isAnyNull(tagName, tagColorIndex);

        this.tagName = tagName;
        this.tagColorIndex = tagColorIndex;
    }

    public Tag(Tag tag) {
        assert tag != null;

        this.tagName = tag.getTagName();
        this.tagColorIndex = tag.getTagColorIndex();
    }

    public Tag() {
    }

    public TagName getTagName() {
        return tagName;
    }

    public void setTagName(TagName tagname) {
        this.tagName = tagname;
    }

    public TagColorIndex getTagColorIndex() {
        return tagColorIndex;
    }

    public void setTagColorIndex(TagColorIndex tagcolorindex) {
        this.tagColorIndex = tagcolorindex;
    }

    public void setTag(Tag updatedTag) {
        this.tagName = updatedTag.tagName;
        this.tagColorIndex = updatedTag.tagColorIndex;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                        && this.isSameStateAs((Tag) other));
    }

    private boolean isSameStateAs(Tag other) {
        return other.getTagName().equals(this.getTagName());
        // && other.getTagcolorindex().equals(this.getTagcolorindex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName.toString() + ']' + '['
                + ColorType.values()[tagColorIndex.getTagColorIndexAsInt()]
                        .name()
                + ']';
    }

}
