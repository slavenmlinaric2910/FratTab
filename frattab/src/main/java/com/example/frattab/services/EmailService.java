// src/main/java/com/example/frattab/services/EmailService.java
package com.example.frattab.services;

import java.util.Map;

public interface EmailService {

    /**
     * Send a plain‐text email.
     *
     * @param to      the recipient’s email address
     * @param subject the subject line
     * @param body    the plain‐text body
     */
    void sendPlainTextEmail(String to, String subject, String body);

    /**
     * Send an HTML email using a Thymeleaf template.
     *
     * @param to        the recipient’s email address
     * @param subject   the subject line
     * @param template  the name of the Thymeleaf template (e.g. "billing-email")
     * @param variables the model variables to pass into the template
     */
    void sendHtmlEmail(String to, String subject, String template, Map<String, Object> variables);
}
