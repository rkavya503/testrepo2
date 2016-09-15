package com.akuacom.utils.ftp;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SSHFTPClientTest{

	@Ignore
	@Test
	public void testFTPClient(){
		FTPClient ftpClient = null;
		try{
			ftpClient = new SSHFTPClientImpl();
			ftpClient.connect("159.99.234.177", 22, "E218290A", "HONmm88!9");
			
			ftpClient.cd("SFTPTestDir");
			
			ftpClient.upload("C:/test.txt");
			
			String content = ftpClient.getRemoteFileContent("test.txt");
			boolean contentOK = content.equals("This is a test message")? true:false;			
			Assert.assertTrue(contentOK);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ftpClient.getRemoteFileToStream("test.txt", baos);
			content = baos.toString();
			contentOK = content.equals("This is a test message")? true:false;			
			Assert.assertTrue(contentOK);
			
			ftpClient.renameRemoteFile("test.txt", "newtest.txt");
			content = ftpClient.getRemoteFileContent("newtest.txt");
			contentOK = content.equals("This is a test message")? true:false;			
			Assert.assertTrue(contentOK);
			
			boolean testDirExist = false;
			boolean newTestFile = false;
			
			ftpClient.makeDir("testDir");
			
			List<String> fileList = ftpClient.getFileNameList();
			
			for (String string : fileList) {
				System.out.println(string);
				if(string.equalsIgnoreCase("testDir")){
					testDirExist = true;
				}				
			}
			Assert.assertTrue(testDirExist);
			testDirExist = false;

			ftpClient.delete("newtest.txt");
			ftpClient.delete("testDir");
			
			fileList = ftpClient.getFileNameList();
			
			for (String string : fileList) {
				if(string.equalsIgnoreCase("testDir")){
					testDirExist = true;
				}
				if(string.equalsIgnoreCase("newtest.txt")){
					newTestFile = true;
				}
				
			}
			Assert.assertFalse(testDirExist);
			Assert.assertFalse(newTestFile);

			ftpClient.close();
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
		finally{
			if(ftpClient != null){
				ftpClient.close();				
			}
		}
		
		
	}
	
//	public static void main(String args[]){
//		FTPClient ftpTest = new SSHFTPClientImpl();
//		//SftpClient client = ftpTest.getFTPClient("159.99.234.177", 22, "E218290A", "HONmm88!9");
//		
//		//ftpTest.renameRemoteFile("159.99.234.177", 22, "E218290A", "HONmm88!9", "\.ssh", oldFileName, newFileName)
//		
//		try{
//			
//			
//			ftpTest.connect("159.99.234.177", 22, "E218290A", "HONmm88!9");			
//			
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ftpTest.getRemoteFileToStream("smit.log", baos);
//			
//			System.out.println(baos.toString());
//			ftpTest.close();
//			
//		}
//		catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
}