package com.app.utilities.utilitiesActivity.bussola.model;

import com.app.utilities.R;

public enum CardinalDirection {
    NORTH(R.string.cardinal_direction_north),
    NORTHEAST(R.string.cardinal_direction_northeast),
    EAST(R.string.cardinal_direction_east),
    SOUTHEAST(R.string.cardinal_direction_southeast),
    SOUTH(R.string.cardinal_direction_south),
    SOUTHWEST(R.string.cardinal_direction_southwest),
    WEST(R.string.cardinal_direction_west),
    NORTHWEST(R.string.cardinal_direction_northwest);
    private final int labelResourceId;

    CardinalDirection(int labelResourceId) {
        this.labelResourceId = labelResourceId;
    }

    public final int getLabelResourceId() {
        return this.labelResourceId;
    }
}
