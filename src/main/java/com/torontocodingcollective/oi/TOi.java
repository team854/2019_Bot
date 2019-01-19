package com.torontocodingcollective.oi;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

/**
 * Driver Controller Sticks: Right Stick X-axis = Drive Motor Turn Left Stick
 * Y-axis = Drive Motor Speed Right Stick Press = Toggle PIDs Left Stick Press =
 * Toggle Compressor Buttons: Start Button = Reset Encoders and Gyro Back Button
 * = Cancel any Command X Button = Automatic intake B Button = Automatic intake
 * cancel A Button = Climb arm up Y Button = Climb arm down
 * 
 * Bumpers/Triggers:
 * 
 * POV: Any Angle = Rotate to the Pressed Angle
 * 
 */
public abstract class TOi {

    /**
     * Return the state of the cancel command button.
     * <p>
     * Typically this is the Back button on the Driver controller
     * 
     * @return {@code true} if the cancel button is currently pressed {@code false}
     *         otherwise
     */
    public abstract boolean getCancelCommand();

    /**
     * Get the stick position for the specified stick
     * 
     * @param stick
     *            the {@link TStick#LEFT} or {@link TStick#RIGHT} stick for the
     *            driver
     * @return {@link TStickPosition} for the left drive stick or {@code null} if
     *         only the right stick is used for driving
     */
    public abstract TStickPosition getDriveStickPosition(TStick stick);

    /**
     * Return the state of the reset button.
     * <p>
     * Typically this is the Start button on the Driver controller
     * 
     * @return {@code true} if the Reset button is currently pressed {@code false}
     *         otherwise
     */
    public abstract boolean getReset();

    /**
     * Return the heading to rotate to from the Driver controller
     * <p>
     * Robots that use a gyro should override this method in order to test the gyro
     * PID
     * 
     * @return an angle between {@code 0} and {@code 360} degrees
     */
    public int getRotateToHeading() {
        return -1;
    }

    /**
     * Get Speed PID Enabled
     * 
     * @return {@code true} if the Speed PIDs are enabled, {@code false} otherwise
     */
    public abstract boolean getSpeedPidEnabled();

    /**
     * Update the elements of the OI that need updating and put data to the
     * SmartDashboard
     */
    public void updatePeriodic() {
    }
}
