//@@author A0138493W

package utask.logic.parser;

import static utask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import utask.logic.commands.Command;
import utask.logic.commands.IncorrectCommand;
import utask.logic.commands.RelocateCommand;

public class RelocateCommandParser {

    public Command parse(String args) {
        boolean isValidPath = ParserUtil.isPathValid(args);

        //default path
        if (args.equals("")) {
            return new RelocateCommand();
        }

        if (isValidPath) {
            return new RelocateCommand(args.trim(), true);
        }

        return new IncorrectCommand(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RelocateCommand.MESSAGE_USAGE));
    }

}
