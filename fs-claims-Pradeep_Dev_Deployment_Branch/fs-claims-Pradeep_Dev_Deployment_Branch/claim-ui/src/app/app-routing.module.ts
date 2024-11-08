import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { InitiateClaimComponent } from './components/initiate-claim/initiate-claim.component';
import { ReportsComponent } from './components/reports/reports.component';
import { WorkQueueComponent } from './components/work-queue/work-queue.component';
import { MyWorkQueueComponent } from './components/my-work-queue/my-work-queue.component';
import { SettingsComponent } from './components/settings/settings.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGaurd } from './ngrx/store/services/authgaurd';
import { MsalGuard } from '@azure/msal-angular';
import { UserRolePermissionComponent } from './components/users-roles-permissions/user-role-permission/user-role-permission.component';
import { RolesMatrixTableComponent } from './components/roles-matrix-table/roles-matrix-table.component';
import { MatrixTableComponent } from './components/matrix-table/matrix-table.component';
import { CreatePermissionsetStepperComponent } from './components/create-permissionset-stepper/create-permissionset-stepper.component';
import { UserProfileManagementComponent } from './components/roles/user-profile-management/user-profile-management.component';
import { ClaimDetailComponent } from './components/claim-detail/claim-detail.component';
import { ClaimDetailInvoiceComponent } from './components/claim-detail-invoice/claim-detail-invoice.component';
import { AddEditLineComponent } from './components/add-edit-line/add-edit-line.component';
import { DeclineClaimComponent } from './components/decline-claim/decline-claim.component';
import { ReassignClaimComponent } from './components/reassign-claim/reassign-claim.component';
import { AnalyzeClaimComponent } from './components/analyze-claim/analyze-claim.component';
import { InventoryCheckComponent } from './components/inventory-check/inventory-check.component';
import { DuplicateComponent } from './components/duplicate/duplicate.component';
import { CreateServiceComponent } from './components/create-service/create-service.component';
import { CreateLineComponent } from './components/create-line/create-line.component';
import { AssociateDocsComponent } from './components/associate-docs/associate-docs.component';
import { ClaimsListComponent } from './components/claims-list/claims-list.component';
import { AdminMenuConfigurationComponent } from './components/admin-menu-configuration/admin-menu-configuration.component';
import { ClaimCodeManagementComponent } from './components/claim-code-management/claim-code-management.component'
import { ResolutionMaintenanceComponent } from './components/resolution-maintenance/resolution-maintenance.component';
import { PhotoMaintenanceComponent } from './components/photo-maintenance/photo-maintenance.component';
import { ViewExportRulesComponent } from './components/view-export-rules/view-export-rules.component';

import { AdminCountConfigComponent } from './components/admin-count-config/admin-count-config.component';
import { BulkUploadComponent } from './components/bulk-upload/bulk-upload.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import { AddTaskListComponent } from './components/add-task-list/add-task-list.component';
import { ViewNoteTemplateComponent } from './components/view-note-template/view-note-template.component';
import { CreateBulkUploadComponent } from './components/create-bulk-upload/create-bulk-upload.component';
import { UserManagerGrpComponent } from './components/user-manager-grp/user-manager-grp.component';
import { AuditHistoryComponent } from './components/audit-history/audit-history.component';
import { InspectionsComponent } from './components/inspections/inspections.component';
import { TotalItemizedCostComponent } from './components/total-itemized-cost/total-itemized-cost.component';
import { RGAAuthorizationComponent } from './components/rga-authorization/rga-authorization.component';
import { ViewRgaComponent } from './components/view-rga/view-rga.component';
import { NewViewRgaComponent } from './components/new-view-rga/new-view-rga.component';
import { ListRgaComponent } from './components/list-rga/list-rga.component';
import { ReceiptRgaComponent } from './components/receipt-rga/receipt-rga.component';
import { ProcessRgaReceiptComponent } from './components/process-rga-receipt/process-rga-receipt.component';
import { PickupScheduleComponent } from './components/pickup-schedule/pickup-schedule.component';
import { SearchClaimHistoryComponent } from './components/search-claim-history/search-claim-history.component';

