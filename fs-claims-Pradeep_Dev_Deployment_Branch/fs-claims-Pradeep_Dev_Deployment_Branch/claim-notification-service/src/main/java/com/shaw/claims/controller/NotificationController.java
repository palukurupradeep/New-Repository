package com.shaw.claims.controller;

import com.shaw.claims.dto.SendEmailDTO;
import com.shaw.claims.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification/v1")
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @GetMapping("/sendEmail")
    public ResponseEntity<Void> sendEmail(){
        notificationService.sendEmails();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    @PostMapping("/sendEmailNotification")
    public ResponseEntity<String> sentEmail(@RequestBody SendEmailDTO sendEmailDTO){
       
        return new ResponseEntity<>( notificationService.sentEmail(sendEmailDTO),HttpStatus.OK);
    }
}
