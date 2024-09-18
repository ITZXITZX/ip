package bob.command;

import bob.storage.Storage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * ListCommand class executes list command.
 */
public class ByeCommand extends Command {

    /**
     * Constructor to initalise ByeCommand
     *
     * @param input
     */
    public ByeCommand(String input) {
        super(input);
    }

    @Override
    public String execute(TaskList taskList, Storage storage, Ui ui) {
        this.updateIsExitToTrue();
        return "GoodBye";
    }
}
