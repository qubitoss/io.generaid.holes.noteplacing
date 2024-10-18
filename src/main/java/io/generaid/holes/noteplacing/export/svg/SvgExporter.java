package io.generaid.holes.noteplacing.export.svg;

import io.generaid.holes.noteplacing.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.math.BigDecimal.TWO;
import static java.math.MathContext.DECIMAL128;

public class SvgExporter {

    private final HolePanel holePanel;
    private final SvgExporterConfiguration config;

    private SvgExporter(HolePanel holePanel, SvgExporterConfiguration configuration) {
        this.holePanel = holePanel;
        this.config = configuration;
    }

    public static void exportToSvg(HolePanel holePanel) {
        exportToSvg(holePanel, SvgExporterConfiguration.create());
    }

    public static void exportToSvg(HolePanel holePanel, SvgExporterConfiguration configuration) {
        new SvgExporter(holePanel, configuration).export();
    }

    private void export() {
        StringBuilder svg = new StringBuilder();
        BigDecimal svgWidth = findHighestXValueOnHole().add(findWidestWOnNote().multiply(TWO));
        BigDecimal svgHeight = findHighestYValueOnHole().add(findWidestHOnNote().multiply(TWO));

        String header = String.format("<svg width='%s' height='%s' viewBox='0 0 %s %s' xmlns='http://www.w3.org/2000/svg'>\n",
                svgWidth, svgHeight, svgWidth, svgHeight);

        svg.append(header);
        svg.append(String.format("<g transform='translate(%s) scale(%s)'>", config.getQuadrant().getTranslate(svgWidth, svgHeight), config.getQuadrant().getScale()));
//        svg.append("<rect width='100%' height='100%' fill='green' />");

        streamHoleWithNotes().forEach(holeWithNote -> svg.append(createHoleWithNoteAsSvg(holeWithNote)));
        streamHoleWithNotes().forEach(holeWithNote -> svg.append(createHoleWithNoteAsSvg(holeWithNote)));

        svg.append("</g></svg>");

        writeToFile(svg.toString());
    }

    private BigDecimal findHighestXValueOnHole() {
        return findMaxOnFunction(streamHoles(), Hole::getXplusW);
    }

    private BigDecimal findHighestYValueOnHole() {
        return findMaxOnFunction(streamHoles(), Hole::getYplusH);
    }

    private BigDecimal findWidestWOnNote() {
        return findMaxOnFunction(streamNotes(), Note::w);
    }

    private BigDecimal findWidestHOnNote() {
        return findMaxOnFunction(streamNotes(), Note::h);
    }

    private Stream<Hole> streamHoles() {
        return streamHoleWithNotes().map(HoleWithNote::hole);
    }

    private Stream<Note> streamNotes() {
        return streamHoleWithNotes().map(HoleWithNote::note);
    }

    private Stream<HoleWithNote> streamHoleWithNotes() {
        return holePanel.getHoleWithNotes().values().stream();
    }

    private <T extends Area> BigDecimal findMaxOnFunction(Stream<T> stream, Function<T, BigDecimal> f) {
        return stream
                .max(Comparator.comparing(f))
                .map(f)
                .orElse(BigDecimal.ZERO);
    }

    private String createHoleWithNoteAsSvg(HoleWithNote holeWithNote) {
        return createSvgRect(holeWithNote)
                + createAreaAsSvg(holeWithNote.hole())
                + createAreaAsSvg(holeWithNote.note());
    }

    private String createAreaAsSvg(Area area) {
        return createSvgRect(area) + createSvgText(area);
    }

    private String createSvgRect(Area area) {
        String additionalXmlRectAttributes = config.getAdditionalXmlRectAttributes(area);
        String rectColor = config.getRectColor(area);
        return String.format("<rect x='%s' y='%s' width='%s' height='%s' fill='%s' %s/>", area.x(), area.y(), area.w(), area.h(), rectColor, additionalXmlRectAttributes);
    }

    private String createSvgText(Area area) {
        BigDecimal cx = area.x().add(area.w().divide(TWO, DECIMAL128));
        BigDecimal cy = area.y().add(area.h().divide(TWO, DECIMAL128));
        BigDecimal fontSize = area.w().min(area.h()).multiply(new BigDecimal("0.5"));
        String additionalXmlTextAttributes = config.getAdditionalXmlTextAttributes(area);
        String textColor = config.getTextColor(area);

        String textTemplate = "<text x='%s' y='%s' transform-origin='%s %s' transform='scale(%s)' font-size='%s' font-family='Arial' fill='%s' text-anchor='middle' dominant-baseline='middle' %s>%s</text>\n";
        return String.format(textTemplate, cx, cy, cx, cy, config.getQuadrant().getScale(), fontSize, textColor, additionalXmlTextAttributes, area.tag());
    }

    private void writeToFile(String content) {
        try {
            Files.write(config.getTargetFilePath(), content.getBytes());
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

}
