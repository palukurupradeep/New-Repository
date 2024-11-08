import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchClaimHistoryComponent } from './search-claim-history.component';

describe('SearchClaimHistoryComponent', () => {
  let component: SearchClaimHistoryComponent;
  let fixture: ComponentFixture<SearchClaimHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchClaimHistoryComponent]
    });
    fixture = TestBed.createComponent(SearchClaimHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
