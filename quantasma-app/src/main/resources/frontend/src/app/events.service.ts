import { Injectable } from '@angular/core';
import { AccountState } from "../shared/account-state.model";
import { Quote } from "../shared/quote.model";
import { Observable, Subject } from "rxjs/index";

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  private sources: EventSource[] = [];

  private quotesSubject: Subject<Quote> = this.initializeSubject('events/quotes', 'quote-event');
  private accountStateSubject: Subject<AccountState> = this.initializeSubject('events/accountState', 'account_state-event');

  constructor() { }

  private initializeSubject<E>(url: string, event: string): Subject<E> {
    const subject: Subject<E> = new Subject();
    this.sources.push(EventsService.subjectLinkedEventSource(url, event, subject));
    return subject;
  }

  public quotes(): Observable<Quote> {
    return this.quotesSubject.asObservable();
  }

  public accountState(): Observable<AccountState> {
    return this.accountStateSubject.asObservable();
  }

  private static subjectLinkedEventSource<E>(url: string, event: string, subject: Subject<E>): EventSource {
    const eventSource: EventSource = new EventSource(url);
    eventSource.addEventListener(event, EventsService.onSseMessage(subject));
    eventSource.onerror = EventsService.onSseError();
    return eventSource;
  }

  private static onSseMessage<E>(subject: Subject<E>) {
    return ((event: MessageEvent) => {
      subject.next(JSON.parse(event.data));
    }) as (event: Event) => void;
  }

  private static onSseError() {
    return (evt: any) => console.log("SSE Event failure: ", evt);
  }
}