package com.akuacom.utils.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.akuacom.utils.ftp.exception.AuthentificationException;
import com.akuacom.utils.ftp.exception.ConnectionNotOpenException;
import com.akuacom.utils.ftp.exception.RemoteFileNotFoundException;

/**
 * SSHFTPClient is for download / rename remote ftp over ssh protocol.
 * 
 * See SSHFPClientImpl for implementation
 * 
 * @author Li Fei
 */

public interface FTPClient {

	/**
	 * <p>
	 * Open an SFTP client with given url, port, user name and password for file
	 * transfer operations
	 * 
	 * @exception IOException
	 *                If an IO error occurs during the operation
	 * @exception AuthentificationException
	 *                If user name and password are not correct
	 */
	public void connect(String url, int port, String username, String password)
			throws AuthentificationException, IOException;

	/**
	 * <p>
	 * Changes the working directory on the remote server.
	 * </p>
	 * 
	 * @param dir
	 *            the new working directory
	 * 
	 * @throws IOException
	 *             if an IO error occurs or the file does not exist
	 */
	public void cd(String remoteFilePath) throws IOException,
			ConnectionNotOpenException;

	/**
	 * <p>
	 * Convert remote file to given OutputStream
	 * </p>
	 * 
	 * @param FTP
	 *            file name
	 * 
	 * @param Given
	 *            OutputStream (ByteArrayOutputStream, FileOutputStream, etc.)
	 * 
	 * @throws IOException
	 *             if an IO error occurs or the file does not exist
	 */
	public void getRemoteFileToStream(String fileName, OutputStream ops)
			throws IOException, ConnectionNotOpenException;

	/**
	 * <p>
	 * Get file content from FTP server.
	 * </p>
	 * 
	 * @param FTP
	 *            file name
	 * @return Return the content of the file as text.
	 * 
	 * @throws IOException
	 *             if an IO error occurs or the file does not exist
	 */
	public String getRemoteFileContent(String fileName) throws IOException,
			ConnectionNotOpenException;

	/**
	 * <p>
	 * Rename file name.
	 * </p>
	 * 
	 * @throws if an RemoteFileNotFoundException error occurs means the file
	 *         does not exist
	 * @throws if IO error occurs, maybe that is because the target file does
	 *         not writable.
	 */
	public void renameRemoteFile(String oldFileName, String newFileName)
			throws IOException, RemoteFileNotFoundException,
			ConnectionNotOpenException;

	/**
	 * <p>
	 * Upload file to FTP server.
	 * </p>
	 * 
	 * @throws if IO error occurs, maybe that is because the target directory
	 *         does not writable.
	 * 
	 *         localFile example C:\test.txt
	 * 
	 */
	public void upload(String localFile) throws IOException,
			ConnectionNotOpenException;

	/**
	 * <p>
	 * Upload file to FTP server.
	 * </p>
	 * 
	 * @throws if IO error occurs, maybe that is because the target directory
	 *         does not writable.
	 * 
	 *         remoteFile example "test.csv"
	 * 
	 */
	public void upload(InputStream inputStream, String remoteFile)
			throws IOException, ConnectionNotOpenException;

	/**
	 * <p>
	 * Delete file on FTP server.
	 * </p>
	 * 
	 * @throws if IO error occurs, maybe that is because the target file does
	 *         not exist or login user hasn't enough authority
	 * 
	 *         remoteFile is a file name. It does not include path. For example:
	 *         test.txt
	 * 
	 */
	public void delete(String remoteFile) throws IOException,
			ConnectionNotOpenException;

	/**
	 * <p>
	 * Get file list of current directory.
	 * </p>
	 * 
	 * @throws if IO error occurs, maybe that is because the target file does
	 *         not exist or login user hasn't enough authority
	 * 
	 */
	public List<String> getFileNameList() throws IOException,
			ConnectionNotOpenException;

	/**
	 * <p>
	 * Create directory.
	 * </p>
	 * 
	 * @throws if IO error occurs, maybe that is because the target file does
	 *         not exist or login user hasn't enough authority
	 * 
	 */
	public void makeDir(String dirName) throws IOException,
			ConnectionNotOpenException;

	/**
	 * <p>
	 * Create directory.
	 * </p>
	 * 
	 * @throws if IO error occurs, maybe that is because the target file does
	 *         not exist or login user hasn't enough authority
	 * 
	 */
	public Boolean fileIsExist(String fileName) throws IOException,
			ConnectionNotOpenException;
	
	
	
	/**
	 * Close ftp connection
	 */
	public void close();
}
