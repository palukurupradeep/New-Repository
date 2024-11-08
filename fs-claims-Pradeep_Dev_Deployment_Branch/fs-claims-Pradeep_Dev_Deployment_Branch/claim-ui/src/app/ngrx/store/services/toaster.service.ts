import { Injectable } from '@angular/core';
import { ToasterComponent } from 'src/app/components/toaster/toaster.component';

@Injectable({
  providedIn: 'root'
})
export class ToasterService {
  private toasterComponent!: ToasterComponent;

  setToasterComponent(toasterComponent: ToasterComponent) {
    this.toasterComponent = toasterComponent;
  }

  showToast(type: 'success' | 'error' | 'warning', message: string) {
    if (this.toasterComponent) {
      this.toasterComponent.showToast(type, message);
    }
  }
}
