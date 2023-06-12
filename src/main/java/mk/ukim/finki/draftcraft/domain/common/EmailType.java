package mk.ukim.finki.draftcraft.domain.common;

public enum EmailType {

  CREATE_USER("Добредојдовте!",
      "Сега сте корисник на Craftsy e-shop апликацијата. За да можете да се најавите на вашиот профил треба да сетирате лозинка", "Креирај лозинка"),
  CONFIRM_EMAIL_ADDRESS("Потврди ја email адресата!",
      "За да станете корисник на Craftsy e-shop апликацијата, треба да ја потврдите вашата email адреса.", "Потврди"),
  RESET_PASSWORD("Ресетирај ја лозинката!",
      "Доколку ја имате заборавено вашата лозинка или сакате да ја промените, кликнете на следното копче: ", "Ресетирај лозинка");

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
