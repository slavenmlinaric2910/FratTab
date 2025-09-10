package com.example.frattab.services.impl;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.frattab.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@Primary // ensure this wins if another EmailService exists
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Value("${app.mail.from}")
  private String from;

  @Value("${app.mail.replyTo}")
  private String replyTo;

  public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  @Override
  public void sendHtmlEmail(@NonNull String to,
      @NonNull String subject,
      @NonNull String templateName,
      @NonNull Map<String, Object> vars) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(
          mimeMessage,
          MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
          StandardCharsets.UTF_8.name());

      Context ctx = new Context();
      ctx.setVariables(vars);
      String html = templateEngine.process(templateName, ctx);

      helper.setFrom(from);
      helper.setTo(to);
      helper.setReplyTo(replyTo);
      helper.setSubject(subject);
      helper.setText(html, true);

      mailSender.send(mimeMessage);
    } catch (MessagingException ex) {
      throw new RuntimeException("Failed to send email to " + to, ex);
    }
  }
}
