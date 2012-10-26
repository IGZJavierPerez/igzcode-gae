package com.igzcode.java.gae.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Utility to send mails using the javax.mail API in GAE.
 */
public class MailUtil {
	
	/**
	 * Send a text email.
	 * 
	 * @param p_from
	 * @param p_to
	 * @param p_cc
	 * @param p_bcc
	 * @param p_replyTo
	 * @param p_subject
	 * @param p_body
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	static public void sendTextMail(
			String[] p_from
			, String[][] p_to
			, String[][] p_cc
			, String[][] p_bcc
			, String[][] p_replyTo
			, String p_subject
			, String p_body ) throws UnsupportedEncodingException, MessagingException{
		
		sendMail(
				p_from
				,p_to
				,p_cc
				,p_bcc
				,p_replyTo
				,p_subject
				,false
				,p_body );
	}
	
	/**
	 * Send a html email.
	 * 
	 * @param p_from
	 * @param p_to
	 * @param p_cc
	 * @param p_bcc
	 * @param p_replyTo
	 * @param p_subject
	 * @param p_body
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	static public void sendHtmlMail(
			String[] p_from
			, String[][] p_to
			, String[][] p_cc
			, String[][] p_bcc
			, String[][] p_replyTo
			, String p_subject
			, String p_body ) throws UnsupportedEncodingException, MessagingException{
		
		sendMail(
				p_from
				,p_to
				,p_cc
				,p_bcc
				,p_replyTo
				,p_subject
				,true
				,p_body );
	}
	
	static private void sendMail(
			  String[] p_from
			, String[][] p_to
			, String[][] p_cc
			, String[][] p_bcc
			, String[][] p_replyTo
			, String p_subject
			, Boolean p_isHtml
			, String p_body ) throws UnsupportedEncodingException, MessagingException{
		
	    	Properties props = new Properties();
	    	Session session = Session.getDefaultInstance(props, null);
	    	
            MimeMessage msg = new MimeMessage(session);
            String encoding = "UTF-8"; 
            msg.setHeader("Content-Type", "text/html; charset="+encoding);
            
            //Set SUBJECT
            msg.setSubject(p_subject,encoding);
            
            //Set BODY
            if(!p_isHtml){
            	msg.setText(p_body);
            }else{
            	Multipart mp = new MimeMultipart();
            	MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(p_body, "text/html");
                mp.addBodyPart(htmlPart);
                msg.setContent(mp);
            }
            
            //Set FROM
            if(p_from!=null){
            	if(p_from.length==1){
            		msg.setFrom(new InternetAddress(p_from[0]));
            	}else if(p_from.length==2){
            		msg.setFrom(new InternetAddress(p_from[0], p_from[1]));
            	}
            }
            
            //Set TO
            if(p_to!=null){
            	for(int i=0;i<p_to.length;i++){
            		for(int j=0; j<p_to[i].length;j=j+2){
            			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(p_to[i][j], p_to[i][j+1]));
            		}
            	}
            }
            
            //Set CC
            if(p_cc!=null){
            	for(int i=0;i<p_cc.length;i++){
            		for(int j=0; j<p_cc[i].length;j=j+2){
            			msg.addRecipient(Message.RecipientType.CC,new InternetAddress(p_cc[i][j], p_cc[i][j+1]));
            		}
            	}
            }
            
            //Set BCC
            if(p_bcc!=null){
            	for(int i=0;i<p_bcc.length;i++){
            		for(int j=0; j<p_bcc[i].length;j=j+2){
            			msg.addRecipient(Message.RecipientType.BCC,new InternetAddress(p_bcc[i][j], p_bcc[i][j+1]));
            		}
            	}
            }
            
            //Set REPLY-TO 
            if(p_replyTo!=null){
            	Address[] replyToAddresses = new Address[p_replyTo.length];
            	for(int i=0;i<p_replyTo.length;i++){
            		String email = p_replyTo[i][0];
            		String name = p_replyTo[i][1];
            		Address address =  new InternetAddress(email,name);
            		replyToAddresses[i] = address;
            	}
            	msg.setReplyTo(replyToAddresses);            
            }
    		
            Transport.send(msg);
	    
	}
}
