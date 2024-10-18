package io.generaid.holes.noteplacing.export.svg;

import io.generaid.holes.noteplacing.Area;
import io.generaid.holes.noteplacing.Hole;
import io.generaid.holes.noteplacing.HoleWithNote;
import io.generaid.holes.noteplacing.Note;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SvgExporterConfiguration {
    private final Quadrant quadrant;
    private final Path targetFilePath;
    private final String additionalXmlRectAttributesForHole;
    private final String additionalXmlRectAttributesForNote;
    private final String additionalXmlRectAttributesForHoleWithNote;
    private final String additionalXmlTextAttributesForHole;
    private final String additionalXmlTextAttributesForNote;
    private final String holeRectColor;
    private final String noteRectColor;
    private final String holeWithNoteRectColor;
    private final String holeTextColor;
    private final String noteTextColor;

    private SvgExporterConfiguration(Builder builder) {
        this.quadrant = builder.quadrant;
        this.targetFilePath = builder.targetFilePath;
        this.additionalXmlRectAttributesForHole = builder.additionalXmlRectAttributesForHole;
        this.additionalXmlRectAttributesForNote = builder.additionalXmlRectAttributesForNote;
        this.additionalXmlRectAttributesForHoleWithNote = builder.additionalXmlRectAttributesForHoleWithNote;
        this.additionalXmlTextAttributesForHole = builder.additionalXmlTextAttributesForHole;
        this.additionalXmlTextAttributesForNote = builder.additionalXmlTextAttributesForNote;
        this.holeRectColor = builder.holeRectColor;
        this.noteRectColor = builder.noteRectColor;
        this.holeWithNoteRectColor = builder.holeWithNoteRectColor;
        this.holeTextColor = builder.holeTextColor;
        this.noteTextColor = builder.noteTextColor;
    }

    public Quadrant getQuadrant() {
        return quadrant;
    }

    public Path getTargetFilePath() {
        return targetFilePath;
    }

    public String getAdditionalXmlRectAttributes(Area area) {
        return switch (area) {
            case Hole ignored -> additionalXmlRectAttributesForHole;
            case Note ignored2 -> additionalXmlRectAttributesForNote;
            case HoleWithNote ignored3 -> additionalXmlRectAttributesForHoleWithNote;
            default -> throw new IllegalArgumentException("Unsupported Area type: " + area.getClass().getName());
        };
    }

    public String getAdditionalXmlTextAttributes(Area area) {
        return switch (area) {
            case Hole ignored -> additionalXmlTextAttributesForHole;
            case Note ignored2 -> additionalXmlTextAttributesForNote;
            default -> throw new IllegalArgumentException("Unsupported Area type: " + area.getClass().getName());
        };
    }

    public String getRectColor(Area area) {
        return switch (area) {
            case Hole ignored -> holeRectColor;
            case Note ignored2 -> noteRectColor;
            case HoleWithNote ignored3 -> holeWithNoteRectColor;
            default -> throw new IllegalArgumentException("Unsupported Area type: " + area.getClass().getName());
        };
    }

    public String getTextColor(Area area) {
        return switch (area) {
            case Hole ignored -> holeTextColor;
            case Note ignored2 -> noteTextColor;
            default -> throw new IllegalArgumentException("Unsupported Area type: " + area.getClass().getName());
        };
    }

    public static SvgExporterConfiguration create() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Quadrant quadrant = Quadrant.I;
        private Path targetFilePath = Paths.get("/tmp/holes.svg");
        private String additionalXmlRectAttributesForHole = "";
        private String additionalXmlRectAttributesForNote = "fill-opacity='0.4'";
        private String additionalXmlRectAttributesForHoleWithNote = "fill-opacity='0.2'";
        private String additionalXmlTextAttributesForHole = "";
        private String additionalXmlTextAttributesForNote = "fill-opacity='0.4'";
        private String holeRectColor = "red";
        private String noteRectColor = "blue";
        private String holeWithNoteRectColor = "orange";
        private String holeTextColor = "white";
        private String noteTextColor = "white";

        private Builder() {
        }

        public Builder quadrant(Quadrant quadrant) {
            this.quadrant = quadrant;
            return this;
        }

        public Builder targetFilePath(Path targetFilePath) {
            this.targetFilePath = targetFilePath;
            return this;
        }

        public Builder additionalXmlRectAttributesForHole(String additionalXmlRectAttributesForHole) {
            this.additionalXmlRectAttributesForHole = additionalXmlRectAttributesForHole;
            return this;
        }

        public Builder additionalXmlRectAttributesForNote(String additionalXmlRectAttributesForNote) {
            this.additionalXmlRectAttributesForNote = additionalXmlRectAttributesForNote;
            return this;
        }

        public Builder additionalXmlRectAttributesForHoleWithNote(String additionalXmlRectAttributesForHoleWithNote) {
            this.additionalXmlRectAttributesForHoleWithNote = additionalXmlRectAttributesForHoleWithNote;
            return this;
        }

        public Builder additionalXmlTextAttributesForHole(String additionalXmlTextAttributesForHole) {
            this.additionalXmlTextAttributesForHole = additionalXmlTextAttributesForHole;
            return this;
        }

        public Builder additionalXmlTextAttributesForNote(String additionalXmlTextAttributesForNote) {
            this.additionalXmlTextAttributesForNote = additionalXmlTextAttributesForNote;
            return this;
        }

        public Builder holeRectColor(String holeRectColor) {
            this.holeRectColor = holeRectColor;
            return this;
        }

        public Builder noteRectColor(String noteRectColor) {
            this.noteRectColor = noteRectColor;
            return this;
        }

        public Builder holeWithNoteRectColor(String holeWithNoteRectColor) {
            this.holeWithNoteRectColor = this.holeWithNoteRectColor;
            return this;
        }

        public Builder holeTextColor(String holeTextColor) {
            this.holeTextColor = holeTextColor;
            return this;
        }

        public Builder noteTextColor(String noteTextColor) {
            this.noteTextColor = noteTextColor;
            return this;
        }

        public SvgExporterConfiguration build() {
            return new SvgExporterConfiguration(this);
        }
    }
}
