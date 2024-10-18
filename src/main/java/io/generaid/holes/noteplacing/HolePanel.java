package io.generaid.holes.noteplacing;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.generaid.holes.noteplacing.PlacementChance.*;
import static java.util.Comparator.comparing;

public class HolePanel {

    private final Map<String, HoleWithNote> holeWithNotes;

    public HolePanel(Map<String, Hole> holes, Map<String, Note> notes) {
        // TODO ensure that holes are not overlapping each other
        this.holeWithNotes = createHoleWithNotes(holes, notes);
        evaluateNotePlacements();
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

    private void evaluateNotePlacements() {
        holeWithNotes.forEach((tag, holeWithNote) -> {

            holeWithNote.getPlacementAreas().forEach((zone, placementArea) -> {

                for (Hole hole : streamHoles().toList()) {
                    if (placementArea.isOverlapping(hole)) {
                        placementArea.setPlacementChance(IMPOSSIBLE);
                        return;
                    }
                }

                for (HoleWithNote otherHoleWithNote : holeWithNotes.values()) {
                    if (holeWithNote == otherHoleWithNote)
                        continue;

                    if (placementArea.isOverlapping(otherHoleWithNote)) {
                        placementArea.setPlacementChance(POSSIBLE);
                        // TODO calc OverlappedProportion
                        // placementArea.setOverlappedProportion(zone, BigDecimal.ZERO);
                        break;
                    }
                }

                if (placementArea.getPlacementChance() == UNKNOWN) {
                    placementArea.setPlacementChance(EXCELLENT);
                }
            });
        });
    }

    public Map<String, HoleWithNote> getHoleWithNotes() {
        return Collections.unmodifiableMap(holeWithNotes);
    }

    public void placeNotes() {
        holeWithNotes.forEach((tag, holeWithNote) -> {
            Note note = holeWithNote.getNote();

            Optional<PlacementArea> bestPlacement = holeWithNote.getPlacementAreas().entrySet().stream()
                    .filter(e -> e.getValue().getPlacementChance() == EXCELLENT)
                    .min(comparing(e -> e.getKey().getProirity()))
                    .map(Map.Entry::getValue);

            if (bestPlacement.isPresent()) {
                note.setX(bestPlacement.get().x());
                note.setY(bestPlacement.get().y());
                return;
            }

            List<PlacementArea> possiblePlacement = holeWithNote.getPlacementAreas().entrySet().stream()
                    .filter(e -> e.getValue().getPlacementChance() == POSSIBLE)
                    .sorted(comparing(e -> e.getKey().getProirity()))
                    .map(Map.Entry::getValue)
                    .toList();

            if (possiblePlacement.isEmpty()) {
                System.err.println("No placement found for Note " + note.tag());
                return;
            }

            for (PlacementArea placementArea : possiblePlacement) {
                note.setX(placementArea.x());
                note.setY(placementArea.y());

                if (!isOverlapping(note))
                    return;
            }

            System.err.println("Placement found for Note " + note.tag() + " has overlaps");
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

    private Stream<Hole> streamHoles() {
        return holeWithNotes.values().stream().map(HoleWithNote::getHole);
    }

    private Stream<Note> streamNotes() {
        return holeWithNotes.values().stream().map(HoleWithNote::getNote);
    }
}
