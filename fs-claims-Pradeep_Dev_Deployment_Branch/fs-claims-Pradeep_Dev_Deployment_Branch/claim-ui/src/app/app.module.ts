import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { MSAL_GUARD_CONFIG, MsalGuardConfiguration, MSAL_INSTANCE, MsalModule, MsalService } from '@azure/msal-angular';
import { SidenavComponent } from './components/sidenav/sidenav.component';
import { InitiateClaimComponent } from './components/initiate-claim/initiate-claim.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ReportsComponent } from './components/reports/reports.component';
import { WorkQueueComponent } from './components/work-queue/work-queue.component';
import { ThemeStyleComponent } from './components/theme-style/theme-style.component';
import { BodyComponent } from './components/body/body.component';
import { FooterComponent } from './components/footer/footer.component';
import { SettingsComponent } from './components/settings/settings.component';
import { ClaimDuesComponent } from './components/dashboard/claim-dues/claim-dues.component';
import { OdItemsComponent } from './components/dashboard/od-items/od-items.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StoreModule } from '@ngrx/store';
import { LoginComponent } from './components/login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { loginReducer } from './ngrx/store/reducers/login.reducer';
import { AuthGaurd } from './ngrx/store/services/authgaurd';
import { ListFilterPipe } from './pipe/ListFilterPipe';
import { CommonModule, DatePipe, DecimalPipe } from '@angular/common';
import { RecClaimsComponent } from './components/dashboard/rec-claims/rec-claims.component';
import { NgbPaginationModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { RecClaimsService } from './mockApis/rec-claims.service';
import { BodyClassService } from './ngrx/store/services/body-class.service';
import { AuthInterceptorService } from './mockApis/auth-interceptor';
import { TableComponent } from './shared/table/table.component';
import { PhoneNumberDirective } from './utils/phone-number.directive';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { InputRestrictionDirective } from './utils/input-restriction.directive';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ApplicationRolesPermissionComponent } from './components/application-roles-permission/application-roles-permission.component';
import {MatStepperModule} from '@angular/material/stepper';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {NgFor} from '@angular/common';
import {MatRadioModule} from '@angular/material/radio';
import { UserProfileManagementComponent } from './components/roles/user-profile-management/user-profile-management.component';
import { MatrixTableComponent } from './components/matrix-table/matrix-table.component';
import { StepperComponent } from './components/stepper-component/stepper/stepper.component';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { UserRolePermissionComponent } from './components/users-roles-permissions/user-role-permission/user-role-permission.component';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { RolesMatrixTableComponent } from './components/roles-matrix-table/roles-matrix-table.component';
import { PermissionsStepperComponent } from './components/roles/permissions-stepper/permissions-stepper.component';
import { UsermanagementStepperComponent } from './components/roles/usermanagement-stepper/usermanagement-stepper.component';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { RolesService } from './ngrx/store/services/roles.service';
import { CreatePermissionsetStepperComponent } from './components/create-permissionset-stepper/create-permissionset-stepper.component';
import { ClaimDetailComponent } from './components/claim-detail/claim-detail.component';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import {MatTreeNestedDataSource, MatTreeModule} from '@angular/material/tree';

import { TreeModule } from 'primeng/tree';
import { StyleClassModule } from 'primeng/styleclass';
import { TreeTableModule } from 'primeng/treetable';
import { TimelineModule } from 'primeng/timeline';
import { NodeService } from './ngrx/store/services/nodeservice';



import { ClaimCustomerDetailsComponent } from './components/claim-detail/claim-customer-details/claim-customer-details.component';

