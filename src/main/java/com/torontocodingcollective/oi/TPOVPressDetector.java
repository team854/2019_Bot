package com.torontocodingcollective.oi;

/**
 * The TButtonPressDetector class implements a Button Press Detector on the POV.
 * <p>
 * This routine will only return a POV angle on the first occurance of a after
 * all POV buttons were released. It will then return {@code -1} for all
 * subsequent calls to the {@link #get()} routine even if a POV button is still
 * pressed.
 * <p>
 * All POV buttons must be released before the next press is detected.
 * <p>
 * NOTE: This routine will not perform well when trying to get a 45 deg value
 * since it is unlikely that the underlying POV will return a 45 deg increment
 * on the first press. It is recommended to only use this class to detect values
 * of 0, 90, 180 and 270 degrees.
 */
public class TPOVPressDetector {

    private final TGameController gameController;
    private int                   previousPovValue;

    /**
     * Declare a press detector over the POV <br>
     * 
     * @param gameController
     *            object
     */
    public TPOVPressDetector(TGameController gameController) {
        this.gameController = gameController;
        this.previousPovValue = gameController.getPOV();
    }

    /**
     * Get the current state of the POV Detector.
     * <p>
     * NOTE: This routine should only be called once per controller cycle since
     * subsequent calls will always return {@value -1}
     * 
     * @return {@code true} if a press was detected since the last call or
     *         {@code false} otherwise
     */
    public int get() {

        int newPOVValue = gameController.getPOV();

        // If the button was previously pressed (!= -1) then
        // do not update the POV angle until the POV is released.
        if (previousPovValue != -1) {

            // Only update the value if the new value
            // is not pressed (-1).
            if (newPOVValue == -1) {
                previousPovValue = -1;
            }
        } else {

            // If the POV was not previously pressed (previous value = -1),
            // then return the new angle value if there is a new button press.
            if (newPOVValue != -1) {
                previousPovValue = newPOVValue;
                return newPOVValue;
            }
        }

        return -1;
    }

}
