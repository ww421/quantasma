package quantasma.core;

import lombok.Getter;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.TimeSeriesDefinitionImpl;

@Getter
public class BaseContext implements Context {
    private final DataService dataService;
    private final OrderService orderService;
    private final StrategyControl strategyControl;

    public BaseContext(DataService dataService, OrderService orderService, StrategyControl strategyControl) {
        this.dataService = dataService;
        this.orderService = orderService;
        this.strategyControl = strategyControl;
    }

    public static class Builder {
        private DataService dataService;
        private OrderService orderService;
        private StrategyControl strategyControl;

        public Builder() {
            orderService = new NullOrderService();
            strategyControl = new BaseStrategyControl();
            dataService = new BaseDataService(new MarketData(MultipleTimeSeriesBuilder
                                                                     .basedOn(new TimeSeriesDefinitionImpl(BarPeriod.M1))
                                                                     .symbols("EURUSD")
                                                                     .build()));
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withOrderService(OrderService orderService) {
            this.orderService = orderService;
            return this;
        }

        public Builder withDataService(DataService dataService) {
            this.dataService = dataService;
            return this;
        }

        public Builder withStrategyControl(StrategyControl strategyControl) {
            this.strategyControl = strategyControl;
            return this;
        }

        public Builder withTimeSeries(MultipleTimeSeriesBuilder multipleTimeSeriesBuilder) {
            this.dataService = new BaseDataService(new MarketData(multipleTimeSeriesBuilder.build()));
            return this;
        }

        public Builder withMarketData(MarketData marketData) {
            this.dataService = new BaseDataService(marketData);
            return this;
        }

        public Context build() {
            return new BaseContext(dataService, orderService, strategyControl);
        }
    }
}