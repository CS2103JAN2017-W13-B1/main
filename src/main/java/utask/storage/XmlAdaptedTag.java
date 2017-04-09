package utask.storage;

import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlValue;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;

/**
 * JAXB-friendly adapted version of the Tag.
 */
public class XmlAdaptedTag {

//    @XmlValue
//    public String tagName;
    @XmlElement(required = true)
    private String tagName;
    @XmlElement(required = true)
    private String tagColorIndex;

    /**
     * Constructs an XmlAdaptedTag.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTag() {}

    /**
     * Converts a given Tag into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedTag(Tag source) {
        tagName = source.getTagName().toString();
        tagColorIndex = source.getTagColorIndex().toString();
    }

    /**
     * Converts this jaxb-friendly adapted tag object into the model's Tag object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
    public Tag toModelType() throws IllegalValueException {
        return new Tag(new TagName(tagName), new TagColorIndex(tagColorIndex));
    }

}
