package com.qualitest.tacoe.lsbm;

import java.nio.file.Path;
import java.util.Map;

public class NoSuchDeviceFoundInScanListException extends Exception {

    /**
     * constructor.
     *
     * @param   mapOfSearchFilter   map of search filter with searh criteria.
     */
    public NoSuchDeviceFoundInScanListException(final Map mapOfSearchFilter) {
        super("Device not found in Scan list with criteria:  "
            + mapOfSearchFilter.keySet().toArray()[0]
            + "="
            + mapOfSearchFilter.values().toArray()[0]);
    }
}
