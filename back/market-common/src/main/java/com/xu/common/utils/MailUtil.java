package com.xu.common.utils;

import com.xu.common.pojo.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * @author AITIAN
 */
@Component
public class MailUtil {

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private MailProperties mailProperties;

    public void sendSimpleMail(String toEmail, String subject, String text) {
        subject = "".equals(subject) ? "测试邮件" : subject;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getFrom());
        mailMessage.setTo(toEmail);

        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }

    public void sendHtmlMail(String toEmail , String subject, String text, Map<String, String> attachmentMap) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        subject = "".equals(subject) ? "测试邮件" : subject;
        //是否发送的邮件是富文本（附件，图片，html等）
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setFrom(mailProperties.getFrom());
        messageHelper.setTo(toEmail);

        messageHelper.setSubject(subject);
        //重点，默认为false，显示原始html代码，无效果
        messageHelper.setText("<h1>测试邮件内容</h1>", true);

        if(attachmentMap != null){
            attachmentMap.forEach((key, value) -> {
                try {
                    File file = new File(value);
                    if (file.exists()) {
                        messageHelper.addAttachment(key, new FileSystemResource(file));
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
        }

        javaMailSender.send(mimeMessage);
    }

}
