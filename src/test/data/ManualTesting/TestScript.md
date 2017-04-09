# Test Script (for Manual Testing)

## Loading the sample data

1. Ensure that you put `SampleData.xml` inside the `data\` folder (if the `data` directory does not exist inside the directory where the app is located, create it), and rename it as `utask.xml`
2. Start `[W13-B1][UTask].jar` by double clicking it.

**Result:** The task list is loaded.

## Open help

1. Type `help`
2. Close `help` window

**Result:** A new window opened with title "μTask - User Guide", listing out all the command available, user may refer to this page for commands

## Show suggestions

1. Type `?` (Do not hit `Enter`)

**Result:** All the available commands will be shown as suggestions

## Get previous entered command

1. Press `up-arrow` to retrieve previous entered commands

**Result:** Note that you can use `up-arrow`, and `down-arrow` to retrieve previous entered commands.

## Create new floating task

1. Type `create read newspaper`

**Result:** A new floating task for "Read the newspaper" is created and selected at the bottom of To-dos (Right side).

## Create new task with deadline

1. Type `create finish product testing /by today`

**Result:** A new task "finish product testing" with deadline today is created and selected under `Today` section(Left side).

## Create new event

1. Type `create dinner with Mary /by tmr /from 1700 to 1800`.

**Result:** A new task "dinner with Mary" with period of 1700 to 1800 tomorrow is created and selected under `Tomorrow` section(Left side).

## Update the task name

1. Type `update 1 /name This is the updated Task`

**Result:** The first task (with index 1) is updated to "This is the updated Task".

## Update the task deadline

1. Type `update 1 /by today`.

**Result:** The first task's (with index 1) deadline is updated to today's date, and will be moved under `today`.

## Update the task period

1. Type `update 1 /from 1750 to 2000`.

**Result:** The first task's (with index 1) period is update to 1750 to 2000.

## Update the task by adding new tag

1. Type `update 1 /tag exciting`.

**Result:** The first task's tag has been changed to "exciting" with a random color.

## Update the task by adding multiple tags

1. Type `update 1 /tag fun /tag school`.

**Result:** The first task's tag has been changed to "fun" and "school".

## Update floating task to event

1. Ensure that the index 42 is under `To-dos` section (with no deadline and period), otherwise you may pick any index under `To-dos` section.
2. Type `update 42 /by today /from 1500 to 1700`.

**Result:** The floating task (with index 42) is updated to event with new deadline "today" and period "1500 to 1700", moved and selected to `Today` section (Left side).

## Remove the period of the task

1. Ensure that the first task has a period.
2. Type `update 1 /from -`.

**Result:**  The first task's period is removed.

## Remove the deadline of the task

1. Ensure that the first task has a deadline.
2. Type `update 1 /by -`.

**Result:** The first task's deadline and period (if have any) both are removed, and will be moved and selected at the To-dos section (Right side).

## Remove the tag in a task

1. Type `update 1 /tag`.

**Result:** All tags are removed for the first task.

## Complete a task

1. Type `done 1`.

**Result:** The first task will be removed from the current view, and it will be marked as `Complete`

## Find tasks by name

1. Type `find dinner`.

**Result:** A new overlay will be presented with your find results, all the tasks name contains "dinner" will be displayed to you (3 such tasks). Note that find can find any keyword in any field of the task.

## Sort in find results

1. Type `sort a asc`.
2. Press `ESC` to return.

**Result:** Find results will be reordered according to name with ascending alphabet order.

## Find tasks by tags

1. Type `find summer`.

**Result:** Tasks that are tagged with "summer" are shown (9 such tasks). Note that find command does not discriminate tags and names.

## Find tasks by status

1. Type `find complete`.

**Result:** Tasks that are completed by `done` command will be displayed. Note that find command does not discriminate status and names.

## Uncomplete a task in find result

1. Ensure that task with index 2 with status `Complete`.
2. Type `undone 2`.
3. Press `ESC` to return.

**Result:** Task's (index 2) status has been update to `Incomplete` and all the tasks will be display to you. Note that you may also perform other command in find result.

## Find tasks by deadline

1. Type `find May`.
2. Press `ESC` to return.

**Result:** Tasks with deadline in May will be display. Note that the Month you try to find is in short form, such as Apr, Dec. Also you may find day of the week such as Mon, Tue, Sun.

## Find tasks by period

1. Type `find 1500`.
2. Press `ESC` to return.

**Result:** Tasks with period 1500 will be displayed.

## Create a new tag

1. Ensure tag "important" does not exist.
2. Type `createtag important /color red`.
3. Press `ESC` to return.

**Result:** A new tag with name "important" and color red is created, a popup with all the existing tags will be displayed.

## Update an existing tag

1. Ensure tag "summer" exist.
2. Type `updatetag summer /name summerlife /color orange`.
3. Press `ESC` to return.

**Result:** Tag with name "summer" is updated to "summerlife" with color orange, a popup with all the existing tags will be displayed. All the task with tag "summer" are updated to "summerlife" with color orange.

## Delete an existing tag

1. Ensure tag "summerlife" exist.
2. Type `deletetag summerlife`.
3. Press `ESC` to return.

**Result:** Tag with name "summerlife" is deleted, a popup with all the existing tags will be displayed. Tag "summerlife" will be removed from the related task as well.

## Alias a command

1. Ensure command for alias is exist.
2. Type `alias c /as create`.

**Result:** Now you may use c as create command, a popup with all the existing aliases will be displayed. The alias will be saved, next time you open UTask, you can still use c as create command.

## Use alias (c) as create

1. Ensure alias c is exist.
2. Type `c test alias`

**Result:** A task "test alias" is created under `To-dos` section and selected.

## Unalias an alias

1. Ensure alias c is exist.
2. Type `unalias c`

**Result:** Alias `c` is removed, a popup with all the existing aliases will be displayed.

## Undo a previous action

1. Type `create Wrong Task"`.
2. Type `undo`.

**Result:** The task list is restored back to the state before step 1 was executed (i.e. "Wrong Task" should not exist after step 2).

## Redo something that we previously undo

1. Type `create redo task`.
2. Type `undo`.
3. Type `redo`.

**Result:** The "redo task" is added back to the list even though we undo it in step 2.

## Delete a certain task

1. Type `delete 3`

**Result:** The third task is deleted.

## Delete multiple tasks

1. Type `delete 1, 5, 6 to 11`

**Result:** The task with index 1, 5, 6, 7, 8, 9, 10, 11 are deleted.

## Clear the entire list

1. Type `clear`

**Result:** The entire task list is empty.

## Relocate the directory of the task list files

1. If you use windows, the path should be something like `C:\Dropbox`
    Otherwise, the path should be something like `/Users/James/Dropbox`
1. Type `relocate "validpath"`

**Result:** The data files inside `data\` should be relocated to `"validpath"`.

## Relocate to default location

1. Type `relocate`

**Result:** The data file will be moved to default location `data\`

