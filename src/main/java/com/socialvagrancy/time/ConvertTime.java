//===================================================================
// ConvertTime
//      Description:
//          This class handles the logic required to covert time
//          between the different Java date/time objects and human
//          readable Strings. The goal was to centralize the logic
//          in one place, so I don't have to include it constantly.
//===================================================================

package com.socialvagrancy.utils.time;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertTime {
    public static String instantToDateString(Instant timestamp) {
        // Convert to ZonedDateTime with UTC offset
        ZonedDateTime zonedDateTime = timestamp.atZone(ZoneOffset.UTC);

        // Formatter for your format: yyyy-MM-dd'T'HH:mm:ssz
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz");

        String formatted = formatter.format(zonedDateTime);
    
        return formatted;
    }
}
