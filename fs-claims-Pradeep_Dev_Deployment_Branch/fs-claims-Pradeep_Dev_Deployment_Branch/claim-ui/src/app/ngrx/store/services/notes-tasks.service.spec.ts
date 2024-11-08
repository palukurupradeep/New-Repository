import { TestBed } from '@angular/core/testing';

import { NotesTasksService } from './notes-tasks.service';

describe('NotesTasksService', () => {
  let service: NotesTasksService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotesTasksService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
