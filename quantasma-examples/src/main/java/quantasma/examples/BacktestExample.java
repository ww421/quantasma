package quantasma.examples;

import org.ta4j.core.Order;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.core.TestOrderService;
import quantasma.core.MutableNum;
import quantasma.core.TestMarketData;
import quantasma.core.TestManager;
import quantasma.core.timeseries.GroupTimeSeriesDefinition;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.TimeSeriesDefinitionImpl;

public class BacktestExample {
    public static void main(String[] args) {
        // tag::BacktestExample[]
        final TestMarketData testMarketData = new TestMarketData(
                MultipleTimeSeriesBuilder.basedOn(new TimeSeriesDefinitionImpl(BarPeriod.M1))
                                         .symbols("EURUSD")
                                         .aggregate(GroupTimeSeriesDefinition.of("EURUSD")
                                                                             .add(new TimeSeriesDefinitionImpl(BarPeriod.M5)))
                                         .build());

        final MutableNum orderAmountRef = MutableNum.valueOf(0);

        final Context context = new BaseContext.Builder()
                .withMarketData(testMarketData)
                .withOrderService(new TestOrderService(orderAmountRef))
                .build();

        final Strategy rsiStrategy = RSIStrategy.buildBullish(context);

        // Feed historical data by calling testMarketData.add()

        final TestManager manager = new TestManager(testMarketData, "EURUSD", orderAmountRef);
        final TradingRecord result = manager.run(rsiStrategy, Order.OrderType.BUY);
        // Proper criterion can be used now on the result
        // end::BacktestExample[]
    }
}
