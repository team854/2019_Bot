package com.torontocodingcollective.oi;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RumbleManager class is used to control the rumble on a GameController.
 */
public class TRumbleManager {

    private final TGameController gameController;
    private String                smartDashboardName;
    private long                  timerEndTime    = 0;
    private int                   pulseCount      = 0;
    private double                pulseOnSeconds  = 0;
    private double                pulseOffSeconds = 0;

    private enum State {
        ON, OFF, PULSE_ON, PULSE_OFF;
    };

    private State state = State.OFF;

    public TRumbleManager(String name, TGameController gameController) {
        this.gameController = gameController;
        this.smartDashboardName = name + " rumble";
    }

    public void rumbleOn() {
        state = State.ON;
    }

    public void rumbleOff() {
        state = State.OFF;
    }

    public void rumbleOn(double seconds) {
    	timerEndTime = System.currentTimeMillis() + (long) (seconds * 1000);
        state = State.ON;
    }

    /**
     * Set the controller to rumble for a number of pulses based on the 
     * specified timing
     * <p>
     * NOTE: If a second rumble command happens before the pulses are
     *       completed, then the pulses will be cancelled and the new
     *       rumble state will take precedence.  This means that 
     *       the rumble pulse should only be called once.
     * @param count number of pulses
     * @param onSeconds number of seconds for each pulse to turn on
     * @param offSeconds number of seconds for each pulse to turn off
     */
    public void rumblePulse(int count, double onSeconds, double offSeconds) {
    	pulseCount      = count;
    	pulseOnSeconds  = onSeconds;
    	pulseOffSeconds = offSeconds;
    	timerEndTime = System.currentTimeMillis() + (long) (onSeconds * 1000);
    	state = State.PULSE_ON;
    }

    private void updateRumble() {

        switch (state) {
        case ON:
            gameController.setRumble(1.0);
            
            // Check for timeout on rumble
            if (timerEndTime > 0) {
            	if (System.currentTimeMillis() > timerEndTime) {
            		state = State.OFF;
                    gameController.setRumble(0);
                    timerEndTime = 0;
            	}
            }
            break;

        case PULSE_ON:
            gameController.setRumble(1.0);
        	if (System.currentTimeMillis() > timerEndTime) {
        		state = State.PULSE_OFF;
            	timerEndTime = System.currentTimeMillis() + (long) (pulseOffSeconds * 1000);
            	pulseCount --;
        	}
            break;

        case PULSE_OFF:
            gameController.setRumble(0);
            // Pulse is decremented at the end of the PULSE_ON
            if (pulseCount == 0) {
            	state = State.OFF;
                timerEndTime = 0;
                break;
            }
        	if (System.currentTimeMillis() > timerEndTime) {
        		state = State.PULSE_ON;
            	timerEndTime = System.currentTimeMillis() + (long) (pulseOnSeconds * 1000);
        	}
            break;

        case OFF:
            gameController.setRumble(0);
            timerEndTime = 0;
            break;

        }
    }

    public void updatePeriodic() {

        updateRumble();

        SmartDashboard.putBoolean(smartDashboardName, state == State.ON);
    }

}
