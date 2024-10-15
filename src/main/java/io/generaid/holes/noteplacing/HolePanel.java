package io.generaid.holes.noteplacing;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class HolePanel {

    private List<Hole> holes = new ArrayList<>();

    public void setHoles(List<Hole> holes) {
        // TODO check if no overlapping to each other and reject if so
        this.holes = new ArrayList<>(holes);
    }

    public void addHole(Hole hole) {
        // TODO check if overlaps with existent holes and reject if so
        holes.add(hole);
    }

    // ---
    // TODO outsource below

    private enum Quadrant {
        I(false, true), // 1, -1
        II(true, true), // -1, -1
        III(true, false), // -1, 1
        IV(false, false); // 1, 1

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

    private final Quadrant quadrant = Quadrant.IV;

    public void exportToSvg() {
        StringBuilder svg = new StringBuilder();
        BigDecimal svgWidth = findMaxOnX();
        BigDecimal svgHeight = findMaxOnY();

        String header = String.format("<svg width='%s' height='%s' viewBox='0 0 %s %s' xmlns='http://www.w3.org/2000/svg'>\n",
                svgWidth, svgHeight, svgWidth, svgHeight);

        svg.append(header);
        svg.append(String.format("<g transform='translate(%s) scale(%s)'>", quadrant.getTranslate(svgWidth, svgHeight), quadrant.getScale()));
//        svg.append("<rect width='100%' height='100%' fill='green' />");

        for (Hole hole : holes) {
            svg.append(createHoleAsSvg(hole));
        }

        svg.append("</g></svg>");

        writeToFile(svg.toString());
    }

    private BigDecimal findMaxOnX() {
        return findMinOnFunction(Hole::getXplusW);
    }

    private BigDecimal findMaxOnY() {
        return findMinOnFunction(Hole::getYplusH);
    }

    private BigDecimal findMinOnFunction(Function<Hole, BigDecimal> f) {
        return holes.stream()
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
        String text = String.format(textTemplate, cx, cy, cx, cy, quadrant.getScale() ,fontSize, hole.tag());

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
