package com.zamska.ptsim.statistics.formatters;

import com.zamska.ptsim.statistics.CollectorResult;

import java.util.List;

public class TabularFormatter implements CollectorResultFormatter {
    @Override
    public String format(List<CollectorResult> results) {
        StringBuilder sb = new StringBuilder();
        results.forEach(r -> sb.append(
                String.format(
                        "%1$-30s = %2$-50.02f\n",
                        r.getKey(), r.getValue()
                )
                )
        );
        return sb.toString();
    }
}
