/**
 * 
 */
package com.akuacom.pss2.program.dbp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.utils.ftp.FTPClient;
import com.akuacom.utils.ftp.SSHFTPClientImpl;
import com.akuacom.utils.ftp.exception.AuthentificationException;
import com.akuacom.utils.ftp.exception.ConnectionNotOpenException;

/**
 *
 */
public class SCEFTPClient{
	private String host;
	private int port;
	private String username;
	private String password;
	
	//the backup path
	private String filename;
	private String backupPath;
	private String backupFolder;
	
	private FTPClient ftpClient=null;
	
	public SCEFTPClient(String host, int port, String username, String password){
		super();
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
		
		ftpClient=new SSHFTPClientImpl();
	}

	public SCEFTPClient(String host, int port, String username, String password, String filename){
		super();
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
		this.filename=filename;
		
		ftpClient=new SSHFTPClientImpl();
	}
	
	public SCEFTPClient(String host, int port, String username, String password, String filename, String backupPath){
		super();
		this.host=host;
		this.port=port;
		this.username=username;
		this.password=password;
		this.filename=filename;
		this.backupPath=backupPath;
		this.backupFolder=getBackupFolder();
		
		ftpClient=new SSHFTPClientImpl();
	}

	public void connect()  throws AppServiceException {
		try {
			ftpClient.connect(host, port, username, password);
		} catch (AuthentificationException e) {
			String messages="Incorrect credential to access the ftp server "+host+" (" +port+"), user name: "+username;
			throw new AppServiceException(messages, e);
		} catch (IOException e) {
			String messages="Can not access the ftp server "+host+" (" +port+"), user name: "+username;
			throw new AppServiceException(messages, e);
		}
	}
	
	public void testConnection() throws AppServiceException {
		connect();
		
		if (this.backupPath==null || this.backupPath.isEmpty()) return;
		
    	try {
			ftpClient.cd(this.backupPath);
		} catch (Exception e) {
			try {
				ftpClient.makeDir(backupPath);
			} catch (IOException e1) {
				String messages="Can not make backup directory "+backupPath+". Failed to access ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e1);
			} catch (ConnectionNotOpenException e1) {
				String messages="Can not make backup directory "+backupPath+". Connection not open to ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e1);
			}
			
			try {
				ftpClient.cd(backupPath);
			} catch (IOException e2) {
				String messages="Can not access backup directory "+backupPath+", ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e2);
			} catch (ConnectionNotOpenException e2) {
				String messages="Can not access backup directory "+backupPath+". Connection not open to ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e2);
			}
		}
	}
	
	public List<String> getFilenames(String path) throws AppServiceException {
		
		try {
			if (path!=null && path.trim().length()!=0)
				ftpClient.cd(path);
		} catch (Exception e2) {
			String messages="Can not access event file path "+path+", ftp server: "+host+"(" +port+")";
			throw new AppServiceException(messages, e2);
		}

		try {
			return ftpClient.getFileNameList();
		} catch (Exception e) {
			String messages="Failed to read the remote files from ftp server "+host+"(" +port+")";
			throw new AppServiceException(messages, e);
		}
	}
	
	public String getFileContents() throws AppServiceException {
		String contents=null;
		
		try {
			if (ftpClient.fileIsExist(filename)){
				contents = ftpClient.getRemoteFileContent(filename);
			}
		} catch (IOException e) {
			String messages="Can not read the remote file from ftp server "+host+"(" +port+"), file name: "+filename;
			throw new AppServiceException(messages, e);
		} catch (ConnectionNotOpenException e) {
			String messages="Can not connect to the ftp server "+host+"(" +port+").";
			throw new AppServiceException(messages, e);
		}
		
		return contents;
	}
	
