package com.shaw.claims.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shaw.claims.dto.ResourceMenuDTO;
import com.shaw.claims.services.ResourceMenuService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/menu/v1")
public class ResourceMenuController {
	Logger log = LogManager.getLogger(ResourceMenuController.class);
    @Autowired
    ResourceMenuService resourceMenuService;

    @GetMapping("/fetchResourceMenus")
    @Operation(description = "<h2><b>Possible FormName are : resourceMenu, adminResourceMenu.</h2></b>")
    public ResponseEntity<List<ResourceMenuDTO>> fetchResourceMenus(@RequestParam(name = "formName") String formName){
        log.info("Inside ResourceMenuController.fetchParentResourceMenus");
        return new ResponseEntity<>(resourceMenuService.fetchResourceMenus(formName), HttpStatus.OK);
    }
    
    @PostMapping("/addResourceMenu")
    public ResponseEntity<ResourceMenuDTO> addResourceMenu(@RequestBody ResourceMenuDTO resourceMenuDTO) {
        log.info("Inside ResourceMenuController.addResourceMenu requestBody :: {}", resourceMenuDTO);
        ResourceMenuDTO resourceMenuResponseDTO = resourceMenuService.addResourceMenu(resourceMenuDTO);
        return new ResponseEntity<>(resourceMenuResponseDTO,HttpStatus.OK);
    }
    
    @PutMapping("/updateResourceMenu")
    public ResponseEntity<ResourceMenuDTO> updateResourceMenu(@RequestBody ResourceMenuDTO resourceMenuDTO) {
        log.info("Inside ResourceMenuController.updateResourceMenu requestBody :: {}", resourceMenuDTO);
        ResourceMenuDTO resourceMenuResponseDTO = resourceMenuService.updateResourceMenu(resourceMenuDTO);
        return new ResponseEntity<>(resourceMenuResponseDTO,HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteResourceMenu")
	public ResponseEntity<Void> deleteResourceMenu(@RequestParam(name = "resourceMenuId") Integer resourceMenuId) {
		log.info("Inside ResourceMenuController.deleteResourceMenu");
		resourceMenuService.deleteResourceMenu(resourceMenuId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

    @PutMapping("/enableordisableMenu")
    public ResponseEntity<Void> enableordisableMenu(@RequestParam(name = "resourcesMenuId") 
    int resourcesMenuId,@RequestParam(name = "statusId") int statusId)
    {
    	log.info("Inside ResourceMenuController.enableordisableMenu");
    	resourceMenuService.enableordisableMenu(resourcesMenuId, statusId);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
