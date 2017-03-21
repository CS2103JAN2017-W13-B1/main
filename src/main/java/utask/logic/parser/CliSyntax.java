package utask.logic.parser;

import java.util.regex.Pattern;

import utask.logic.parser.ArgumentTokenizer.Prefix;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("/name");
    public static final Prefix PREFIX_DEADLINE = new Prefix("/by");
    public static final Prefix PREFIX_TIMESTAMP = new Prefix("/from");
    public static final Prefix PREFIX_FREQUENCY = new Prefix("/repeat");
    public static final Prefix PREFIX_DONE = new Prefix("/done");
    public static final Prefix PREFIX_TAG = new Prefix("/tag");

    /* Patterns definitions */
    public static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

}
