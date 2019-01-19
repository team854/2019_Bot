package com.torontocodingcollective.oi;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Handles input from a GameController
 * <p>
 * This class adds functionality to the standard FRC
 * {@link edu.wpi.first.wpilibj.Joystick} class to access all buttons and axis
 * by name for a standard game controller.
 * <p>
 * This abstract class has 3 known implementations <br>
 * {@link TGameController_Logitech} <br>
 * {@link TGameController_PS} <br>
 * {@link TGameController_Xbox}
 *
 */
public abstract class TGameController extends Joystick {

    /**
     * The deadband used for this TGameController when getting the value of an axis.
     * <p>
     * If the axis value of the stick or trigger is less than this value, then a
     * value of zero is returned. Most GameControllers will have a deadband of less
     * than 0.07.
     * <p>
     * Changes to this value will take place immediately.
     * <p>
     * In order to test the deadband of the sticks on your controller set this value
     * to 0 to turn off the deadband filter.
     * <p>
     * Values greater than the deadband are returned. Values less than or equal to
     * the deadband are returned as zero.
     * <p>
     * For the default deadband of .07, values of .08 and higher will not be
     * filtered.
     */
    public double axisDeadband = 0.07;

    /**
     * Construct an instance of a GameController.
     * <p>
     * The joystick index is the USB port on the drivers station.
     * 
     * @param port
     *            The port on the Driver Station that the joystick is plugged into.
     */
    protected TGameController(int port) {
        super(port);
    }

    /**
     * Get the axis of the GameController stick
     * 
     * @param stick
     *            {@link TStick#LEFT} or {@link TStick#RIGHT}
     * @param axis
     *            {@link TAxis#X} or {@link TAxis#Y}
     * @return double value in the range 0 to 1.0 rounded to the nearest .01.
     */
    public abstract double getAxis(TStick stick, TAxis axis);

    /**
     * Get the button on the GameController
     * 
     * @param button
     *            a valid {@link TButton} value for this GameController. <br>
     *            NOTE: if the button is not valid for the controller, then the
     *            value false
     * @return boolean {@code true} if pressed, {@code false} otherwise.
     */
    public abstract boolean getButton(TButton button);

    /**
     * Get the button on the GameController corresponding to the press of the stick
     * on the controller
     * <p>
     * All game controllers have a button attached to the stick that is activated by
     * pushing the stick downward. This routine returns the value of that button
     * press.
     * 
     * @param stick
     *            {@link TStick} value on this gamecontroller. <br>
     *            NOTE: if the stick is not valid for the controller, then the value
     *            returned is {@code false}
     * @return boolean {@code true} if pressed, {@false otherwise}
     */
    public abstract boolean getButton(TStick stick);

    /**
     * Get the value of the trigger as a button
     * <p>
     * Some game controllers have a button trigger, and some have an analog trigger.
     * This routine returns the value of a trigger as a button value of {@code true}
     * or {@code false}
     * 
     * @param trigger
     *            {@link TTrigger#LEFT} or {@link TTrigger#RIGHT} <br>
     *            NOTE: if the trigger is not valid for the game controller then the
     *            value returned is {@code false}
     * @return boolean {@code true} if the trigger is pressed {@code false}
     *         otherwise.
     */
    public boolean getButton(TTrigger trigger) {
        return getTrigger(trigger) > 0.3;
    }

    /**
     * Get a string representing the pressed buttons on this controller
     * 
     * @return
     */
    protected abstract String getButtonString();;

    /**
     * Round the axis to the nearest 100th.
     * 
     * @param axisNumber
     *            value indicating the raw axis index in the DriverStation.
     *            DriverStation axis indicators start at index 0.
     * @return axisValue rounded to 2 decimal places and filtered by the current
     *         {@link #axisDeadband}.
     */
    protected double getFilteredRawAxis(int axisNumber) {

        // Round to the nearest 100th
        double axisValue = Math.round(super.getRawAxis(axisNumber) * 100.0) / 100.0;

        if (Math.abs(axisValue) <= axisDeadband) {
            return 0;
        }

        return axisValue;
    }

