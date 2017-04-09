package utask.model.tag;

import java.util.Objects;

import utask.commons.util.CollectionUtil;
import utask.ui.helper.TagColorHelper;

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

        this.tagname = tag.getTagName();
        this.tagcolorindex = tag.getTagColorIndex();
        tagCount = 0;
    }

    public Tag() {
    }

    public TagName getTagName() {
        return tagname;
    }

    public void setTagName(TagName tagname) {
        this.tagname = tagname;
    }

    public TagColorIndex getTagColorIndex() {
        return tagcolorindex;
    }

    public void setTagColorIndex(TagColorIndex tagcolorindex) {
        this.tagcolorindex = tagcolorindex;
    }

    public Integer getTagCount() {
        return tagCount;
    }

    public void setTagCount(Integer tagCount) {
        this.tagCount = tagCount;
    }

    public void setTag(Tag updatedTag) {
        this.tagname = updatedTag.tagname;
        this.tagcolorindex = updatedTag.tagcolorindex;
        this.tagCount = updatedTag.tagCount;
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
        return Objects.hash(tagname);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagname.toString() + ']' + '[' + TagColorHelper
                .getColorValueFromIndex(tagcolorindex.getTagColorIndexAsInt())
                + ']';
    }

}
