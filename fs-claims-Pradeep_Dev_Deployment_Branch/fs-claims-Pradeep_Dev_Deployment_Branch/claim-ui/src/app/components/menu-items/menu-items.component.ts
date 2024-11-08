import { Component, HostListener, Input } from '@angular/core';

@Component({
  selector: 'app-menu-items',
  templateUrl: './menu-items.component.html',
  styleUrls: ['./menu-items.component.scss']
})
export class MenuItemsComponent {
  @Input() menuItem: any;

  toggleSubMenu(menuItem: any): void {
    
    if (!menuItem.linkUrl) {
      menuItem.expanded = !menuItem.expanded;
    } else if(menuItem.linkUrl && menuItem.subMenu && menuItem.subMenu.length > 0){
      menuItem.expanded = !menuItem.expanded;
    }
  }
  @HostListener('document:click', ['$event'])
onDocumentClick(event: MouseEvent): void {
  const sidebar = document.querySelector('.sidenav');
  const clickedInside = sidebar?.contains(event.target as Node);

  if (!clickedInside) {
    this.toggleSubMenu(this.menuItem);
  }
}
}
