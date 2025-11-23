package com.example.localmarkettracker.utils;

import com.example.localmarkettracker.models.ItemPrice;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.components.*;
import java.util.ArrayList;
import java.util.List;

public class ChartUtils {

    public static void plotPriceHistory(LineChart chart, List<ItemPrice> list) {
        if (list == null || list.isEmpty()) {
            chart.clear();
            return;
        }

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            entries.add(new Entry(i, (float) list.get(i).getPrice()));
        }
        LineDataSet ds = new LineDataSet(entries, list.get(0).getProductName());
        ds.setLineWidth(2f);
        ds.setCircleRadius(3f);
        LineData ld = new LineData(ds);
        chart.setData(ld);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.invalidate();
    }
}
