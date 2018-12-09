package quantasma.app.service;

import quantasma.integrations.event.AccountStateEvent;
import quantasma.integrations.event.QuoteEvent;
import reactor.core.publisher.Flux;

public interface EventsService {
    Flux<QuoteEvent> quotes();

    void publish(QuoteEvent quoteEvent);

    Flux<AccountStateEvent> accountState();

    void publish(AccountStateEvent accountStateEvent);
}