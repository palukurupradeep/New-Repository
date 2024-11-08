import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
    
@Injectable
({
    providedIn: 'root',
  })

export class NodeService {

  
  
  getFileSystemNodesData() {
    return [  
        {  
            "data":{  
                "name":"Resources",
                "Disable":"",
                "Edit":"",
                "Delete":""
            },
            "children":[  
                {  
                    "data":{  
                      "name":"Residential",
                      "Disable":"",
                      "Edit":"",
                      "Delete":""
                    },
                    "children":[  
                        {  
                            "data":{  
                                "name":"Shawfloors.com",
                                "Disable":"",
                                "Edit":"",
                                "Delete":""
                            }
                        },
                        {  
                            "data":{  
                                "name":"Shaw Flooring Solutions",
                                "Disable":"",
                                "Edit":"",
                                "Delete":""
                            }
                        }
                    ]
                }, 
                      {  
                      "data":{  
                        "name":"Commercial",
                        "Disable":"",
                        "Edit":"",
                        "Delete":""
                      },
                      "children":[  
                          {  
                              "data":{  
                                  "name":"Shaw Contract",
                                  "Disable":"",
                                  "Edit":"",
                                  "Delete":""
                              }
                          },                        
                      ]
                  },
                  {  
                    "data":{  
                        "name":"ShawOnline Live",
                        "Disable":"",
                        "Edit":"",
                        "Delete":""
                    }
                }, 
                
              {  
                "data":{  
                    "name":"Reason Codes",
                    "Disable":"",
                    "Edit":"",
                    "Delete":""
                }
            }, 
            {  
              "data":{  
                  "name":"Carpet and Rug Institute",
                  "Disable":"",
                  "Edit":"",
                  "Delete":""
              }
          }, 
          {  
            "data":{  
                "name":"Request POD",
                "Disable":"",
                "Edit":"",
                "Delete":""
            },"children":[  
              {  
                  "data":{  
                    "name":"Residential",
                    "Disable":"",
                    "Edit":"",
                    "Delete":""
                  },
                
              }
            ]
        }, 
        {  
          "data":{  
              "name":"Sample Size Requirements",
              "Disable":"",
              "Edit":"",
              "Delete":""
          }
      }, 
      {  
        "data":{  
            "name":"Product Data Management System",
            "Disable":"",
            "Edit":"",
            "Delete":""
        }
    }, 
    {  
      "data":{  
          "name":"Claim Letters",
          "Disable":"",
          "Edit":"",
          "Delete":""
      }
  }, 
  {  
    "data":{  
        "name":"TRAC",
        "Disable":"",
        "Edit":"",
        "Delete":""
    }
}, 
{  
  "data":{  
      "name":"Quickship Commercial Styles",
      "Disable":"",
      "Edit":"",
      "Delete":""
  }
}, 
{  
  "data":{  
      "name":"Quickship Patcraft Styles",
      "Disable":"",
      "Edit":"",
      "Delete":""
  }
},  
{  
  "data":{  
      "name":"Stop Shipment",
      "Disable":"",
      "Edit":"",
      "Delete":""
  }
},              
            ],
            
        },       
    ]

// return [
//     {
//         "createdByUserId": null,
//         "modifiedByUserId": null,
//         "resourcesMenuId": 1,
//         "menuName": "Residential",
//         "linkUrl": "",
//         "parentMenuId": 0,
//         "subMenu": [
//             {
//                 "createdByUserId": null,
//                 "modifiedByUserId": null,
//                 "resourcesMenuId": 14,
//                 "menuName": "Shawfloors.com",
//                 "linkUrl": "http://www.shawfloors.com",
//                 "parentMenuId": 1,
//                 "subMenu": null
//             },
//             {
//                 "createdByUserId": null,
//                 "modifiedByUserId": null,
//                 "resourcesMenuId": 15,
//                 "menuName": "Shaw Flooring Solutions",
//                 "linkUrl": "https://shawflooringsolutions.com",
//                 "parentMenuId": 1,
//                 "subMenu": null
//             },
//             {
//                 "createdByUserId": null,
//                 "modifiedByUserId": null,
//                 "resourcesMenuId": 35,
//                 "menuName": "Test",
//                 "linkUrl": "test@google.com",
//                 "parentMenuId": 1,
//                 "subMenu": null
//             }
//         ]
//     }
//     ]
}



// getFileSystemNodesData() {
//   return [
//       {
          
      
//           data: {
//               name: 'Documents',
//               size: '75kb',
//               type: 'Folder'
//           },
//           children: [
//               {
//                   data: {
//                       name: 'Work',
//                       size: '55kb',
//                       type: 'Folder'
//                   },
//                   children: [
//                       {
//                           data: {
//                               name: 'Expenses.doc',
//                               size: '30kb',
//                               type: 'Document'
//                           }
//                       },
//                       {
//                           data: {
//                               name: 'Resume.doc',
//                               size: '25kb',
//                               type: 'Resume'
//                           }
//                       }
//                   ]
//               },
//               {
//                   data: {
//                       name: 'Home',
//                       size: '20kb',
//                       type: 'Folder'
//                   },
//                   children: [
//                       {
//                           data: {
//                               name: 'Invoices',
//                               size: '20kb',
//                               type: 'Text'
//                           }
//                       }
//                   ]
//               }
//           ]
//       }
//   ];
// }

getFilesystem() {
    return Promise.resolve(this.getFileSystemNodesData());
}

    
};
