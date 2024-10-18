package io.generaid.holes.noteplacing;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TWO;

public class HoleWithNote implements Area {

    // TODO
    private final static BigDecimal MARGIN = ONE;

    private final Hole hole;
    private final Note note;
    private final PlacementRules placementRules = new PlacementRules();

    public HoleWithNote(Hole hole, Note note) {
        this.hole = hole;
        this.note = note;
        if (!hole.tag().equals(note.tag())) {
            throw new IllegalArgumentException("Hole and note must have the same tag! Tag on hole: '" + hole.tag() + "', tag on note: '" + note.tag() + "'");
        }
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
}
