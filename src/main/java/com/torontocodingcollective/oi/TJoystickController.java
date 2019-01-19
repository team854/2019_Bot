package com.torontocodingcollective.oi;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Handles input from a Joystick type controller
 * <p>
 * This class adds functionality to the standard FRC
 * {@link edu.wpi.first.wpilibj.Joystick} class to access all buttons and axis
 * by name.
 * <p>
 * This abstract class has 3 known implementations <br>
 * {@link TJoystickController_Logitech}
 *
 */
public abstract class TJoystickController extends Joystick {

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
    public double     axisDeadband = 0.07;

    private final int maxSupportedButtons;

    /**
     * Construct an instance of a TJoystickController.
     * <p>
     * The joystick index is the USB port on the drivers station.
     * 
     * @param port
     *            The port on the Driver Station that the joystick is plugged into.
     */
    protected TJoystickController(int port, int maxSupportedButtons) {
        super(port);
        this.maxSupportedButtons = maxSupportedButtons;
    }

    /**
     * Round the axis to the nearest 100th.
     * 
     * @param axis
     * @return axis rounded to 2 decimal places
     */
    protected double filterAxisValue(double axisValue) {

        // Round to the nearest 100th
        double roundedValue = Math.round(axisValue * 100) / 100;

        if (Math.abs(roundedValue) <= axisDeadband) {
            return 0;
        }

        return roundedValue;
    };

    /**
     * Get the axis of the TJoystickController stick
     * 
     * @param axis
     *            {@link TAxis#X}, {@link TAxis#Y}, {@link TAxis#TWIST} or
     *            {@link TAxis#SLIDER}
     * 
     * @return double value in the range 0 to 1.0
     */
    public double getAxis(TAxis axis) {
        switch (axis) {
        case X:
            return filterAxisValue(super.getX());
        case Y:
            return filterAxisValue(super.getY());
        case TWIST:
            return filterAxisValue(super.getTwist());
        case SLIDER:
            return filterAxisValue(super.getThrottle());
        default:
            return 0;
        }
    }

    /**
     * Get the button on theTJoystickController
     * 
     * @param button
     *            a valid {@link TButton} value for thisTJoystickController. <br>
     *            NOTE: if the button is not valid for the controller, then the
     *            value false
     * @return boolean {@code true} if pressed, {@code false} otherwise.
     */
    public boolean getButton(TButton button) {
        if (button.value < 1) {
            return false;
        }
        if (button.value > maxSupportedButtons) {
            return false;
        }
        return super.getRawButton(button.value);
    }

    private String getButtonString() {

        StringBuilder sb = new StringBuilder();

        if (getButton(TButton.ONE)) {
            sb.append(" T");
        }

        for (int i = 2; i <= maxSupportedButtons; i++) {
            if (getRawButton(i)) {
                sb.append(' ').append(i);
            }
        }

        return sb.toString().trim();
    }

    /**
     * Get the stick position for this joystick
     * <p>
     * This stick position
     * 
     * @return
     */
    public TStickPosition getStickPosition() {
        return new TStickPosition(getAxis(TAxis.X), getAxis(TAxis.Y));
    };

    /**
     * Get the trigger on theTJoystickController
     * <p>
     * Joystick controllers have only one trigger on the center stick
     * 
     * @return double value in the range 0 to 1.0
     */
    @Override
    public boolean getTrigger() {
        return getButton(TButton.ONE);
    }

    /**
     * Is Stick Active
     * 
     * @param stick
     *            {@link TStick#LEFT} or {@link TStick#RIGHT}
     * @return {@code true} if active, {@code false} otherwise
     */
    public boolean isStickActive() {

        TStickPosition stickPosition = getStickPosition();

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
     * Indicates if the user is actively pressing a button or activating an stick on
     * the controller
     * <p>
     * NOTE: The slider value is not checked because it does not return to a home
     * state. If the user is only moving the slider, that will not be detected by
     * this routine.
     * 
     * @return {@code true} if the user is actively using the controller,
     *         {@code false} otherwise
     */
    public boolean isUserActive() {

        // If the main joystick is active, then the user is active
        if (isStickActive()) {
            return true;
        }
        if (Math.abs(getAxis(TAxis.TWIST)) > 0) {
            return true;
        }

        // If any button is pressed, then the user is active
        if (isUserButtonActive()) {
            return true;
        }

        return false;
    }

    /**
     * Determine if any of the buttons are pressed on this TJoystickController
     * 
     * @return {@code true} if any button is pressed, {@code false} otherwise.
     */
    private boolean isUserButtonActive() {
        for (int i = 1; i <= maxSupportedButtons; i++) {
            if (getRawButton(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(super.getName());

        if (isUserActive()) {
            sb.append(' ').append("Active");
        }
        sb.append(' ').append(getStickPosition()).append(" Twist(").append(getAxis(TAxis.TWIST)).append(')')
                .append(" Slider(").append(getAxis(TAxis.SLIDER)).append(')');

        String buttonString = getButtonString();
        if (!buttonString.isEmpty()) {
            sb.append(' ').append(buttonString);
        }

        return sb.toString();
    }
}
