package utask.testutil;

import utask.commons.exceptions.IllegalValueException;
import utask.model.UTask;
import utask.model.task.EventTask;
import utask.model.task.UniqueTaskList;

/**
 *
 */
public class TypicalTask {

    public TestTask a, b, c, d, e, f, g, h, i, dueTask, todayTask, tomorrowTask, futureTask, todoTask;

    public TypicalTask() {
        try {
            a = new TaskBuilder().withName("Ascertain work has been complete")
                    .withFrequency("Every Friday").withTimestamp("011217", "0800 to 2000")
                    .withDeadline("010115")
                    .withStatus("incomplete")
                    .withTags("important").build();
            b = new TaskBuilder().withName("Busy with project").withFrequency("-")
                    .withTimestamp("021217", "0800 to 1230").withDeadline("021217")
                    .withTags("busy", "important").withStatus("incomplete").build();
            c = new TaskBuilder().withName("Cook Dinner Tonight").withDeadline("031217")
                    .withTimestamp("031217", "1500 to 1830").withFrequency("-")
                    .withStatus("incomplete").build();
            d = new TaskBuilder().withName("Dinner with Alice").withDeadline("041217")
                    .withTimestamp("041217", "1600 to 1800").withFrequency("-")
                    .withStatus("incomplete").build();
            e = new TaskBuilder().withName("Eat at Mcdonalds").withDeadline("051217")
                    .withTimestamp("051217", "1700 to 1800").withFrequency("-")
                    .withStatus("incomplete").build();
            f = new TaskBuilder().withName("Free").withDeadline("061217")
                    .withTimestamp("061217", "1800 to 2359").withFrequency("Every Year")
                    .withStatus("incomplete").build();
            g = new TaskBuilder().withName("Go out with Alice").withDeadline("071217")
                    .withTimestamp("071217", "1900 to 2000").withFrequency("-")
                    .withStatus("incomplete").build();

            // Manually added
            h = new TaskBuilder().withName("Help Alice with project").withDeadline("081217")
                    .withTimestamp("081217", "0900 to 1300").withFrequency("-").withStatus("incomplete").build();
            i = new TaskBuilder().withName("Inspect Goods").withDeadline("091217")
                    .withTimestamp("091217", "0900 to 1300").withFrequency("-").withStatus("incomplete").build();

            dueTask = new TaskBuilder().withName("Forgot my work")
                    .withStatus("incomplete").withDeadline("010217").build();
            todayTask = new TaskBuilder().withName("Do this today")
                    .withStatus("incomplete").withDeadline("today").build();
            tomorrowTask = new TaskBuilder().withName("Do this tomorrow")
                    .withStatus("incomplete").withDeadline("today").build();
            futureTask = new TaskBuilder().withName("Do this in year 2020")
                    .withStatus("incomplete").withDeadline("010120").build();
            todoTask = new TaskBuilder().withName("Do tests").withStatus("incomplete").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadUTaskWithSampleData(UTask ab) {
        for (TestTask task : new TypicalTask().getTypicalTasks()) {
            try {
                ab.addTask(new EventTask(task));
            } catch (UniqueTaskList.DuplicateTaskException e) {
                assert false : "not possible";
            }
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{a, b, c, d, e, f, g};
    }

    public UTask getTypicalAddressBook() {
        UTask ab = new UTask();
        loadUTaskWithSampleData(ab);
        return ab;
    }
}
