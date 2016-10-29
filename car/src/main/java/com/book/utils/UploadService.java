package com.book.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.stereotype.Component;

import com.google.api.services.storage.model.StorageObject;
import com.google.common.io.Files;

@Component
public class UploadService {
	
	private static final String PROJECT_ID = "626448901";
	private static final String ACCOUNT_ID = "car-book@car-book.iam.gserviceaccount.com";
	private static final String APP_NAME = "Car Book Service";
	private static final String BUCKET_NAME = "ejsederbook";

	/**
	 * Method to upload files on Storage
	 * 
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(String fileName, InputStream inputStream) throws Exception {
		
		// Save the file in the JVM Temporary Directory
		File file = saveToTmpDir(fileName, inputStream);
		
		// Upload the file to the Cloud storage and returns the URL
		String url = uploadToCloudStorage(file);
		
		return url;
		
	}
	
	/**
	 * This method does the file upload on Google Cloud Storage
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private String uploadToCloudStorage(File file) throws Exception {
		
		Properties prop = new Properties();
		InputStream input = null;
		
		String filename = "P12Path.properties";
		input = getClass().getClassLoader().getResourceAsStream(filename);
		
		prop.load(input);
		
		// Gets the value of the property: the file path
		String string = prop.getProperty("P12File");
		
		// .p12 primary key file (We are getting this file from properties, which the value of system property is called that way
		// "p12File", setted at the arguments value of Tomcat during the initialization)
//		String string = System.getProperty("p12File");
		
		if(string == null) {
			throw new IOException("Error on server");
		}
		
		// Getting the .p12 file
		File p12File = new File(string);
		
		if(!p12File.exists()) {
			throw new IOException("Error on server");
		}
		
		// Using the storage class connection here
		CloudStorageUtil storageUtil = new CloudStorageUtil(APP_NAME);
		
		// Connect with Google API Storage
		storageUtil.connect(ACCOUNT_ID, p12File);
		
		// Gets the file data
		String fileName = file.getName();
		String contentType = getContentType(fileName);
		String storageProjectId = PROJECT_ID;
		
		// The upload of the file on storage happens here in this method
		StorageObject object = storageUtil.upload(storageProjectId, BUCKET_NAME, file, contentType);
		
		if(object == null){
			throw new IOException("Error during upload");
		}
		
		// Returns the file url
		String url = String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, object.getName());
		
		return url;
		
	}
	
	/**
	 * Get the content type of the file here
	 * 
	 * @param fileName
	 * @return
	 */
	private String getContentType(String fileName) {
		String ext = Files.getFileExtension(fileName);
		
		if("png".equals(ext)){
			return "image/png";
			
		}
		else if("jpg".equals(ext) || "jpeg".equals(ext)){
			return "image/jpg";
		}
		else if("gif".equals(ext)){
			return "image/gif";
			
		}
		
		return "text/plain";
	}
	
	/**
	 * This method saves the file on the JVM temporary directory
	 * and from there it will be uploaded to the Cloud Storage
	 * 
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private File saveToTmpDir(String fileName, InputStream inputStream) throws IOException {
		
		if(fileName == null || inputStream == null){
			throw new IllegalArgumentException("Invalid parameters");
		}
		
		// Temporary JVM folder
		File tmpDir = new File(System.getProperty("java.io.tmpdir"), "Cars");
		
		// Check if the folder exists
		if(!tmpDir.exists()){
			// Create folder if it doesn't exist
			tmpDir.mkdir();
		}
		
		// Create the file
		File file = new File(tmpDir, fileName);
		
		// Open OutpouStream to write in the file
		FileOutputStream outputStream = new FileOutputStream(file);
		
		// Write the data in the file
		IOUtils.copy(inputStream, outputStream);
		IOUtils.closeQuietly(outputStream);
		
		return file;
		
	}
	
	public String passToBase64(FormDataMultiPart multiPart) {
		
		if(multiPart != null){
			Set<String> keys = multiPart.getFields().keySet();
			
			for (String key : keys) {
				
				try {
					// Get an InputStream to read the file
					FormDataBodyPart field = multiPart.getField(key);
					InputStream inputStream = field.getValueAs(InputStream.class);

					// Gets the image file bytes
					byte[] bytes = IOUtils.toByteArray(inputStream);
					
					// Encode to base64 format here
					String base64 = Base64.getEncoder().encodeToString(bytes);
					
					return base64;
					
				} catch (Exception e) {
					e.printStackTrace();
					return "Error " + e.getMessage();
					
				}
			}
		}
		
		return "Invalid Request";
	}
	
	public String getPhotoBase64(String fileName, String base64) throws Exception {
		
		String path = "";
		
		if(fileName != null && base64 != null){

			// Decode: convert base64 to bytes array
			byte[] bytes = Base64.getDecoder().decode(base64);
			
			InputStream inputStream = new ByteArrayInputStream(bytes);
			
			// Make the file upload to a JVM temp folder
			path = uploadFile(fileName, inputStream);

			
			// Just showing the results
			System.out.println("File " + path);
			
		}
		
		return path;
	}

}
