import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  ApplicationObject,
  PermissionResponse,
  permissionObject,
  permissionSetObj,
} from 'src/app/interfaces/roles-permission.model';
import { RolesService } from 'src/app/ngrx/store/services/roles.service';
import { MatStepper } from '@angular/material/stepper';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

@Component({
  selector: 'app-permissions-stepper',
  templateUrl: './permissions-stepper.component.html',
  styleUrls: ['./permissions-stepper.component.scss'],
})
export class PermissionsStepperComponent implements OnInit {
  isLinear = true;
  permissionForm!: FormGroup;
  @ViewChild('stepper') private myStepper!: MatStepper;

  permissionObject: ApplicationObject[] = [];
  public selectedPermissions: { [key: string]: boolean } = {};
  public selectedActions: { [key: number]: boolean } = {};
  public selectedFunctions: { [key: number]: boolean } = {};
  public expandedPermissions: { [key: number]: boolean } = {};
  public selPermList: any = [];
  public permissionSetObj!: permissionSetObj;
  public perObjects!: any;
  public objArray: permissionObject[] = [];
  functionsVisible: boolean[] = [];
  actionsVisible: boolean[] = [];
  permissionsVisible: boolean[] = [];
  public permissionId: any;
  permissionFlag: boolean = false;
  permissionSetFlag: boolean = false;

  constructor(
    private form: FormBuilder,
    private rolesService: RolesService,
    private toasterService: ToasterService,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getAllPermissionObjects();

    this.permissionForm = this.form.group({
      permissionCode: ['', Validators.required],
      permissionDescription: ['', Validators.required],
    });
  }

  // Getting Permission Objects
  getAllPermissionObjects() {
    this.rolesService.getApplicationObjects().subscribe((data) => {
      console.log(data, 'data');
      this.permissionObject = data;
    });
  }

  addPermission() {
    this.rolesService.addPermission(this.permissionForm.value).subscribe(
      (response: PermissionResponse) => {
        console.log('Permission added:', response.permissionId);
        this.permissionId = response.permissionId;
        console.log('Permission Added');

        if (response.permissionId) {
          this.toasterService.showToast('success', 'Permission created Successfully');
    
          this.permissionFlag =true;
          this.myStepper.next();
        }
      },
      (error) => {
        if (error.status == 400) {
          this.toasterService.showToast('error', 'Permissions already exists');
    
          this.myStepper.selectedIndex = 0;
        }
      }
    );
    this.permissionFlag = true;
    this.cdRef.detectChanges();
  }

  isStep1Completed(): boolean {
    return this.permissionForm.valid;
  }
  addPermissionAndNext() {
    this.addPermission();
    this.myStepper.next();
  }

  goBack() {
    this.permissionObject.forEach((element) => {
      this.selectedPermissions[element.objectId] = false;
    });
    this.permissionForm.patchValue({
      permissionCode: [''],
      permissionDescription: [''],
    });
    this.myStepper.previous();
    // this.stepper.selectedIndex = 1;
  }

  togglePermissions(index: number): void {
    this.permissionsVisible[index] = !this.permissionsVisible[index];
    if (this.permissionsVisible[index]) {
      this.functionsVisible[index] = true;
    }
  }
  toggleFunctions(index: number): void {
    this.functionsVisible[index] = !this.functionsVisible[index];
  }

  toggleActions(index: number): void {
    this.actionsVisible[index] = !this.actionsVisible[index];
  }

  permissionHasActionsOrFunctions(permission: any): boolean {
    return (
      (permission.applicationFunctions &&
        permission.applicationFunctions.length > 0) ||
      (permission.applicationActions &&
        permission.applicationActions.length > 0)
    );
  }

  selectPermission(permissionId: number, index: number) {
    this.selectedPermissions[permissionId] =
      !this.selectedPermissions[permissionId];
  }

  selectFunction(functionId: number, index: number) {
    this.selectedFunctions[functionId] = !this.selectedFunctions[functionId];
    console.log(
      this.selectedFunctions[functionId],
      'this.selectedFunctions[functionId]'
    );
  }
  selectAction(actionId: number, index: number) {
    this.selectedActions[actionId] = !this.selectedActions[actionId];
  }
  collapseAll(): void {
    this.permissionsVisible = this.permissionsVisible.map(() => false);
    this.functionsVisible = this.functionsVisible.map(() => false);
    this.actionsVisible = this.actionsVisible.map(() => false);
  }

  savePermissionObject() {
    const savedObject = {
      permissionId: this.permissionId,
      objects: [] as {
        objectId: number;
        functionIds: number[];
        actionIds: number[];
      }[],
    };

    for (let i = 0; i < this.permissionObject.length; i++) {
      const permission = this.permissionObject[i];

      const object = {
        objectId: permission.objectId,
        functionIds: [] as number[],
        actionIds: [] as number[],
      };

      // Check if the permission is selected
      if (this.selectedPermissions[permission.objectId]) {
        // Check if functions are visible and selected
        if (this.functionsVisible[i]) {
          for (const functionItem of permission.applicationFunctions) {
            if (this.selectedFunctions[functionItem.functionId]) {
              object.functionIds.push(functionItem.functionId);
            }
          }
        }

        // Check if actions are visible and selected
        if (this.actionsVisible[i]) {
          for (const actionItem of permission.applicationActions) {
            if (this.selectedActions[actionItem.actionId]) {
              object.actionIds.push(actionItem.actionId);
            }
          }
        }

        savedObject.objects.push(object);
      }
    }

    // Log the result to the console
    console.log('Saved Permission Object:', savedObject);
    // Here you can send the `savedObject` to your backend or perform any other desired action.
    this.rolesService.createPermissionSet(savedObject).subscribe((data) => {
      this.toasterService.showToast('success', 'PermissionSet added to Permission Successfully');
    
  
    });
    this.selectedPermissions = {};
    this.selectedFunctions = {};
    this.selectedActions = {};
    this.permissionFlag = false;
    this.permissionSetFlag = false;
    this.collapseAll();
    this.goBack();
  }
  
}
