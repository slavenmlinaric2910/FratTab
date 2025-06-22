// src/main/java/com/example/frattab/services/impl/SendGridEmailService.java
package com.example.frattab.services.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.frattab.services.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class SendGridEmailService implements EmailService {

    private final SendGrid sendGridClient;

    // ← inject the _already auto-configured_ SpringTemplateEngine
    private final SpringTemplateEngine templateEngine;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    @Override
    public void sendPlainTextEmail(String to, String subject, String body) {
        Mail mail = new Mail();
        mail.setFrom(new Email(fromEmail));
        mail.setSubject(subject);

        Personalization personalization = new Personalization();
        personalization.addTo(new Email(to));
        mail.addPersonalization(personalization);

        mail.addContent(new Content("text/plain", body));

        executeSend(mail);
    }

    @Override
    public void sendHtmlEmail(String to,
                              String subject,
                              String templateName,
                              Map<String, Object> variables) {

        // 1) Create a Thymeleaf context and set your model variables
        Context ctx = new Context();
        ctx.setVariables(variables);

        // 2) Let Spring’s auto-configured engine locate your .html under /templates/
        String htmlBody = templateEngine.process(templateName, ctx);

        // 3) Build the SendGrid Mail payload
        Mail mail = new Mail();
        mail.setFrom(new Email(fromEmail));
        mail.setSubject(subject);

        Personalization personalization = new Personalization();
        personalization.addTo(new Email(to));
        mail.addPersonalization(personalization);

        mail.addContent(new Content("text/html", htmlBody));

        executeSend(mail);
    }

    private void executeSend(Mail mail) {
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGridClient.api(request);
            int code = response.getStatusCode();
            if (code < 200 || code >= 300) {
                throw new RuntimeException("SendGrid API error: " + code + " / " + response.getBody());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to send email via SendGrid", ex);
        }
    }
}
