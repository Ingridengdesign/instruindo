package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    public void enviarEmailSimples(String para, String assunto, String corpo) {

        String senhaUsada = environment.getProperty("spring.mail.password");
        System.out.println("\n[DEBUG EmailService] A TENTAR ENVIAR EMAIL.");
        System.out.println("[DEBUG EmailService] Senha Sendo Usada: " + senhaUsada);

        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();

            mensagem.setFrom(environment.getProperty("spring.mail.username"));
            mensagem.setTo(para);
            mensagem.setSubject(assunto);
            mensagem.setText(corpo);

            mailSender.send(mensagem);
            System.out.println("[DEBUG EmailService] Email enviado com sucesso para: " + para);
        } catch (Exception e) {
            System.err
                    .println("[DEBUG EmailService] Erro ao enviar email para: " + para + " | Erro: " + e.getMessage());
        }
    }
}