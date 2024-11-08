import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PickupScheduleService {

  constructor() { }

  private claims = [
    {
      claimNumber: '12345',
      rgaNumber: 'RGA001',
      customerNumber: '0234533',
      customerName: 'John Doe',
      customerEmail: 'john@test.com',
      customerPhone: '+1-123-456-7890',
      claimDate: '09/23/2023',
      rgaDate: '04/18/2024',
      pickupDate: '10/10/2024',
      rescheduleDate: '10/20/2024',
      routeSchedule: 'Route 101',
      rgaQuantity: 10,
      specialEquipment: true
    },
    {
      claimNumber: '67890',
      rgaNumber: 'RGA002',
      customerNumber: '0567890',
      customerName: 'Jane Smith',
      customerEmail: 'jane@test.com',
      customerPhone: '+1-234-567-8901',
      claimDate: '08/15/2023',
      rgaDate: '04/10/2024',
      pickupDate: '09/10/2024',
      rescheduleDate: '09/20/2024',
      routeSchedule: 'Route 102',
      rgaQuantity: 5,
      specialEquipment: false
    },
    {
      claimNumber: '11223',
      rgaNumber: 'RGA003',
      customerNumber: '0987654',
      customerName: 'Alice Johnson',
      customerEmail: 'alice@test.com',
      customerPhone: '+1-345-678-9012',
      claimDate: '07/20/2023',
      rgaDate: '03/15/2024',
      pickupDate: '08/15/2024',
      rescheduleDate: '08/25/2024',
      routeSchedule: 'Route 103',
      rgaQuantity: 8,
      specialEquipment: true
    }
    
  ];

  getClaimByNumber(claimNumber: string): Observable<any> {
    const claim = this.claims.find(claim => claim.claimNumber === claimNumber);
    return of(claim || null);
  }

  
  getRgaData(rgaNumber: string): Observable<any> {
    const rgaData = this.claims.find(data => data.rgaNumber === rgaNumber);
    return of(rgaData || null); 
  }
}
