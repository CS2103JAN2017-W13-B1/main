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
   > <img src="images/mockup-v0.0.png" width="600">

4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. typing **`help`** and pressing <kbd>Enter</kbd> will open the help window.
5. Some example commands you can try:
   * **`list`** : lists all task
   * **`add`**` name my first task` :
     adds a task named `my first task` to μTask.
   * **`delete`**` 3` : deletes the 3rd task shown in the current list
   * **`exit`** : exits the app
6. Refer to the [Features](#features) section below for details of each command.<br>


----------


## 2. Features

> **Command Format**
>
> * Words in `UPPER_CASE` are the parameters.
> * Items in `SQUARE_BRACKETS` are optional.
> * Items with `...` after them can have multiple instances.
> * Parameters can be in any order.

### 2.1. Viewing help : `help`

Description: Displays a help menu to aid users in using μTask. <br>

Format: `help`

> Help is also shown if you enter an incorrect command e.g. `abcd`

### 2.2. Setting save location: `setpath`

Description: Designates the save directory for μTask. <br>

Format: `setpath PATH`

> `PATH` provided by the user has to be a valid folder for the command to successfully execute. 
> `PATH` has to be enclosed within quotes `"`

Examples:

* `setpath "C:\TEMP"`

### 2.3. Creating tag: `createtag`

Description: Creates a new tag in μTask. <br>

Format: `createtag NAME [/color COLOR]`

> * `NAME` provided must be unique and currently not existing in the μTask
> * `COLOR` provided can come in a form of 6 digit hexadecimal `RRGGBB` or plain English

| Symbol | Meaning            | Example        |
|--------|--------------------|----------------|
| RR     | Value of Red hue   | 08             |
| GG     | Value of Green hue | ff             |
| BB     | Value of Blue hue  | 8e             |

Examples:

* `createtag urgent /color dark red`
* `createtag low priority /color 00ffff`

### 2.4. Listing tags: `listtag`

Description: Lists μTask's current database for all stored tags.<br>

Format: `listtag`
> * If no tags exists within the database, μTask will prompt and inform you.
> * μTask will list and number the tags according to the  chronological order of creation

Examples:
* `listtag`

### 2.5. Updating a tag: `updatetag`

Description: Updates an existing tag in μTask. You can perform update on a specific task after `listtag` command has been executed. <br>

Format: `updatetag INDEX [/name NAME] [/color COLOR]`

> * Updates the tag at the specified `INDEX`.
    The index refers to the index number shown after `listtag` has been executed.<br>
    The index **must be a positive integer** 1, 2, 3, ...
> * `NAME` provided must be unique and currently not existing in the μTask
> * `COLOR` provided can come in a form of 6 digit hexadecimal `RRGGBB` or plain English

| Symbol | Meaning            | Example        |
|--------|--------------------|----------------|
| RR     | Value of Red hue   | 08             |
| GG     | Value of Green hue | ff             |
| BB     | Value of Blue hue  | 8e             |

Examples:

* `updatetag 1 /name urgent /color ffffff`<br>
  Updates the tag at `index` 1 to have `urgent` as name and `ffffff` as color.

* `updatetag 2 /color 888888`<br>
  Updates the tag at `index` 2 to have `888888` as color.

### 2.6. Deleting a tag: `deletetag`

Description: Deletes the specified tag from μTask.<br>

Format: `deletetag INDEX`

> * Deletes the tag at the specified `INDEX`. <br>
> * The index refers to the index number shown on the retrieved listing after `listtag` command is used. <br>
> * The index **must be a positive integer** 1, 2, 3, ...

Examples:

* `listtag`<br>
  `deletetag 2`<br>
  Deletes the 2nd tag in μTask.
* All existing tasks affected will have the specific tag removed.

### 2.7. Creating task: `create`

Description: Creates a new task in μTask. <br>

Format: `create NAME [/by DEADLINE] [/from START_TIME to END_TIME] [/repeat FREQUENCY] [/tag TAG...]`

> * `DEADLINE`, `START_TIME` and `END_TIME` uses a combination of  `HHMM DDMMYY` format to represent date and time
	 * The order of `HHMM` and `DDMMYY` is flexible
	 * `DDMMYY` is mandatory
	 * `HHMM` is optional because if it is not provided, default value of `0000` will be used


| Symbol | Meaning            | Example        |
|--------|--------------------|----------------|
| HH     | hour of day (0~23) | 08             |
| MM     | minute of hour     | 50             |
| DD     | day of year        | 28             |
| MM     | month of year      | 04             |
| YY     | year               | 2017           |

> * Tasks can have any number of tags (including 0)
> * Based on attributes provided during task creation, the type of resulting Task will be determined based on the following table:


| Attribute provided during task creation                       | Type of task created |
|---------------------------------------------------------------|----------------------|
| `[/from START_TIME to END_TIME]`                              | Event                |
| `[/by DEADLINE]`                                              | Deadline             |
| Neither `[/from START_TIME to END_TIME]` nor `[/by DEADLINE]` | Floating             |

Examples:

* `create watch movie from me to you /from 1830 010317 to 010317 2030 `
* `create read essay by tutor /by 200217 /tag urgent /tag assignment`

### 2.8. Listing / Searching tasks: `list`

Description: Searches μTask's current database for specific tasks based on inputs provided.<br>

Format: `list [TYPE] [/by DEADLINE] [/from START_TIME] [/to END_TIME] [/tag TAG...] [/done YES|NO]`
> * If no parameters are provided, μTask will list all unexpired tasks from current date time in which the command is executed
> * `TYPE` refers to the type of task determined during task creation
> * μTask will list and number the tasks chronologically which fulfill the search requirements


Examples:
* `list`
* `list float /tag urgent`
* `list deadline /by 2359 310817 /tag math /done NO`
* `list event /from 201017 to 221017 /done YES`

### 2.9. Finding all tasks containing any keyword in their description: `find`

Description: Finds tasks whose description contains any of the given keywords.<br>

Format: `Find KEYWORD...`

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

### 2.10. Viewing a task: `select`

Description: Views specific task's details based on the given index provided. <br>

Format: `select INDEX or select last`

> * Views the task at the specified `INDEX`.
    The index refers to the index number shown after `list` or `find` command has been executed.<br>
    The index **must be a positive integer** 1, 2, 3, ...
> * `last` refers to the very last entry displayed by μTask

Examples:

* `select 1`<br>
  Displays in depth details of the task at `index` 1.
  
* `select last`<br>
  Displays in depth details of the task with the maximal `index`.

### 2.11. Updating a task: `edit`

Description: Updates an existing task in μTask. You can perform update on a specific task after `list` command has been executed. <br>

Format: `update INDEX [/name NAME] [/by DEADLINE] [/from START_TIME to END_TIME] [/repeat FREQUENCY] [/tag TAG...][/done YES|NO]`


> * Updates the task at the specified `INDEX`.
    The index refers to the index number shown after `list` or `find` command has been executed.<br>
    The index **must be a positive integer** 1, 2, 3, ...
> * At least one of the optional fields must be provided.
> * Existing values will be updated to the input values.
> * When updating tags, the existing tags of the task will be removed i.e adding of tags is not cumulative.
> * You can remove all the task tags by typing `/tag` without specifying any tags after it. 

Examples:

* `update 1 /name do homework`<br>
  Updates the name of task at `index` 1 to "do homework".

* `update 2 /tag urgent`<br>
  Updates the tag of the task at `index` 2 to "urgent and removes all existing tags, if applicable.

### 2.12. Updating status of task: `done`

Description: Updates the `status` of an existing task to `yes`. <br>

Format: `done INDEX`


> * Updates the task at the specified `INDEX`.
    The index refers to the index number shown after `list` or `find` command has been executed.<br>
    The index **must be a positive integer** 1, 2, 3, ...

Examples:

* `done 1`<br>
  Updates the status of task at `index` 1 to done.

### 2.13. Deleting a task: `delete`

Description: Deletes the specified task from μTask. <br>

Format: `delete INDEX`

> * Deletes the task at the specified `INDEX`. <br>
> * The index refers to the index number shown on the retrieved listing after `list` or `find` command is used. <br>
> * The index **must be a positive integer** 1, 2, 3, ...

Examples:

* `list`<br>
  `delete 2`<br>
  Deletes the 2nd task in μTask.
  
* `find terra`<br>
  `delete 1`<br>
  Deletes the 1st task in the results of the `find` command.
  
### 2.14. Clearing all entries : `clear`

Description: Clears all stored tasks from the μTask.<br>

Format: `clear`

### 2.15. Undoing previous actions: `undo`

Description: Reverts changes made within μTask based on the provided amount of `STEPS`. <br>

Format: `undo [/last STEPS]`

> * Based on the value of `STEPS` provided, μTask will undo the specified amount of times. <br>
> * If the provided `STEPS` is higher than the number of commands executed within the session, μTask will prompt for confirmation before losing all changes made. <br>
> * The `STEPS` **must be a positive integer** 1, 2, 3, ...

Examples:

* `undo /last 4`<br>
  Reverts last 4 commands executed within μTask.

### 2.16. Redoing previous actions: `redo`

Description: Re-applies the changes reverted by undo within μTask based on the provided amount of `STEPS`. <br>

Format: `redo [/last STEPS]`

> * Based on the value of `STEPS` provided, μTask will redo the specified amount of times. <br>
> * If the provided `STEPS` is higher than the number of undo executed within the session, μTask will prompt for confirmation before re-applying all changes made. <br>
> * The `STEPS` **must be a positive integer** 1, 2, 3, ...

Examples:

* `redo /last 4`<br>
  Re-applies last 4 changes reverted by undo within μTask.

### 2.17. Exiting the program : `exit`

Description: Exits the program.<br>

Format: `exit`

### 2.18. Saving the data

Description:  μTask data are saved in the hard disk automatically after any command that changes the data.<br>
There is no need to save manually.


----------


## 3. FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with
       the file that contains the data of your previous μTask folder.


----------


## 4. Commands Format List

Command | Format  
-------- | :--------
Help | `help`
Set Path| `setpath PATH`
CreateTag | `createtag NAME [/color COLOR]` 
ListTag | `listtag` 
UpdateTag | `updatetag INDEX [/name NAME] [/color COLOR]`
DeleteTag | `deletetag INDEX`
CreateTask | `create NAME [/by DEADLINE] [/from START_TIME to END_TIME] [/repeat FREQUENCY] [/tag TAG...]`
ListTask | `list [TYPE] [/by DEADLINE] [/from START_TIME] [/to END_TIME] [/tag TAG...] [/done YES|NO]`
Find | `find KEYWORD...`
Select | `select INDEX` *or* `select last`
UpdateTask | `update INDEX [/name NAME] [/by DEADLINE] [/from START_TIME to END_TIME] [/repeat FREQUENCY] [/tag TAG...][/done YES|NO]`
Done | `done INDEX`
DeleteTask | `delete INDEX`
Clear | `clear`
Undo | `undo [/last STEPS]`
Redo | `redo [/last STEPS]`
Exit | `exit`