import { ClaimDetailActivitiesComponent } from './components/claim-detail-activities/claim-detail-activities.component';
import { ClaimRateDetailComponent } from './components/claim-rate-detail/claim-rate-detail.component';
import { ClaimTaskViewComponent } from './components/claim-task-view/claim-task-view.component';
import { ClaimDetailInvoiceComponent } from './components/claim-detail-invoice/claim-detail-invoice.component';
import { AddEditLineComponent } from './components/add-edit-line/add-edit-line.component';
import { DeclineClaimComponent } from './components/decline-claim/decline-claim.component';
import { ReassignClaimComponent } from './components/reassign-claim/reassign-claim.component';
import { AnalyzeClaimComponent } from './components/analyze-claim/analyze-claim.component';
import { EndUserClaimDetailComponent } from './components/end-user-claim-detail/end-user-claim-detail.component';
import { InventoryCheckComponent } from './components/inventory-check/inventory-check.component';
import { DuplicateComponent } from './components/duplicate/duplicate.component';
import { CreateServiceComponent } from './components/create-service/create-service.component';
import { CreateLineComponent } from './components/create-line/create-line.component';
import { environment } from 'src/environments/environment';
import { IPublicClientApplication, InteractionType, PublicClientApplication } from '@azure/msal-browser';
import { AssociateDocsComponent } from './components/associate-docs/associate-docs.component';
import { ClaimsListComponent } from './components/claims-list/claims-list.component';
import { MyWorkQueueComponent } from './components/my-work-queue/my-work-queue.component';
import { WorkQueueFilterComponent } from './components/work-queue-filter/work-queue-filter.component';
import { WorkQueueTableComponent } from './components/work-queue-table/work-queue-table.component';
import { WorkQueueFieldchooserComponent } from './components/work-queue-fieldchooser/work-queue-fieldchooser.component';
import { DashboardCardComponent } from './components/dashboard/dashboard-card/dashboard-card.component';
import { DashboardTableComponent } from './components/dashboard/dashboard-table/dashboard-table.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { NgApexchartsModule } from 'ng-apexcharts';
import { AdminMenuConfigurationComponent } from './components/admin-menu-configuration/admin-menu-configuration.component';
import { ClaimCodeManagementComponent } from './components/claim-code-management/claim-code-management.component';

import { ResolutionMaintenanceComponent } from './components/resolution-maintenance/resolution-maintenance.component';
import { PhotoMaintenanceComponent } from './components/photo-maintenance/photo-maintenance.component';
import { ViewExportRulesComponent } from './components/view-export-rules/view-export-rules.component';
import { AdminCountConfigComponent } from './components/admin-count-config/admin-count-config.component';
import { BulkUploadComponent } from './components/bulk-upload/bulk-upload.component';
import { CreateBulkUploadComponent } from './components/create-bulk-upload/create-bulk-upload.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import { AddTaskListComponent } from './components/add-task-list/add-task-list.component';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatTabsModule} from '@angular/material/tabs';
import { ViewNoteTemplateComponent } from './components/view-note-template/view-note-template.component';
import { UserManagerGrpComponent } from './components/user-manager-grp/user-manager-grp.component';
import { MatSelectModule } from '@angular/material/select';
import { AuditHistoryComponent } from './components/audit-history/audit-history.component';
import { InspectionsComponent } from './components/inspections/inspections.component';
import { TotalItemizedCostComponent } from './components/total-itemized-cost/total-itemized-cost.component';
// import { RGAPathComponent } from './components/rga-path/rga-path.component';
import { DefectPercentComponent } from './components/defect-percent/defect-percent.component';
import {RGAAuthorizationComponent} from './components/rga-authorization/rga-authorization.component';
import { ViewRgaComponent } from './components/view-rga/view-rga.component';
import { NewViewRgaComponent } from './components/new-view-rga/new-view-rga.component';
import { ListRgaComponent } from './components/list-rga/list-rga.component';
import { ReceiptRgaComponent } from './components/receipt-rga/receipt-rga.component';
import { ProcessRgaReceiptComponent } from './components/process-rga-receipt/process-rga-receipt.component';
import { PickupScheduleComponent } from './components/pickup-schedule/pickup-schedule.component';
import { NoSpecialCharactersDirective } from './utils/NoSpecialCharacters.directive';
import {SearchClaimHistoryComponent} from './components/search-claim-history/search-claim-history.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MenuItemsComponent } from './components/menu-items/menu-items.component';
import { EditRgaComponent } from './components/edit-rga/edit-rga.component';
import { ViewCrmComponent } from './components/view-crm/view-crm.component';
import { PrepareDispositionComponent } from './components/prepare-disposition/prepare-disposition.component';
import { MaintainDispositionComponent } from './components/maintain-disposition/maintain-disposition.component';
import { SearchInvoiceHistoryComponent } from './components/search-invoice-history/search-invoice-history.component';
import { EditCrmDetailsComponent } from './components/edit-crm-details/edit-crm-details.component';
import { PrepareCrmComponent } from './components/prepare-crm/prepare-crm.component';
import { ClaimVendorListComponent } from './components/claim-vendor-list/claim-vendor-list.component';
import { ClaimVendorDetailComponent } from './components/claim-vendor-detail/claim-vendor-detail.component';
import { ClaimVendorMapComponent } from './components/claim-vendor-map/claim-vendor-map.component';
import { ClaimVendorInvoiceComponent } from './components/claim-vendor-invoice/claim-vendor-invoice.component';
import { InspectionScreenComponent } from './components/inspection-screen/inspection-screen.component';
import { InspectionRequestComponent } from './components/inspection-request/inspection-request.component';
import { ClaimVendorProfileComponent } from './components/claim-vendor-profile/claim-vendor-profile.component';
import { ToasterComponent } from './components/toaster/toaster.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { PhoneNumberFormatterPipe } from './pipe/phoneNumberFormaterPipe';import { MassUploadDetailComponent } from './components/mass-upload-detail/mass-upload-detail.component';
import { RollHistoryComponent } from './components/roll-history/roll-history.component';
import { ManagerCrmComponent } from './components/manager-crm/manager-crm.component';
import { RollLineageComponent } from './components/roll-lineage/roll-lineage.component';
import { ShopOrderComponent } from './components/shop-order/shop-order.component';
import { ProductCatalogComponent } from './components/product-catalog/product-catalog.component';
import { ProductCatalogSpecComponent } from './components/product-catalog-spec/product-catalog-spec.component';
import { MfgStyleNumberComponent } from './components/mfg-style-number/mfg-style-number.component';
import { PlantInformationComponent } from './components/plant-information/plant-information.component';
import { CarpetInventorySummaryComponent } from './components/carpet-inventory-summary/carpet-inventory-summary.component';

