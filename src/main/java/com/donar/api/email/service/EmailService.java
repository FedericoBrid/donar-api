package com.donar.api.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendPasswordResetEmail(String email, String token) {

        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Recuperación de contraseña - Donar+");

        message.setText(
                "Hola,\n\n" +
                        "Recibimos una solicitud para restablecer tu contraseña.\n\n" +
                        "Para continuar, ingresá al siguiente enlace:\n" +
                        resetUrl + "\n\n" +
                        "Este enlace vence en 30 minutos y solo puede utilizarse una vez.\n\n" +
                        "Si no solicitaste este cambio, podés ignorar este correo.\n\n" +
                        "Equipo Donar+"
        );

        mailSender.send(message);
    }

    @Value("${app.frontend.url}")
    private String frontendUrl;
}