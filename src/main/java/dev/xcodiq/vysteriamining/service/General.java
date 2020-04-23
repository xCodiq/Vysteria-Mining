/*
 *   ~
 *   ~ Copyright 2020 NeverEndingPvP. All rights reserved.
 *   ~
 *   ~ Licensed under the NeverEndingPvP License, Version 1.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~
 *   ~ You are not allowed to edit the source.
 *   ~ You are not allowed to edit this text.
 *   ~ You are not allowed to sell this source.
 *   ~ You are not allowed to distribute this source in any way.
 *   ~ You are not allowed to claim this as yours.
 *   ~ You are not allowed to distribute.
 *   ~ You are not allowed to make own terms.
 *   ~ You are not allowed to place own warranty.
 *   ~ You are not allowed to make any sublicense.
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS.
 *   ~
 *   ~ Author: xCodiq (Discord: Codiq#3662)
 *   ~
 */

package dev.xcodiq.vysteriamining.service;

/**
 * Thanks Hyfe for letting me use this!
 * https://github.com/Hyfe-JavaDebug/simple-spigot/blob/master/src/main/java/me/hyfe/simplespigot/service/General.java
 */
public class General {

    public static String formatSeconds(long initialSeconds) {
        long years = initialSeconds / 31536000;
        long months = initialSeconds % 31536000 / 2592000; // Months calculated with 30 days.
        long weeks = initialSeconds % 2592000 / 604800;
        long days = initialSeconds % 604800 / 86400;
        long hours = initialSeconds % 86400 / 3600;
        long minutes = initialSeconds % 3600 / 60;
        long seconds = initialSeconds % 60;
        if (minutes < 1) {
            return String.format("%ds", seconds);
        }
        if (hours < 1) {
            return String.format("%dm %ds", minutes, seconds);
        }
        if (days < 1) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        }
        if (weeks < 1) {
            return String.format("%dd %dh %dm %ds", days, hours, minutes, seconds);
        }
        if (months < 1) {
            return String.format("%dw %dd %dh %dm %ds", weeks, days, hours, minutes, seconds);
        }
        if (years < 1) {
            return String.format("%dmo %dw %dd %dh %dm %ds", months, weeks, days, hours, minutes, seconds);
        }
        return String.format("%dy %dmo %dw %dd %dh %dm %ds", years, months, weeks, days, hours, minutes, seconds);
    }

    private static String decimal(String s, int i) {
        int b = s.length() - i;
        return s.substring(0, b) + "." + s.substring(b, b + 2);
    }

    /**
     * Get a converted string
     *
     * @param integer the integer you want to convert
     * @return converted integer {@value "123456" -> "123k"}
     */
    public static String formatInt(int integer) {
        String n = Integer.toString(integer);
        int l = n.length();
        if (l < 4) return n;
        if (l < 7) {
            return decimal(n, 3) + "k";
        }
        if (l < 10) {
            return decimal(n, 6) + "M";
        }
        if (l < 13) {
            return decimal(n, 9) + "B";
        }
        if (l < 16) {
            return decimal(n, 12) + "T";
        }
        if (l < 19) {
            return decimal(n, 15) + "Q";
        }
        return n;
    }
}
