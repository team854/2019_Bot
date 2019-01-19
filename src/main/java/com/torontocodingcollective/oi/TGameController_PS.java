package com.torontocodingcollective.oi;

public class TGameController_PS extends TGameController {

    public TGameController_PS(int port) {
        super(port);
    }

    @Override
    public double getAxis(TStick stick, TAxis axis) {

        switch (stick) {

        case LEFT:
            switch (axis) {
            case X:
                return super.getFilteredRawAxis(0);
            case Y:
                return super.getFilteredRawAxis(1);
            default:
                break;
            }

        case RIGHT:
            switch (axis) {
            case X:
                return super.getFilteredRawAxis(2);
            case Y:
                return super.getFilteredRawAxis(3);
            default:
                break;
            }

        default:
            return 0.0;
        }
    }

    @Override
    public boolean getButton(TButton button) {

        // Get the maximum buttons for this controller
        int buttonCount = super.getButtonCount();

        switch (button) {

        case A:
        case X_SYMBOL:
            return getRawButton(2);
        case B:
        case CIRCLE:
            return getRawButton(3);
        case X:
        case SQUARE:
            return getRawButton(1);
        case Y:
        case TRIANGLE:
            return getRawButton(4);

        case LEFT_BUMPER:
            return getRawButton(5);
        case RIGHT_BUMPER:
            return getRawButton(6);

        case BACK:
        case SELECT:
        case SHARE:
            return getRawButton(9);
        case START:
        case OPTIONS:
            return getRawButton(10);
        default:
            // Continue with more buttons
            break;
        }

        if (buttonCount < 13) {
            return false;
        }

        switch (button) {
        case PS:
            return getRawButton(13);
        default:
            // Continue with more buttons
            break;
        }

        if (buttonCount < 14) {
            return false;
        }

        switch (button) {
        case TOUCHPAD:
            return getRawButton(14);

        default: // Unknown button
            return false;
        }
    }

    @Override
    public boolean getButton(TStick stick) {

        switch (stick) {

        case LEFT:
            return getRawButton(11);
        case RIGHT:
            return getRawButton(12);

        default:
            return false;
        }
    }

    @Override
    protected String getButtonString() {

        StringBuilder sb = new StringBuilder();

        // Playstation controllers use the square, triangle, etc buttons
        if (getButton(TButton.TRIANGLE)) {
            sb.append(" Tri");
        }
        if (getButton(TButton.SQUARE)) {
            sb.append(" Square");
        }
        if (getButton(TButton.CIRCLE)) {
            sb.append(" Circle");
        }
        if (getButton(TButton.X_SYMBOL)) {
            sb.append(" X");
        }
        if (getButton(TButton.LEFT_BUMPER)) {
            sb.append(" LB");
        }
        if (getButton(TButton.RIGHT_BUMPER)) {
            sb.append(" RB");
        }
        if (getButton(TButton.SHARE)) {
            sb.append(" Share");
        }
        if (getButton(TButton.OPTIONS)) {
            sb.append(" Options");
        }
        if (getButton(TButton.PS)) {
            sb.append(" PS");
        }
        if (getButton(TButton.TOUCHPAD)) {
            sb.append(" Touchpad");
        }
        if (getButton(TStick.LEFT)) {
            sb.append(" L-Stick");
        }
        if (getButton(TStick.RIGHT)) {
            sb.append(" R-Stick");
        }

        return sb.toString().trim();
    }

    @Override
    public double getTrigger(TTrigger trigger) {

        return 0.0;
    }

    @Override
    protected boolean isUserButtonActive() {
        // Playstation controllers use the square, triangle, etc buttons
        if (getButton(TButton.TRIANGLE) || getButton(TButton.SQUARE) || getButton(TButton.CIRCLE)
                || getButton(TButton.X_SYMBOL) || getButton(TButton.LEFT_BUMPER) || getButton(TButton.RIGHT_BUMPER)
                || getButton(TButton.SHARE) || getButton(TButton.OPTIONS) || getButton(TButton.PS)
                || getButton(TButton.TOUCHPAD) || getButton(TStick.LEFT) || getButton(TStick.RIGHT)) {
            return true;
        }

        return false;
    }
}
