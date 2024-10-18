package io.generaid.holes.noteplacing;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.math.BigDecimal.TWO;
import static java.math.MathContext.DECIMAL128;

public class HolePanel {

    private final Map<String, HoleWithNote> holeWithNotes;

    public HolePanel(Map<String, Hole> holes, Map<String, Note> notes) {
        // TODO ensure that holes are not overlapping each other
        this.holeWithNotes = createHoleWithNotes(holes, notes);
        // evaluate placement of notes
    }

    private Map<String, HoleWithNote> createHoleWithNotes(Map<String, Hole> holes, Map<String, Note> notes) {
        return holes.values().stream()
                .map(hole -> {
                    Note note = notes.get(hole.tag());
                    if (note == null)
                        return null;
                    return new HoleWithNote(hole, note);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(e -> e.getHole().tag(), e -> e));
    }

    public Map<String, HoleWithNote> getHoleWithNotes() {
        return Collections.unmodifiableMap(holeWithNotes);
    }

    public void placeNotes() {
        holeWithNotes.forEach((tag, holeWithNote) -> {
            Hole hole = holeWithNote.getHole();
            Note note = holeWithNote.getNote();
            BigDecimal margin = holeWithNote.getMargin();

            // set on right side
            note.setX(hole.getXplusW().add(margin));
            note.setY(hole.getCenterY().subtract(note.h().divide(TWO, DECIMAL128)));

            if (!isOverlapping(note))
                return;

            // set on left side
            note.setX(hole.x().subtract(note.w()).subtract(margin));
            note.setY(hole.getCenterY().subtract(note.h().divide(TWO, DECIMAL128)));

            if (!isOverlapping(note))
                return;

            // set on top
            note.setX(hole.getCenterX().subtract(note.w().divide(TWO, DECIMAL128)));
            note.setY(hole.getYplusH().add(margin));

            if (!isOverlapping(note))
                return;

            // set on bottom
            note.setX(hole.getCenterX().subtract(note.w().divide(TWO, DECIMAL128)));
            note.setY(hole.y().subtract(note.h()).subtract(margin));
        });
    }

    public void placeNotesAgain() {
        holeWithNotes.forEach((tag, holeWithNote) -> {
            Hole hole = holeWithNote.getHole();
            Note note = holeWithNote.getNote();
            BigDecimal margin = holeWithNote.getMargin();

            if (!isOverlapping(note))
                return;

            // set on right side
            note.setX(hole.getXplusW().add(margin));
            note.setY(hole.getCenterY().subtract(note.h().divide(TWO, DECIMAL128)));

            if (!isOverlapping(note))
                return;

            // set on left side
            note.setX(hole.x().subtract(note.w()).subtract(margin));
            note.setY(hole.getCenterY().subtract(note.h().divide(TWO, DECIMAL128)));

            if (!isOverlapping(note))
                return;

            // set on top
            note.setX(hole.getCenterX().subtract(note.w().divide(TWO, DECIMAL128)));
            note.setY(hole.getYplusH().add(margin));

            if (!isOverlapping(note))
                return;

            // set on bottom
            note.setX(hole.getCenterX().subtract(note.w().divide(TWO, DECIMAL128)));
            note.setY(hole.y().subtract(note.h()).subtract(margin));
        });
    }

    public Map<Note, List<Area>> getAllOverlappingAreas() {
        return streamNotes()
                .map(e -> Map.entry(e, getOverlappingAreas(e)))
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Area> getOverlappingAreas(Area area) {
        return streamAllAreas()
                .filter(e -> e != area)
                .filter(area::isOverlapping)
                .toList();
    }

    private boolean isOverlapping(Area area) {
        return streamAllAreas()
                .filter(e -> e != area)
                .anyMatch(area::isOverlapping);
    }

    private Stream<Area> streamAllAreas() {
        return holeWithNotes.entrySet().stream()
                .flatMap(e -> Stream.of(e.getValue().getHole(), e.getValue().getNote()));
    }

    private Stream<Note> streamNotes() {
        return holeWithNotes.values().stream().map(HoleWithNote::getNote);
    }
}