import { EditRgaComponent } from './components/edit-rga/edit-rga.component';
import { ViewCrmComponent } from './components/view-crm/view-crm.component';
import { PrepareDispositionComponent } from './components/prepare-disposition/prepare-disposition.component';
import { MaintainDispositionComponent } from './components/maintain-disposition/maintain-disposition.component';
import { SearchInvoiceHistoryComponent } from './components/search-invoice-history/search-invoice-history.component';
import { EditCrmDetailsComponent } from './components/edit-crm-details/edit-crm-details.component';
import { PrepareCrmComponent } from './components/prepare-crm/prepare-crm.component';
import { ClaimVendorListComponent } from './components/claim-vendor-list/claim-vendor-list.component';
import { ClaimVendorDetailComponent } from './components/claim-vendor-detail/claim-vendor-detail.component';
import { ClaimVendorInvoiceComponent } from './components/claim-vendor-invoice/claim-vendor-invoice.component';
import { ClaimVendorMapComponent } from './components/claim-vendor-map/claim-vendor-map.component';
import { InspectionScreenComponent } from './components/inspection-screen/inspection-screen.component';
import { InspectionRequestComponent } from './components/inspection-request/inspection-request.component';
import { ClaimVendorProfileComponent } from './components/claim-vendor-profile/claim-vendor-profile.component';
import { MassUploadDetailComponent } from './components/mass-upload-detail/mass-upload-detail.component';
import { ManagerCrmComponent } from './components/manager-crm/manager-crm.component';
import { RollHistoryComponent } from './components/roll-history/roll-history.component';
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
import { ViewInventoryComponent } from './components/view-inventory/view-inventory.component';
import { InventoryDetailComponent } from './components/inventory-detail/inventory-detail.component';


import { BatchjobComponent } from './components/batchjob/batchjob.component';

import { AdminDataScreensComponent } from './components/admin-data-screens/admin-data-screens.component';
import { ListMyInspectionComponent } from './components/list-my-inspection/list-my-inspection.component';
import { RemoveRgaComponent } from './components/remove-rga/remove-rga.component';
import { ClaimVendorEmailComponent } from './components/claim-vendor-email/claim-vendor-email.component';
import { ClaimsVendorSearchComponent } from './components/claims-vendor-search/claims-vendor-search.component';
import { ProductCatalogClaimInfoComponent } from './components/product-catalog-claim-info/product-catalog-claim-info.component';




