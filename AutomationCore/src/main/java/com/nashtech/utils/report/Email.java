package com.nashtech.utils.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;

import com.nashtech.common.Common;

public class Email {
	
	/**
	 * @param strOutput
	 * 		The output zip file name
	 * @param strInput
	 * 		The file or folder that need to compress
	 * @param strBaseFolder
	 * 		The folder that contains the file which need to be compressed
	 * 		Or the path of itself in case compress a folder
	 * @throws Exception
	 */
	public void compressReport(String strOutput, String strInput, String strBaseFolder) throws Exception {
		
		File zipcheck = new File(strOutput);

		if (zipcheck.exists()) {
			zipcheck.delete();
		}
		FileOutputStream fos = new FileOutputStream(strOutput);
		ZipOutputStream zos = new ZipOutputStream(fos);
		// level - the compression level (0-9)
		zos.setLevel(9);
		addFolder(zos, strInput, strBaseFolder);
		zos.close();
	}


	/**
	 * To compress a file or a folder
	 * 
	 * @param target
	 * 		ZipOutputStream: the output file stream
	 * @param source
	 * 		The file or folder that need to compress
	 * @param baseFolderName
	 * 		The folder that contains the file which need to be compressed
	 * 		Or the path of itself in case compress a folder
	 * @throws Exception
	 */
	public void addFolder(ZipOutputStream target, String source, String baseFolderName) throws Exception {
		File sourceFile = new File(source);
		File baseFolder = new File(baseFolderName);
		if (sourceFile.exists()) {
			if (sourceFile.isDirectory()) {
				File f2[] = sourceFile.listFiles();
				for (int i = 0; i < f2.length; i++) {
					addFolder(target, f2[i].getAbsolutePath(), baseFolderName);
				}
			} else {
				// add file
				// extract the relative name for entry purpose
				String entryName = source.substring(
						baseFolder.getAbsolutePath().length() + 1, sourceFile.getAbsolutePath().length());
				ZipEntry ze = new ZipEntry(entryName);
				target.putNextEntry(ze);
				FileInputStream in = new FileInputStream(source);
				int len;
				byte buffer[] = new byte[1024];
				while ((len = in.read(buffer)) > 0) {
					target.write(buffer, 0, len);
				}
				in.close();
				target.closeEntry();
			}
		} else {
			System.out.println("File or directory not found " + source);
		}

	}

	/**
	 * This method is used to send an email and reports.zip to customer when
	 * running the test suite finishes
	 * 
	 * @author Hanoi Automation team
	 * @throws Exception
	 */
	public void sendEmail(String strAttachment) throws Exception {

		String strSmtpHost = Common.getConfigValue("SMTPHost");
		String strSmtpEncryption = Common.getConfigValue("SMTPEncryption");
		String strSmtpPort = Common.getConfigValue("SMTPPort");
		int inPort = Integer.parseInt(strSmtpPort);
		String strFrom = Common.getConfigValue("From");
		String strUser = Common.getConfigValue("EmailUser").trim().toString();
		String strPassword = Common.getConfigValue("EmailPassword").trim()
				.toString();
		String strTo = Common.getConfigValue("To");
		String strCC = Common.getConfigValue("CC");
		String strReplyTo = Common.getConfigValue("ReplyTo");

		// Getting CurrentDate
		String strCurrentDate = Common.getCurrentDate();

		// Attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(strAttachment);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("Reports.zip");

		// HTML Format
		HtmlEmail email = new HtmlEmail();
		email.setHostName(strSmtpHost);
		// Defining the Smto Encryption
		if (strSmtpEncryption.equalsIgnoreCase("SSL")) {
			email.setSSLOnConnect(true);
		}
		email.setSmtpPort(inPort);
		email.setStartTLSEnabled(true);

		// Authentication
		email.setAuthenticator(new DefaultAuthenticator(strUser, strPassword));

		// From and Subject
		email.setFrom(strFrom);
		email.setSubject("Reg: Automation Execution Results on "
				+ strCurrentDate);

		// Global analytics image adding in the body message
		String htmlmessage = "<p class=MsoNormal><font size=3 face=Arial>Hello All,</font></p>"
				+ "<p class=MsoNormal><font size=1 face=Arial>&nbsp;</font></p>"
				+ "<p class=MsoNormal><font size=3 face=Arial>Please find the attached Test Suite(s) Results.</font></p>"
				+ "<p class=MsoNormal><font size=2 face=Arial>&nbsp;</font></p>"
				+ "<p class=MsoNormal><font size=2 face=Arial><o:p>Regards,</o:p></font></p>"
				+ "<p class=MsoNormal><font size=2 face=Arial><o:p>Automation Testing Team,</o:p></font></p>";

		// Email Message
		email.setHtmlMsg(htmlmessage);

		// Setting the To, CC and Reply to Address
		String[] To = strTo.split(",");
		String[] CC = strCC.split(",");
		String[] ReplyTo = strReplyTo.split(",");
		for (int i = 0; i < To.length; i++) {
			email.addTo(To[i]);
		}
		for (int j = 0; j < CC.length; j++) {
			email.addCc(CC[j]);
		}
		for (int k = 0; k < ReplyTo.length; k++) {
			email.addReplyTo(ReplyTo[k]);
		}

		// Attachment
		email.attach(attachment);
		// Sending the Mail
		email.send();

		System.out.println("Mail Sent");
	}

}
