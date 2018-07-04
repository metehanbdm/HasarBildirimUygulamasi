package com.example.rsyazilim.rs_ihbar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rsyazilim.rs_ihbar.MainActivity;
import com.example.rsyazilim.rs_ihbar.model.DamageRecordInfo;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailHelper  extends  AsyncTask<Void , Void, Void>{
    Activity activity;
    DamageRecordInfo damageRecordInfo;

    public MailHelper(Activity activity , DamageRecordInfo damageRecordInfo)
    {
        this.activity = activity;
        this.damageRecordInfo = damageRecordInfo;
    }



    @Override
    protected Void doInBackground(Void... voids) {
        sendToMail();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        activity.finish();
        Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
        activity.startActivity(intent);

    }



    public void sendToMail() {


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(ApiVariables.GMAIL_MAILSENDER_EMAIL, ApiVariables.GMAIL_MAILSENDER_PASSWORD);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ApiVariables.GMAIL_MAILSENDER_EMAIL)); //gönderen mail
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(ApiVariables.GMAIL_MAILRECEIVER_EMAIL)); //alıcı mail
            message.setSubject(" RS Servis Destek Talebi: " + damageRecordInfo.getPhone());
            StringBuilder builder = new StringBuilder("Android RS Destek Uygulaması Üzerinden  Açılan Destek Talebi<br />");
            builder.append("Kullancı Adı: Metehan Badem<br />");
            builder.append("Destek Açıklaması: ").append(damageRecordInfo.getInformation()).append("<br /><br />");

            //https://www.google.com/maps/search/?api=1&query=36.26577,-92.54324
            builder.append("https://www.google.com/maps/search/?api=1&query=")
                    .append(damageRecordInfo.getLat())
                    .append(",")
                    .append(damageRecordInfo.getLng());
            message.setText(builder.toString() ,"text/html","utf-8");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(builder.toString(), "text/html; charset=utf-8");


            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart , 0);


            if (damageRecordInfo.getImage1() !=null && !damageRecordInfo.getImage1().isEmpty()) {
                MimeBodyPart messageBodyPart1 = new MimeBodyPart();
                DataSource source = new FileDataSource(CommonUtils.getRealPathFromURI(activity , Uri.parse(damageRecordInfo.getImage1())));
                messageBodyPart1.setDataHandler(new DataHandler(source));
                messageBodyPart1.setFileName("1.FOTOGRAF");
                multipart.addBodyPart(messageBodyPart1 , 1);
            }

            if (damageRecordInfo.getImage2() !=null && !damageRecordInfo.getImage2().isEmpty()) {
                MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                DataSource source = new FileDataSource(CommonUtils.getRealPathFromURI(activity , Uri.parse(damageRecordInfo.getImage2())));
                messageBodyPart2.setDataHandler(new DataHandler(source));
                messageBodyPart2.setFileName("2.FOTOGRAF");
                multipart.addBodyPart(messageBodyPart2 ,2);
            }

            if (damageRecordInfo.getImage3() !=null && !damageRecordInfo.getImage3().isEmpty()) {
                MimeBodyPart messageBodyPart3 = new MimeBodyPart();
                DataSource source = new FileDataSource(CommonUtils.getRealPathFromURI(activity , Uri.parse(damageRecordInfo.getImage3())));
                messageBodyPart3.setDataHandler(new DataHandler(source));
                messageBodyPart3.setFileName("3.FOTOGRAF");
                multipart.addBodyPart(messageBodyPart3 , 3);
            }

            if (damageRecordInfo.getImage4() !=null && !damageRecordInfo.getImage4().isEmpty()) {
                MimeBodyPart messageBodyPart4 = new MimeBodyPart();
                DataSource source = new FileDataSource(CommonUtils.getRealPathFromURI(activity , Uri.parse(damageRecordInfo.getImage4())));
                messageBodyPart4.setDataHandler(new DataHandler(source));
                messageBodyPart4.setFileName("4.FOTOGRAF");
                multipart.addBodyPart(messageBodyPart4 ,4);
            }

            message.setContent(multipart);

            Transport.send(message);
            damageRecordInfo = new DamageRecordInfo();

            System.out.println("Mail Gonderildi");

        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(activity, "Mail Gonderilirken Hata Olustu", Toast.LENGTH_SHORT).show();
        }
    }


}
