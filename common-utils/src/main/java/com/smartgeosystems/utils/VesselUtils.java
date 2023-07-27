package com.smartgeosystems.utils;

import java.sql.Timestamp;
import java.time.Instant;

public final class VesselUtils {
    public static boolean isAton(Long mmsi) {
        var mmsiValue = String.valueOf(mmsi);
        return mmsiValue.startsWith("999");
    }

    public static boolean checkPackageTime(Timestamp currentPackageTime, Timestamp updatePackageTime) {
        if (currentPackageTime != null) {
            if (updatePackageTime != null) {
                return currentPackageTime.getTime() < updatePackageTime.getTime();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean checkPackageTime(Timestamp currentPackageTime, Instant updatePackageTime) {
        var updateTimestamp = updatePackageTime == null ? null : Timestamp.from(updatePackageTime);
        return checkPackageTime(currentPackageTime, updateTimestamp);
    }

    public static Double genLength(Double dimensionToBowA, Double dimensionToSternB){
        return (dimensionToBowA == null ? 0 : dimensionToBowA) +
                (dimensionToSternB == null ? 0 : dimensionToSternB);
    }

    public static Double genWidth(Double dimensionToPortC, Double dimensionToStarboardD){
        return (dimensionToPortC == null ? 0 : dimensionToPortC) +
                (dimensionToStarboardD == null ? 0 : dimensionToStarboardD);
    }
}
