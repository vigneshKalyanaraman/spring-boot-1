package com.obs.util;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IoUtils {

	private static final int DEFAULT_BUFFER_SIZE = 1024;

	private static final Logger logger					= LogManager.getLogger(IoUtils.class );


	private static IoUtils myInstance = null;
	public static IoUtils getInstance()
	{
		if(myInstance == null)
			myInstance = new IoUtils();
		return myInstance;
	}
	private int bufferSize = 1024;

	private static final Logger log					= LogManager.getLogger(IoUtils.class);

	public IoUtils() {
		this(DEFAULT_BUFFER_SIZE);
	}

	public IoUtils(int bufferSize) { 
		this.bufferSize = bufferSize;
	}

	public String read(String filePath) throws IOException {
		FileInputStream       inputStream   = new FileInputStream(new File(filePath));
		ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
		copy(inputStream, outputStream);
		return outputStream.toString();
	}

	/**
	 * convert InputStream to String
	 *
	 * @param is
	 * @return String
	 */
	public static String getStringFromInputStream(InputStream is) {
		StringBuilder sb = new StringBuilder();
		String line;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(is));) {

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Unable to process input stream to output a string!", e);
			} else {
				log.warn("Unable to process input stream to output a string!");
			}
		}
		return sb.toString();
	}

	/**
	 * copy file 
	 * @param inputStream
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	public int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte[] buffer = new byte[bufferSize];
		int totalBytesRead = 0;
		int bytesRead      = 0;
		while (-1 != (bytesRead = inputStream.read(buffer))) {
			outputStream.write(buffer, 0, bytesRead);
			totalBytesRead += bytesRead;
		}
		return totalBytesRead;
	}  

	/**
	 * extract Bytes
	 * @param ImagePath
	 * @return
	 * @throws IOException
	 */
	public byte[] extractBytes (String imagePath) throws IOException {
		byte[] imageInByte =  null;
		File fnew=new File(imagePath);
		if(fnew.isFile()){
			BufferedImage originalImage=ImageIO.read(fnew);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			ImageIO.write(originalImage, getExtection(imagePath), baos );
			imageInByte=baos.toByteArray();
		}
		return imageInByte;
	}

	/**
	 * get fileName Extection
	 * @param fileName
	 * @return
	 */
	public static String getExtection(String fileName){
		int pos = fileName.lastIndexOf('.');
		return fileName.substring(pos+1);
	}

	/**
	 * read All from reader
	 * @param Reader
	 * @return String
	 * @throws IOException
	 */
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	/**
	 * read json from url
	 * @param url as String
	 * @return String
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws Exception
	 */
	public String readJsonFromUrl(String url) throws IOException{

		try(InputStream is = new URL(url).openStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));) {
			return readAll(rd);
		} 
		catch(Exception e)
		{
			return e.getMessage();
		}
	}

	/**
	 * Write Images
	 * @param imagePath
	 * @param imageName
	 * @param data
	 * @return boolean
	 * @throws IOException 
	 */
	public boolean writeImages(String imagePath,String imageName, InputStream data) throws IOException
	{
		boolean flag = false;
		byte[] mediaData;
		BufferedImage image = null;
		String ext = getExtection(imageName);
		mediaData = new byte[data.available()];
		File photoFile = new File(imagePath);
		if(!photoFile.exists()) 
			photoFile.mkdirs();
		int count = 0;
		while (data.read(mediaData) > 0) {
			count = count+1;
		}
		try(ByteArrayInputStream imageStream = new ByteArrayInputStream(mediaData);) {
			String pathDelimiteer = "/";
			image = ImageIO.read(imageStream);
			flag = ImageIO.write(image, ext, new File(imagePath+pathDelimiteer+imageName));

		} catch (IOException e) {
			log.error("Error While writeImages:",e);
			   
		}

		return flag;
		
	}

	/**
	 * Copy Folder and it's Files Source to Destination
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public void copyFolder(File src, File dest) throws IOException{
		if(src.isDirectory()){
			if(!dest.exists()){
				dest.mkdir();
			}
			String[] files = src.list();
			if(files != null && files.length>0)
			{
				for (String file : files) {
					File srcFile = new File(src, file);
					File destFile = new File(dest, file);
					copyFolder(srcFile,destFile);
				}
			}
		}else{

			try(	InputStream in = new FileInputStream(src);
					OutputStream out = new FileOutputStream(dest);)
			{

				byte[] buffer = new byte[1024];

				int length;
				while ((length = in.read(buffer)) > 0){
					out.write(buffer, 0, length);
				}
			}
			catch(Exception e)
			{
				logger.error(e);
			}
		}
	}

	/**
	 * write File From InputStream
	 * Use bytes stream to support all file types
	 * @param in
	 * @param dest
	 * @throws IOException
	 */
	public void writeFileFromInputStream(InputStream ini,File dest) throws IOException{
		try(OutputStream out = new FileOutputStream(dest);
				InputStream in =ini;	)
		{
			byte[] buffer = new byte[1024];

			int length;

			while ((length = in.read(buffer)) > 0){
				out.write(buffer, 0, length);
			}
		}

		catch(Exception e)
		{
			logger.error(e);
		}

	}
	public void deleteDirectory(File path) 
			throws IOException {
		Path pathToBeDeleted = path.toPath();
		Files.walkFileTree(pathToBeDeleted, 
				new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult postVisitDirectory(
					Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult visitFile(
					Path file, BasicFileAttributes attrs) 
							throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * return content type of a file
	 * @param extension
	 * @return String
	 */	
	public static String getContentType(String extension){
		if(extension.equals("pdf")){
			return "application/pdf";
		}
		if(extension.equals("xls") || extension.equals("xlsx")){
			return"application/vnd.ms-excel";
		}
		if(extension.equals("doc") || extension.equals("docx")){
			return"application/msword";
		}
		if(extension.equals("gif"))	{
			return "image/gif";
		}
		if(extension.equals("png"))	{
			return "image/png";
		}
		if(extension.equals("jpg") || extension.equals("jpeg")){
			return "image/jpeg";
		}
		if(extension.equals("tif") || extension.equals("tiff"))	{
			return "image/tiff";
		}
		return "";
	}	
}