import { ProductCatalogResultsComponent } from './components/product-catalog-results/product-catalog-results.component';
import { ProductCatalogSpecificationsComponent } from './components/product-catalog-specifications/product-catalog-specifications.component';
import { PlantInformationDataComponent } from './components/plant-information-data/plant-information-data.component';
import { InventoryDetailComponent } from './components/inventory-detail/inventory-detail.component';
import { ViewInventoryComponent } from './components/view-inventory/view-inventory.component';

import { ProductCatalogSpecViewComponent } from './components/product-catalog-spec-view/product-catalog-spec-view.component';
import { ProductCatalogSpecViewColorsComponent } from './components/product-catalog-spec-view-colors/product-catalog-spec-view-colors.component';
import { ShopOrderRollsComponent } from './components/shop-order-rolls/shop-order-rolls.component';

import { MfgProductionRoutingComponent } from './components/mfg-production-routing/mfg-production-routing.component';

import { BatchjobComponent } from './components/batchjob/batchjob.component';
import { AdminDataScreensComponent } from './components/admin-data-screens/admin-data-screens.component';
import { ListMyInspectionComponent } from './components/list-my-inspection/list-my-inspection.component';
import { RemoveRgaComponent } from './components/remove-rga/remove-rga.component';
import { ClaimVendorEmailComponent } from './components/claim-vendor-email/claim-vendor-email.component';
import { ClaimsVendorSearchComponent } from './components/claims-vendor-search/claims-vendor-search.component';
import { ProductCatalogClaimInfoComponent } from './components/product-catalog-claim-info/product-catalog-claim-info.component';





const isIE = window.navigator.userAgent.indexOf('MSIE ') > -1 || window.navigator.userAgent.indexOf('Trident/') > -1;


export function MSALInstanceFactory(): IPublicClientApplication {
  return new PublicClientApplication({
    auth: {
      clientId: environment.clientId,
      authority: environment.authority,
      redirectUri: environment.redirectUrl,
    },
    cache: {
      cacheLocation: 'localStorage',
      storeAuthStateInCookie: isIE, // Set to true if using IE
    },
  });
}
export function MSALGuardConfigFactory(): MsalGuardConfiguration {
  return {
    interactionType: isIE ? InteractionType.Redirect : InteractionType.Popup,
    authRequest: {
      scopes: ['user.read'],
    },
  };
}

