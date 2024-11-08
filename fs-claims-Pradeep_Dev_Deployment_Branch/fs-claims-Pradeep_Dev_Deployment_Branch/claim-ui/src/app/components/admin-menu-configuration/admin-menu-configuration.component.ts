import { Component, Input, Output, ViewChild, OnInit } from '@angular/core';

import { TreeNode } from 'primeng/api';
import { NodeService } from '../../ngrx/store/services/nodeservice'
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { SmartCodeService } from 'src/app/ngrx/store/services/smart-code.service';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';


interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
interface MenuItem {
  linkUrl: string,
  menuDescription: string,
  menuName: string,
  parentMenuId:string,
  resourcesMenuId: string,
  statusId: number
}

// interface MenuNode {
//   data: MenuItem;
//   children?: MenuNode[];
// }

interface MenuItem {
  linkUrl: string;
  menuDescription: string;
  menuName: string;
  parentMenuId: string;
  resourcesMenuId: string;
  statusId: number;
}

interface MenuNode {
  data: MenuItem;
  children?: MenuNode[];
}




@Component({
  selector: 'app-admin-menu-configuration',
  templateUrl: './admin-menu-configuration.component.html',
  styleUrls: ['./admin-menu-configuration.component.scss']
})
export class AdminMenuConfigurationComponent implements OnInit {

  @Input() collapsed = false;
  @Input() screenWidth = 0;
  isSideNavCollapsed: boolean = true;   
  
  files!: TreeNode[]; 
  datafiles:any
  public resourceMenuId!: number;
  public statusId!:number;
  public selectedRow:number = 0;
  public errorMessage:any

 public addAdminForm = this._formBuilder.group({
    menuName:['',Validators.required],
    description:['',Validators.required],
    menuUrl:['',Validators.required],
    parentMenu:['',Validators.required]

  })

  public editAdminForm = this._formBuilder.group({
    editMenuName: [''],
    editDescription: [''],
    editMenuUrl: [''],
    editParentMenu: ['']

  })
  public adminPayload: any;
  public updateadminPayload: any
  // public adminPayload:any;
  // public updateadminPayload:any
  public parentMenuDropDownValues:any = [];
  public statusMessage:boolean = false;
  public selectedMenuName:any;
  public filteredParentDropDwnvalues: any = [];
  public editParentDropDwnvalue: any = [];
  public parentDropDwnvalues: any = [];
  public tempdropDwndata: any = [];
  public tempchildIds : any = [];

  constructor(private nodeService: NodeService,
    private bodyClassService: BodyClassService,
    private smartCodeService: SmartCodeService,
    private _formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
  
    private toasterService: ToasterService) { }

  ngOnInit() {
    // this.nodeService.getFilesystem().then((files) => (this.files = files));
    // console.log('datatesting', this.files)
    //this.smartCodeService.fetchResourceMenus().then((files:any) => (this.files = files));
    this.fetchresourceMenus();

  }

  // convertMenuData(data: any[],type:string): MenuNode[] {
  //   const result: MenuNode[] = [];

  //   data.forEach((item, index) => {
  //     const node: MenuNode = {
  //       data: {
  //         linkUrl
  //           : item.linkUrl,
  //         menuDescription
  //           : item.menuDescription,
  //         menuName
  //           : item.menuName,

  //         parentMenuId
  //           :
  //           item.parentMenuId,
  //         resourcesMenuId
  //           : item.resourcesMenuId,
  //         statusId
  //           : item.statusId
  //       }
  //     };

  //     if (item.subMenu) {
  //       node.children = this.convertMenuData(item.subMenu,'subMenu');
  //     }
  //     if (index === 0 && type!='subMenu') {
  //       result.push({
  //         data: {
  //           menuName: "Resources",
  //           linkUrl : '',
  //         menuDescription : '',

  //         parentMenuId:'',
  //         resourcesMenuId:'',
  //         statusId: 1
  //         },
  //         children: [node]
  //       })
  //     }
  //     else {
  //       result.push(node);
  //     }

  //   });

  //   return result;
  // }

  // requiredOutPut: any = [];
  // fetchresourceMenus() {
  //   this.smartCodeService.fetchResourceMenus('adminResourceMenu').subscribe(
  //     (data: any) => {
  //       //console.log('datatesting', this.files)
  //       // this.files =  data.body;
  //       // let requiredOutPut = { "data": [] };

