package org.cqframework.cql.execution;

import org.eclipse.persistence.jpa.jpql.Assert;
import org.testng.annotations.Test;

import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Time;
import org.joda.time.Partial;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CqlListOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.cqframework.cql.elm.execution.Contains#evaluate(Context)}
     */
    @Test
    public void testContains() throws JAXBException {
        Context context = new Context(library);
        Object result;

        // result = context.resolveExpressionRef(library, "ContainsABNullHasNull").getExpression().evaluate(context);
        // assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ContainsABCHasA").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ContainsJan2012True").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ContainsJan2012False").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ContainsTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ContainsTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Distinct#evaluate(Context)}
     */
    @Test
    public void testDistinct() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "DistinctEmptyList").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        // result = context.resolveExpressionRef(library, "DistinctNullNullNull").getExpression().evaluate(context);
        // assertThat(result, is(new ArrayList<Object>() {{
        //     add(null);
        // }}));
        //
        // result = context.resolveExpressionRef(library, "DistinctANullANull").getExpression().evaluate(context);
        // assertThat(result, is(Arrays.asList("a", null)));

        result = context.resolveExpressionRef(library, "Distinct112233").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3)));

        result = context.resolveExpressionRef(library, "Distinct123123").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3)));

        result = context.resolveExpressionRef(library, "DistinctAABBCC").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList("a", "b", "c")));

        result = context.resolveExpressionRef(library, "DistinctABCABC").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList("a", "b", "c")));

        result = context.resolveExpressionRef(library, "DistinctDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 10, 5})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 1, 1})));
        assertThat(((ArrayList<Object>)result).size(), is(2));

        result = context.resolveExpressionRef(library, "DistinctTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(2));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Equal#evaluate(Context)}
     */
    @Test
    public void testEqual() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "EqualNullNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EqualEmptyListNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EqualNullEmptyList").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EqualEmptyListAndEmptyList").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "Equal12And123").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "Equal123And12").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "Equal123And123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EqualDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EqualDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EqualDateTimeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EqualTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EqualTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Except#evaluate(Context)}
     */
    @Test
    public void testExcept() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "ExceptEmptyListAndEmptyList").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef(library, "Except1234And23").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 4)));

        result = context.resolveExpressionRef(library, "Except23And1234").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef(library, "ExceptDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));
        assertThat(((ArrayList<Object>)result).size(), is(1));

        result = context.resolveExpressionRef(library, "ExceptTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(1));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Exists#evaluate(Context)}
     */
    @Test
    public void testExists() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "ExistsEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ExistsListNull").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "Exists1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "Exists12").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ExistsDateTime").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ExistsTime").getExpression().evaluate(context);
        assertThat(result, is(true));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Flatten#evaluate(Context)}
     */
    @Test
    public void testFlatten() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "FlattenEmpty").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef(library, "FlattenListNullAndNull").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(null, null)));

        result = context.resolveExpressionRef(library, "FlattenList12And34").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3, 4)));

        result = context.resolveExpressionRef(library, "FlattenDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 12, 10})));
        assertThat(((ArrayList<Object>)result).size(), is(2));

        result = context.resolveExpressionRef(library, "FlattenTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(2));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.First#evaluate(Context)}
     */
    @Test
    public void testFirst() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "FirstEmpty").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "FirstNull1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "First1Null").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "First12").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "FirstDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));

        result = context.resolveExpressionRef(library, "FirstTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.In#evaluate(Context)}
     */
    @Test
    public void testIn() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "InNullEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "InNullAnd1Null").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "In1Null").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "In1And12").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "In3And12").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "InDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "InDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "InTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "InTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Includes#evaluate(Context)}
     */
    @Test
    public void testIncludes() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "IncludesEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IncludesListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "Includes123AndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "Includes123And2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "Includes123And4").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "IncludesDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IncludesDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "IncludesTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IncludesTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.IncludedIn#evaluate(Context)}
     */
    @Test
    public void testIncludedIn() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "IncludedInEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IncludedInListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IncludedInEmptyAnd123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IncludedIn2And123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IncludedIn4And123").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "IncludedInDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IncludedInDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "IncludedInTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "IncludedInTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Indexer#evaluate(Context)}
     */
    @Test
    public void testIndexer() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "IndexerNull1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Indexer0Of12").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "Indexer1Of12").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "Indexer2Of12").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IndexerNeg1Of12").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IndexerDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));

        result = context.resolveExpressionRef(library, "IndexerTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
    }


    /**
     * {@link org.cqframework.cql.elm.execution.IndexOf#evaluate(Context)}
     */
    @Test
    public void testIndexOf() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "IndexOfEmptyNull").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef(library, "IndexOfNullEmpty").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "IndexOfNullIn1Null").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "IndexOf1In12").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "IndexOf2In12").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "IndexOf3In12").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef(library, "IndexOfDateTime").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "IndexOfTime").getExpression().evaluate(context);
        assertThat(result, is(1));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Intersect#evaluate(Context)}
     */
    @Test
    public void testIntersect() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "IntersectEmptyListAndEmptyList").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef(library, "Intersect1234And23").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(2, 3)));

        result = context.resolveExpressionRef(library, "Intersect23And1234").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(2, 3)));

        result = context.resolveExpressionRef(library, "IntersectDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 12, 10})));
        assertThat(((ArrayList<Object>)result).size(), is(2));

        result = context.resolveExpressionRef(library, "IntersectTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(2));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Last#evaluate(Context)}
     */
    @Test
    public void testLast() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "LastEmpty").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "LastNull1").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef(library, "Last1Null").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Last12").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "LastDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 12, 10})));

        result = context.resolveExpressionRef(library, "LastTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Length#evaluate(Context)}
     */
    @Test
    public void testLength() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "LengthEmpty").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef(library, "LengthNull1").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "Length1Null").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "Length12").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef(library, "LengthDateTime").getExpression().evaluate(context);
        assertThat(result, is(3));

        result = context.resolveExpressionRef(library, "LengthTime").getExpression().evaluate(context);
        assertThat(result, is(6));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Equivalent#evaluate(Context)}
     */
    @Test
    public void testEquivalent() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "EquivalentEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivalentABCAndABC").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivalentABCAndAB").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivalentABCAnd123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Equivalent123AndABC").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "Equivalent123AndString123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivalentDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivalentDateTimeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivalentDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "EquivalentTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "EquivalentTimeNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "EquivalentTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.NotEqual#evaluate(Context)}
     */
    @Test
    public void testNotEqual() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "NotEqualEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "NotEqualABCAndABC").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "NotEqualABCAndAB").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "NotEqualABCAnd123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "NotEqual123AndABC").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "NotEqual123AndString123").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "NotEqualDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "NotEqualDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "NotEqualTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "NotEqualTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ProperIncludes#evaluate(Context)}
     */
    @Test
    public void testProperlyIncludes() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "ProperIncludesEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ProperIncludesListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ProperIncludes123AndEmpty").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ProperIncludes123And2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ProperIncludes123And4").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ProperIncludesDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ProperIncludesDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ProperIncludesTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ProperIncludesTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.ProperIncludedIn#evaluate(Context)}
     */
    @Test
    public void testProperlyIncludedIn() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "ProperIncludedInEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ProperIncludedInListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ProperIncludedInEmptyAnd123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ProperIncludedIn2And123").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ProperIncludedIn4And123").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef(library, "ProperIncludedInDateTimeTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef(library, "ProperIncludedInDateTimeFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.SingletonFrom#evaluate(Context)}
     */
    @Test
    public void testSingletonFrom() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "SingletonFromEmpty").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "SingletonFromListNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef(library, "SingletonFrom1").getExpression().evaluate(context);
        assertThat(result, is(1));

        try {
            result = context.resolveExpressionRef(library, "SingletonFrom12").getExpression().evaluate(context);
            Assert.fail("List with more than one element should throw an exception");
        } catch (IllegalArgumentException ex) {
            assertThat(ex, isA(IllegalArgumentException.class));
        }

        result = context.resolveExpressionRef(library, "SingletonFromDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));

        result = context.resolveExpressionRef(library, "SingletonFromTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
    }

    /**
     * {@link org.cqframework.cql.elm.execution.Union#evaluate(Context)}
     */
    @Test
    public void testUnion() throws JAXBException {
        Context context = new Context(library);
        Object result;

        result = context.resolveExpressionRef(library, "UnionEmptyAndEmpty").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList()));

        result = context.resolveExpressionRef(library, "UnionListNullAndListNull").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(null, null)));

        result = context.resolveExpressionRef(library, "Union123AndEmpty").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3)));

        result = context.resolveExpressionRef(library, "Union123And2").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3, 2)));

        result = context.resolveExpressionRef(library, "Union123And4").getExpression().evaluate(context);
        assertThat(result, is(Arrays.asList(1, 2, 3, 4)));

        result = context.resolveExpressionRef(library, "UnionDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2001, 9, 11})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 5, 10})));
        assertThat(((DateTime)((ArrayList<Object>)result).get(2)).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2014, 12, 10})));
        assertThat(((ArrayList<Object>)result).size(), is(3));

        result = context.resolveExpressionRef(library, "UnionTime").getExpression().evaluate(context);
        assertThat(((Time)((ArrayList<Object>)result).get(0)).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(1)).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(2)).getPartial(), is(new Partial(Time.getFields(4), new int[] {12, 59, 59, 999})));
        assertThat(((Time)((ArrayList<Object>)result).get(3)).getPartial(), is(new Partial(Time.getFields(4), new int[] {10, 59, 59, 999})));
        assertThat(((ArrayList<Object>)result).size(), is(4));
    }
}
