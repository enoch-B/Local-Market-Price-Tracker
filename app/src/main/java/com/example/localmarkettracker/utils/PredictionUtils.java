package com.example.localmarkettracker.utils;

import java.util.Random;

/**
 * PredictionUtils - small heuristic and placeholder for TFLite model integration.
 * For now returns simple moving average + small noise.
 */
public class PredictionUtils {

    public static double predictNextPrice(String productName) {
        // placeholder: return random nearby value (replace with TFLite model inference later)
        Random r = new Random(productName.hashCode());
        double base = 100.0 + Math.abs(productName.hashCode() % 50); // deterministic-ish base
        return base + r.nextDouble() * 10 - 5;
    }
}
