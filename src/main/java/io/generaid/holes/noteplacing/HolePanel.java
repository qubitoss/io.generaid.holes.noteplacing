package io.generaid.holes.noteplacing;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TWO;

public class HolePanel {

    private Map<String, Hole> holes;
    private Map<String, Note> notes;

    public HolePanel(Map<String, Hole> holes, Map<String, Note> notes) {
        // TODO ensure that holes are not overlapping each other
        this.holes = holes;
        this.notes = notes;
    }

    public void placeNotes() {
        notes.forEach((tag, note) -> {
            Hole hole = holes.get(tag);
            if (hole == null)
                return;

            // set on right side
            note.setX(hole.getXplusW().add(ONE));
            note.setY(hole.getCenterY().subtract(note.h().divide(new BigDecimal(2))));

            if (!isOverlapping(note))
                return;

            // set on left side
            note.setX(hole.x().subtract(note.w()).subtract(ONE));
            note.setY(hole.getCenterY().subtract(note.h().divide(new BigDecimal(2))));

            if (!isOverlapping(note))
                return;

            // set on top
            note.setX(hole.getCenterX().subtract(note.w().divide(new BigDecimal(2))));
            note.setY(hole.getYplusH().add(ONE));

            if (!isOverlapping(note))
                return;

            // set on bottom
            note.setX(hole.getCenterX().subtract(note.w().divide(new BigDecimal(2))));
            note.setY(hole.y().subtract(note.h()).subtract(ONE));
        });
    }

    private boolean isOverlapping(Area area) {
        boolean overlappsWithAHole = holes.values().stream()
                .filter(e -> e != area)
                .anyMatch(area::isOverlapping);

        if (overlappsWithAHole)
            return true;

        return notes.values().stream()
                .filter(e -> e != area)
                .anyMatch(area::isOverlapping);
    }

    // ---
    // TODO outsource below

    private enum Quadrant {
        I(false, true),
        II(true, true),
        III(true, false),
        IV(false, false);

        private final boolean transformOnX;
        private final boolean transformOnY;

        Quadrant(boolean transformOnX, boolean transformOnY) {
            this.transformOnX = transformOnX;
            this.transformOnY = transformOnY;
        }

        public String getScale() {
            return String.format("%s, %s", transformOnX ? "-1" : "1", transformOnY ? "-1" : "1");
        }

        public String getTranslate(BigDecimal x, BigDecimal y) {
            return String.format("%s, %s", transformOnX ? x.toString() : "0", transformOnY ? y.toString() : "0");
        }
    }

    private final Quadrant quadrant = Quadrant.I;

    public void exportToSvg() {
        StringBuilder svg = new StringBuilder();
        BigDecimal svgWidth = findHighestXValueOnHole().add(findWidestWOnNote().multiply(TWO));
        BigDecimal svgHeight = findHighestYValueOnHole().add(findWidestHOnNote().multiply(TWO));

        String header = String.format("<svg width='%s' height='%s' viewBox='0 0 %s %s' xmlns='http://www.w3.org/2000/svg'>\n",
                svgWidth, svgHeight, svgWidth, svgHeight);

        svg.append(header);
        svg.append(String.format("<g transform='translate(%s) scale(%s)'>", quadrant.getTranslate(svgWidth, svgHeight), quadrant.getScale()));
//        svg.append("<rect width='100%' height='100%' fill='green' />");

        for (Hole hole : holes.values()) {
            svg.append(createHoleAsSvg(hole));
        }

        for (Note note : notes.values()) {
            svg.append(createNoteAsSvg(note));
        }

        svg.append("</g></svg>");

        writeToFile(svg.toString());
    }

    private BigDecimal findHighestXValueOnHole() {
        return findMaxOnFunction(holes.values(), Hole::getXplusW);
    }

    private BigDecimal findHighestYValueOnHole() {
        return findMaxOnFunction(holes.values(), Hole::getYplusH);
    }

    private BigDecimal findWidestWOnNote() {
        return findMaxOnFunction(notes.values(), Note::w);
    }

    private BigDecimal findWidestHOnNote() {
        return findMaxOnFunction(notes.values(), Note::h);
    }

    private <T extends Area>  BigDecimal findMaxOnFunction(Collection<T> list, Function<T, BigDecimal> f) {
        return list.stream()
                .max(Comparator.comparing(f))
                .map(f)
                .orElse(BigDecimal.ZERO);
    }

    private String createHoleAsSvg(Hole hole) {
        BigDecimal cx = hole.x().add(hole.w().divide(BigDecimal.TWO));
        BigDecimal cy = hole.y().add(hole.h().divide(BigDecimal.TWO));
        BigDecimal fontSize = hole.w().min(hole.h()).multiply(new BigDecimal("0.5"));

        String textTemplate = "<text x='%s' y='%s' transform-origin='%s %s' transform='scale(%s)' font-size='%s' font-family='Arial' fill='white' text-anchor='middle' dominant-baseline='middle'>%s</text>\n";

        String rect = String.format("<rect x='%s' y='%s' width='%s' height='%s' fill='red'/>", hole.x(), hole.y(), hole.w(), hole.h());
        String text = String.format(textTemplate, cx, cy, cx, cy, quadrant.getScale(), fontSize, hole.tag());

        return rect + text;
    }

    private String createNoteAsSvg(Note note) {
        BigDecimal cx = note.x().add(note.w().divide(BigDecimal.TWO));
        BigDecimal cy = note.y().add(note.h().divide(BigDecimal.TWO));
        BigDecimal fontSize = note.w().min(note.h()).multiply(new BigDecimal("0.5"));

        String textTemplate = "<text x='%s' y='%s' transform-origin='%s %s' transform='scale(%s)' font-size='%s' font-family='Arial' fill='white' fill-opacity='0.4' text-anchor='middle' dominant-baseline='middle'>%s</text>\n";

        String rect = String.format("<rect x='%s' y='%s' width='%s' height='%s' fill='blue' fill-opacity='0.4'/>", note.x(), note.y(), note.w(), note.h());
        String text = String.format(textTemplate, cx, cy, cx, cy, quadrant.getScale(), fontSize, note.tag());

        return rect + text;
    }

    private void writeToFile(String content) {
        try {
            Files.write(Paths.get("/tmp/holes.svg"), content.getBytes());
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
