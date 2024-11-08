import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RolesMatrixTableComponent } from './roles-matrix-table.component';

describe('RolesMatrixTableComponent', () => {
  let component: RolesMatrixTableComponent;
  let fixture: ComponentFixture<RolesMatrixTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RolesMatrixTableComponent]
    });
    fixture = TestBed.createComponent(RolesMatrixTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
