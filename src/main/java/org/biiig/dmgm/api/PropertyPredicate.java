package org.biiig.dmgm.api;

import java.util.function.BiFunction;

public interface PropertyPredicate extends BiFunction<PropertyStore, Long, Boolean> {
}
