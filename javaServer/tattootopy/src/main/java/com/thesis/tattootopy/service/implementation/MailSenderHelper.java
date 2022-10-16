package com.thesis.tattootopy.service.implementation;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class MailSenderHelper {

    private static MailSenderHelper mailSenderHelper = null;

    private MailSenderHelper() {
    }

    public static MailSenderHelper getMailSenderHelperInstance() {
        if (mailSenderHelper == null) {
            synchronized (MailSenderHelper.class) {
                if (mailSenderHelper == null) {
                    // if the instance is null, then we initialize it
                    mailSenderHelper = new MailSenderHelper();
                }
            }
        }
        return mailSenderHelper;
    }

    synchronized public void sendmail(String emailAddress, String path) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("tattootopy@gmail.com", "password123,,,");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("tattootopy@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress));
        msg.setSubject("Your tattOOtopy artwork delivery");
        msg.setContent("Check out this masterpiece... Your tattoo design is fresh out of the over. \n Let us know what you're thinking about if and maybe..." +
                "\n send us a picture with the final result!" +
                "\n\n XoX, tattOOtopy Team", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "    <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>\n" +
                "    <meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\"/>\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Abril+Fatface\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Ubuntu\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Cabin\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Nunito\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "    <style>\n" +
                "        * {\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        a[x-apple-data-detectors] {\n" +
                "            color: inherit !important;\n" +
                "            text-decoration: inherit !important;\n" +
                "        }\n" +
                "\n" +
                "        #MessageViewBody a {\n" +
                "            color: inherit;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            line-height: inherit\n" +
                "        }\n" +
                "\n" +
                "        @media (max-width:520px) {\n" +
                "            .icons-inner {\n" +
                "                text-align: center;\n" +
                "            }\n" +
                "\n" +
                "            .icons-inner td {\n" +
                "                margin: 0 auto;\n" +
                "            }\n" +
                "\n" +
                "            .row-content {\n" +
                "                width: 100% !important;\n" +
                "            }\n" +
                "\n" +
                "            .column .border {\n" +
                "                display: none;\n" +
                "            }\n" +
                "\n" +
                "            table {\n" +
                "                table-layout: fixed !important;\n" +
                "            }\n" +
                "\n" +
                "            .stack .column {\n" +
                "                width: 100%;\n" +
                "                display: block;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; background-color: #0d1518; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #0d1518;\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-1\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #0d1518;); background-position: top center; background-repeat: no-repeat;\" width=\"100%\">\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 500px;\" width=\"500\">\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <td class=\"column column-1\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\">\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"image_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"width:100%;padding-right:0px;padding-left:0px;padding-top:50px;\">\n" +
                "                                                <div align=\"center\" style=\"line-height:10px\"><img alt=\"logo\" src=\"https://i.ibb.co/SPkbd6Q/logo-Transparent.png\" style=\"display: block; height: auto; border: 0; width: s00px; max-width: 100%;\" title=\"logo\" width=\"200\"/></div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"padding-top:45px;\">\n" +
                "                                                <div style=\"font-family: Arial, sans-serif\">\n" +
                "                                                    <div class=\"txtTinyMce-wrapper\" style=\"font-size: 14px; font-family: 'Abril Fatface', Arial, 'Helvetica Neue', Helvetica, sans-serif; mso-line-height-alt: 16.8px; color: #bf95d2; line-height: 1.2;\">\n" +
                "                                                        <p style=\"margin: 0; font-size: 14px; text-align: center;\"><span style=\"font-size:80px;\"><span style=\"\">tattOOtopy</span></span></p>\n" +
                "                                                    </div>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"image_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"width:100%;padding-right:0px;padding-left:0px;padding-top:45px;\">\n" +
                "                                                <div align=\"center\" style=\"line-height:10px\"><img alt=\"GIF\" src=\"https://media.giphy.com/media/jtthR7ju45fkByptkU/giphy.gif\" style=\"display: block; height: auto; border: 0; width: 375px; max-width: 100%;\" title=\"GIF\" width=\"375\"/></div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"padding-bottom:5px;padding-top:30px;\">\n" +
                "                                                <div style=\"font-family: Arial, sans-serif\">\n" +
                "                                                    <div class=\"txtTinyMce-wrapper\" style=\"font-size: 14px; font-family: 'Abril Fatface', Arial, 'Helvetica Neue', Helvetica, sans-serif; mso-line-height-alt: 16.8px; color: #ffffff; line-height: 1.2;\">\n" +
                "                                                        <p style=\"margin: 0; font-size: 14px; text-align: center;\"><span style=\"font-size:42px;\"><span style=\"\">Here <span style=\"color:#bf95d2;\">are</span> <span style=\"color:#ffcf02;\">the results</span></span></span></p>\n" +
                "                                                    </div>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"padding-bottom:20px;padding-left:60px;padding-right:60px;padding-top:20px;\">\n" +
                "                                                <div style=\"font-family: sans-serif\">\n" +
                "                                                    <div class=\"txtTinyMce-wrapper\" style=\"font-size: 14px; font-family: Arial, Helvetica Neue, Helvetica, sans-serif; mso-line-height-alt: 28px; color: #ffffff; line-height: 2;\">\n" +
                "                                                        <p style=\"margin: 0; font-size: 14px; text-align: justify;\">Hi inked buddy,<br/><br/>Here is your long-awaited artwork... We put our Generatve Adversial Network to work and here you have your new tattoo design.<br/>Let us know what you're thinking!<br/><br/>XoX,<br/>tattOOtpy Team</p>\n" +
                "                                                    </div>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-2\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #0d1518; background-image: url('https://i.ibb.co/Bnf0j61/stelle1.png'); background-repeat: repeat;\" width=\"100%\">\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 500px;\" width=\"500\">\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <td class=\"column column-1\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"25%\">\n" +
                "                                    <div class=\"spacer_block\" style=\"height:80px;line-height:0px;font-size:1px;\"> </div>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-3\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 500px;\" width=\"500\">\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <td class=\"column column-1\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\">\n" +
                "                                    <div class=\"spacer_block\" style=\"height:60px;line-height:60px;font-size:1px;\"> </div>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-4\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 500px;\" width=\"500\">\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <td class=\"column column-3\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"25%\">\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"button_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"padding-bottom:15px;padding-left:10px;padding-right:10px;padding-top:15px;text-align:center;\">\n" +
                "                                                <div align=\"center\">\n" +
                "                                                    <a href=\"https://www.example.com/\" style=\"text-decoration:none;display:inline-block;color:#ffd804;background-color:#0d1518;border-radius:2px;width:auto;border-top:1px solid #0d1518;font-weight:400;border-right:1px solid #0d1518;border-bottom:1px solid #0d1518;border-left:1px solid #0d1518;padding-top:0px;padding-bottom:0px;font-family:Arial, Helvetica Neue, Helvetica, sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all;\" target=\"_blank\"><span style=\"padding-left:20px;padding-right:20px;font-size:12px;display:inline-block;letter-spacing:2px;\"><span style=\"font-size: 16px; line-height: 1.2; word-break: break-word; mso-line-height-alt: 19px;\"><span data-mce-style=\"font-size: 12px; line-height: 14px;\" style=\"font-size: 12px; line-height: 14px;\">GO TO<br/>PROFILE</span></span></span></a>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                                <td class=\"column column-4\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"25%\">\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"button_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"padding-bottom:15px;padding-left:10px;padding-right:10px;padding-top:15px;text-align:center;\">\n" +
                "                                                <div align=\"center\">\n" +
                "                                                    <a href=\"https://www.example.com/\" style=\"text-decoration:none;display:inline-block;color:#ffd804;background-color:#0d1518;border-radius:2px;width:auto;border-top:1px solid #0d1518;font-weight:undefined;border-right:1px solid #0d1518;border-bottom:1px solid #0d1518;border-left:1px solid #0d1518;padding-top:0px;padding-bottom:0px;font-family:Arial, Helvetica Neue, Helvetica, sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all;\" target=\"_blank\"><span style=\"padding-left:20px;padding-right:20px;font-size:12px;display:inline-block;letter-spacing:2px;\"><span style=\"font-size: 16px; line-height: 1.2; word-break: break-word; mso-line-height-alt: 19px;\"><span data-mce-style=\"font-size: 12px; line-height: 14px;\" style=\"font-size: 12px; line-height: 14px;\">OUR<br/>BEST ARTWORKS</span></span></span></a>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-5\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 500px;\" width=\"500\">\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <td class=\"column column-1\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 0px; padding-bottom: 0px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\">\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td>\n" +
                "                                                <div align=\"center\">\n" +
                "                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                                                        <tr>\n" +
                "                                                            <td class=\"divider_inner\" style=\"font-size: 1px; line-height: 1px; border-top: 2px dotted #D3A3E8;\"><span> </span></td>\n" +
                "                                                        </tr>\n" +
                "                                                    </table>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"social_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"padding-bottom:10px;padding-left:10px;padding-right:10px;padding-top:70px;text-align:center;\">\n" +
                "                                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"social-table\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"184px\">\n" +
                "                                                    <tr>\n" +
                "                                                        <td style=\"padding:0 7px 0 7px;\"><a href=\"https://instagram.com/\" target=\"_blank\"><img alt=\"Instagram\" height=\"32\" src=\"https://i.ibb.co/gj9bZHg/instalogo.png\" style=\"display: block; height: auto; border: 0;\" title=\"Instagram\" width=\"32\"/></a></td>\n" +
                "                                                    </tr>\n" +
                "                                                </table>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"padding-bottom:70px;padding-left:10px;padding-top:10px;\">\n" +
                "                                                <div style=\"font-family: Arial, sans-serif\">\n" +
                "                                                    <div class=\"txtTinyMce-wrapper\" style=\"font-size: 14px; font-family: 'Nunito', Arial, 'Helvetica Neue', Helvetica, sans-serif; mso-line-height-alt: 16.8px; color: #d3a3e8; line-height: 1.2;\">\n" +
                "                                                        <p style=\"margin: 0; font-size: 12px; text-align: center; letter-spacing: 2px;\"><span style=\"font-size:12px;\">© 2022 tattOOtopy| 13D Suceava, </span></p>\n" +
                "                                                        <p style=\"margin: 0; font-size: 12px; text-align: center; letter-spacing: 2px;\"><span style=\"font-size:12px;\">Romania, Cluj Napoca</span></p>\n" +
                "                                                    </div>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table><!-- End -->\n" +
                "</body>\n" +
                "</html>", "text/html");


        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        MimeBodyPart attachPart = new MimeBodyPart();

        attachPart.attachFile(path);
        multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }


}
