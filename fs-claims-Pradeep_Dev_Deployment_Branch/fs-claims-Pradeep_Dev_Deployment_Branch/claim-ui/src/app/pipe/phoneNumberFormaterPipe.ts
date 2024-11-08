import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'phoneNumberFormatter'
})
export class PhoneNumberFormatterPipe implements PipeTransform {
  transform(value: string): string {
    // Check if the input is a valid number and has exactly 10 digits
    if (!value || value.length !== 10 || isNaN(+value)) {
      return value; // Return the input as it is if it's not a valid phone number
    }

    // Format the number into the desired format: +1 (XXX) XXX-XXXX
    const countryCode = '+1';
    const areaCode = value.substring(0, 3);
    const centralOfficeCode = value.substring(3, 6);
    const lineNumber = value.substring(6);

    return `${countryCode} (${areaCode}) ${centralOfficeCode}-${lineNumber}`;
  }
}
