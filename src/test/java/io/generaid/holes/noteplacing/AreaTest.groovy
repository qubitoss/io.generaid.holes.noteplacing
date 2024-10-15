package io.generaid.holes.noteplacing

import spock.lang.Specification

class AreaTest extends Specification {

    def 'should recognize if areas are overlapping - #description'(int[] p1, int[] p2, boolean hasOverlap) {
        setup:
        Area a1 = createArea("a1", p1[0], p1[1], p1[2], p1[3])
        Area a2 = createArea("a2", p2[0], p2[1], p2[2], p2[3])

        when:
        boolean actualA1A2 = a1.isOverlapping(a2)
        boolean actualA2A1 = a2.isOverlapping(a1)

        then:
        hasOverlap == actualA1A2
        hasOverlap == actualA2A1

        where:
        p1               | p2               | hasOverlap || description
        [10, 30, 10, 10] | [30, 10, 10, 10] | false      || 'x no overlap, y no overlap'
        [10, 30, 10, 10] | [15, 10, 10, 10] | false      || 'x overlap, y no overlap'
        [10, 15, 10, 10] | [30, 10, 10, 10] | false      || 'x no overlao, y overlap'

        [10, 15, 10, 10] | [15, 10, 10, 10] | true       || 'x overlap, y overlap'
        [10, 10, 10, 10] | [10, 10, 10, 10] | true       || 'both areas on exact same position with same size'
        [10, 10, 10, 10] | [20, 10, 10, 10] | false      || 'horizontally next to each other'

        [20, 20, 10, 10] | [10, 10, 10, 10] | false      || 'corner on same position'
        [10, 15, 10, 10] | [10, 10, 10, 10] | true       || 'vertical next to each other with overlap'
        [10, 10, 10, 10] | [15, 10, 10, 10] | true       || 'horizontally next to each other with overlap'

        [10, 20, 10, 10] | [10, 10, 10, 10] | false      || 'vertical next to each other'
        [10, 10, 30, 30] | [20, 20, 10, 10] | true       || 'one area is smaller and full in bigger area'
        [20, 15, 10, 10] | [10, 10, 30, 10] | true       || 'smaller area overlap bigger area vertically'

        [10, 10, 10, 30] | [15, 20, 10, 10] | true       || 'smaller area overlap biggger area horizontally'
        [10, 10, 10, 30] | [30, 20, 10, 10] | false      || 'smaller area no overlap bigger area vertically'
        [20, 30, 10, 10] | [10, 10, 30, 10] | false      || 'smaller area no overlap bigger area horizontally'
    }

    private Area createArea(String tag, int x, int y, int w, int h) {
        new AreaImpl(tag, new BigDecimal(x), new BigDecimal(y), new BigDecimal(w), new BigDecimal(h))
    }

    private static record AreaImpl(String tag, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) implements Area {
        @Override
        BigDecimal getXplusW() { x.add(w) }

        @Override
        BigDecimal getYplusH() { y.add(h) }
    }
}
