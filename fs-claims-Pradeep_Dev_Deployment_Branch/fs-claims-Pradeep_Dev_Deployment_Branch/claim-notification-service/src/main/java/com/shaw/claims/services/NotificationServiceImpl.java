package com.shaw.claims.services;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailAttachment;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import com.shaw.claims.dto.SendEmailDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Value("${azure.email.endpoint}")
    private String endpoint;
    @Value("${azure.email.key.credentials}")
    private String credentials;
    @Value("${azure.email.senderaddress}")
    private String senderAddress;

    @Override
    public void sendEmail() {
        String connectionString = "endpoint=\n" +
                "https://sz-ue2-claimworx-d-acs-01.unitedstates.communication.azure.com/;accesskey=onY5gSoZx2RStlWxBY1snGCk3zvCrmqUPQd3O4EkuqMs8d7E4LVzee8v6C1XWk1nYtnDzqOmobiAd3HTP3vZkg==";
        EmailClient emailClient = new EmailClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        EmailMessage message = new EmailMessage()
                .setSenderAddress("DoNotReply@claims.shawinc.com")
                .setToRecipients("test945@gmail.com")
                .setSubject("Test email from Azure Service")
                .setBodyPlainText("Email Successfully send from Azure Communication Service!!!!");

        SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(message);
        PollResponse<EmailSendResult> response = poller.waitForCompletion();

        System.out.println("Operation Id: " + response.getValue().getId());

    }

    @Override
    public void sendEmails() {
        AzureKeyCredential azureKeyCredential = new AzureKeyCredential(credentials);
        EmailClient emailClient = new EmailClientBuilder()
                .endpoint(endpoint)
                .credential(azureKeyCredential)
                .buildClient();
        EmailMessage message = new EmailMessage()
                .setSenderAddress(senderAddress)
                .setToRecipients("test945@gmail.com")
                .setCcRecipients("test549@gmail.com")
                .setSubject("Test email from Azure Service")
                .setBodyPlainText("Email Successfully send from Azure Communication Service!!!!");
        SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(message);
        PollResponse<EmailSendResult> response = poller.waitForCompletion();
        System.out.println("Operation Id: " + response.getValue().getId());
    }

    @Override
    public void sendEmailAttachment() {
        AzureKeyCredential azureKeyCredential = new AzureKeyCredential(credentials);
        EmailClient emailClient = new EmailClientBuilder()
                .endpoint(endpoint)
                .credential(azureKeyCredential)
                .buildClient();
        BinaryData attachmentContent = BinaryData.fromFile(new File("C:/Users/GuptaA/test.pdf").toPath());
        EmailAttachment attachment = new EmailAttachment(
                "test.pdf",
                "application/pdf",
                attachmentContent
        );
        EmailAddress toAddress = new EmailAddress("test945@gmail.com")
                .setDisplayName("Test");
        EmailMessage message = new EmailMessage()
                .setSenderAddress(senderAddress)
                .setToRecipients(toAddress)
                .setSubject("Test Subject")
                .setBodyPlainText("Attached Test file")
                .setAttachments(attachment);
        SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(message);
        PollResponse<EmailSendResult> response = poller.waitForCompletion();
        System.out.println("Operation Id: " + response.getValue().getId());
    }

	@Override
	public String sentEmail(SendEmailDTO sendEmailDTO) {
		 AzureKeyCredential azureKeyCredential = new AzureKeyCredential(credentials);
	        EmailClient emailClient = new EmailClientBuilder()
	                .endpoint(endpoint)
	                .credential(azureKeyCredential)
	                .buildClient();
	        EmailMessage message = new EmailMessage()
	                .setSenderAddress(senderAddress)
	                .setToRecipients(sendEmailDTO.getToRecipients())
	                .setCcRecipients(sendEmailDTO.getCcRecipients())
	                .setSubject(sendEmailDTO.getSubject())
	                .setBodyPlainText(sendEmailDTO.getBody());
	        SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(message);
	        PollResponse<EmailSendResult> response = poller.waitForCompletion();
	        System.out.println("Operation Id: " + response.getValue().getId());
		return response.getValue().getId();
	}
}
