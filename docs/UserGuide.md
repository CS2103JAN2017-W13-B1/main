# μTask - User Guide

By : `Team SE-EDU`  &nbsp;&nbsp;&nbsp;&nbsp; Since: `Jun 2016`  &nbsp;&nbsp;&nbsp;&nbsp; Licence: `MIT`

---

1. [Quick Start](#quick-start)
2. [Features](#features)
3. [FAQ](#faq)
4. [Command Summary](#command-summary)

## 1. Quick Start

0. Ensure you have Java version `1.8.0_60` or later installed in your Computer.<br>

   > Having any Java 8 version is not enough. <br>
   > This app will not work with earlier versions of Java 8.

1. Download the latest `utask.jar` from the [releases](../../../releases) tab.
2. Copy the file to the folder you want to use as the home folder for your μTask manager.
3. Double-click the file to start the app. The Graphic User Interface should appear in a few seconds.
   > <img src="images/Ui.png" width="600">

4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. typing **`help`** and pressing <kbd>Enter</kbd> will open the help window.
5. Some example commands you can try:
   * **`list`** : lists all task
   * **`add`**` name my first task` :
     adds a task named `my first task` to μTask.
   * **`delete`**` 3` : deletes the 3rd task shown in the current list
   * **`exit`** : exits the app
6. Refer to the [Features](#features) section below for details of each command.<br>


## 2. Features

> **Command Format**
>
> * Words in `UPPER_CASE` are the parameters.
> * Items in `SQUARE_BRACKETS` are optional.
> * Items with `...` after them can have multiple instances.
> * Parameters can be in any order.

### 2.1. Viewing help : `help`

Format: `help`

> Help is also shown if you enter an incorrect command e.g. `abcd`

### 2.2. Creating task: `create`

Creates a task to μTask<br>
Format: `create NAME [by DEADLINE] [from START_TIME to END_TIME] [repeat FREQUENCY] [tag TAG...]`

> * DEADLINE, START_TIME and END_TIME use HHMM DDMMYY format to represent date and time
> Tasks can have any number of tags (including 0)

Examples:

* `create terra on leave from 0830 10317 to 0830 150317`
* `create send update on project status from 1600 200217 to 1730 200217 tag urgent`

### 2.3. Listing all tasks: `list`

Shows a list of all tasks in μTask.<br>
Format: `list`

### 2.4. Updating a task: `edit`

Updates an existing task in μTask.<br>
Format: `update INDEX [name NAME] [by DEADLINE] [from START_TIME to END_TIME] [repeat FREQUENCY] [tag TAG...][done YES|NO]`


> * Updates the task at the specified `INDEX`.
    The index refers to the index number shown in the task listing.<br>
    The index **must be a positive integer** 1, 2, 3, ...
> * At least one of the optional fields must be provided.
> * Existing values will be updated to the input values.
> * When updating tags, the existing tags of the task will be removed i.e adding of tags is not cumulative.
> * You can remove all the task tags by typing `tag` without specifying any tags after it. 

Examples:

* `update 1 done yes`<br>
  Updates the 1st task to done.

* `update 2 tag urgent`<br>
  Updates the tag of the 2nd task to urgent and clears all existing tags, if applicable.

### 2.5. Finding all tasks containing any keyword in their description: `find`

Finds tasks whose description contains any of the given keywords.<br>
Format: `Find | find KEYWORD...`

> * The search is case sensitive. e.g `grocery` will not match `Grocery`
> * The order of the keywords does not matter. e.g. `Grocery Store` will match `Store Grocery`
> * Only the description of the task is searched.
> * Only full words will be matched e.g. `Gro` will not match `Grocery`
> * Tasks matching at least one keyword will be returned (i.e. `OR` search).
    e.g. `Grocery` will match `Store Grocery`

Examples:

* `find Buy`<br>
  Returns `Buy Grocery` but not `buy`
* `find Impt Clear John`<br>
  Returns Any tasks having descriptions `Impt`, `Clear`, or `John`

### 2.6. Deleting a task: `delete`

Deletes the specified task from the μTask. Irreversible.<br>
Format: `delete INDEX`

> Deletes the task at the specified `INDEX`. <br>
> The index refers to the index number shown on the retrieved listing.<br>
> The index **must be a positive integer** 1, 2, 3, ...

Examples:

* `list`<br>
  `delete 2`<br>
  Deletes the 2nd task in μTask.
* `find terra`<br>
  `delete 1`<br>
  Deletes the 1st task in the results of the `find` command.
  
### 2.7. Clearing all entries : `clear`

Clears all stored tasks from the μTask.<br>
Format: `clear`

### 2.8. Exiting the program : `exit`

Exits the program.<br>
Format: `exit`

### 2.9. Saving the data

μTask data are saved in the hard disk automatically after any command that changes the data.<br>
There is no need to save manually.

## 3. FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with
       the file that contains the data of your previous μTask folder.

## 4. Commands Format List

Command | Format  
-------- | :--------
CreateTask | `create NAME [by DEADLINE] [from START_TIME to END_TIME] [repeat FREQUENCY] [tag TAG...]`
ListTask | `list [TYPE] [by DEADLINE] [from START_TIME] [to END_TIME] [tag TAG...] [done YES|NO]`
UpdateTask | `update INDEX [name NAME] [by DEADLINE] [from START_TIME to END_TIME] [repeat FREQUENCY] [tag TAG...][done YES|NO]`
DeleteTask | `delete INDEX`
CreateTag | `createtag NAME [color COLOR]` 
ListTag | `listtag` 
UpdateTag | `updatetag INDEX [name NAME] [color COLOR]`
DeleteTag | `deletetag INDEX`
Update | `update`
Select | `select INDEX` *or* `select last`
Find | `find KEYWORD...`
Done | `done INDEX`
Undo | `undo [last STEPS]`
Redo | `redo [last STEPS]`
Clear | `clear`
Set Path| `setpath PATH`
Help | `help`


