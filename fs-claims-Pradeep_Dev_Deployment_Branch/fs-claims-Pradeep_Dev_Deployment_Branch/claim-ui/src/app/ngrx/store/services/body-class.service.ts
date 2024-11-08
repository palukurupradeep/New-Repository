import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BodyClassService {

  constructor() { }
  userService:any
  adminService:any
  private selectedGroupNameSubject = new BehaviorSubject<number>(1);
  private selectedManagerNameSubject = new BehaviorSubject<number | null>(null);

  selectedGroupName$ = this.selectedGroupNameSubject.asObservable();
  selectedManagerName$ = this.selectedManagerNameSubject.asObservable();

  setSelectedGroupAndManager(groupName: number, managerName: number | null) {
    this.selectedGroupNameSubject.next(groupName);
    this.selectedManagerNameSubject.next(managerName);
  }

  getBodyClass(isSideNavCollapsed: boolean, screenWidth: number): string {
    let styleClass = '';

    if (isSideNavCollapsed && screenWidth > 768) {
      styleClass = 'body-trimmed';
    } else if (isSideNavCollapsed && screenWidth <= 768 && screenWidth > 0) {
      styleClass = 'body-md-screen';
    }

    return styleClass;
  }

}
