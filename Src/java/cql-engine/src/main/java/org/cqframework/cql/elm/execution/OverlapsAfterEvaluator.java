package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/*
overlaps after(left Interval<T>, right Interval<T>) Boolean

The overlaps after operator returns true if the first interval overlaps the second and ends after it.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class OverlapsAfterEvaluator extends OverlapsAfter {

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left != null && right != null) {
      Object leftStart = left.getStart();
      Object leftEnd = left.getEnd();
      Object rightStart = right.getStart();
      Object rightEnd = right.getEnd();

      return (Value.compareTo(leftEnd, rightEnd, ">") && OverlapsEvaluator.overlaps(left, right));
    }
    return null;
  }
}
