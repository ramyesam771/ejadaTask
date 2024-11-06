package com.ejadaSolutions.common.utils.timer;

import com.ejadaSolutions.common.utils.logs.MyLogger;
import org.apache.logging.log4j.core.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * implementation for a timer
 *
 * @author Mahmoud Osama
 */
public class MyTimer {
    public static Logger log = new MyLogger().getLogger();

    /**
     * Retrieves the current time formatted as a string and logs the action.
     *
     * This method fetches the current time using `LocalDateTime.now()` and formats it according to the pattern
     * "yy_MM_dd_HH_mm_ss". This format includes the year, month, day, hour, minute, and second, making it
     * suitable for file naming or timestamping purposes.
     *
     * Logs the formatted current time for reference.
     *
     * @return The current time formatted as a string in the pattern "yy_MM_dd_HH_mm_ss".
     */
    public String getCurrentTimeInString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy_MM_dd_HH_mm_ss");
        String formattedTime = now.format(formatter);
        log.info("Current time formatted as: {}", formattedTime);
        return formattedTime;
    }

}