  //       const convertedData = this.convertMenuData(data.body,'menu');
  //       // console.log(convertedData);
  //       this.files = convertedData;
  //       this.parentMenuDropDownValues =  data.body;
  //       this.selectedMenuName = data.body[0].resourcesMenuId
  //       //  console.log('check....', this.files)
  //       // this.source = data.body;


  //     },
  //     (error: any) => {
  //       // console.error('Error fetching resource menus:', error);
  //     }
  //   );
  // }

  convertMenuData(data: any[], type: string): MenuNode[] {
    const result: MenuNode[] = [];
  
    data.forEach((item) => {
      const node: MenuNode = {
        data: {
          linkUrl: item.linkUrl,
          menuDescription: item.menuDescription,
          menuName: item.menuName,
          parentMenuId: item.parentMenuId ? item.parentMenuId.toString() : '',
          resourcesMenuId: item.resourcesMenuId.toString(),
          statusId: item.statusId,
        },
      };
  
      if (item.subMenu && item.subMenu.length > 0) {
        node.children = this.convertMenuData(item.subMenu, 'subMenu');
      }
  
      result.push(node);
    });
  
    return result;
  }
  
  fetchresourceMenus() {
    this.smartCodeService.fetchResourceMenus('adminResourceMenu').subscribe(
      (data: any) => {
        const rawData = data.body;
        let convertedData = this.convertMenuData(rawData, 'menu');
        
        // Wrap in a single top-level "Resources" node
        if (convertedData.length > 0 && convertedData[0].data.menuName !== "Resources") {
          convertedData = [{
            data: {
              menuName: "Resources",
              linkUrl: '',
              menuDescription: '',
              parentMenuId: '',
              resourcesMenuId: '0',
              statusId: 1,
            },
            children: convertedData,
          }];
        }
  
        this.files = convertedData;
        this.parentMenuDropDownValues = rawData;
        this.selectedMenuName = rawData[0].resourcesMenuId.toString();
        const flattenedMenu = this.flattenMenu(this.parentMenuDropDownValues);
         this.filteredParentDropDwnvalues = flattenedMenu.filter((ele:any)=>ele.statusId == 1)
         const allSubmenus = this.getSubmenus(this.parentMenuDropDownValues);
        //  this.editParentDropDwnvalue = allSubmenus.filter((obj:any) => !this.tempchildIds.includes(obj.resourcesMenuId));
        this.editParentDropDwnvalue = this.filteredParentDropDwnvalues
         this.tempchildIds = []
      },
      (error: any) => {
        console.error('Error fetching resource menus:', error);
      }
    );
  }

 
  // adding dropdown values for new child  // 
  flattenMenu(menu: any[], parentName: string = ''): any[] {
    let result: any[] = [];

    menu.forEach(item => {
      const formattedName = parentName ? `${parentName} - ${item.menuName}` : item.menuName;
      result.push({
        menuName: formattedName,
        linkUrl: item.linkUrl,
        menuDescription: item.menuDescription,
        parentMenuId:item.parentMenuId,
        resourcesMenuId:item.resourcesMenuId,
        statusId: item.statusId

      });

      if (item.subMenu && item.subMenu.length > 0) {
        if(formattedName == 'Resources'){
          result = result.concat(this.flattenMenu(item.subMenu ));
        }else{
          result = result.concat(this.flattenMenu(item.subMenu , formattedName));
        }
        
      }
    });

    return result;
  }



  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }

  openAddAdminModel(){
    const modelDiv = document.getElementById('addModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }


  addAdminMenu() {
    this.adminPayload = {
      "createdByUserId": Number(localStorage.getItem('userId')),
      "modifiedByUserId": Number(localStorage.getItem('userId')),
      // "createdDate": "2024-07-11T05:41:22.543Z",
      // "resourcesMenuId": 0,
      "menuName": this.addAdminForm.value.menuName,
      "linkUrl": this.addAdminForm.value.menuUrl,
      "parentMenuId": this.addAdminForm.value.parentMenu,
      "menuDescription":this.addAdminForm.value.description ,
      // "subMenu": [''],
      "statusId":1
  }

 
  this.smartCodeService.addResourceMenu(this.adminPayload).subscribe(data=>{
    this.toasterService.showToast('success', 'Admin Menu added is successfully');
    const modelDiv = document.getElementById('addModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    this.addAdminForm.patchValue({
      menuName : '',
      menuUrl:'',
      parentMenu:'',
      description:''
    })
    this.fetchresourceMenus();
  },
  (error) => {
    // this.errorMessage = error.error.detail
    this.closeModal('addModel')


    this.toasterService.showToast('error', error.error.detail);
     })
  
  }
  getSubmenus(menu:any) {
    let result: any[] = [];

    menu.forEach((item:any) => {
      result.push({
        menuName: item.menuName,
        linkUrl: item.linkUrl,
        menuDescription: item.menuDescription,
        parentMenuId:item.parentMenuId,
        resourcesMenuId:item.resourcesMenuId,
        statusId: item.statusId,
       

      });

      if (item.subMenu && item.subMenu.length > 0) {
        result = result.concat(this.getSubmenus(item.subMenu ));
      }else{
        this.tempchildIds.push(item.resourcesMenuId)
      }
      
    });
    return result;
}




  editModal(rowData:any){
    this.editAdminForm.patchValue({
      editMenuName:rowData.menuName,
      editDescription:rowData.menuDescription,
      editMenuUrl:rowData.linkUrl,
      editParentMenu:rowData.resourcesMenuId
    })
    this.selectedRow = Number(rowData.resourcesMenuId)
    // console.log( this.selectedRow,'............. this.selectedRow')
    const modelDiv = document.getElementById('editModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
   
  }

  closeModal(id:string){
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  updateAdminMenu() {
    let parentMenuId = this.editParentDropDwnvalue.filter((ele:any)=>ele.resourcesMenuId == this.editAdminForm.value.editParentMenu)
    this.updateadminPayload = {
      "createdByUserId": Number(localStorage.getItem('userId')),
        "modifiedByUserId": Number(localStorage.getItem('userId')),
        "resourcesMenuId":this.selectedRow,
        "menuName":this.editAdminForm.value.editMenuName ,
        "linkUrl": this.editAdminForm.value.editMenuUrl,
        "parentMenuId": parentMenuId[0].parentMenuId,
        "menuDescription": this.editAdminForm.value.editDescription,
        // "subMenu": [ ],
        // "statusId": statusId[0].statusId
    }

   
    this.smartCodeService.updateResourceMenu(this.updateadminPayload).subscribe(data => {
      this.toasterService.showToast('success', 'Admin menu is updated successfully.');
      this.closeModal('editModel')
      this.fetchresourceMenus();
    },
    (error) => {
      this.toasterService.showToast('success', 'Failed to updated Admin menu');
    })
  }

    deleteModal(rowData:any){
      const modelDiv = document.getElementById('deleteModel');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
      this.resourceMenuId = rowData.resourcesMenuId
      // console.log(this.resourceMenuId,'......data')
    }

    deleteResourceMenu(){
      this.smartCodeService.deleteResourceMenu(this.resourceMenuId).subscribe(data=>{
      
        this.toasterService.showToast('success', 'Admin menu deleted successfully.');
        this.closeModal('deleteModel')
        this.fetchresourceMenus();
      },
      (error) => {
      
        this.toasterService.showToast('error', ' Admin menu Failed To Delete');
      })
    }

    openStatusModal(event:Event,rowData:any){
      event.preventDefault();
      if(rowData.statusId == 1){
          this.statusMessage = true;
      }else{
        this.statusMessage = false;
      }
      const modelDiv = document.getElementById('statusModel');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
      this.resourceMenuId = rowData.resourcesMenuId
      this.statusId = rowData.statusId
    }

    enableordisableMenu(){
      this.statusId = this.statusId == 1 ? 2 : 1;
      this.smartCodeService.enableordisableMenu( this.resourceMenuId,this.statusId).subscribe(data=>{
        const msg = this.statusMessage?'Admin menu Deactivated successfully' : 'Admin menu activated successfully' 
        
        
        this.toasterService.showToast('success', this.statusMessage?'Admin menu Deactivated successfully' : 'Admin menu activated successfully' );
        this.closeModal('statusModel')
        this.fetchresourceMenus();
      },
      (error) => {
       
        
        this.toasterService.showToast('error', 'Admin menu Status action  Failed');
       
      })
    }

}