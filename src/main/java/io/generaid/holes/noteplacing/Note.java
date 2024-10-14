package io.generaid.holes.noteplacing;

import java.math.BigDecimal;

public record Note(String tag, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) implements Area {

    @Override
    public BigDecimal getX() {
        return x;
    }

    @Override
    public BigDecimal getY() {
        return y;
    }

    @Override
    public BigDecimal getW() {
        return w;
    }

    @Override
    public BigDecimal getH() {
        return h;
    }

    @Override
    public BigDecimal getXplusW() {
        return x.add(w);
    }

    @Override
    public BigDecimal getYplusH() {
        return y.add(h);
    }
}
