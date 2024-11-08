package com.shaw.claims.dto;

import lombok.Data;

@Data
public class SendEmailDTO {
private String[] toRecipients;
private String[] ccRecipients;
private String subject;
private String body;
private Byte[] attachment;
}
