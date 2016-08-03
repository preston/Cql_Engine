package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.Iterator;

/*
* The Max operator returns the maximum element in the source.
* If the source contains no non-null elements, null is returned.
* If the source is null, the result is null.
* Possible return types include: Integer, BigDecimal, Quantity, DateTime, Time, String
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class MaxEvaluator extends Max {

  public static Object max(Object source) {
    if (source instanceof Iterable) {
      Iterable<Object> element = (Iterable<Object>)source;
      Iterator<Object> itr = element.iterator();

      if (!itr.hasNext()) { return null; } // empty list
      Object max = itr.next();
      while (max == null && itr.hasNext()) { max = itr.next(); }
      while (itr.hasNext()) {
        Object value = itr.next();

        if (value == null) { continue; } // skip null

        if ((Boolean)GreaterEvaluator.greater(value, max)) { max = value; }
      }
      return max;
    }
    else { return null; }
  }

  @Override
  public Object evaluate(Context context) {

    Object source = getSource().evaluate(context);
    if (source == null) { return null; }


    return max(source);
  }
}
