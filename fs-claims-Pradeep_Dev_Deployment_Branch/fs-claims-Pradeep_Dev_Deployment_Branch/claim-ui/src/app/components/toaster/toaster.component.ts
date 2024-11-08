import { Component } from '@angular/core';

interface Toast {
  type: 'success' | 'error' | 'warning';
  message: string;
}

@Component({
  selector: 'app-toaster',
  templateUrl: './toaster.component.html',
  styleUrls: ['./toaster.component.scss']
})
export class ToasterComponent {
  toasts: Toast[] = [];

  showToast(type: 'success' | 'error' | 'warning', message: string) {
    const toast: Toast = { type, message };
    this.toasts.push(toast);
    setTimeout(() => this.removeToast(toast), 3000);  // Auto-remove after 3 seconds
  }

  removeToast(toast: Toast) {
    this.toasts = this.toasts.filter(t => t !== toast);
  }
}
