package bob.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import bob.exception.InvalidTaskException;
import bob.parser.Parser;
import bob.storage.Storage;
import bob.ui.Ui;

/**
 * Performs operations on the list of Tasks.
 */
public class TaskList {

    private ArrayList<Task> records;
    private int latestRecordedIndex;

    /**
     * Initialises TaskList with an existing record.
     * @param recordedList Arraylist of existing records.
     */
    public TaskList(ArrayList<Task> recordedList) {
        this.records = recordedList;
        this.latestRecordedIndex = recordedList.size();
    }

    /**
     * Initialises a new TaskList.
     */
    public TaskList() {
        this.records = new ArrayList<>();
        this.latestRecordedIndex = 0;
    }


    /**
     * Prints a list of all recorded user inputs.
     */
    public void listRecords() {
        String allRecords = "Here are the tasks in your list:\n\t";
        for (int i = 0; i < records.size(); i++) {
            int num = i + 1;
            Task currTask = records.get(i);
            if (num == records.size()) {
                allRecords += num + "." + currTask.getTaskListItem();
            } else {
                allRecords += num + "." + currTask.getTaskListItem() + "\n\t";
            }
        }
        Ui.printLines(allRecords);
    }

    /**
     * Checks if the item indexed by user is a valid index in the records.
     * If records only have 5 items, itemIndex 5 is valid but itemIndex 6 is not.
     *
     * @param itemIndex Index of the item in the TaskList.
     * @return True if index is an index within TaskList, else false.
     */
    public boolean isValidRecord(int itemIndex) {
        return (itemIndex > 0 && itemIndex <= records.size());
    }



    /**
     * Removes the task from records, based on the task's index in records.
     * @param input Input given by the user.
     */
    public void delete(String input) {
        try {
            String[] separateKeyword = input.split(" ", 2); //separate the keyword from the rest of string
            if (separateKeyword.length == 1) {
                throw new InvalidTaskException("OOPS!!! The description of delete cannot be empty.");
            }
            if (!isValidRecord(Integer.parseInt(separateKeyword[1]))) {
                throw new InvalidTaskException("Invalid input. Integer required between range of record items.");
            }

            Task taskToDelete = records.get(Integer.parseInt(separateKeyword[1]) - 1);
            String immediateAdd = "Noted. I've removed this task:\n\t"
                    + taskToDelete.getTaskListItem()
                    + "\n\t"
                    + "Now you have "
                    + (String.valueOf(latestRecordedIndex - 1))
                    + " tasks in the list.";
            Ui.printLines(immediateAdd);
            this.records.remove(Integer.parseInt((separateKeyword[1]).trim()) - 1);
            this.latestRecordedIndex -= 1;
        } catch (InvalidTaskException e) {
            System.err.println((e.getMessage()));
        }
    }

    /**
     * Updates whether the task in the record is completed or not completed.
     * @param inputWords String array of the words given as input.
     * @param isCompleted Whether the task is marked as completed or incompleted.
     */
    public void updateMark(String input, String[] inputWords, boolean isCompleted) {
        int itemNumberByUser = Integer.valueOf(inputWords[1]);
        if (inputWords.length == 1) {
            System.out.println("Please input which item number you want to mark.");
        } else if (this.records.size() < itemNumberByUser || itemNumberByUser <= 0) {
            System.out.println("Item index out of range.");
        } else {
            Task currTask = this.records.get(itemNumberByUser - 1);
            assert currTask != null : "Updated task cannot be null";
            if (isCompleted) {
                currTask.markTask(true);
            } else {
                currTask.markTask(false);
            }
        }
    }

