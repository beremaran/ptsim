package com.zamska.ptsim.statistics;

import java.util.List;

public interface CollectorResultFormatter {
    String format(List<CollectorResult> results);
}
