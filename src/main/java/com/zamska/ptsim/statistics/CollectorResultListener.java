package com.zamska.ptsim.statistics;

import java.util.List;

public interface CollectorResultListener {
    void onCollectorResult(List<CollectorResult> results);
}
