package com.torontocodingcollective;

public class TUtil {

    public static double round(double value, int decimals) {
        
        if (decimals < 0) {
            return value;
        }
        
        double roundFactor = 1;
        
        for (int i=0; i<decimals; i++) {
            roundFactor *= 10;
        }
        
        return Math.round(value * roundFactor) / roundFactor;
    }
}
