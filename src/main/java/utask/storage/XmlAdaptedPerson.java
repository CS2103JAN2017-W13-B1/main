package utask.storage;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList;
import utask.model.tag.UniqueTagList.DuplicateTagException;
import utask.model.task.Deadline;
import utask.model.task.DeadlineTask;
import utask.model.task.EventTask;
import utask.model.task.FloatingTask;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.Timestamp;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedPerson {

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String deadline;
    @XmlElement(required = true)
    private String timestamp;
    @XmlElement(required = true)
    private String frequency;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPerson() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedPerson(ReadOnlyTask source) {
        name = source.getName().fullName;
        deadline = source.getDeadline().value;
        timestamp = source.getTimestamp().value;
        frequency = source.getFrequency().value;
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Task toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();

        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        return getTaskTypeFromParams(personTags);
    }


    private Task getTaskTypeFromParams(final List<Tag> personTags) throws IllegalValueException, DuplicateTagException {
        final Name name = new Name(this.name);
        final Deadline deadline;
        final Timestamp timestamp;
        final Frequency frequency;
        final UniqueTagList tags = new UniqueTagList(personTags);

        //TODO: Do a helper/factory to spawn necessary objects
        if ("".equals(this.deadline)) {
            deadline = Deadline.getEmptyDeadline();
        } else {
            deadline = new Deadline(this.deadline);
        }

        if ("".equals(this.timestamp)) {
            timestamp = Timestamp.getEmptyTimestamp();
        } else {
            timestamp = new Timestamp(this.timestamp);
        }

        if ("".equals(this.frequency)) {
            frequency = Frequency.getEmptyFrequency();
        } else {
            frequency = new Frequency(this.frequency);
        }

        if (!"".equals(this.deadline) && !"".equals(this.timestamp)) {
            return new EventTask(name, deadline, timestamp, frequency, tags);
        } else if ("".equals(this.timestamp)) {
            return new DeadlineTask(name, deadline, frequency, tags);
        } else {
            return new FloatingTask(name, frequency, tags);
        }
    }
}
