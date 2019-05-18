package com.zamska.ptsim.statistics;

import com.zamska.ptsim.statistics.collectors.DataCollector;

public interface DataCollectorListener {
    void onRegisterDataCollector(DataCollector collector);
}
