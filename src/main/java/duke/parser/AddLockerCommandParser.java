package duke.parser;

import duke.exceptions.DukeException;
import duke.logic.commands.AddLockerCommand;
import duke.logic.commands.Command;

import duke.models.Address;
import duke.models.Locker;
import duke.models.SerialNumber;
import duke.models.Tag;
import duke.models.Zone;
import duke.parser.utilities.MapTokensToArguments;
import duke.parser.utilities.ParserTokenizer;
import duke.parser.utilities.Token;


import java.util.stream.Stream;

import static duke.parser.utilities.Syntax.TOKEN_ADDRESS;
import static duke.parser.utilities.Syntax.TOKEN_SERIAL;
import static duke.parser.utilities.Syntax.TOKEN_ZONE;

public class AddLockerCommandParser {

    /**
     * This function is used to parse the user input for adding a new locker to the list.
     * Later it will include all checks for validating the user input
     * @param userInput stores the user input
     * @return reference to the class AddLockerCommand
     * @throws DukeException when the command format is invalid
     */
    public Command parse(String userInput) throws DukeException {
        MapTokensToArguments mapTokensToArguments =
                ParserTokenizer.tokenize(userInput, TOKEN_SERIAL, TOKEN_ADDRESS, TOKEN_ZONE);
        if (!checkAllTokensPresent(mapTokensToArguments,
                TOKEN_SERIAL, TOKEN_ADDRESS, TOKEN_ZONE)
                || !mapTokensToArguments.getTextBeforeFirstToken().isEmpty()) {
            throw new DukeException(" Invalid command format");
        }

        SerialNumber serialNumber = ParserCheck.parseSerialNumber(
                mapTokensToArguments.getValue(TOKEN_SERIAL).get());
        Address address = ParserCheck.parseAddress(
                mapTokensToArguments.getValue(TOKEN_ADDRESS).get());
        Zone zone = ParserCheck.parseZone(mapTokensToArguments.getValue(TOKEN_ZONE).get());
        Tag tag = new Tag("not-in-use");
        Locker locker = new Locker(serialNumber, address, zone, tag);
        return new AddLockerCommand(locker);
    }

    private static boolean checkAllTokensPresent(MapTokensToArguments mapTokensToArguments,
                                                 Token... tokens) {

        return Stream.of(tokens).allMatch(token -> mapTokensToArguments
                .getValue(token).isPresent());
    }
}