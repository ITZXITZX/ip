package bob.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    protected String by;
    protected LocalDate date;
    /**
     * Constructor to initialise a task.
     *
     * @param description Input based on user.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        this.marker = "/by";
    }

    /**
     * Constructor to initialise a task.
     *
     * @param description Input based on user.
     */
    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        this.by = by;
        this.marker = "/by";
    }

    /**
     * Constructor to initialise a task.
     *
     * @param description Input based on user.
     */
    public Deadline(String description, LocalDate date) {
        super(description);
        this.date = date;
        this.marker = "/by";
    }

    // Returns the letter representing deadline.
    @Override
    public String taskLetter() {
        return "D";
    }

    /**
     * Returns a string representation of the file format in which we store the Deadline.
     */
    @Override
    public String fileFormat () {
        String part1 = super.fileFormat();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String dateTimeOutput = (date != null) ? date.format(outputFormatter) : by;
        return part1 + " | " + dateTimeOutput;
    }
}
