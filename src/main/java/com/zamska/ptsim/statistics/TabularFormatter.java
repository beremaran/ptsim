package com.zamska.ptsim.statistics;

import java.util.List;

public class TabularFormatter implements CollectorResultFormatter {
    @Override
    public String format(List<CollectorResult> results) {
        StringBuilder sb = new StringBuilder();
        results.forEach(r -> sb.append(
                String.format(
                        "%1$-20s = %2$-20.02f\n",
                        r.getKey(), r.getValue()
                )
                )
        );
        return sb.toString();
    }
}
