package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/*
*** NOTES FOR INTERVAL ***
!=(left Interval<T>, right Interval<T>) Boolean

The not equal (!=) operator for intervals returns true if its arguments are not the same value.
The not equal operator is a shorthand for invocation of logical negation (not) of the equal operator.

*** NOTES FOR LIST ***
!=(left List<T>, right List<T>) Boolean

The not equal (!=) operator for lists returns true if its arguments are not the same value.
The not equal operator is a shorthand for invocation of logical negation (not) of the equal operator.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class NotEqualEvaluator extends NotEqual {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return !org.cqframework.cql.runtime.Value.equals(left, right);
    }
}
