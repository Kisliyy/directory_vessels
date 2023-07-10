package com.smartgeosystems.directory_vessels.utils;

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
}
