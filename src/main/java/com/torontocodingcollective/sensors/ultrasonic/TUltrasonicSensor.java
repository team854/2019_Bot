package com.torontocodingcollective.sensors.ultrasonic;

import edu.wpi.first.wpilibj.AnalogInput;

public class TUltrasonicSensor {

    private double            v40;            // Use this voltage to determine "far" and "close" distance

    double                    m1, m2, b1, b2; // slope and b values for two different equations

    private final AnalogInput analogInput;

    public TUltrasonicSensor(int analogInputPort) {
        analogInput = new AnalogInput(analogInputPort);
    }

    /**
     * Calibrate the sensor by using two y=mx+b equations so that given a voltage,
     * we can determine the distance as voltage depends on the distance
     * 
     * @param v20
     *            voltage at a distance of 20 inches
     * @param v40
     *            voltage at a distance of 40 inches
     * @param v80
     *            voltage at a distance of 80 inches
     */
    public void calibrate(double v20, double v40, double v80) {
        this.v40 = v40;

        // calculating m1, b1 for voltage < v40
        m1 = (40.0 - 20.0) / (v40 - v20);
        b1 = 20.0 - (m1 * v20);

        // calculating m2, b2 for voltage > v40
        m2 = (80 - 40) / (v80 - v40);
        b2 = 40.0 - (m2 * v40);
    }

    /***
     * Get the distance in inches from the back face of the Ultrasonic sensor
     * 
     * @return double distance in inches
     */
    public double getDistance() {

        double distance = 0;
        double voltage = analogInput.getVoltage();

        // The voltage is not really linear, so for far and close distance we have two
        // equations
        if (voltage < v40) {
            distance = m1 * voltage + b1;
        } else {
            distance = m2 * voltage + b2;
        }

        // return the distance
        return distance;
    }
}
