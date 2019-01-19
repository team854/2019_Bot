package com.torontocodingcollective.oi;

public class TGameController_Logitech extends TGameController {

    public TGameController_Logitech(int port) {
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
                return super.getFilteredRawAxis(4);
            case Y:
                return super.getFilteredRawAxis(5);
            default:
                break;
            }

        default:
            return 0.0;
        }
    }

    @Override
    public boolean getButton(TButton button) {

        switch (button) {

        case A:
        case X_SYMBOL:
            return getRawButton(1);
        case B:
        case CIRCLE:
            return getRawButton(2);
        case X:
        case SQUARE:
            return getRawButton(3);
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
            return getRawButton(7);
        case START:
        case OPTIONS:
            return getRawButton(8);

        default:
            return false;
        }
    }

    @Override
    public boolean getButton(TStick stick) {

        switch (stick) {

        case LEFT:
            return getRawButton(9);
        case RIGHT:
            return getRawButton(10);

        default:
            return false;
        }
    }

    @Override
    protected String getButtonString() {

        StringBuilder sb = new StringBuilder();

        // Logitech Controllers use the A, B, X, Y buttons
        if (getButton(TButton.A)) {
            sb.append(" A");
        }
        if (getButton(TButton.B)) {
            sb.append(" B");
        }
        if (getButton(TButton.X)) {
            sb.append(" X");
        }
        if (getButton(TButton.Y)) {
            sb.append(" Y");
        }
        if (getButton(TButton.LEFT_BUMPER)) {
            sb.append(" LB");
        }
        if (getButton(TButton.RIGHT_BUMPER)) {
            sb.append(" RB");
        }
        if (getButton(TButton.START)) {
            sb.append(" Start");
        }
        if (getButton(TButton.BACK)) {
            sb.append(" Back");
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

        switch (trigger) {

        case LEFT:
            return getFilteredRawAxis(2);
        case RIGHT:
            return getFilteredRawAxis(3);

        default:
            return 0.0;
        }
    }

    @Override
    protected boolean isUserButtonActive() {
        // Logitech controllers use the square, triangle, etc buttons
        if (getButton(TButton.A) || getButton(TButton.B) || getButton(TButton.X) || getButton(TButton.Y)
                || getButton(TButton.LEFT_BUMPER) || getButton(TButton.RIGHT_BUMPER) || getButton(TButton.START)
                || getButton(TButton.BACK) || getButton(TStick.LEFT) || getButton(TStick.RIGHT)) {
            return true;
        }

        return false;
    }

}
