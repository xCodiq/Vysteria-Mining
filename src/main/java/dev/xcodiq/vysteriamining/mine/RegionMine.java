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

package dev.xcodiq.vysteriamining.mine;

import java.util.List;
import java.util.Map;

public class RegionMine {

    private final String regionId;
    private final String displayName;
    private final List<Map<String, Integer>> requirements;

    /**
     * Instantiates a new Region mine.
     *
     * @param regionId     the region id
     * @param displayName  the display name
     * @param requirements the requirements
     */
    public RegionMine(String regionId, String displayName, List<Map<String, Integer>> requirements) {
        this.regionId = regionId;
        this.displayName = displayName;
        this.requirements = requirements;
    }

    /**
     * Gets region id.
     *
     * @return the region id
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets requirements.
     *
     * @return the requirements
     */
    public List<Map<String, Integer>> getRequirements() {
        return requirements;
    }
}
