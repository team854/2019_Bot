package com.torontocodingcollective.speedcontroller;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.DMC60;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PWMSpeedController;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SD540;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * TPwmSpeedController controls one or more speed controllers of the same type
 * connected to the PWM interface on the RoboRio.
 * <p>
 * If more than one PWM port is specified, the same drive signal is sent to all
 * speed controllers
 * <p>
 * Care should be taken to ensure that the speed controllers are properly
 * connected and are all driving the motors in the same direction.
 */
public class TPwmSpeedController extends TSpeedController {

    /**
     * Enum of all PWM speed controller types supported by the wpilib.
     */
    public enum TPwmSpeedControllerType {
        /** DMC60 see {@link DMC60} */
        DMC60,
        /** SD540 see {@link SD540} */
        SD540,
        /** Spark see {@link Spark} */
        SPARK,
        /** Jaguar see {@link Jaguar} */
        JAGUAR,
        /** Talon see {@link Talon} */
        TALON,
        /** TalonSR see {@link Talon} */
        TALON_SR,
        /**
         * TalonSRX see {@link PWMTalonSRX}
         * <p>
         * NOTE: If using the TalonSRX attached to a CAN bus, see
         * {@link TCanSpeedController}
         */
        TALON_SRX,
        /** Victor 888 or 884 see {@link Victor} */
        VICTOR,
        /** VictorSP see {@link VictorSP} */
        VICTOR_SP,
        /**
         * VictorSPX see {@link PWMVictorSPX}
         * <p>
         * NOTE: If using the VictorSPX attached to a CAN bus, see
         * {@link TCanSpeedController}
         */
        VICTOR_SPX
    }

    private List<PWMSpeedController> speedControllerList = new ArrayList<PWMSpeedController>();

    /**
     * PWM Speed Controller
     * <p>
     * Supports any number of PWM speed controllers of the same type driving in the
     * same direction. The same output signal will be sent to all PWM ports
     * associated with this controller.
     * 
     * @param controllerType
     *            a valid {@link TPwmSpeedControllerType}
     * @param pwmPort
     *            the port number of the primary controller
     * @param isInverted
     *            {@code true} if the motors are inverted, {@code false} otherwise
     * @param followerPwmPorts
     *            and optional list of follower ports
     */
    public TPwmSpeedController(TPwmSpeedControllerType controllerType, int pwmPort, boolean isInverted,
            int... followerPwmPorts) {
        super(isInverted);
        speedControllerList.add(newController(controllerType, pwmPort));
        for (int followerPort : followerPwmPorts) {
            speedControllerList.add(newController(controllerType, followerPort));
        }
    }

    /**
     * PWM Speed Controller
     * <p>
     * Supports any number of PWM speed controllers of the same type driving in the
     * same direction. The same output signal will be sent to all PWM ports
     * associated with this controller.
     * 
     * @param controllerType
     *            a valid {@link TPwmSpeedControllerType}
     * @param pwmPort
     *            the port number of the primary controller
     * @param followerPwmPorts
     *            and optional list of follower ports
     */
    public TPwmSpeedController(TPwmSpeedControllerType controllerType, int pwmPort, int... followerPwmPorts) {
        this(controllerType, pwmPort, false, followerPwmPorts);
    }

    /**
     * PWM Speed Controller
     * <p>
     * Supports two PWM speed controllers of different types driving the same drive
     * train. The same output signal will be sent to the follower PWM ports.
     * 
     * @param controllerType
     *            a valid {@link TPwmSpeedControllerType}
     * @param pwmPort
     *            the port number of the primary controller
     * @param followerControllerType
     *            a valid {@link TPWMControllerType}
     * @param followerPwmPort
     *            the port number of the primary controller
     */
    public TPwmSpeedController(TPwmSpeedControllerType controllerType, int pwmPort,
            TPwmSpeedControllerType followerControllerType, int followerPwmPort) {
        this(controllerType, pwmPort, followerControllerType, followerPwmPort, false);
    }

    /**
     * PWM Speed Controller
     * <p>
     * Supports two PWM speed controllers of different types driving the same drive
     * train. The same output signal will be sent to the follower PWM ports.
     * 
     * @param controllerType
     *            a valid {@link TPwmSpeedControllerType}
     * @param pwmPort
     *            the port number of the primary controller
     * @param followerControllerType
     *            a valid {@link TPwmSpeedControllerType}
     * @param followerPwmPort
     *            the port number of the primary controller
     * @param isInverted
     *            {@code true} if the motors are inverted, {@code false} otherwise
     */
    public TPwmSpeedController(TPwmSpeedControllerType controllerType1, int pwmPort1,
            TPwmSpeedControllerType controllerType2, int pwmPort2, boolean isInverted) {
        super(isInverted);
        speedControllerList.add(newController(controllerType1, pwmPort1));
        speedControllerList.add(newController(controllerType2, pwmPort2));
    }

    @Override
    public double get() {

        if (speedControllerList.isEmpty()) {
            return 0;
        }

        double speed = speedControllerList.get(0).get();

        if (getInverted()) {
            speed = -speed;
        }
        return speed;
    }

    private PWMSpeedController newController(TPwmSpeedControllerType controllerType, int pwmPort) {

        switch (controllerType) {
        case DMC60:
            return new DMC60(pwmPort);
        case SD540:
            return new SD540(pwmPort);
        case SPARK:
            return new Spark(pwmPort);
        case JAGUAR:
            return new Jaguar(pwmPort);
        case TALON_SR:
        case TALON:
            return new Talon(pwmPort);
        case TALON_SRX:
            return new PWMTalonSRX(pwmPort);
        case VICTOR:
            return new Victor(pwmPort);
        case VICTOR_SP:
            return new VictorSP(pwmPort);
        case VICTOR_SPX:
            return new PWMVictorSPX(pwmPort);
        default:
            return new VictorSP(pwmPort);
        }
    }

    @Override
    public void set(double speed) {

        if (getInverted()) {
            speed = -speed;
        }
        for (PWMSpeedController speedController : speedControllerList) {
            speedController.set(speed);
        }
    }

}
