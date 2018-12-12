package quantasma.examples;

import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.BaseTradeEngine;
import quantasma.core.Context;
import quantasma.core.NullOrderService;
import quantasma.core.TradeEngine;
import quantasma.core.TradeStrategy;
import quantasma.core.analysis.parametrize.Parameters;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.examples.RSIStrategy.ParameterList;

import java.time.ZonedDateTime;

public class TradeAppExample {
    public static void main(String[] args) {
        // tag::tradeAppExample[]
        // Any strategy based on TradeStrategy interface needs a Context object
        final Context context = new BaseContext.Builder()
                .withTimeSeries(
                        MultipleTimeSeriesBuilder.basedOn(
                                // Smallest accessible time window for all defined below symbols
                                TimeSeriesDefinition.limited(BarPeriod.M1, 100))
                                                 .symbols("EURUSD", "EURGBP")
                                                 // You can define any number of additional time windows for above symbols
                                                 .aggregate(TimeSeriesDefinition.Group.of("EURUSD")
                                                                                      .add(TimeSeriesDefinition.limited(BarPeriod.M5, 100))
                                                                                      .add(TimeSeriesDefinition.limited(BarPeriod.M30, 100)))
                )
                // OrderService implementations integrate an app with external APIs
                .withOrderService(new NullOrderService())
                .build();

        final TradeStrategy rsiStrategy = RSIStrategy.buildBullish(context,
                                                                   Parameters.from(ParameterList.class)
                                                                             .add(ParameterList.TRADE_SYMBOL, "EURUSD")
                                                                             .add(ParameterList.RSI_PERIOD, 14)
                                                                             .add(ParameterList.RSI_LOWER_BOUND, 30)
                                                                             .add(ParameterList.RSI_UPPER_BOUND, 70));

        // Only registered strategies are given market data
        context.getStrategyControl().register(rsiStrategy);

        final TradeEngine tradeEngine = BaseTradeEngine.create(context);

        // Example call on market data change
        tradeEngine.process("EURUSD", ZonedDateTime.now(), 1.13757, 1.13767);

        // Will fail silently because the symbol wasn't registered within time series definitions
        tradeEngine.process("EURJPY", ZonedDateTime.now(), 129.653, 129.663);
        // end::tradeAppExample[]
    }

}
