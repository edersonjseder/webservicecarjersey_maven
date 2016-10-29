package com.book.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;
import com.google.common.collect.ImmutableList;

/**
 * Util class with methods to make the connection with the Cloud storage -
 * -open the connection
 * -gets the authorization with OAuth via token
 * -gets the bucket data
 * -get the files in the bucket
 * -upload the file on storage
 * 
 * @author ederson
 *
 */
public class CloudStorageUtil {
	private Storage client;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private final String applicationName;
	private HttpTransport httpTransport;
	
	public CloudStorageUtil(String applicationName) {
		super();
		this.applicationName = applicationName;
	}
	
	// Connect to the Google API Cloud Storage
	public void connect(String accountId, File p12File) throws Exception {
		// Create the object to transport data
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		
		// calling to OAuth and receive authorization to connect
		Credential credential = authorize(accountId, p12File);
		
		// Get the connection to the Google Cloud Storage here
		client = new Storage.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(applicationName).build();
		
	}
	
	// Authorize the access with "Server-Side Authorization"
	private Credential authorize(String accountId, File p12File) throws Exception {
	    // Set up authorization code flow.
	    // Here we are listing all of the available scopes.
		Set<String> scopes = new HashSet<String>();
	    scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
	    scopes.add(StorageScopes.DEVSTORAGE_READ_ONLY);
	    scopes.add(StorageScopes.DEVSTORAGE_READ_WRITE);
	    
	    // ** Authorize the application **
	    JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	    
	    // Initialize the transport
	    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

	    // Setting the parameters to access the Storage through credentials
	    GoogleCredential credential = new GoogleCredential.Builder()
	    										.setTransport(httpTransport).setJsonFactory(JSON_FACTORY)
	    										.setServiceAccountId(accountId)
	    										.setServiceAccountPrivateKeyFromP12File(p12File)
	    										.setServiceAccountScopes(scopes).build();
	    System.out.println("Success.");
	    
	    return credential;
	}
	
	// Get a bucket object
	public Bucket getBucket(String bucketName) throws IOException {
		// Gets the bucket name here
		Storage.Buckets.Get getBucket = client.buckets().get(bucketName);
		
		//Gets the bucket object
		getBucket.setProjection("full");
		Bucket bucket = getBucket.execute();
		
		return bucket;
	}
	
	public List<StorageObject> getBucketFiles(Bucket bucket) throws IOException {
	      // List the contents of the bucket.
	      Storage.Objects.List listObjects = client.objects().list(bucket.getName());
	      
	      com.google.api.services.storage.model.Objects objects;
	      
	      // Get a list of the contents in the bucket
	      ArrayList<StorageObject> all = new ArrayList<StorageObject>();
	      
	      do {
	    	  // Here it gets the objects in the bucket
	        objects = listObjects.execute();
	        
	        List<StorageObject> items = objects.getItems();
	        
	        if (null == items) {
	          
	        	System.out.println("There were no objects in the given bucket.");
	          
	          return all;
	          
	        }
	        
	        all.addAll(items);
	        
	        
	        for (StorageObject object : items) {
	          System.out.println(object.getName() + " (" + object.getSize() + " bytes)");
	        }
	        
	        // Set the objects in a list objects
	        listObjects.setPageToken(objects.getNextPageToken());
	        
	      } while (null != objects.getNextPageToken()); // While buckets still have inside to be used

	      return all;
	      
	}
	
	// Upload file (INSERT) in the storage
	public StorageObject upload(String projectId, String bucketName, File file, String contentType) throws IOException {
		
		if(client == null) {
			throw new IOException("Cloud Storage API is not connected");
			
		}
		
		if(file == null || !file.exists() | !file.isFile()){
			throw new FileNotFoundException("File not found");
			
		}
		
		// File name
		String fileName = file.getName();
		
		// Reads the file
		InputStream inputStream = new FileInputStream(file);
		// Gets the length of the information string
		long byteCount = file.length();
		
		InputStreamContent mediaContent = new InputStreamContent(contentType, inputStream);
		
		mediaContent.setLength(byteCount);
		
		// Configures the ACL access = Access Control List
		ImmutableList<ObjectAccessControl> acl = ImmutableList.of(new ObjectAccessControl().setEntity(String.format("project-owners-%s", projectId)).setRole("OWNER"),
																  new ObjectAccessControl().setEntity(String.format("project-editors-%s", projectId)).setRole("OWNER"),
																  new ObjectAccessControl().setEntity(String.format("project-viewers-%s", projectId)).setRole("READER"),
																  new ObjectAccessControl().setEntity("allUsers").setRole("READER") // Public URL 
																  );
		
		// Configure the object
		StorageObject object = new StorageObject();
		object.setName(fileName);
		object.setContentType(contentType);
		object.setAcl(acl);
		
		// This is the method that inserts the file on the storage
		Storage.Objects.Insert insertObject = client.objects().insert(bucketName, object, mediaContent);
		
		// check if the limit is 2MB
		if(mediaContent.getLength() > 0 && mediaContent.getLength() <= 2 * 1000 * 1000 /* 2MB */) {
			insertObject.getMediaHttpUploader().setDirectUploadEnabled(true);
		}
		
		// Confirm the insertion
		insertObject.execute();
		
		return object;
	}
}
