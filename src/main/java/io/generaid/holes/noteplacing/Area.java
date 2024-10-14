package io.generaid.holes.noteplacing;

import java.math.BigDecimal;

// TODO better name?
public interface Area {

    default boolean isOverlapping(Area other) {
        return isOverlappingOnX(other) && isOverlappingOnY(other);
    }

    default boolean isOverlappingOnX(Area other) {
        if (this.getXplusW().compareTo(other.getX()) > 0)
            return this.getX().compareTo(other.getXplusW()) < 0;
        else
            return false;
    }

    default boolean isOverlappingOnY(Area other) {
        if (this.getYplusH().compareTo(other.getY()) > 0)
            return this.getY().compareTo(other.getYplusH()) < 0;
        else
            return false;
    }

    BigDecimal getX();

    BigDecimal getY();

    BigDecimal getW();

    BigDecimal getH();

    BigDecimal getXplusW();

    BigDecimal getYplusH();
}
