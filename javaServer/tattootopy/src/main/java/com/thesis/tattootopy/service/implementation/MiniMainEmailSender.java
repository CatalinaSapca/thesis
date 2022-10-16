package com.thesis.tattootopy.service.implementation;

import javax.mail.MessagingException;
import java.io.IOException;

public class MiniMainEmailSender {
    public static void main(String[] args) throws MessagingException, IOException {
        MailSenderHelper mailSenderHelper = MailSenderHelper.getMailSenderHelperInstance();
        mailSenderHelper.sendmail("sapca.catalina@gmail.com", "/home/sapca/PycharmProjects/tattootoy_GAN/steps/AFantasyWorld.png");
    }
}
