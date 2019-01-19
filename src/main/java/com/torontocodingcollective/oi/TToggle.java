package com.torontocodingcollective.oi;

/**
 * The TToggle class implements a Toggle on the passed in button. The
 * {@link #updatePeriodic() } method must be called each loop for this toggle in
 * order for the toggle to track each button state. The toggle switches state on
 * a transition from not pressed, button {@code false} to pressed, button
 * {@code true}.
 * <p>
 * Use the {@link #get()} method to get the current state of the toggle.
 */
public class TToggle {

    private boolean               toggleState;
    private final TGameController gameController;
    private final TButton         button;
    private final TStick          stick;
    private final TTrigger        trigger;
    private boolean               previousButtonState;

    /**
     * Declare a toggle over the specified button on the GameController <br>
     * The initial state will be set to {@code false}.
     * <p>
     * If using a Stick button, use the constructor
     * {@link #TToggle(TGameController, TStick)} <br>
     * if using a Trigger as a button, use the constructor
     * {@link #TToggle(TGameController, TTrigger)}
     * 
     * @param gameController
     *            object
     * @param button
     *            to use for the toggle
     */
    public TToggle(TGameController gameController, TButton button) {
        this(gameController, button, false);
    }

    /**
     * Declare a toggle over the specified button on the GameController
     * <p>
     * If using a Stick button, use the constructor
     * {@link #TToggle(TGameController, TStick)} <br>
     * if using a Trigger as a button, use the constructor
     * {@link #TToggle(TGameController, TTrigger)}
     * 
     * @param gameController
     *            object
     * @param button
     *            to use for the toggle
     * @param initialState
     *            of the toggle
     */
    public TToggle(TGameController gameController, TButton button, boolean initialState) {
        this.gameController = gameController;
        this.button = button;
        this.stick = null;
        this.trigger = null;
        this.toggleState = initialState;
        this.previousButtonState = gameController.getButton(button);
    }

    /**
     * Declare a toggle over the specified button on the GameController Stick (push)
     * <br>
     * The initial state will be set to {@code false}.
     * 
     * @param gameController
     *            object
     * @param stick
     *            (push) to use for the toggle
     */
    public TToggle(TGameController gameController, TStick stick) {
        this(gameController, stick, false);
    }

    /**
     * Declare a toggle over the specified button on the GameController Stick (push)
     * 
     * @param gameController
     *            object
     * @param stick
     *            (push) to use for the toggle
     * @param initialState
     *            of the toggle
     */
    public TToggle(TGameController gameController, TStick stick, boolean initialState) {
        this.gameController = gameController;
        this.button = null;
        this.stick = stick;
        this.trigger = null;
        this.toggleState = initialState;
        this.previousButtonState = gameController.getButton(stick);
    }

    /**
     * Declare a toggle over the specified trigger on the GameController <br>
     * The initial state will be set to {@code false}.
     * <p>
     * Triggers on modern game controllers are typically analog returning a
     * {@code double} value from 0.0 to 1.0. This routine determines the trigger to
     * be pressed .3 and released below that value.
     * 
     * @param gameController
     *            object
     * @param trigger
     *            to use for the toggle
     */
    public TToggle(TGameController gameController, TTrigger trigger) {
        this(gameController, trigger, false);
    }

    /**
     * Declare a toggle over the specified trigger on the GameController
     * <p>
     * Triggers on modern game controllers are typically analog returning a
     * {@code double} value from 0.0 to 1.0. This routine determines the trigger to
     * be pressed .3 and released below that value.
     * 
     * @param gameController
     *            object
     * @param trigger
     *            to use for the toggle
     * @param initialState
     *            of the toggle
     */
    public TToggle(TGameController gameController, TTrigger trigger, boolean initialState) {
        this.gameController = gameController;
        this.button = null;
        this.stick = null;
        this.trigger = trigger;
        this.toggleState = initialState;
        this.previousButtonState = gameController.getButton(trigger);
    }

    /**
     * Get the current state of the toggle
     * 
     * @return {@code true} or {@code false}
     */
    public boolean get() {
        return toggleState;
    }

    /**
     * Set the current state of the toggle
     * 
     * @param set
     *            value {@code true} or {@code false}
     */
    public void set(boolean set) {
        toggleState = set;
    }

    /**
     * UpdatePeriodic
     * <p>
     * This routine must be called every loop in order to update the state of the
     * toggle based on the game controller and button.
     */
    public void updatePeriodic() {

        boolean curButtonState = false;

        if (button != null) {
            curButtonState = gameController.getButton(button);
        }
        if (stick != null) {
            curButtonState = gameController.getButton(stick);
        }
        if (trigger != null) {
            curButtonState = gameController.getButton(trigger);
        }

        if (curButtonState && !previousButtonState) {
            toggleState = !toggleState;
        }
        previousButtonState = curButtonState;
    }
}
