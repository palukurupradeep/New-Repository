import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-defect-percent',
  templateUrl: './defect-percent.component.html',
  styleUrls: ['./defect-percent.component.scss']
})
export class DefectPercentComponent {
  formGroup: FormGroup;

  constructor(private fb: FormBuilder) {
    this.formGroup = this.fb.group({
      numericInput: ['', [Validators.required, Validators.pattern('^[0-9]*$')]]
    });
  }
  validateInput(event: KeyboardEvent) {
    const charCode = event.charCode;
    const charStr = String.fromCharCode(charCode);

    // Allow only numbers (0-9)
    if (!charStr.match(/^[0-9]$/)) {
      event.preventDefault();
    }
  }

}
