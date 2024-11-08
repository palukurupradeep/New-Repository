import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './mockApis/login.service';
import { ToasterComponent } from './components/toaster/toaster.component';
import { ToasterService } from './ngrx/store/services/toaster.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
 export class AppComponent implements OnInit {
  @ViewChild(ToasterComponent) toasterComponent!: ToasterComponent;


  ngAfterViewInit() {
    this.toasterService.setToasterComponent(this.toasterComponent);
  }
  title = 'first-app';
  isSideNavCollapsed = false;
  screenWidth = 0;
  isLoggedIn: boolean = false;

  constructor(private loginService: LoginService, private router: Router, private toasterService: ToasterService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.loginService.getIsLoggedIn();

    // if (!this.isLoggedIn) {
    //   this.router.navigate(['/login']);
    // }
  }

onToggleSideNav(data: SideNavToggle): void {
  this.screenWidth = data.screenWidth;
  this.isSideNavCollapsed = data.collapsed;
}
}