    /**
     * Adds a task to records.
     * @param input Input given by a user.
     * @param inputWords Partially processed array of input.
     */
    public void addTask(String input, String[] inputWords) {
        try {
            String keyword = inputWords[0];
            Task newTask = getTask(keyword, input); //initialise the exact Task class
            String immediateAdd = "Got it. I've added this task:\n\t"
                    + "  ["
                    + newTask.getTaskLetter()
                    + "][ ] "
                    + this.getInputDescription(input)
                    + "\n\t"
                    + "Now you have "
                    + (String.valueOf(latestRecordedIndex + 1))
                    + " tasks in the list.";
            Ui.printLines(immediateAdd);
            this.records.add(this.latestRecordedIndex, newTask);
            this.latestRecordedIndex += 1;
        } catch (InvalidTaskException e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * Returns the actual task type instance.
     *
     * @param keyword Represents the action that the user wants to do.
     * @param input Input String provided by user.
     * @return
     */
    public Task getTask(String keyword, String input) throws InvalidTaskException {
        Task newTask = new Task("");
        String[] inputWords = input.split("\s+");
        String taskDescription = this.getDescriptionOnly(input); //gets the specific task description based on keyword.
        switch (keyword) {
        case "todo":
            if (taskDescription.equals("")) {
                throw new InvalidTaskException("OOPS!!! The description of a todo cannot be empty.");
            }
            newTask = new Todo(taskDescription);
            break;
        case "deadline":
            if (taskDescription.equals("")) {
                throw new InvalidTaskException("OOPS!!! The description of a deadline cannot be empty.");
            }

            String deadlineString = inputWords[Arrays.asList(inputWords).indexOf("/by") + 1];
            LocalDate deadline = Parser.parseDate(deadlineString);

            newTask = new Deadline(taskDescription, deadline);
            break;
        case "event":
            if (taskDescription.equals("")) {
                throw new InvalidTaskException("OOPS!!! The description of a event cannot be empty.");
            }
            if (!inputWords[Arrays.asList(inputWords).indexOf("/from") + 3].equals("/to")) {
                throw new InvalidTaskException("Invalid use of event format."
                        + "Should be  '<description> /from <day> <start_time> /to <end_time>'");
            }
            String day = inputWords[Arrays.asList(inputWords).indexOf("/from") + 1];
            String startTime = inputWords[Arrays.asList(inputWords).indexOf("/from") + 2];
            String endTime = inputWords[Arrays.asList(inputWords).indexOf("/to") + 1];
            newTask = new Event(taskDescription, day, startTime, endTime);
            break;
        default:
            throw new InvalidTaskException("Unable to create task. Please key in a valid keyword.");
        }
        assert newTask != null : "New task created for get task cannot be null";
        return newTask;
    }

    /**
     * Returns String representation of the Task's description.
     * This description includes the task specific details.
     *
     * @param input original input given by the user.
     */
    public String getInputDescription(String input) throws InvalidTaskException {
        String[] separateKeyword = input.split(" ", 2); //separate the keyword from the rest of string
        switch (separateKeyword[0]) {
        case "todo":
            if (separateKeyword.length == 1) {
                throw new InvalidTaskException("OOPS!!! The description of todo cannot be empty.");
            }
            return separateKeyword[1];
        case "deadline":
            if (separateKeyword.length == 1) {
                throw new InvalidTaskException("OOPS!!! The description of deadline cannot be empty.");
            }
            String[] subString1 = separateKeyword[1].split("/by");
            if (subString1.length <= 1) {
                throw new InvalidTaskException("Invalid use of deadline. Should be '... /by ...'.");
            }
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
            LocalDate x = Parser.parseDate(subString1[1].trim());

            if (x == null) {
                throw new InvalidTaskException("Invalid date format provided.");
            }

            String part2 = x.format(outputFormatter);
            return subString1[0].trim() + " (by: " + part2 + ")";
        case "event":
            if (separateKeyword.length == 1) {
                throw new InvalidTaskException("OOPS!!! The description of event cannot be empty.");
            }

            String[] subString2 = separateKeyword[1].split("/from");
            if (subString2.length <= 1) {
                if (subString2.length == 0) {
                    throw new InvalidTaskException("OOPS!!! The event description cannot be empty.");
                }
                throw new InvalidTaskException("Invalid use of event format. "
                        + "Should be  '<description> /from <day> <start_time> /to <end_time>'");
            }

            String[] subString3 = subString2[1].split("/to");
            if (subString3.length <= 1) {
                throw new InvalidTaskException("Invalid use of event format. Should be '<description> "
                        + "/from <day> <start_time> /to <end_time>'.");
            }
            if (subString3[0].trim().isEmpty()) {
                throw new InvalidTaskException("OOPS!!! The start time for the event cannot be empty.");
            }
            if (subString3[1].trim().isEmpty()) {
                throw new InvalidTaskException("OOPS!!! The end time for the event cannot be empty.");
            }

            return subString2[0].trim() + " (from:" + subString3[0] + " to:" + subString3[1] + ")";

        default:
            return input;
        }
    }

    /**
     * Returns the String representation of the task description only.
     * @param input input by the user.
     * @return String representation of task description.
     * @throws InvalidTaskException
     */
    public String getDescriptionOnly(String input) throws InvalidTaskException {
        String[] separateKeyword = input.split(" ", 2); //separate the keyword from the rest of string
        switch (separateKeyword[0]) {
        case "todo":
            if (separateKeyword.length == 1) {
                throw new InvalidTaskException("OOPS!!! The description of todo cannot be empty.");
            }
            return separateKeyword[1];
        case "deadline":
            if (separateKeyword.length == 1) {
                throw new InvalidTaskException("OOPS!!! The description of deadline cannot be empty.");
            }
            String[] subString1 = separateKeyword[1].split("/by");
            if (subString1.length <= 1) {
                throw new InvalidTaskException("Invalid use of deadline. Should be '<event> /by <date>'.");
            }
            return subString1[0].trim();
        case "event":
            if (separateKeyword.length == 1) {
                throw new InvalidTaskException("OOPS!!! The description of event cannot be empty.");
            }
            String[] subString2 = separateKeyword[1].split("/from");
            if (subString2.length <= 1) {
                if (subString2.length == 0) {
                    throw new InvalidTaskException("OOPS!!! The event description cannot be empty.");
                }
                throw new InvalidTaskException("Invalid use of event format."
                        + "Should be '<description> /from <day> <start_time> /to <end_time>'");
            }
            return subString2[0].trim();

        default:
            return input;
        }
    }

    /**
     * Saves tasks updates by user to records.
     */
    public void saveRecords(Storage storage) {
        assert storage != null : "Need storage in order to save records.";
        storage.saveRecordsToStorage(records);
    }

    /**
     * Finds the tasks that has a description matching keyword entered by user.
     * @param input String representation of the command given by user.
     */
    public void find(String input) {
        String[] inputArray = input.split("\s+");
        if (inputArray.length > 2) {
            System.out.println("find command can only be used to find 1 keyword.");
        } else if (inputArray.length <= 1) {
            System.out.println("Please enter the exact keyword you want to find.");
        } else {
            boolean isTargetPresent = false;
            String target = inputArray[1];
            ArrayList<Task> matchingRecords = new ArrayList<>();
            for (Task x : records) {
                if (x.isTargetInDescription(target) == true) {
                    matchingRecords.add(x);
                    isTargetPresent = true;
                }
            }
            if (isTargetPresent == true) {
                assert matchingRecords != null : "If flagged as present, target must be in matching records";
                Ui.showSearchResults(matchingRecords);
            } else {
                Ui.showEmptySearchResults();
            }
        }
    }

    /**
     * Returns the size of records.
     *
     * @return
     */
    public int getRecordSize() {
        assert this.records != null : "Records must be present to get record size.";
        return this.records.size();
    }

    /**
     * Returns the Task of the indicated index in records.
     *
     * @param index index of task seend by user in the current records,
     * @return
     */
    public Task getIndexedTask(int index) {
        assert this.records != null : "Records must be present to get indexed task.";
        return this.records.get(index - 1);
    }

    /**
     * Returns an arraylist of records.
     */
    public ArrayList<Task> getAllRecords() {
        assert this.records != null : "Records must be present to get all records.";
        return this.records;
    }

    /**
     * Removes the task at the index specified by the user.
     *
     * @param index Index that corresponds to the task visible to the user.
     */
    public void removeRecord(int index) {
        assert this.records != null : "Records must be present to remove a single record.";
        this.records.remove(index - 1);
        this.latestRecordedIndex -= 1;
    }

    /**
     * Returns the String representation of the task at specified index when task is deleted.
     *
     * @param index Index of the task as viewed displayed using the list command.
     * @return
     */
    public String getDeletedTaskString(int index) {
        assert index > 0 : "Index of deleted task should be greater than zero to get deleted task string.";
        String deletedTaskString = "Noted. I've removed this task:\n\t"
                + records.get(index - 1).getTaskListItem()
                + "\n\t"
                + "Now you have "
                + (String.valueOf(latestRecordedIndex - 1))
                + " tasks in the list.";
        return deletedTaskString;
    }

    /**
     * Returns the String representation of the task at specified index when task is added.
     *
     * @return
     */
    public String getAddedTaskString() {
        Task addedTask = this.getIndexedTask(this.getLatestRecordedIndex());
        String commandString = "Got it. I've added this task:\n\t"
                + addedTask.getTaskListItem()
                + "\n\t"
                + "Now you have "
                + (String.valueOf(this.getLatestRecordedIndex()))
                + " tasks in the list.";
        return commandString;
    }

    /**
     * Increments the latest recorded index by 1.
     */
    public void incrementLatestRecordedIndex() {
        this.latestRecordedIndex++;
    }

    /**
     * Returns the latest recorded index in the current task list from the viewer's perspective.
     */
    public int getLatestRecordedIndex() {
        return this.latestRecordedIndex;
    }
}
