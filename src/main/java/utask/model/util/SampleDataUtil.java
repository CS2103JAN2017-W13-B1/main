//@@author A0138493W

package utask.model.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import utask.model.ReadOnlyUTask;
import utask.model.UTask;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.DeadlineTask;
import utask.model.task.EventTask;
import utask.model.task.FloatingTask;
import utask.model.task.Frequency;
import utask.model.task.IsCompleted;
import utask.model.task.Name;
import utask.model.task.Task;
import utask.model.task.Timestamp;
import utask.model.task.UniqueTaskList.DuplicateTaskException;

public class SampleDataUtil {
    private static final String[] DAY_OF_WEEK = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static List<Task> sampleList = new ArrayList<Task>();

    public static List<Task> getSampleTasks() {
        generateSampleTasks();
        return sampleList;
    }

    public static ReadOnlyUTask getSampleAddressBook() {
        try {
            UTask sampleAB = new UTask();
            for (Task sampleTask : getSampleTasks()) {
                sampleAB.addTask(sampleTask);
            }
            return sampleAB;
        } catch (DuplicateTaskException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }

    private static void generateSampleTasks() {
        for (int i = 1; i <= 20; i++) {
            try {
                sampleList.add(generateEventTaskWithSeed(i));
                sampleList.add(generateDeadlineTaskWithSeed(i));
                sampleList.add(generateFloatingTaskWithSeed(i));
            } catch (Exception e) {
                assert false : "sample data cannot be invalid";
            }
        }
    }

    private static Task generateEventTaskWithSeed(int seed) throws Exception {
        return new EventTask(new Name("Event Task " + seed),
                new Deadline(generateDeadline(seed)), new Timestamp("0000 to 2359"),
                new Frequency("Every " + randomFrequency()),
                new UniqueTagList(new Tag("tag" + Math.abs(seed)),
                        new Tag("tag" + Math.abs(seed + 1))),
                new IsCompleted("no"));
    }

    private static Task generateDeadlineTaskWithSeed(int seed) throws Exception {
        return new DeadlineTask(new Name("Deadline Task " + seed),
                new Deadline(generateDeadline(seed)),
                new Frequency("Every " + randomFrequency()),
                new UniqueTagList(new Tag("tag" + Math.abs(seed)),
                        new Tag("tag" + Math.abs(seed + 1))),
                new IsCompleted("no"));
    }

    private static Task generateFloatingTaskWithSeed(int seed) throws Exception {
        return new FloatingTask(new Name("Floating Task " + seed),
                new Frequency("Every " + randomFrequency()),
                new UniqueTagList(new Tag("tag" + Math.abs(seed)),
                        new Tag("tag" + Math.abs(seed + 1))),
                new IsCompleted("no"));
    }

    private static String generateDeadline(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, (-10 + i));
        DateFormat fmt = new SimpleDateFormat("ddMMyy");
        return fmt.format(calendar.getTime());
    }

    private static String randomFrequency() {
        Random randomGenerator = new Random();
        return DAY_OF_WEEK[randomGenerator.nextInt(6)];
    }
}
