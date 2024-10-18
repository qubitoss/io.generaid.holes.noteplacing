package io.generaid.holes.noteplacing.export.svg;

import java.math.BigDecimal;

public enum Quadrant {
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