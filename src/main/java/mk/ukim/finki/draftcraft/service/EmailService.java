package mk.ukim.finki.draftcraft.service;


import mk.ukim.finki.draftcraft.domain.enumeration.EmailType;
import mk.ukim.finki.draftcraft.dto.EmailDto;

public interface EmailService {

  void sendSimpleEmail(EmailDto emailDto, EmailType type);

}