/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.utility;

import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.bookstore.user.domain.Order;
import com.bookstore.user.domain.User;

@Component
public class MailConstructor {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TemplateEngine templateEngine;

	public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, 
			String token, User user, String password, String subject) {
		
		String url = contextPath+"/newUser?token="+token;
		String message = "\nPlease click on this link to verify your email and "
				+ "edit your personal information. "
				+ "Your password is :\n"+password;
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));
		email.setSubject(subject);
		email.setText(url+message);
		
		return email;
				
	}
	
	public MimeMessagePreparator constructOrderConfirmationEmail(User user, Order order, Locale locale) {
		
		Context context = new Context();
		
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getCartItemList());
		
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				// TODO Auto-generated method stub
				
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());
				email.setFrom(new InternetAddress(env.getProperty("support.email")));
				email.setSubject("Order Confirmation - " + order.getId());
				email.setText(text, true);
				
			}
		};
		
		return messagePreparator;
				
	}
	
}
