import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusViewComponent } from './status-dashboard.component';

describe('StatusDashboardComponent', () => {
  let component: StatusViewComponent;
  let fixture: ComponentFixture<StatusViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatusViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});