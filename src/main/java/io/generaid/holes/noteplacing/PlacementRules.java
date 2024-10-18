package io.generaid.holes.noteplacing;

import java.util.EnumMap;

public class PlacementRules {

    private final EnumMap<PlacementPlace, PlacementPolicy> policies = new EnumMap<>(PlacementPlace.class);

    public PlacementPolicy getPolicy(PlacementPlace place) {
        return policies.getOrDefault(place, PlacementPolicy.UNKNOWN);
    }

    public void setPolicy(PlacementPlace place, PlacementPolicy policy) {
        policies.put(place, policy);
    }
}
