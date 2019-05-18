package com.zamska.ptsim.statistics.formatters;

import com.zamska.ptsim.statistics.CollectorResult;

import java.util.List;

public interface CollectorResultFormatter {
    String format(List<CollectorResult> results);
}
