package com.torontocodingcollective.speedcontroller;

import com.torontocodingcollective.TConst;
import com.torontocodingcollective.pid.TSpeedPID;
import com.torontocodingcollective.sensors.encoder.TEncoder;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * Common interface for all TSpeedControllers
 * <p>
 * All TSpeedControllers must implement the {@link SpeedController} interface
 */
public abstract class TSpeedController implements SpeedController {

    private boolean isInverted = TConst.NOT_INVERTED;

    protected TSpeedController(boolean isInverted) {
        this.isInverted = isInverted;
    }

    @Override
    public void disable() {
        stopMotor();
    }

    @Override
    public boolean getInverted() {
        return isInverted;
    }

    /**
     * NOTE: This routine is not used in the TorontoFramework but is provided for
     * compile reasons to support the {@link SpeedController} interface and in case
     * a TSpeedController is used in a wpilib {@link PIDController}. It is
     * recommended instead to use the TorontoJar {@link TSpeedPID} instead
     */
    @Override
    public void pidWrite(double output) {
        set(output);
    }

    @Override
    public void setInverted(boolean isInverted) {
        if (isInverted != this.isInverted) {
            stopMotor();
            this.isInverted = isInverted;
        }
    }

    @Override
    public void stopMotor() {
        set(0);
    }

    /**
     * Get the encoder attached to this TSpeedController
     * <p>
     * By default, the encoder will be set with the same inversion setting as the
     * motor and is assumed to be a 2-channel quadrature encoder.
     * 
     * @returns TEncoder attached to this device or {@code null} if this device does
     *          not support an attached encoder
     */
    public TEncoder getEncoder() {
        System.out.println("GetEncoder is not supported for " + this.getClass().getName());
        return null;
    }

}
