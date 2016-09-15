package com.akuacom.utils.ftp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.akuacom.utils.ftp.exception.AuthentificationException;
import com.akuacom.utils.ftp.exception.ConnectionNotOpenException;
import com.akuacom.utils.ftp.exception.RemoteFileNotFoundException;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.sftp.SftpFile;

public class SSHFTPClientImpl implements FTPClient {

	private SftpClient client;
	private SshClient ssh;

	@Override
	public void connect(String url, int port, String username, String password)
			throws AuthentificationException, IOException {

		// if (ssh.isConnected()) {
		// ssh.disconnect();
		// }

		ssh = new SshClient();
		ssh.connect(url, port, new AlwaysAllowingConsoleKnownHostsKeyVerification());

		// Authenticate
		PasswordAuthenticationClient passwordAuthenticationClient = new PasswordAuthenticationClient();
		passwordAuthenticationClient.setUsername(username);
		passwordAuthenticationClient.setPassword(password);

		int result = ssh.authenticate(passwordAuthenticationClient);

		if (result != AuthenticationProtocolState.COMPLETE) {
			throw new AuthentificationException();
		}
		// Open the SFTP channel
		client = ssh.openSftpClient();
	}

	@Override
	public void close() {
		client = null;
		if(ssh != null){
			if (ssh.isConnected()) {
				ssh.disconnect();
			}			
		}
	}

	@Override
	public void cd(String remoteFilePath) throws IOException,
			ConnectionNotOpenException {
		if (ssh != null && client != null) {
			if (ssh.isConnected() && !client.isClosed()) {
				client.cd(remoteFilePath);
			} else {
				throw new ConnectionNotOpenException();
			}

		} else {
			throw new ConnectionNotOpenException();
		}
	}

	@Override
	public String getRemoteFileContent(String fileName) throws IOException,
			ConnectionNotOpenException {

		String remoteFileContent = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (ssh != null && client != null) {
			if (ssh.isConnected() && !client.isClosed()) {
				client.get(fileName, baos);
				remoteFileContent = baos.toString();
			} else {
				throw new ConnectionNotOpenException();
			}
		} else {
			throw new ConnectionNotOpenException();
		}

		baos.close();

		return remoteFileContent;
	}

	@Override
	public void getRemoteFileToStream(String fileName, OutputStream ops)
			throws IOException, ConnectionNotOpenException {
		if (ssh != null && client != null) {
			if (ssh.isConnected() && !client.isClosed()) {
				client.get(fileName, ops);
			} else {
				throw new ConnectionNotOpenException();
			}
		} else {
			throw new ConnectionNotOpenException();
		}
	}

	@Override
	public void renameRemoteFile(String oldName, String newName)
			throws IOException, RemoteFileNotFoundException,
			ConnectionNotOpenException {
		if (ssh != null && client != null) {
			if (ssh.isConnected() && !client.isClosed()) {

				try {
					client.get(oldName);
				} catch (Exception e) {
					throw new RemoteFileNotFoundException(oldName);
				}
				client.rename(oldName, newName);
			} else {
				throw new ConnectionNotOpenException();
			}
		} else {
			throw new ConnectionNotOpenException();
		}
	}

	@Override
	public void upload(String localFile) throws IOException,
			ConnectionNotOpenException {
		if (ssh != null && client != null) {
			if (ssh.isConnected() && !client.isClosed()) {
				client.put(localFile);
			} else {
				throw new ConnectionNotOpenException();
			}
		} else {
			throw new ConnectionNotOpenException();
		}
	}

	@Override
	protected void finalize() {
		if (ssh.isConnected()) {
			ssh.disconnect();
		}
	}

	@Override
	public void delete(String remoteFile) throws IOException,
			ConnectionNotOpenException {
		if (ssh != null && client != null) {
			if (ssh.isConnected() && !client.isClosed()) {
				client.rm(remoteFile);
			} else {
				throw new ConnectionNotOpenException();
			}
		} else {
			throw new ConnectionNotOpenException();
		}
	}

	@Override
	public List<String> getFileNameList() throws IOException,
			ConnectionNotOpenException {
		List fileList = null;
		List<String> fileNameList = null;
		
		if (ssh != null && client != null) {
			if (ssh.isConnected() && !client.isClosed()) {
				fileList = client.ls();
				if (fileList != null) {
					fileNameList = new ArrayList<String>();
					for (Object file : fileList) {
						if (!((SftpFile)file).getFilename().equalsIgnoreCase(".")
								&& !((SftpFile)file).getFilename().equalsIgnoreCase("..")) {
							fileNameList.add(((SftpFile)file).getFilename());
						}
					}
				}
			} else {
				throw new ConnectionNotOpenException();
			}
		} else {
			throw new ConnectionNotOpenException();
		}
		return fileNameList;
	}

	@Override
	public void makeDir(String dirName) throws IOException,
			ConnectionNotOpenException {
		if (ssh != null && client != null) {
			if (ssh.isConnected() && !client.isClosed()) {
				client.mkdir(dirName);
			} else {
				throw new ConnectionNotOpenException();
			}
		} else {
			throw new ConnectionNotOpenException();
		}
		
	}

	@Override
	public void upload(InputStream inputStream, String remoteFile)
			throws IOException, ConnectionNotOpenException {

		if (ssh != null && client != null) {
			if (ssh.isConnected() && !client.isClosed()) {
				client.put(inputStream, remoteFile);
			} else {
				throw new ConnectionNotOpenException();
			}
		} else {
			throw new ConnectionNotOpenException();
		}
		
	}

	@Override
	public Boolean fileIsExist(String fileName) throws ConnectionNotOpenException {
		
		Boolean fileExist = true;
		
		try{
			if (ssh != null && client != null) {
				if (ssh.isConnected() && !client.isClosed()) {
					client.get(fileName);
				} else {
					throw new ConnectionNotOpenException();
				}
			} else {
				throw new ConnectionNotOpenException();
			}			
		}
		catch (IOException e) {
			fileExist = false;
		}
		
		return fileExist;
	}
}
