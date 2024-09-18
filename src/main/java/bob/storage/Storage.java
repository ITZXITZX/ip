package bob.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.TaskList;
import bob.task.Todo;

/**
 * Handles operations relating to saving or loading tasks in the file.
 */
public class Storage {

    private String filePath;

    /**
     * Constructor for the Storage instance.
     * @param filePath File path to the location of the file where records are saved.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Updates Bob's records and prints out all existing records.
     *
     * @return Arraylist of tasks based on the previous instance when the chatBot was running.
     */
    public ArrayList<Task> loadTaskList() {
        try {
            File file = new File(filePath);
            System.out.println("File location: " + file.getAbsolutePath());
            if (!file.exists()) {

                boolean isFileCreated = file.createNewFile();

                if (!isFileCreated) {
                    System.out.println("Failed to create file when loading task.");
                }
            }
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            ArrayList<Task> tempRecords = new ArrayList<>();

            System.out.println("\t====== Current Records =====\n");
            System.out.println("Current file path = " + this.filePath);
            for (Task task: tempRecords) {
                System.out.println("Task in tempRecords: ");
                System.out.println(task.getTaskListItem());
            }
            System.out.println("Hello");
            System.out.println("Line input = " + line);

            while (line != null && !line.equals("")) {
                Task task = this.loadTask(line);
                System.out.println(task.getTaskListItem());
                tempRecords.add(task);
                System.out.println("\t" + line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            return tempRecords;
        } catch (IOException e) {
            super.toString();
        }
        return null;
    };

    /**
     * Reads file and updates taskList for the next instruction given by user.
     *
     * @return Updated TaskList.
     */
    public TaskList loadUpdatedTaskList() {
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            ArrayList<Task> tempRecords = new ArrayList<>();

            while (line != null && !line.equals("")) {
                Task task = this.loadTask(line);
                tempRecords.add(task);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            return new TaskList(tempRecords);
        } catch (IOException e) {
            super.toString();
        }
        return new TaskList();
    }

    /**
     * Parses the content from a line in the saved file to return the corresponding Task object.
     * @param lineInput A single line in the saved file.
     * @return Task based on the data specified in the lineInput.
     * @throws IOException
     */
    public Task loadTask(String lineInput) throws IOException {
        String[] parts = lineInput.split(" \\| ");
        boolean isDone = (parts[1].equals("1")) ? true : false;

        switch (parts[0]) {
        case "T":
            return new Todo(parts[2], isDone);
        case "D":
            return new Deadline(parts[2], parts[3], isDone);
        case "E":
            System.out.println("parts length = " + parts.length);
            System.out.println("part[0] = " + parts[0]);
            System.out.println("part[1] = " + parts[1]);
            System.out.println("part[2] = " + parts[2]);
            System.out.println("part[3] = " + parts[3]);
            return new Event(parts[2], parts[3], parts[4], parts[5], isDone);
        default:
            throw new IOException("unable to parse Data for loading.");
        }
    }

    /**
     * Saves tasks updates by user to storage
     * @param records records of the tasks currently tracked by the chatBot.
     */
    public void saveRecordsToStorage(ArrayList<Task> records) {

        File file = new File(filePath);
        try {
            File dataDir = file.getParentFile();
            if (dataDir != null && !dataDir.exists()) {
                boolean isDirCreated = dataDir.mkdir();
                if (!isDirCreated) {
                    System.out.println("Failed to create directory for saving records.");
                }
            }
            if (!file.exists()) {
                boolean isFileCreated = file.createNewFile();
                if (!isFileCreated) {
                    System.out.println("Failed to create new file when saving records.");
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Task x: records) {
                String fileFormatLine = x.getFileFormat();
                bufferedWriter.write(fileFormatLine);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("Unable to save records to storage.");
        }
    }
}
