package io.generaid.holes.noteplacing;

import java.math.BigDecimal;

import static io.generaid.holes.noteplacing.PlacementChance.*;

public class PlacementArea implements Area {

    private final BigDecimal x;
    private final BigDecimal y;
    private final BigDecimal w;
    private final BigDecimal h;

    private PlacementChance placementChance = UNKNOWN;
    private BigDecimal overlappedProportion = BigDecimal.ZERO;

    PlacementArea(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public PlacementChance getPlacementChance() {
        return placementChance;
    }

    public void setPlacementChance(PlacementChance placementChance) {
        this.placementChance = placementChance;
    }

    public BigDecimal getOverlappedProportion(PlacementZone zone) {
        return switch (placementChance) {
            case EXCELLENT -> BigDecimal.ZERO;
            case POSSIBLE -> overlappedProportion;
            case IMPOSSIBLE, UNKNOWN ->
                    throw new IllegalStateException("Can't provide overlapped proportion for " + zone);
        };
    }

    public void setOverlappedProportion(PlacementZone zone, BigDecimal overlappedProportion) {
        switch (placementChance) {
            case POSSIBLE -> this.overlappedProportion = overlappedProportion;
            case EXCELLENT, IMPOSSIBLE, UNKNOWN ->
                    throw new IllegalStateException("Can't set overlapped proportion for " + zone);
        }
    }

    // TODO think about it again
    @Override
    public String tag() {
        return null;
    }

    // TODO outsource to AbstactArea
    @Override
    public BigDecimal x() {
        return x;
    }

    @Override
    public BigDecimal y() {
        return y;
    }

    @Override
    public BigDecimal w() {
        return w;
    }

    @Override
    public BigDecimal h() {
        return h;
    }
}
