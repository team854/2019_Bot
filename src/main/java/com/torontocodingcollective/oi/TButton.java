package com.torontocodingcollective.oi;

/**
 * This class is used to name all of the buttons on a controller.
 * <p>
 * Depending on the controller type, different buttons are used.
 */
public enum TButton {

    // Joystick Controller
    /** Button 1 */
    ONE(1),
    /** Button 2 */
    TWO(2),
    /** Button 3 */
    THREE(3),
    /** Button 4 */
    FOUR(4),
    /** Button 5 */
    FIVE(5),
    /** Button 6 */
    SIX(6),
    /** Button 7 */
    SEVEN(7),
    /** Button 8 */
    EIGHT(8),
    /** Button 9 */
    NINE(9),
    /** Button 10 */
    TEN(10),
    /** Button 11 */
    ELEVEN(11),
    /** Button 12 */
    TWELVE(12),

    // GameController - Logitech, XBox
    /**
     * XBox or Logitech Button A, equivalent to PS controllers
     * {@link TButton#X_SYMBOL}
     */
    A(-1),
    /**
     * XBox or Logitech Button B, equivalent to PS controllers
     * {@link TButton#CIRCLE}
     */
    B(-1),
    /**
     * XBox or Logitech Button X, equivalent to PS controllers
     * {@link TButton#SQUARE}
     */
    X(-1),
    /**
     * XBox or Logitech Button Y, equivalent to PS controllers
     * {@link TButton#TRIANGLE}
     */
    Y(-1),

    // Playstation
    // Mapping of A, B, X, Y
    /**
     * Playstation Button X symbol, equivalent to XBox controllers {@link TButton#A}
     */
    X_SYMBOL(-1),
    /**
     * Playstation Button Circle, equivalent to XBox controllers {@link TButton#B}
     */
    CIRCLE(-1),
    /**
     * Playstation Button Square, equivalent to XBox controllers {@link TButton#X}
     */
    SQUARE(-1),
    /**
     * Playstation Button Triangle, equivalent to XBox controllers {@link TButton#Y}
     */
    TRIANGLE(-1),

    /** Right Bumper */
    RIGHT_BUMPER(-1),
    /** Left Bumper */
    LEFT_BUMPER(-1),
    /** Back Button */
    BACK(-1),
    /** Start Button */
    START(-1),

    // PS3 mapping of BACK button
    /**
     * Playstation 3 Select button, equivalent to XBox controllers
     * {@link TButton#BACK}
     */
    SELECT(-1),

    // PS4
    // Mapping of BACK and START buttons
    /**
     * Playstation 4 Share button, equivalent to XBox controllers
     * {@link TButton#BACK}
     */
    SHARE(-1),
    /**
     * Playstation 4 Share button, equivalent to XBox controllers
     * {@link TButton#START}
     */
    OPTIONS(-1),

    // Additional PS4 buttons
    /** Playstation 4 Touch pad button */
    TOUCHPAD(-1),
    /** Playstation 4 PlayStation button */
    PS(-1);

    public final int value;

    TButton(int value) {
        this.value = value;
    }

}
