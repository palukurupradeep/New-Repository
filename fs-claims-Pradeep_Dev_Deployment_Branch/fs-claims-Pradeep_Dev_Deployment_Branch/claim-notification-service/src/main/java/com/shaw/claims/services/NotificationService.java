package com.shaw.claims.services;

import com.shaw.claims.dto.SendEmailDTO;

public interface NotificationService {
    void sendEmail();
    void sendEmails();
    void sendEmailAttachment();
	String sentEmail(SendEmailDTO sendEmailDTO);
}
