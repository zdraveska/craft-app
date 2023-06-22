//package mk.ukim.finki.draftcraft.rest;
//
//import lombok.SneakyThrows;
//import mk.ukim.finki.draftcraft.domain.enumeration.EmailType;
//import mk.ukim.finki.draftcraft.dto.EmailDto;
//import mk.ukim.finki.draftcraft.service.impl.EmailServiceImpl;
//import mk.ukim.finki.draftcraft.utils.BaseIntegrationTest;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//class EmailServiceIT extends BaseIntegrationTest {
//
//  @Autowired
//  private EmailServiceImpl emailService;
//
//  @Test
////  @WithMockUser
//  @SneakyThrows
////  @Disabled
//  public void sendEmailTest()  {
//
//    // given
//    EmailType type = EmailType.CONFIRM_EMAIL_ADDRESS;
//    EmailDto emailDto = generateEmail(type);
//
//    // when
//    emailService.sendSimpleEmail(emailDto, type);
//
//    // then
//    var receivedMessages = smtpServer.getReceivedMessages();
//    Assertions.assertEquals(1, receivedMessages.length);
//
//    var current = receivedMessages[0];
//
//    Assertions.assertEquals(emailDto.getSubject(), current.getSubject());
//    Assertions.assertEquals(emailDto.getTo(), current.getAllRecipients()[0].toString());
//    String actualBody = current.getContent().toString().trim().replace("\r\n", "\n");
//    assertThat(actualBody).contains(emailDto.getSubject());
//    assertThat(actualBody).contains(emailDto.getToSurname());
//    assertThat(actualBody).contains(emailDto.getToName());
//    assertThat(actualBody).contains(emailDto.getUrl());
//    assertThat(actualBody).contains(emailDto.getBody());
//    assertThat(actualBody).contains(type.getButton());
//    }
//}
