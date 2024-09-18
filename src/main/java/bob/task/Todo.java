package bob.task;

/**
 * Task that are meant to be done with no specified deadline.
 */
public class Todo extends Task {
    private static final String TASK_LETTER = "T";
    /**
     * Constructor to initialise a task.
     *
     * @param description Input based on user.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Constructor to initialise a task that has been recorded before.
     *
     * @param description Input based on user.
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }


    // Returns the letter representing todo.
    @Override
    public String getTaskLetter() {
        return TASK_LETTER;
    }

    /**
     * Returns a string representation of the file format in which we store the Todo.
     */
    @Override
    public String getFileFormat() {
        String part1 = super.getFileFormat();
        return part1;
    }

    /**
     * Returns a string representation for a todo task in the printed list.
     */
    @Override
    public String getTaskListItem() {
        return super.getTaskListItem();
    }
}
