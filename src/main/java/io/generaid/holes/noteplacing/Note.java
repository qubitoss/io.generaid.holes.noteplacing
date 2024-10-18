package io.generaid.holes.noteplacing;

import java.math.BigDecimal;

public class Note implements Area {

    private final String tag;
    private BigDecimal x;
    private BigDecimal y;
    private final BigDecimal w;
    private final BigDecimal h;

    public Note(String tag, String x, String y, String w, String h) {
        this(tag, new BigDecimal(x), new BigDecimal(y), new BigDecimal(w), new BigDecimal(h));
    }

    public Note(String tag, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        this.tag = tag;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public String tag() {
        return tag;
    }

    @Override
    public BigDecimal x() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    @Override
    public BigDecimal y() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    @Override
    public BigDecimal w() {
        return w;
    }

    @Override
    public BigDecimal h() {
        return h;
    }

    @Override
    public String toString() {
        return "Note[" +
                "tag='" + tag + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                ']';
    }
}