	public String getFileContents(String filename) throws AppServiceException {
		String contents=null;		
		try {
			contents = ftpClient.getRemoteFileContent(filename);
		} catch (IOException e) {
			String messages="Can not read the remote file from ftp server "+host+"(" +port+"), file name: "+filename;
			throw new AppServiceException(messages, e);
		} catch (ConnectionNotOpenException e) {
			String messages="Can not connect to the ftp server "+host+"(" +port+").";
			throw new AppServiceException(messages, e);
		}
		
		return contents;
		
	}
    public void backupEventFile(String fileString) throws AppServiceException {

    	try {
			ftpClient.delete(filename);
		} catch (IOException e1) {
			String messages="Can not remove the file from ftp server "+host+"(" +port+"), file name: "+filename;
			throw new AppServiceException(messages, e1);
		} catch (ConnectionNotOpenException e1) {
			String messages="Can not connect to the ftp server "+host+"(" +port+").";
			throw new AppServiceException(messages, e1);
		}
		
    	try {
			ftpClient.cd(this.backupPath);
		} catch (Exception e) {
			try {
				ftpClient.makeDir(backupPath);
			} catch (IOException e1) {
				String messages="Can not make backup directory "+backupPath+". Failed to access ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e1);
			} catch (ConnectionNotOpenException e1) {
				String messages="Can not make backup directory "+backupPath+". Connection not open to ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e1);
			}
			
			try {
				ftpClient.cd(backupPath);
			} catch (IOException e2) {
				String messages="Can not access backup directory "+backupPath+", ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e2);
			} catch (ConnectionNotOpenException e2) {
				String messages="Can not access backup directory "+backupPath+". Connection not open to ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e2);
			}
		}
		
        try {
        	ftpClient.cd(backupFolder);
        } catch (Exception e) {
        	try {
				ftpClient.makeDir(backupFolder);
				ftpClient.cd(backupFolder);
			} catch (IOException e1) {
				String message="Can not access the backup folder. Ftp server: "+host+"(" +port+"), backup folder: "+backupPath+"/"+backupFolder;
				throw new AppServiceException(message, e1);
			} catch (ConnectionNotOpenException e1) {
				String message="Can not access backup directory "+backupPath+"/"+backupFolder+". Connection not open to ftp server: "+host+"(" +port+")";
				throw new AppServiceException(message, e1);
			}
        }

        InputStream is = new ByteArrayInputStream(fileString.getBytes());
        
        try {
			ftpClient.upload(is, filename);
		} catch (IOException e) {
			String messages="Failed to write the backup folder. Ftp server: "+host+"(" +port+"), backup folder: "+backupFolder;
			throw new AppServiceException(messages, e);
		} catch (ConnectionNotOpenException e) {
			String messages="Can not connect to ftp server "+host+"(" +port+"), folder name: "+backupFolder;
			throw new AppServiceException(messages, e);
		}
    }
    public void backupEventFile(String fileName,String fileContent,String oldPath,String newPath) throws AppServiceException {

    	if (oldPath.equalsIgnoreCase(newPath))
    		return;
    	
    	try {
    		//current in the old path
//    		ftpClient.cd(oldPath);
			ftpClient.delete(fileName);
		} catch (IOException e1) {
			String messages="Can not remove the file from ftp server "+host+"(" +port+"), file name: "+filename;
			throw new AppServiceException(messages, e1);
		} catch (ConnectionNotOpenException e1) {
			String messages="Can not connect to the ftp server "+host+"(" +port+").";
			throw new AppServiceException(messages, e1);
		}
		
    	try {
    		if (oldPath!=null && !oldPath.isEmpty()) {
    			int depth=oldPath.split("/").length;
    			if (oldPath.startsWith("/") || oldPath.startsWith("./"))
    				depth=depth-1;
    			for (int i=0; i<depth;i++)
    				ftpClient.cd("..");
    		}
    		
			ftpClient.cd(newPath);
		} catch (Exception e) {
			try {
				ftpClient.makeDir(newPath);
			} catch (IOException e1) {
				String messages="Can not make backup directory "+backupPath+". Failed to access ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e1);
			} catch (ConnectionNotOpenException e1) {
				String messages="Can not make backup directory "+backupPath+". Connection not open to ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e1);
			}
			
			try {
				ftpClient.cd(newPath);
			} catch (IOException e2) {
				String messages="Can not access backup directory "+backupPath+", ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e2);
			} catch (ConnectionNotOpenException e2) {
				String messages="Can not access backup directory "+backupPath+". Connection not open to ftp server: "+host+"(" +port+")";
				throw new AppServiceException(messages, e2);
			}
		}

        InputStream is = new ByteArrayInputStream(fileContent.getBytes());
        
        try {
			ftpClient.upload(is, fileName);
			ftpClient.cd("..");
			ftpClient.cd(oldPath);
		} catch (IOException e) {
			String messages="Failed to write the backup folder. Ftp server: "+host+"(" +port+"), backup folder: "+backupFolder;
			throw new AppServiceException(messages, e);
		} catch (ConnectionNotOpenException e) {
			String messages="Can not connect to ftp server "+host+"(" +port+"), folder name: "+backupFolder;
			throw new AppServiceException(messages, e);
		}
    }
    
    public void close(){
    	if (ftpClient!=null)
    		 ftpClient.close();
    }
    
    private String getBackupFolder(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        
        return format.format(now);
    }

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBackupPath() {
		return backupPath;
	}
	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
		this.backupFolder=getBackupFolder();
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