const routes: Routes = [

  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [MsalGuard] },
  { path: 'initiateClaim', component: InitiateClaimComponent, canActivate: [MsalGuard] },
  { path: 'reports', component: ReportsComponent, canActivate: [MsalGuard] },
  { path: 'workQueue', component: WorkQueueComponent, canActivate: [MsalGuard] },
  { path: 'work-history', component: MyWorkQueueComponent, canActivate: [MsalGuard] },
  { path: 'admin/roles', component: RolesMatrixTableComponent, canActivate: [MsalGuard] },
  { path: 'admin/users', component: UserProfileManagementComponent, canActivate: [MsalGuard] },
  { path: 'admin/permissions', component: MatrixTableComponent, canActivate: [MsalGuard] },
  { path: 'admin/permissions/createpermissionset', component: CreatePermissionsetStepperComponent, canActivate: [MsalGuard] },
  { path: 'associated-docs', component: AssociateDocsComponent, canActivate: [MsalGuard] },
  { path: 'claim-detail/:id', component: ClaimDetailComponent, canActivate: [MsalGuard] },
  { path: 'resources-menu-config', component: AdminMenuConfigurationComponent },
  { path: 'resolution-maintenance', component: ResolutionMaintenanceComponent },
  { path: 'photo-maintenance', component: PhotoMaintenanceComponent },
  { path: 'view-export-rules', component: ViewExportRulesComponent },
  { path: 'widget-config', component: AdminCountConfigComponent },

  { path: 'bulk-upload', component: BulkUploadComponent },
  { path: 'view-note-template', component: ViewNoteTemplateComponent },

  { path: 'create-bulk-upload', component: CreateBulkUploadComponent },
  { path: 'total-itemized-cost', component: TotalItemizedCostComponent },

  { path: 'claim-code-management', component: ClaimCodeManagementComponent, canActivate: [MsalGuard] },
  { path: 'workQueue/:type', component: WorkQueueComponent },
  { path: 'workQueue/:type/:value', component: WorkQueueComponent },// Route with parameter for different types
  { path: 'task-list/:id/:claimisvoid', component: TaskListComponent },

  { path: 'prep-rga', component: RGAAuthorizationComponent },
  { path: 'edit-rga', component: EditRgaComponent },
  { path: 'view-rga', component: ViewRgaComponent },
  { path: 'maintain-rga', component: NewViewRgaComponent },
  { path: 'list-rga', component: ListRgaComponent },
  { path: 'receipt-rga', component: ReceiptRgaComponent },
  { path: 'process-receipt', component: ProcessRgaReceiptComponent },
  { path: 'pickup-schedule', component: PickupScheduleComponent },
  { path: 'search-claim-history', component: SearchClaimHistoryComponent },

  { path: 'view-crm', component: ViewCrmComponent },
  { path: 'prepare-disposition', component: PrepareDispositionComponent },
  { path: 'maintain-disposition', component: MaintainDispositionComponent },
  { path: 'search-invoice-history', component: SearchInvoiceHistoryComponent },
  { path: 'prepare-crm', component: PrepareCrmComponent },
  { path: 'manager-crm', component: ManagerCrmComponent },
  { path: 'edit-crm', component: EditCrmDetailsComponent },
  { path: 'claim-vendor-list', component: ClaimVendorListComponent },
  { path: 'claim-vendor-detail', component: ClaimVendorDetailComponent },
  { path: 'claim-vendor-map', component: ClaimVendorMapComponent },
  { path: 'claim-vendor-invoice', component: ClaimVendorInvoiceComponent },
  { path: 'inspection-screen', component: InspectionScreenComponent },
  { path: 'inspection-request', component: InspectionRequestComponent },
  { path: 'claim-vendor-profile', component: ClaimVendorProfileComponent },
  { path: 'mass-upload-detail', component: MassUploadDetailComponent },
  { path: 'search-invoice-history', component: SearchInvoiceHistoryComponent },
  { path: 'roll-history', component: RollHistoryComponent },
  { path: 'roll-lineage', component: RollLineageComponent },
  { path: 'shop-order', component: ShopOrderComponent },
  { path: 'product-catalog', component: ProductCatalogComponent },
  { path: 'product-catalog-spec', component: ProductCatalogSpecComponent },
  { path: 'mfg-style-number', component: MfgStyleNumberComponent },
  { path: 'plant-information', component: PlantInformationComponent },
  { path: 'carpet-inventory-summary', component: CarpetInventorySummaryComponent },
  { path: 'roll-history/:rollNumber', component: RollHistoryComponent },
  { path: 'product-catalog-claim-info', component: ProductCatalogClaimInfoComponent },
  { path: 'product-catalog-specifications', component: ProductCatalogSpecificationsComponent },

  { path: 'plant-information-data', component:PlantInformationDataComponent  },
  { path: 'view-inventory', component:ViewInventoryComponent  },
  { path: 'inventory-detail', component:InventoryDetailComponent  },



  {path:'task-list/:id/:claimisvoid', component:TaskListComponent},

  {path: 'prep-rga', component: RGAAuthorizationComponent},
  {path:'edit-rga', component:EditRgaComponent},
  {path: 'view-rga', component: ViewRgaComponent},
  {path: 'maintain-rga',component: NewViewRgaComponent},
  {path: 'list-rga', component:ListRgaComponent},
  {path: 'receipt-rga', component:ReceiptRgaComponent},
  {path: 'process-receipt', component: ProcessRgaReceiptComponent},
  {path: 'pickup-schedule', component: PickupScheduleComponent},
  {path: 'search-claim-history',component: SearchClaimHistoryComponent},

  {path: 'view-crm', component: ViewCrmComponent},
  {path: 'prepare-disposition', component: PrepareDispositionComponent},
  {path: 'maintain-disposition', component: MaintainDispositionComponent},
  {path:'search-invoice-history', component: SearchInvoiceHistoryComponent},
  {path:'prepare-crm', component: PrepareCrmComponent},
  {path:'manager-crm', component: ManagerCrmComponent},
  {path:'edit-crm', component: EditCrmDetailsComponent},
  {path:'claim-vendor-list', component: ClaimVendorListComponent},
  {path:'claim-vendor-detail', component: ClaimVendorDetailComponent},
  {path:'claim-vendor-map', component: ClaimVendorMapComponent},
  {path:'claim-vendor-invoice', component: ClaimVendorInvoiceComponent},
  {path:'inspection-screen', component: InspectionScreenComponent},
  {path:'inspection-request', component: InspectionRequestComponent},
  {path: 'claim-vendor-profile', component: ClaimVendorProfileComponent},
  {path: 'mass-upload-detail', component: MassUploadDetailComponent},
  {path: 'search-invoice-history', component: SearchInvoiceHistoryComponent},
  {path: 'roll-history', component: RollHistoryComponent},
  {path: 'roll-lineage', component: RollLineageComponent},
  {path: 'shop-order', component: ShopOrderComponent},
  {path: 'product-catalog', component: ProductCatalogComponent},
  {path: 'product-catalog-spec', component: ProductCatalogSpecComponent},
  {path: 'mfg-style-number', component: MfgStyleNumberComponent},
  {path: 'plant-information', component: PlantInformationComponent},
  {path: 'carpet-inventory-summary', component: CarpetInventorySummaryComponent},
  {path: 'product-catalog-claim-info', component: ProductCatalogClaimInfoComponent},
  {path: 'admin/jobconfiguration', component: BatchjobComponent},
  {path: 'admin-data-screen', component: AdminDataScreensComponent},
  {path: 'remove-rga', component: RemoveRgaComponent},
  {path: 'list-my-inspection', component: ListMyInspectionComponent},
  {path: 'claim-vendor-mail', component: ClaimVendorEmailComponent},
  {path: 'claim-vendor-search', component: ClaimsVendorSearchComponent},

  // {path:'user-manager-grp', component:UserManagerGrpComponent},

  // {path:'task-list', component:TaskListComponent},
  // {path:'add-task-list', component:AddTaskListComponent},

  // {path:'inventory-check', component:InventoryCheckComponent,canActivate: [MsalGuard]},
  // {path:'duplicate', component:DuplicateComponent,canActivate: [MsalGuard]},
  // {path:'check-service', component:CreateServiceComponent,canActivate: [MsalGuard]},
  // {path:'check-line', component:CreateLineComponent,canActivate: [MsalGuard]},
  // {path:'claim-detail-invoice', component:ClaimDetailInvoiceComponent,canActivate: [MsalGuard]},
  // { path: 'add-edit-line', component: AddEditLineComponent,canActivate: [MsalGuard] },
  // { path: 'decline-claim', component: DeclineClaimComponent,canActivate: [MsalGuard] },
  // { path: 'reassign-claim', component: ReassignClaimComponent,canActivate: [MsalGuard] },
  // { path: 'analyze-claim', component:AnalyzeClaimComponent,canActivate: [MsalGuard]},
  // { path: 'settings', component: SettingsComponent,canActivate: [AuthGaurd] },
  // { path: 'claims', component: ClaimsListComponent, canActivate: [MsalGuard]},
  // { path: 'workQueue/:type/:status', component: WorkQueueComponent }
  // {path:'audit-history', component:AuditHistoryComponent},
  // {path:'inspections', component:InspectionsComponent},
];


@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
