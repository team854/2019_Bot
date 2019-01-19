package com.torontocodingcollective;

public class TConst {

    /** Orientation is inverted */
    public static final boolean INVERTED                = true;
    /** Orientation is not inverted */
    public static final boolean NOT_INVERTED            = false;

    /** Default timeout for all commands */
    public static final double  DEFAULT_COMMAND_TIMEOUT = 5;
    /** No command timeout. Use this value to disable the command timeout. */
    public static final double  NO_COMMAND_TIMEOUT      = -1;

    /** Brake at the end of the command */
    public static final boolean BRAKE_WHEN_FINISHED     = true;
    /** Coast at the end of the command */
    public static final boolean COAST_WHEN_FINISHED     = false;
}
