package bob.task;

import bob.ui.Ui;

public class Task {
    private String description;
    private boolean isDone;
//    private String marker = " ";

    /**
     * Constructor to initialise a task.
     * @param description Input based on user.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructor to initialise a task previously recorded.
     * @param description Input based on user.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns whether or not a task is completed.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns a string representation for a line item in the printed list,
     */
    public String getTaskListItem() {
        return("[" + getTaskLetter() +"][" + this.getStatusIcon() + "] " + this.description);
    }

    /**
     * Marks the task as completed or not completed.
     * @param value Whether the task is completed.
     * @return String representation based on whether the task is marked as completed or not completed.
     */
    public void markTask(boolean value) {
        if (value) {
            this.isDone = true;
            String finishedMarking = "OK, I've marked this task as done:\n\t"
                    + "["
                    + getTaskLetter()
                    + "]"
                    + "[X] "
                    + this.description;
            Ui.printLines(finishedMarking);
        } else {
            this.isDone = false;
            String finishedUnmarking = "OK, I've marked this task as not done yet:\n\t"
                    + "["
                    + getTaskLetter()
                    + "]"
                    + "[ ] "
                    + this.description;
            Ui.printLines(finishedUnmarking);
        }
    }

    public String getTaskLetter() {
        return " ";
    }

    /**
     * Returns a string representation of the file format in which we store the task.
     */
    public String getFileFormat() {
        String done = isDone ? "1" : "0";
        return this.getTaskLetter() + " | " + done + " | " + this.description;
    }
}
