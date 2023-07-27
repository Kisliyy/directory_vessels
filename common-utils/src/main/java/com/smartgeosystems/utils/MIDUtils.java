package com.smartgeosystems.utils;


public final class MIDUtils {
    public static Long mmsiToMid(Long mmsi) {

        if (mmsi == null) {
            return null;
        }
        String strMmsi = mmsi.toString();

        if (strMmsi.length() != 9) {
            return null;
        }

        Long mid = null;
        if (strMmsi.charAt(0) == '0' || strMmsi.charAt(0) == '8') {
            mid = Long.valueOf(strMmsi.substring(1, 4));
        } else if (strMmsi.startsWith("00") || strMmsi.startsWith("98") || strMmsi.startsWith("99")) {
            mid = Long.valueOf(strMmsi.substring(2, 5));
        } else if (strMmsi.startsWith("111") || strMmsi.startsWith("970")) {
            mid = Long.valueOf(strMmsi.substring(3, 6));
        } else if (!strMmsi.startsWith("972") && !strMmsi.startsWith("974")) {
            mid = Long.valueOf(strMmsi.substring(0, 3));
        }

        return mid;
    }
}