@NgModule({
  declarations: [
    AppComponent,
    ListFilterPipe,
    ToasterComponent,
    HeaderComponent,
    SidenavComponent,
    InitiateClaimComponent,
    DashboardComponent,
    ReportsComponent,
    ThemeStyleComponent,
    WorkQueueComponent,
    BodyComponent,
    FooterComponent,
    SettingsComponent,
    ClaimDuesComponent,
    OdItemsComponent,
    LoginComponent,
    TableComponent,
    PhoneNumberDirective,
    
    NoSpecialCharactersDirective,
    InputRestrictionDirective,
    ApplicationRolesPermissionComponent,
     UserProfileManagementComponent,
     MatrixTableComponent,
     StepperComponent,
     UserRolePermissionComponent,
     RolesMatrixTableComponent,
     PermissionsStepperComponent,
     UsermanagementStepperComponent,

     CreatePermissionsetStepperComponent,
       ClaimDetailComponent,

       ClaimCustomerDetailsComponent,

       ClaimDetailActivitiesComponent,
       ClaimRateDetailComponent,
       ClaimTaskViewComponent,
       ClaimDetailInvoiceComponent,
       AddEditLineComponent,
       DeclineClaimComponent,
       ReassignClaimComponent,
       AnalyzeClaimComponent,
       EndUserClaimDetailComponent,
       InventoryCheckComponent,
       DuplicateComponent,
       CreateServiceComponent,
       CreateLineComponent,
       AssociateDocsComponent,
       ClaimsListComponent,
       MyWorkQueueComponent,
       WorkQueueFilterComponent,
       WorkQueueTableComponent,
       WorkQueueFieldchooserComponent,
       DashboardCardComponent,
       DashboardTableComponent,
       AdminMenuConfigurationComponent,
       ClaimCodeManagementComponent,
       ResolutionMaintenanceComponent,
       PhotoMaintenanceComponent,
       ViewExportRulesComponent,
       AdminCountConfigComponent,
       BulkUploadComponent,
       CreateBulkUploadComponent,
       TaskListComponent,
       AddTaskListComponent,
       ViewNoteTemplateComponent,
       UserManagerGrpComponent,
       AuditHistoryComponent,
       InspectionsComponent,
       TotalItemizedCostComponent,
      //  RGAPathComponent,
       DefectPercentComponent,
       ViewRgaComponent,
       RGAAuthorizationComponent,
       NewViewRgaComponent,
       ListRgaComponent,
       ReceiptRgaComponent,
       SearchClaimHistoryComponent,
       ProcessRgaReceiptComponent,
       PickupScheduleComponent,
       MenuItemsComponent,
       EditRgaComponent,
       ViewCrmComponent,
       PrepareDispositionComponent,
       MaintainDispositionComponent,
       SearchInvoiceHistoryComponent,
       EditCrmDetailsComponent,
       PrepareCrmComponent,
       ClaimVendorListComponent,
       ClaimVendorDetailComponent,
       ClaimVendorMapComponent,
       ClaimVendorInvoiceComponent,
       InspectionScreenComponent,
       InspectionRequestComponent,
       ClaimVendorProfileComponent,
      //  InspectionRequestComponent,
       ToasterComponent,
       PhoneNumberFormatterPipe,
      MassUploadDetailComponent,
      RollHistoryComponent,
      ManagerCrmComponent,
      RollLineageComponent,
      ShopOrderComponent,
      ProductCatalogComponent,
      ProductCatalogSpecComponent,
      MfgStyleNumberComponent,
      PlantInformationComponent,
      CarpetInventorySummaryComponent,
      ProductCatalogClaimInfoComponent,

      
      ProductCatalogResultsComponent,
      ProductCatalogSpecificationsComponent,
      PlantInformationDataComponent,
      InventoryDetailComponent,
      ViewInventoryComponent,
      ProductCatalogSpecViewComponent,
      ProductCatalogSpecViewColorsComponent,
      ShopOrderComponent,
      ShopOrderRollsComponent,

      MfgProductionRoutingComponent,

      BatchjobComponent,
      AdminDataScreensComponent,
      ListMyInspectionComponent,
      RemoveRgaComponent,
      ClaimVendorEmailComponent,
      ClaimsVendorSearchComponent,





   
  ],
  imports: [
    RecClaimsComponent,
    BrowserModule,
    CommonModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    NgbTypeaheadModule,
		NgbPaginationModule,
    ReactiveFormsModule,
    FormsModule,
    StoreModule.forRoot({ login: loginReducer}), // Add the login reducer to the store
    StoreDevtoolsModule.instrument({ maxAge: 10 }),
    BsDatepickerModule.forRoot(),
    MatSnackBarModule,
    MatStepperModule,
    MatButtonModule,
    MatInputModule,
    MatCheckboxModule,
    NgFor,
    TabsModule.forRoot(),
    MatTableModule,
    MatInputModule,
    MatChipsModule,
    MatIconModule,
    MsalModule,
    TooltipModule.forRoot(),
    PopoverModule.forRoot(),
    ModalModule.forRoot(),
    BsDropdownModule.forRoot(),
    AccordionModule.forRoot(),
    MatTabsModule,
    DragDropModule,
   NgApexchartsModule,
   TreeTableModule,
   TimelineModule,
   MatSelectModule,
   MatRadioModule,
   MatExpansionModule,
   MatAutocompleteModule,
   MatProgressSpinnerModule,
   MatTooltipModule,
   MatSortModule,
   MatPaginatorModule
  ],
  
  
  providers: [
    {
      provide: MSAL_INSTANCE,
      useFactory: MSALInstanceFactory,
    },
    {
      provide: MSAL_GUARD_CONFIG,
      useFactory: MSALGuardConfigFactory,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    },
    MsalService,
    RolesService,
    MsalService,
    MatrixTableComponent,
    DatePipe,
    AuthGaurd,
    RecClaimsService,
    DecimalPipe,
    BodyClassService], // Add AuthGuard as a provider
  schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule { }
