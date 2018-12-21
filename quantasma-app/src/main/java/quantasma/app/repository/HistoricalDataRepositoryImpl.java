package quantasma.app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import quantasma.app.config.service.historical.HistoricalDataServiceProperties;
import quantasma.app.model.PersistentOhlcvTick;
import quantasma.app.model.HistoricalDataSummary;

import java.time.Instant;
import java.util.List;

@Repository
public class HistoricalDataRepositoryImpl implements HistoricalDataRepository {

    private final HistoricalDataServiceProperties properties;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public HistoricalDataRepositoryImpl(HistoricalDataServiceProperties properties, MongoTemplate mongoTemplate) {
        this.properties = properties;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<PersistentOhlcvTick> findBySymbolAndDateBetweenOrderByDate(String symbol, Instant timeGTE, Instant timeLS) {
        return mongoTemplate.find(Query.query(Criteria.where("symbol").is(symbol)
                                                      .and("date").gte(timeGTE).lt(timeLS)),
                                  PersistentOhlcvTick.class,
                                  properties.collectionName());
    }

    @Override
    public PersistentOhlcvTick insert(PersistentOhlcvTick candlestick) {
        mongoTemplate.insert(candlestick, properties.collectionName());
        return null;
    }

    @Override
    public long countBySymbol(String symbol) {
        return mongoTemplate.count(Query.query(Criteria.where("symbol").is(symbol)),
                                   properties.collectionName());
    }

    @Override
    public List<HistoricalDataSummary> symbolsTickSummary() {
        final Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("symbol", "period")
                           .first("symbol").as("symbol")
                           .first("period").as("period")
                           .min("date").as("fromDate")
                           .max("date").as("toDate")
                           .count().as("barCount"));
        return mongoTemplate.aggregate(aggregation,
                                       properties.collectionName(),
                                       HistoricalDataSummary.class)
                            .getMappedResults();
    }

}