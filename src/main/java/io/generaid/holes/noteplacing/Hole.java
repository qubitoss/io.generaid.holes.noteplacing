package io.generaid.holes.noteplacing;

import java.math.BigDecimal;

public record Hole(String tag, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) implements Area {

    public Hole(String tag, String centerX, String centerY, String sizeX, String sizeY) {
        this(tag,
                new BigDecimal(centerX).subtract(new BigDecimal(sizeX).divide(new BigDecimal(2))),
                new BigDecimal(centerY).subtract(new BigDecimal(sizeY).divide(new BigDecimal(2))),
                new BigDecimal(sizeX),
                new BigDecimal(sizeY));
    }
}
