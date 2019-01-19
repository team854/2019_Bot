package com.torontocodingcollective.oi;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RumbleManager class is used to control the rumble on a GameController.
 */
public class TRumbleManager {

    private final TGameController gameController;
    private String                smartDashboardName;
    private boolean               rumbleOn = false;

    private enum State {
        ON, OFF, PAUSE
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

    }

    private void updateRumble() {

        switch (state) {
        case ON:
            gameController.setRumble(1.0);
            break;

        case PAUSE:
            gameController.setRumble(0);
            break;

        case OFF:
            gameController.setRumble(0);
            break;

        }
    }

    public void updatePeriodic() {

        updateRumble();

        SmartDashboard.putBoolean(smartDashboardName, rumbleOn);
    }

}
