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

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String email, String token) {

        String resetUrl = frontendUrl
                + "/reset-password?token="
                + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Recuperación de contraseña - Donar+");

        message.setText("""
                Hola,

                Recibimos una solicitud para restablecer tu contraseña de Donar+.

                Para establecer una nueva contraseña, ingresá al siguiente enlace:

                %s

                Este enlace tiene una validez de 30 minutos.

                Si no solicitaste este cambio, podés ignorar este correo.

                Saludos,
                El equipo de Donar+
                """.formatted(resetUrl));

        mailSender.send(message);
    }
}