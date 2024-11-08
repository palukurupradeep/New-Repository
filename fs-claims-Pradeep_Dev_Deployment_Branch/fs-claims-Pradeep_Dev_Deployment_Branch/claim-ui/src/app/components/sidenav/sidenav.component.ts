import { Component, Output, EventEmitter, OnInit, HostListener, Input } from '@angular/core';
import { navbarData } from './nav-data';
import { navbarSettingsData } from './nav-settings';
import { style, transition, trigger, animate, keyframes } from '@angular/animations';
import { SmartCodeService } from 'src/app/ngrx/store/services/smart-code.service';
import { AuthService } from 'src/app/ngrx/store/services/auth.service';

 
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
 
@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss'],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('40ms',
          style({ opacity: 1 }))
      ]),
      transition(':leave', [
        style({ opacity: 1 }),
        animate('30ms',
          style({ opacity: 0 }))
      ])
    ]),
  
  ]
})
 
export class SidenavComponent implements OnInit {
 
  @Output() onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter();
  collapsed = false;
  screenWidth = 0;
  navData = navbarData;
  navSettingsData = navbarSettingsData;
  showAdminMenu: boolean = false;
  showPermissionsMenu: boolean = false;
  @HostListener('window:resize', ['$event'])
  @Input() childNav!: boolean;
  onResize(event: any) {
    this.screenWidth = window.innerWidth;
    if (this.screenWidth <= 768) {
      this.collapsed = false;
      this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
    }
  }

  public resourceMenu: any[] = [];
  public menuNames:boolean = false;
  public isUserManagementMenuOpen = false;
  public isLookupMenuOpen = false;
  public openMenus: { [key: string]: boolean } = {
    userManagement: false,
    lookupMaintenance: false,
    // Add more menu states if needed
  };

  constructor(
    private smartCodeService:SmartCodeService,
    public authService: AuthService
  ){ }
  
  ngOnInit(): void {
    this.screenWidth = window.innerWidth;
    if (this.childNav !== undefined && this.childNav !== null) {
      this.showAdminMenu = this.childNav;
    }
    this.fetchresourceMenus();
  }

  public fetchresourceMenus(){
    this.smartCodeService.fetchResourceMenus('resourceMenu').subscribe(
      (data: any) => {
        this.resourceMenu = data.body;
        this.resourceMenu.forEach((item:any) => {
          item.expanded = false;
        
        });
      },
      (error: any) => {
        console.error('Error fetching resource menus:', error);
      }
    );
  }

 
  public toggleCollapse(): void {
    this.collapsed = !this.collapsed
    this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
 
  }
 
  public closeSidenav(): void {
    this.collapsed = false;
    this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
  }
  public toggleAdminMenu() {
    this.showAdminMenu = !this.showAdminMenu;
}


public togglePermissionsMenu() {
    this.showPermissionsMenu = !this.showPermissionsMenu;
}

public toggleSubMenu(){
//  this.menuNames = !this.menuNames
}

//  public toggleMenu(menu: string): void {
//   this.openMenus[menu] = !this.openMenus[menu];
// }

@HostListener('document:click', ['$event'])
onDocumentClick(event: MouseEvent): void {
  const sidebar = document.querySelector('.sidenav');
  const clickedInside = sidebar?.contains(event.target as Node);

  if (!clickedInside) {
    // Close all submenus if the click is outside the sidebar
    this.closeAllMenus();
    this.toggleSubMenu();
  }
}

toggleMenu(menu: string): void {
  // Close all other menus
  for (const key in this.openMenus) {
    if (key !== menu) {
      this.openMenus[key] = false;
    }
  }
  // Toggle the selected menu
  this.openMenus[menu] = !this.openMenus[menu];
}

closeAllMenus(): void {
  for (const key in this.openMenus) {
    this.openMenus[key] = false;
  }
}
}