import { Directive, HostListener } from '@angular/core';

@Directive({
  selector: '[appNoSpecialCharacters]'
})
export class NoSpecialCharactersDirective {

  constructor() { }

  @HostListener('input', ['$event']) onInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = input.value;
    const regex = /^[a-zA-Z0-9\s]*$/; // Allow only alphanumeric characters and spaces
    if (!regex.test(value)) {
      input.value = value.replace(/[^a-zA-Z0-9\s]/g, '');
    }
  }
}
