package com.torontocodingcollective.oi;

public class TJoystickController_Logitech extends TJoystickController {

    private static final int MAX_SUPPORTED_BUTTONS = 12;

    public TJoystickController_Logitech(int port) {
        super(port, MAX_SUPPORTED_BUTTONS);
    }

}
