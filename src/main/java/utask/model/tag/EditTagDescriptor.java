package utask.model.tag;

import java.util.Optional;

import utask.commons.util.CollectionUtil;

public class EditTagDescriptor {
    private Optional<TagName> tagName = Optional.empty();
    private Optional<TagColorIndex> tagColor = Optional.empty();

    public EditTagDescriptor() {
    }

    public EditTagDescriptor(EditTagDescriptor toCopy) {
        tagName = toCopy.getTagName();
        tagColor = toCopy.getTagColor();
    }

    /**
     * Returns true if at least one field is edited.
     */
    public boolean isAnyFieldEdited() {
        return CollectionUtil.isAnyPresent(tagName, tagColor);
    }

    public void setTagName(Optional<TagName> name) {
        assert name != null;
        tagName = name;
    }

    public Optional<TagName> getTagName() {
        return tagName;
    }

    public void setTagColor(Optional<TagColorIndex> color) {
        assert color != null;
        tagColor = color;
    }

    public Optional<TagColorIndex> getTagColor() {
        return tagColor;
    }

}
