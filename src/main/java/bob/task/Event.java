package bob.task;

/**
 * Day task that has a start time and end time.
 */
public class Event extends Task {

    private static final String TASK_LETTER = "E";
    protected String startDay;
    protected String startTime;
    protected String endTime;


    /**
     * Constructor to initialise a task.
     *
     * @param description Input based on user.
     */
    public Event(String description, String startDay, String startTime, String endTime) {
        super(description);
        this.startDay = startDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Constructor to initialise a task previously recorded.
     *
     * @param description Input based on user.
     */
    public Event(String description, String startDay, String startTime, String endTime, boolean isDone) {
        super(description, isDone);
        this.startDay = startDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Returns the letter representing event.
    @Override
    public String getTaskLetter() {
        return TASK_LETTER;
    }

    /**
     * Returns a string representation of the file format in which we store the Event.
     */
    @Override
    public String getFileFormat() {
        String part1 = super.getFileFormat();
        return part1 + " | " + startDay + " | " + startTime + " | " + endTime;
    }

    /**
     * Returns a string representation for an event task in the printed list.
     */
    @Override
    public String getTaskListItem() {
        return super.getTaskListItem() + " (from: " + startDay + " " + startTime + " to: " + endTime + ")";
    }
}
