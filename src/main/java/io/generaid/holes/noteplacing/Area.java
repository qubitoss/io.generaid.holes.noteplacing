package io.generaid.holes.noteplacing;

import java.math.BigDecimal;

import static java.math.BigDecimal.TWO;
import static java.math.MathContext.DECIMAL128;

// TODO better name?
public interface Area {

    default boolean isOverlapping(Area other) {
        return isOverlappingOnX(other) && isOverlappingOnY(other);
    }

    default boolean isOverlappingOnX(Area other) {
        BigDecimal thisXplusW = this.x().add(this.w());
        BigDecimal otherXplusW = other.x().add(other.w());

        if (thisXplusW.compareTo(other.x()) > 0)
            return this.x().compareTo(otherXplusW) < 0;
        else
            return false;
    }

    default boolean isOverlappingOnY(Area other) {
        BigDecimal thisYplusH = this.y().add(this.h());
        BigDecimal otherYplusH = other.y().add(other.h());

        if (thisYplusH.compareTo(other.y()) > 0)
            return this.y().compareTo(otherYplusH) < 0;
        else
            return false;
    }

    String tag();

    BigDecimal x();

    BigDecimal y();

    BigDecimal w();

    BigDecimal h();

    default BigDecimal getXplusW() {
        return x().add(w());
    }

    default BigDecimal getYplusH() {
        return y().add(h());
    }

    default BigDecimal getCenterX() {
        return x().add(w().divide(TWO, DECIMAL128));
    }

    default BigDecimal getCenterY() {
        return y().add(h().divide(TWO, DECIMAL128));
    }
}
