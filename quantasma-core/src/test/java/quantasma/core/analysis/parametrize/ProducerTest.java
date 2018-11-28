package quantasma.core.analysis.parametrize;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ProducerTest {

    @Test
    public void givenFewStringValuesWhenCallGetValueShouldAlwaysReturnFirstOne() {
        final Producer producer = Producer.instance();

        final Variable<String> stringVariable = producer._String("var").values("expectedValue", "anything");

        for (int i = 0; i < 123; i++) {
            assertThat(stringVariable.$()).isEqualTo("expectedValue");
        }
    }

    @Test
    public void givenOrderedValuesShouldProduceObjectsInTheSameOrder() {
        final Function<Producer, TestObject> recipe = (p) -> new TestObject(p._int("var1").with(3, 1, 7, 9).$());
        final Producer producer = Producer.instance();

        final Iterator<TestObject> iterator = producer.iterator(recipe);

        assertThat(iterator.next().var1).isEqualTo(3);
        assertThat(iterator.next().var1).isEqualTo(1);
        assertThat(iterator.next().var1).isEqualTo(7);
        assertThat(iterator.next().var1).isEqualTo(9);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void givenReusedVariablesShouldKeepTheSameValueForBoth() {
        final Function<Producer, TestObject> recipe = (p) -> {
            final Variable<Integer> var1 = p._int("var1").with(1, 2);
            final Variable<String> var2 = p._String("var2").with("9", "8");
            return new TestObject(var1.$(), var2.$(), var1.$());
        };
        final Producer producer = Producer.instance();

        final Iterator<TestObject> iterator = producer.iterator(recipe);

        final TestObject _1stCall = iterator.next();
        assertThat(_1stCall.var1).isEqualTo(1);
        assertThat(_1stCall.var2).isEqualTo("9");
        assertThat(_1stCall.var3).isEqualTo(1);

        final TestObject _2stCall = iterator.next();
        assertThat(_2stCall.var1).isEqualTo(2);
        assertThat(_2stCall.var2).isEqualTo("9");
        assertThat(_2stCall.var3).isEqualTo(2);

        final TestObject _3stCall = iterator.next();
        assertThat(_3stCall.var1).isEqualTo(1);
        assertThat(_3stCall.var2).isEqualTo("8");
        assertThat(_3stCall.var3).isEqualTo(1);

        final TestObject _4stCall = iterator.next();
        assertThat(_4stCall.var1).isEqualTo(2);
        assertThat(_4stCall.var2).isEqualTo("8");
        assertThat(_4stCall.var3).isEqualTo(2);

        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void givenDuplicatedValuesShouldProduceObjectsWithoutDuplicates() {
        final Function<Producer, TestObject> recipe = (p) -> new TestObject(p._int("var1").with(1, 2, 3, 2).$());
        final Producer producer = Producer.instance();

        final Iterator<TestObject> iterator = producer.iterator(recipe);

        assertThat(iterator.next().var1).isEqualTo(1);
        assertThat(iterator.next().var1).isEqualTo(2);
        assertThat(iterator.next().var1).isEqualTo(3);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void givenSecondIteratorCallShouldReturnNewIterator() {
        // given
        final Function<Producer, TestObject> recipe = (p) -> new TestObject(p._int("var1").values(1, 3).$());
        final Producer producer = Producer.instance();
        final Iterator<TestObject> iterator = producer.iterator(recipe);
        for (int i = 0; i < 2; i++) {
            assertThat(iterator.hasNext()).isTrue();
            iterator.next();
        }
        assertThat(iterator.hasNext()).isFalse();

        // when
        final Iterator<TestObject> nextIterator = producer.iterator(recipe);

        // then
        for (int i = 0; i < 2; i++) {
            assertThat(nextIterator.hasNext()).isTrue();
            nextIterator.next();
        }
        assertThat(nextIterator.hasNext()).isFalse();
    }

    @Test
    public void given1VariablesShouldProduce4CorrectObjects() {
        // given
        final Function<Producer, TestObject> recipe = (p) -> new TestObject(p._int("var1").values(1, 3, 5, 7).$());

        // when
        final Iterator<TestObject> iterator = Producer.instance().iterator(recipe);

        // then
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().var1).isEqualTo(1);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().var1).isEqualTo(3);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().var1).isEqualTo(5);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().var1).isEqualTo(7);
        assertThat(iterator.hasNext()).isFalse();

        try {
            iterator.next();
        } catch (NoSuchElementException expected) {
            return;
        }
        fail();
    }

    @Test
    public void given2VariablesShouldProduceCorrectObjects() {
        // given
        final Function<Producer, TestObject> recipe = (p) -> new TestObject(p._int("var1").values(1, 3, 5).$(),
                                                                            p._String("var2").values("a", "b", "c").$());

        // when
        final Iterator<TestObject> iterator = Producer.instance().iterator(recipe);

        // then
        assertThat(iterator.hasNext()).isTrue();
        final TestObject _1thCall = iterator.next();
        assertThat(_1thCall.var1).isEqualTo(1);
        assertThat(_1thCall.var2).isEqualTo("a");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _2thCall = iterator.next();
        assertThat(_2thCall.var1).isEqualTo(3);
        assertThat(_2thCall.var2).isEqualTo("a");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _3thCall = iterator.next();
        assertThat(_3thCall.var1).isEqualTo(5);
        assertThat(_3thCall.var2).isEqualTo("a");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _4thCall = iterator.next();
        assertThat(_4thCall.var1).isEqualTo(1);
        assertThat(_4thCall.var2).isEqualTo("b");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _5thCall = iterator.next();
        assertThat(_5thCall.var1).isEqualTo(3);
        assertThat(_5thCall.var2).isEqualTo("b");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _6thCall = iterator.next();
        assertThat(_6thCall.var1).isEqualTo(5);
        assertThat(_6thCall.var2).isEqualTo("b");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _7thCall = iterator.next();
        assertThat(_7thCall.var1).isEqualTo(1);
        assertThat(_7thCall.var2).isEqualTo("c");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _8thCall = iterator.next();
        assertThat(_8thCall.var1).isEqualTo(3);
        assertThat(_8thCall.var2).isEqualTo("c");

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _9thCall = iterator.next();
        assertThat(_9thCall.var1).isEqualTo(5);
        assertThat(_9thCall.var2).isEqualTo("c");

        assertThat(iterator.hasNext()).isFalse();

        try {
            iterator.next();
        } catch (NoSuchElementException expected) {
            return;
        }
        fail();
    }

    @Test
    public void given2VariablesShouldProduce12CorrectObjects() {
        // given
        final Function<Producer, TestObject> recipe = (p) -> new TestObject(p._int("var1").values(1, 3).$(),
                                                                            p._String("var2").values("a", "b", "c").$(),
                                                                            p._int("var3").values(7, 9).$());

        // when
        final Iterator<TestObject> iterator = Producer.instance().iterator(recipe);

        // then
        assertThat(iterator.hasNext()).isTrue();
        final TestObject _1thCall = iterator.next();
        assertThat(_1thCall.var1).isEqualTo(1);
        assertThat(_1thCall.var2).isEqualTo("a");
        assertThat(_1thCall.var3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _2thCall = iterator.next();
        assertThat(_2thCall.var1).isEqualTo(3);
        assertThat(_2thCall.var2).isEqualTo("a");
        assertThat(_2thCall.var3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _3thCall = iterator.next();
        assertThat(_3thCall.var1).isEqualTo(1);
        assertThat(_3thCall.var2).isEqualTo("b");
        assertThat(_3thCall.var3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _4thCall = iterator.next();
        assertThat(_4thCall.var1).isEqualTo(3);
        assertThat(_4thCall.var2).isEqualTo("b");
        assertThat(_4thCall.var3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _5thCall = iterator.next();
        assertThat(_5thCall.var1).isEqualTo(1);
        assertThat(_5thCall.var2).isEqualTo("c");
        assertThat(_5thCall.var3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _6thCall = iterator.next();
        assertThat(_6thCall.var1).isEqualTo(3);
        assertThat(_6thCall.var2).isEqualTo("c");
        assertThat(_6thCall.var3).isEqualTo(7);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _7thCall = iterator.next();
        assertThat(_7thCall.var1).isEqualTo(1);
        assertThat(_7thCall.var2).isEqualTo("a");
        assertThat(_7thCall.var3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _8thCall = iterator.next();
        assertThat(_8thCall.var1).isEqualTo(3);
        assertThat(_8thCall.var2).isEqualTo("a");
        assertThat(_8thCall.var3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _9thCall = iterator.next();
        assertThat(_9thCall.var1).isEqualTo(1);
        assertThat(_9thCall.var2).isEqualTo("b");
        assertThat(_9thCall.var3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _10thCall = iterator.next();
        assertThat(_10thCall.var1).isEqualTo(3);
        assertThat(_10thCall.var2).isEqualTo("b");
        assertThat(_10thCall.var3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _11thCall = iterator.next();
        assertThat(_11thCall.var1).isEqualTo(1);
        assertThat(_11thCall.var2).isEqualTo("c");
        assertThat(_11thCall.var3).isEqualTo(9);

        assertThat(iterator.hasNext()).isTrue();
        final TestObject _12thCall = iterator.next();
        assertThat(_12thCall.var1).isEqualTo(3);
        assertThat(_12thCall.var2).isEqualTo("c");
        assertThat(_12thCall.var3).isEqualTo(9);

        assertThat(iterator.hasNext()).isFalse();

        try {
            iterator.next();
        } catch (NoSuchElementException expected) {
            return;
        }
        fail();
    }

    static class TestObject {
        private final int var1;
        private final String var2;
        private final int var3;

        TestObject(int var1) {
            this.var1 = var1;
            this.var2 = null;
            this.var3 = 0;
        }

        TestObject(int var1, String var2) {
            this.var1 = var1;
            this.var2 = var2;
            this.var3 = 0;
        }

        TestObject(int var1, String var2, int var3) {
            this.var1 = var1;
            this.var2 = var2;
            this.var3 = var3;
        }
    }

}