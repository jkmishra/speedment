package com.speedment.runtime.internal.field.comparator;

import com.speedment.runtime.field.trait.HasByteValue;
import java.util.Comparator;

/**
 * A predicate that evaluates if a value is between two bytes.
 * 
 * @param <ENTITY> entity type
 * @param <D>      database type
 * 
 * @author Emil Forslund
 * @since  3.0.0
 */
public interface ByteFieldComparator<ENTITY, D>  extends Comparator<ENTITY> {
    
    /**
     * Gets the field that is being compared.
     * 
     * @return the compared field
     */
    HasByteValue<ENTITY, D> getField();
    
    /**
     * Returns if this {@code Comparator} is reversed.
     * 
     * @return if this is reversed
     */
    boolean isReversed();
}