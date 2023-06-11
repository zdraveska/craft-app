package mk.ukim.finki.draftcraft.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.draftcraft.domain.common.EmailType;
import mk.ukim.finki.draftcraft.domain.exceptions.EmailException;
import mk.ukim.finki.draftcraft.dto.EmailDto;
import mk.ukim.finki.draftcraft.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;


@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;
  private final String sender;
  private final ITemplateEngine templateEngine;

  public EmailServiceImpl(JavaMailSender javaMailSender,
      @Value("${spring.mail.username}") String sender, ITemplateEngine templateEngine) {
    this.javaMailSender = javaMailSender;
    this.sender = sender;
    this.templateEngine = templateEngine;
  }

  @Override
  public void sendSimpleEmail(EmailDto emailDto, EmailType type) {
    Context context = new Context();
    context.setVariable("type", type);
    context.setVariable("subject",emailDto.getSubject());
    context.setVariable("userName", emailDto.getToName());
    context.setVariable("userSurname", emailDto.getToSurname());
    context.setVariable("url", emailDto.getUrl());
    context.setVariable("message", emailDto.getBody());

    String process = templateEngine.process("email", context);
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
    try {
      helper.setTo(emailDto.getTo());
      helper.setFrom(sender);
      helper.setSubject(emailDto.getSubject());
      helper.setText(process, true);
    } catch (MessagingException e) {
      log.error("Error sending email");
      throw new EmailException(e.getMessage());
    }

    log.debug("Sending email");
    javaMailSender.send(mimeMessage);
  }

}