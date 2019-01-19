package com.torontocodingcollective.pid;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * A null PID Source.
 * <p>
 * This class is used to easily construct a
 * #{@link edu.wpi.first.wpilibj.PIDController} that has no defined source.
 * <p>
 * The TorontoJar class {@link TSpeedPID} extends the wpilib PIDController in
 * order to use the SmartDashboard interface.
 */
public class NullPIDSource implements PIDSource {

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        return 0;
    }

}
