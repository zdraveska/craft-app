//package mk.ukim.finki.draftcraft.rest;
//
//import mk.ukim.finki.draftcraft.domain.enumerations.EmailType;
//import mk.ukim.finki.draftcraft.dto.EmailDto;
//import mk.ukim.finki.draftcraft.service.impl.EmailServiceImpl;
//import mk.ukim.finki.draftcraft.utils.BaseIntegrationTest;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.io.IOException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class EmailServiceIT extends BaseIntegrationTest {
//
//  @Autowired
//  private EmailServiceImpl emailService;
//
//  @Test
//  @WithMockUser
//  public void sendEmailTest() throws MessagingException, IOException {
//
//    // given
//    EmailType type = EmailType.CONFIRM_EMAIL_ADDRESS;
//    EmailDto emailDto = generateEmail(type);
//
//    // when
//    emailService.sendSimpleEmail(emailDto, type);
//
//    // then
//    MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
//    Assertions.assertEquals(1, receivedMessages.length);
//
//    MimeMessage current = receivedMessages[0];
//
//    Assertions.assertEquals(emailDto.getSubject(), current.getSubject());
//    Assertions.assertEquals(emailDto.getTo(), current.getAllRecipients()[0].toString());
//    String actualBody = current.getContent().toString().trim().replace("\r\n", "\n");
//    assertThat(actualBody).contains(emailDto.getSubject());
//    assertThat(actualBody).contains(emailDto.getToSurname());
//    assertThat(actualBody).contains(emailDto.getToName());
//    assertThat(actualBody).contains(emailDto.getUrl());
//    assertThat(actualBody).contains(emailDto.getBody());
//    assertThat(actualBody).contains(type.getButton().toString());
//
//  }
//
//}
