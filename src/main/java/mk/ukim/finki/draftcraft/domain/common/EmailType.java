package mk.ukim.finki.draftcraft.domain.common;

public enum EmailType {
//TODO name of web app

  CREATE_USER("Welcome",
      "You are now an user of *", "Set your password"),
  CONFIRM_EMAIL_ADDRESS("Confirm email address!",
      "In order to become an user of *, you have to confirm your email address.", "Confirm"),
  RESET_PASSWORD("Reset your password!",
      "If you have forgotten or just want to change your password, click on the following button: ", "Reset your password"),
  ACCOUNT_REQUEST_DECLINED("Request declined!",
      "Your account request was declined.","");

  private final String subject;
  private final String body;
  private final String button;


  EmailType(String subject, String body, String button) {
    this.subject = subject;
    this.body = body;
    this.button = button;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public String getButton() {
    return button;
  }
}
