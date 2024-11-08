package com.shaw.claims.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.shaw.claims.dto.DashboardWidgetsResponseDTO;
import com.shaw.claims.dto.GetClaimWatchlistDTO;
import com.shaw.claims.dto.UserDashboardWidgetDTO;
import com.shaw.claims.model.CustomerWatchlist;
import com.shaw.claims.services.DashBoardService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/dashboard/v1")
public class DashBoardController {
	Logger log = LogManager.getLogger(DashBoardController.class);
    @Autowired
    DashBoardService dashBoardService;
    @PostMapping("/addorUpdateCustomerWatchlist")
    public ResponseEntity<CustomerWatchlist> addorUpdateCustomerWatchlist(@RequestBody CustomerWatchlist customerWatchlist){
        log.info("Inside DashBoardController.addorUpdateCustomerWatchlist");
       
        return new ResponseEntity<CustomerWatchlist>(dashBoardService.addorUpdateCustomerWatchlist(customerWatchlist), HttpStatus.OK);
    }
    @PostMapping("/addUserDashboardWidgets")
    public ResponseEntity<Void> addUserDashboardWidgets(@RequestBody UserDashboardWidgetDTO userDashboardWidgetDTO)
	{
		log.info("Inside DashBoardController.addUserDashboardWidgets");
		dashBoardService.addUserDashboardWidgets(userDashboardWidgetDTO);
		return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/fetchUserDashboardWidgetsByUserId")
    public ResponseEntity<List<DashboardWidgetsResponseDTO>> fetchUserDashboardWidgetsByUserId(@RequestParam(name = "createdByUserId") Integer createdByUserId){
         log.info("Inside DashBoardController.fetchUserDashboardWidgetsByUserId");
        return new ResponseEntity<>(dashBoardService.fetchUserDashboardWidgetsByUserId(createdByUserId),HttpStatus.OK);
    }
    @GetMapping("/getCustomerWatchList")
    public ResponseEntity<List<GetClaimWatchlistDTO>> getCustomerWatchList(@RequestParam(name = "createdByUserId") Integer createdByUserId){
    	return new ResponseEntity<>(dashBoardService.getCustomerWatchList(createdByUserId),HttpStatus.OK);
    }
    @GetMapping("/getCustomerWatchListStatus")
    public ResponseEntity<Integer> getCustomerWatchListStatus(@RequestParam(name = "userId") int userId, @RequestParam(name = "customerNumber") String customerNumber){
        return new ResponseEntity<>(dashBoardService.getCustomerWatchListStatus(userId, customerNumber),HttpStatus.OK);
    }
    @GetMapping("/getClaimWatchList")
    public ResponseEntity<List<GetClaimWatchlistDTO>> getClaimWatchList(@RequestParam(name = "userId", required = false) int userId){
        log.info("Inside ClaimDetailsController.getClaimWatchList");
        return new ResponseEntity<>(dashBoardService.ClaimWatchList(userId),HttpStatus.OK);
    }
    @GetMapping("/overdueClaim")
    public ResponseEntity<List<GetClaimWatchlistDTO>> overdueClaim(@RequestParam(name = "userId", required = false) int userId) {
    	log.info("Inside DashBoardController.overdueClaim");
       
        return new ResponseEntity<>(dashBoardService.overDueClaim(userId),HttpStatus.OK);
    }
    @GetMapping("/claimsDue")
    public ResponseEntity<List<GetClaimWatchlistDTO>> claimsDue(@RequestParam(name = "userId", required = false) int userId) {
    	log.info("Inside DashBoardController.claimsDue");
    	 return new ResponseEntity<>(dashBoardService.claimsDue(userId),HttpStatus.OK);
    }

    @DeleteMapping("/deleteClaimWatchList")
    public ResponseEntity<Void> deleteClaimWatchList(@RequestParam(name = "userId") Integer userId,@RequestParam(name = "claimId") Integer claimId)
    {
    	log.info("Inside DashBoardController.deleteClaimWatchList");
    	dashBoardService.deleteClaimWatchList(userId,claimId);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteCustomerWatchList")
    public ResponseEntity<Void> deleteCustomerWatchList(@RequestParam(name = "userId") Integer userId,@RequestParam(name = "customerNumber") String customerNumber)
    {
    	log.info("Inside DashBoardController.deleteCustomerWatchList");
    	dashBoardService.deleteCustomerWatchList(userId,customerNumber);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
