package io.generaid.holes.noteplacing;

public enum PlacementZone {
    NORTH(3),
    NORTH_EAST(8),
    EAST(1),
    SOUTH_EAST(5),
    SOUTH(4),
    SOUTH_WEST(7),
    WEST(2),
    NORTH_WEST(6);

    private final int proirity;

    /**
     * Lower number has higher priority.
     *
     * @param proirity
     */
    PlacementZone(int proirity) {
        this.proirity = proirity;
    }

    public int getProirity() {
        return proirity;
    }
}
