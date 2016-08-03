package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Time;

/*
The component-from operator returns the specified component of the argument.
For DateTime values, precision must be one of: year, month, day, hour, minute, second, or millisecond.
For Time values, precision must be one of: hour, minute, second, or millisecond.
If the argument is null, or is not specified to the level of precision being extracted, the result is null.
*/

/**
* Created by Chris Schuler on 6/22/2016
*/
public class DateTimeComponentFromEvaluator extends DateTimeComponentFrom {

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);
    String precision = getPrecision().value();

    if (precision == null) {
      throw new IllegalArgumentException("Precision must be specified.");
    }

    if (operand instanceof DateTime) {
      DateTime dateTime = (DateTime)operand;

      int idx = DateTime.getFieldIndex(precision);

      if (idx != -1) {
        // check level of precision
        if (idx + 1 > dateTime.getPartial().size()) {
          return null;
        }

        return dateTime.getPartial().getValue(idx);
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
      }
    }

    else if (operand instanceof Time) {
      Time time = (Time)operand;

      int idx = Time.getFieldIndex(precision);

      if (idx != -1) {
        // check level of precision
        if (idx + 1 > time.getPartial().size()) {
          return null;
        }
        return time.getPartial().getValue(idx);
      }
      else {
        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
      }
    }

    throw new IllegalArgumentException(String.format("Cannot DateTimeComponentFrom arguments of type '%s'.", operand.getClass().getName()));
  }
}
