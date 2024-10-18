package io.generaid.holes.noteplacing;

import java.math.BigDecimal;
import java.util.EnumMap;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TWO;
import static java.math.MathContext.DECIMAL128;

public class HoleWithNote implements Area {

    // TODO
    private final static BigDecimal MARGIN = ONE;

    private final Hole hole;
    private final Note note;

    private final EnumMap<PlacementZone, PlacementArea> placementAreas = new EnumMap<>(PlacementZone.class);

    public HoleWithNote(Hole hole, Note note) {
        this.hole = hole;
        this.note = note;
        if (!hole.tag().equals(note.tag())) {
            throw new IllegalArgumentException("Hole and note must have the same tag! Tag on hole: '" + hole.tag() + "', tag on note: '" + note.tag() + "'");
        }
        initPlacementAreas();
    }

    private void initPlacementAreas() {
        for (PlacementZone zone : PlacementZone.values()) {
            placementAreas.put(zone, createPlacementArea(zone));
        }
    }

    public EnumMap<PlacementZone, PlacementArea> getPlacementAreas() {
        return placementAreas;
    }

    @Override
    public String tag() {
        return hole.tag();
    }

    @Override
    public BigDecimal x() {
        return hole.x().subtract(note.w()).subtract(MARGIN);
    }

    @Override
    public BigDecimal y() {
        return hole.y().subtract(note.h()).subtract(MARGIN);
    }

    @Override
    public BigDecimal w() {
        return hole.w().add(note.w().multiply(TWO)).add(MARGIN.multiply(TWO));
    }

    @Override
    public BigDecimal h() {
        return hole.h().add(note.h().multiply(TWO)).add(MARGIN.multiply(TWO));
    }

    public Hole getHole() {
        return hole;
    }

    public Note getNote() {
        return note;
    }

    public BigDecimal getMargin() {
        return MARGIN;
    }

    private PlacementArea createPlacementArea(PlacementZone zone) {
        BigDecimal x = null;
        BigDecimal y = null;

        switch (zone) {
            case NORTH -> {
                x = hole.getCenterX().subtract(note.w().divide(TWO, DECIMAL128));
                y = hole.getYplusH().add(MARGIN);
            }
            case NORTH_EAST -> {
                x = hole.getXplusW().add(MARGIN);
                y = hole.getYplusH().add(MARGIN);
            }
            case EAST -> {
                x = hole.getXplusW().add(MARGIN);
                y = hole.getCenterY().subtract(note.h().divide(TWO, DECIMAL128));
            }
            case SOUTH_EAST -> {
                x = hole.getXplusW().add(MARGIN);
                y = hole.y().subtract(note.h()).subtract(MARGIN);
            }
            case SOUTH -> {
                x = hole.getCenterX().subtract(note.w().divide(TWO, DECIMAL128));
                y = hole.y().subtract(note.h()).subtract(MARGIN);
            }
            case SOUTH_WEST -> {
                x = hole.x().subtract(note.w()).subtract(MARGIN);
                y = hole.y().subtract(note.h()).subtract(MARGIN);
            }
            case WEST -> {
                x = hole.x().subtract(note.w()).subtract(MARGIN);
                y = hole.getCenterY().subtract(note.h().divide(TWO, DECIMAL128));
            }
            case NORTH_WEST -> {
                x = hole.x().subtract(note.w()).subtract(MARGIN);
                y = hole.getYplusH().add(MARGIN);
            }
        }

        return new PlacementArea(x, y, note.w(), note.h());
    }


}
