package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.NoSuchElementException;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.Command;
import utask.logic.commands.DeleteTagCommand;
import utask.logic.commands.IncorrectCommand;
// @@ author A0138423J
public class DeleteTagCommandParser {

    public Command parse(String args) {

        try {
            System.out.println(args);
            String temp = args.trim();
            System.out.println(temp);
            return new DeleteTagCommand(args);
        } catch (NoSuchElementException nsee) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            DeleteTagCommand.MESSAGE_USAGE));
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

}
