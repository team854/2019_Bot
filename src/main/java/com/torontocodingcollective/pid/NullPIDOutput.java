package com.torontocodingcollective.pid;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 * A null PID Output
 * <p>
 * This class is used to easily construct a
 * #{@link edu.wpi.first.wpilibj.PIDController} that has no defined output.
 * <p>
 * The TorontoJar class {@link TSpeedPID} extends the wpilib PIDController in
 * order to use the SmartDashboard interface. It uses this class as its output.
 */
public class NullPIDOutput implements PIDOutput {

    @Override
    public void pidWrite(double output) {
    }

}
