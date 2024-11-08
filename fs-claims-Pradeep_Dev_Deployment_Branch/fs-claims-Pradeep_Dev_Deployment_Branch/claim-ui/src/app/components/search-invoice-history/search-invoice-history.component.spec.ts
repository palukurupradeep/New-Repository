import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchInvoiceHistoryComponent } from './search-invoice-history.component';

describe('SearchInvoiceHistoryComponent', () => {
  let component: SearchInvoiceHistoryComponent;
  let fixture: ComponentFixture<SearchInvoiceHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchInvoiceHistoryComponent]
    });
    fixture = TestBed.createComponent(SearchInvoiceHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