    /**
     * Get the stick position of the specified stick.
     * <p>
     * The TStickPostion object contains the (x,y) coordinates of the stick. Only
     * TStick values of {@link TStick#LEFT} or {@link TStick#RIGHT} are suppported.
     * 
     * @param stick
     *            {@link TStick#LEFT} or {@link TStick#RIGHT}
     * @return TStickPosition of the stick or {@code null} if an invalid stick is
     *         specified.
     */
    public TStickPosition getStickPosition(TStick stick) {
        switch (stick) {
        case LEFT:
        case RIGHT:
            return new TStickPosition(getAxis(stick, TAxis.X), getAxis(stick, TAxis.Y));
        default:
            System.out.println("Unsupported stick type " + stick + " for TGameController.getStickPosition()");
            return null;
        }
    }

    /**
     * Get the trigger on the GameController
     * 
     * @param trigger
     *            {@link TTrigger#LEFT} or {@link TTrigger#RIGHT} for this
     *            GameController. <br>
     *            NOTE: if the trigger is not valid for the controller, then the
     *            value returned is 0.0
     * @return double value in the range 0 to 1.0
     */
    public abstract double getTrigger(TTrigger trigger);

    /**
     * Is Stick Active
     * 
     * @param stick
     *            {@link TStick#LEFT} or {@link TStick#RIGHT}
     * @return {@code true} if active, {@code false} otherwise
     */
    public boolean isStickActive(TStick stick) {

        TStickPosition stickPosition = getStickPosition(stick);

        if (stickPosition == null) {
            return false;
        }

        if (Math.abs(stickPosition.x) > 0 || Math.abs(stickPosition.y) > 0) {
            return true;
        }

        return false;
    }

    /**
     * Is User Active
     * <p>
     * Indicates if the user is actively pressing a button or activating a stick on
     * the controller
     * 
     * @return {@code true} if the user is actively using the controller,
     *         {@code false} otherwise
     */
    public boolean isUserActive() {

        // If one of the sticks is active, then the user is active
        if (isStickActive(TStick.LEFT) || isStickActive(TStick.RIGHT)) {
            return true;
        }

        // If one of the triggers is pressed, then the user is active
        if (getTrigger(TTrigger.LEFT) > 0 || getTrigger(TTrigger.RIGHT) > 0) {
            return true;
        }

        // If the POV is pressed, then the user is active
        if (getPOV() >= 0) {
            return true;
        }

        // If any button is pressed, then the user is active
        if (isUserButtonActive()) {
            return true;
        }

        return false;
    }

    /**
     * Determine if any of the buttons are pressed on this GameController
     * 
     * @return {@code true} if any button is pressed, {@code false} otherwise.
     */
    protected abstract boolean isUserButtonActive();

    /**
     * Set the rumble value for this game controller
     * <p>
     * The TorontoJar currently only supports a single rumble value which is output
     * to both left and right channels simultaneously. <br>
     * NOTE: This routine does not turn off the rumble once set to a non-zero value.
     * Users should set the rumble value off (0) when the rumble is finished.
     * 
     * @param volume
     *            a double value between 0 (off) and 1.0 (full rumble) for the
     *            rumble.
     */
    public void setRumble(double volume) {
        super.setRumble(RumbleType.kLeftRumble, volume);
        super.setRumble(RumbleType.kRightRumble, volume);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(super.getName());

        if (isUserActive()) {
            sb.append(' ').append("Active");
        }
        sb.append(' ').append(getStickPosition(TStick.LEFT)).append(' ').append(getStickPosition(TStick.RIGHT))
                .append(" Triggers(").append(getTrigger(TTrigger.LEFT)).append(',').append(getTrigger(TTrigger.RIGHT))
                .append(')');

        String buttonString = getButtonString();
        if (!buttonString.isEmpty()) {
            sb.append(' ').append(buttonString);
        }

        int pov = getPOV();
        if (pov >= 0) {
            sb.append(" POV(").append(pov).append(')');
        }

        return sb.toString();
    }

}
