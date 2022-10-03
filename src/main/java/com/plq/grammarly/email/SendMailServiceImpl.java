package com.plq.grammarly.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

@Slf4j
@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMail(MailRequest mailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequest.getSendTo().split(","));
        message.setSubject(mailRequest.getSubject());
        message.setText(mailRequest.getContent());
        message.setSentDate(new Date());
        javaMailSender.send(message);
        log.info("发送邮件->{}成功", mailRequest.getSendTo());
    }

    @Override
    public void sendHtmlMail(MailRequest mailRequest) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //邮件收件人 1或多个
            helper.setTo(mailRequest.getSendTo().split(","));
            //邮件主题
            helper.setSubject(mailRequest.getSubject());
            //邮件内容
            helper.setText(mailRequest.getContent(), true);
            //邮件发送时间
            helper.setSentDate(new Date());
            if (mailRequest.getBcc() != null) {
                helper.setBcc(mailRequest.getBcc().split(","));
            }
            if (mailRequest.getFilePaths() != null && !mailRequest.getFilePaths().isEmpty()) {
                for (String filePath : mailRequest.getFilePaths()) {
                    FileSystemResource file = new FileSystemResource(new File(filePath));
                    String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
                    helper.addAttachment(fileName,file);
                }
            }
            javaMailSender.send(message);
            log.info("发送邮件->{}成功", mailRequest.getSendTo());
        } catch (Exception e) {
            log.error("发送邮件时发生异常!", e);
        }
    }
}
