package com.db.dbs.repository.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;
import javax.jcr.LoginException;

import org.springframework.stereotype.Repository;

import javax.jcr.AccessDeniedException;
import javax.jcr.Binary;
import javax.jcr.ItemExistsException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.commons.JcrUtils;

import com.db.dbs.common.DBProperties;
import com.db.dbs.enums.ItemType;
import com.db.dbs.model.Asset;
import com.db.dbs.model.Content;
import com.db.dbs.repository.ContentRepository;
import com.db.dbs.utilities.Utilities;


@Repository
public class JCRRMIContentRepository implements ContentRepository {
	
	@Inject
	DBProperties dbproperties;

	public String findPageHTML(String tenantName, String applicationName, String pageName) throws Exception {
		try{
			String pathtocontent = makeRelPath(tenantName, applicationName, "_pages");
			InputStream is = findContentByPath(pathtocontent, pageName + ".html");
			return IOUtils.toString(is, "utf-8");
		} catch(Exception ex){
			throw new Exception("Page HTML not found", ex);
		}
	}
	
	public String findPageAsJson(String tenantName, String applicationName, String pageName) throws Exception {
		try{
			String pathtocontent = makeRelPath(tenantName, applicationName, "_pages");
			return findContentAsJsonByPath(pathtocontent, pageName + ".html");
		} catch(Exception ex){
			throw new Exception("Page content not found", ex);
		}
	}
	
	public String findComponentHTML(String tenantName, String applicationName, String pageName, String componentName) throws Exception {
		try{
			String pathtocontent = makeRelPath(tenantName, applicationName, pageName, "_components");
			InputStream is = findContentByPath(pathtocontent, componentName + ".html");
			return IOUtils.toString(is, "utf-8");
		} catch(Exception ex){
			throw new Exception("Component HTML not found", ex);
		}
	}

	public String findComponentAsJson(String tenantName, String applicationName, String pageName, String componentName) throws Exception {
		try{
			String pathtocontent = makeRelPath(tenantName, applicationName, pageName, "_components");
			return findContentAsJsonByPath(pathtocontent, componentName + ".html");
		} catch(Exception ex){
			throw new Exception("Component content not found", ex);
		}
	}

	public void updatePageHTML(String tenantName, String applicationName, String pageName, String content) throws Exception {
		Session dbssession = getSession();
		InputStream fileContent = new ByteArrayInputStream(content.getBytes("UTF-8"));
		String relfolderpath = makeRelPath(tenantName, applicationName, "_pages");
		Node folder = getFolder(dbssession, relfolderpath, true);
		Node pagefile = updateFile(dbssession, folder, pageName + ".html", fileContent, "text/html");
	}
	
	public void updateComponentHTML(String tenantName, String applicationName, String componentName, String content) throws Exception {
		Session dbssession = getSession();
		InputStream fileContent = new ByteArrayInputStream(content.getBytes("UTF-8"));
		String relfolderpath = makeRelPath(tenantName, applicationName, "_components");
		Node folder = getFolder(dbssession, relfolderpath, true);
		Node pagefile = updateFile(dbssession, folder, componentName + ".html", fileContent, "text/html");
	}
	
	public void updateComponentHTML(String tenantName, String applicationName, String pageName, String componentName, String content) throws Exception {
		Session dbssession = getSession();
		InputStream fileContent = new ByteArrayInputStream(content.getBytes("UTF-8"));
		String relfolderpath = makeRelPath(tenantName, applicationName);
		if(pageName!=null){
			relfolderpath = relfolderpath + "/_pages/" + pageName;
		}
		relfolderpath += "/_components/" + componentName;
		Node folder = getFolder(dbssession, relfolderpath, true);
		Node pagefile = updateFile(dbssession, folder, componentName + ".html", fileContent, "text/html");
	}

	public void updateItemAsset(String tenantName, String applicationName, String pageName, String relativePath, File file) throws Exception{
		updateItemAsset(tenantName, applicationName, pageName, null, relativePath, file);
	}
	public void updateItemAsset(String tenantName, String applicationName, String pageName, String componentName, String relativePath, File file) throws Exception{
		Session dbssession = getSession();
		InputStream fileContent = new FileInputStream(file);
		String relfolderpath = makeRelPath(tenantName, applicationName);
		if(pageName!=null){
			relfolderpath = relfolderpath + "/_pages/" + pageName;
		}
		if(componentName!=null){
			relfolderpath = relfolderpath + "/_components/" + componentName;
		}
		relfolderpath += relativePath;
		Node folder = getFolder(dbssession, relfolderpath, true);
		String mimetype =  new MimetypesFileTypeMap().getContentType(file);
		Node assetFile = updateFile(dbssession, folder, file.getName(), fileContent, mimetype);
	}
	
	public InputStream findContentByPath(String relativePathToContent, String fileName) throws Exception {
		try{
			InputStream content = null;
			Session dbssession = getSession();
			Node foldernode = getFolder(dbssession, relativePathToContent, false);
			if(foldernode!=null){
				Node contentnode = getFileContent(dbssession, foldernode, fileName);
				Binary binary = contentnode.getProperty("jcr:data").getBinary();
				content = binary.getStream();
			} else {
				throw new Exception("Folder not found");
			}
			return content;
		} catch(Exception ex){
			throw new Exception("Content not found", ex);
		}	
	}
	
	public String findContentAsJsonByPath(String relativePathToContent, String fileName) throws Exception {
		try{
			String contentjson = "{}";
			Session dbssession = getSession();
			Node foldernode = getFolder(dbssession, relativePathToContent, false);
			if(foldernode!=null){
				Node contentnode = getFileContent(dbssession, foldernode, fileName);
				Content content = new Content(contentnode);
				contentjson = content.toJson();
			} else {
				throw new Exception("Folder not found");
			}
			return contentjson;
		} catch(Exception ex){
			throw new Exception("Content not found", ex);
		}	
	}	

	/*
	 * UTILITIES
	 */
	private Session getSession() throws Exception{
		String repositoryurl = dbproperties.getProperty("dbs.dbxr.endpoint");
		String workspacename = dbproperties.getProperty("dbs.dbxr.workspace");
		
		try{
			javax.jcr.Repository repository = JcrUtils.getRepository(repositoryurl);

		    SimpleCredentials creds = new SimpleCredentials(dbproperties.getProperty("dbs.dbxr.username"), dbproperties.getProperty("dbs.dbxr.password").toCharArray());
			try{
			    Session wssession = repository.login(creds, workspacename);
			    return wssession;
			} catch(NoSuchWorkspaceException wsex){
				Session jcrsession = repository.login(creds);
				jcrsession.getWorkspace().createWorkspace(workspacename);
				jcrsession = repository.login(creds, workspacename);
				return jcrsession;
			} catch(LoginException lwex){
				throw new Exception("Unable to login to the workspace " + workspacename);
			} catch(AccessDeniedException lwex){
				throw new Exception("Unable to login to the workspace " + workspacename);
			}
		} catch(RepositoryException rpex){
			throw new Exception("Unable to locate the repository: " + repositoryurl);
		}
		
	}
	
	private Node getFolder(Session dbsSession, String path, boolean createIfNotFound) throws Exception {
		try{
			Node foldernode = dbsSession.getNode("/" + path);
			return foldernode;
		} catch(PathNotFoundException notfoundex){
			if(createIfNotFound){
				try{
					Node root = dbsSession.getRootNode();
					Node foldernode = createFolder(dbsSession, root, path);
					return foldernode;
				} catch(Exception ex) {
					throw new Exception("Unable to create new folder");
				}
			} else {
				return null;
			}
		} catch(Exception ex){
			throw new Exception("Unable to get folder.");
		}
	}
		
	private String makeRelPath(String tenantName, String applicationName){
		return makeRelPath(tenantName, applicationName, null, null);
	}
	private String makeRelPath(String tenantName, String applicationName, String subFolder){
		return makeRelPath(tenantName, applicationName, subFolder, null);
	}
	private String makeRelPath(String tenantName, String applicationName, String subFolder, String parentName){
		String path = tenantName;
		if((applicationName!=null)&&(!applicationName.equals(""))) path += "/" + applicationName;
		if((subFolder!=null)&&(!subFolder.equals(""))) path += "/" + subFolder;
		if((parentName!=null)&&(!parentName.equals(""))) path += "/" + parentName;
		return path;
	}
	
	private InputStream getContentResource(String resourceRelPath){
		InputStream returncontent = null;
		returncontent = JCRRMIContentRepository.class.getResourceAsStream(resourceRelPath);
		return returncontent;
	}
	
	
	/*
	 * JCR FUNCTIONS
	 */
	private Node createFolder(Session session, Node parentNode, String folderName) throws ItemExistsException, PathNotFoundException, VersionException, ConstraintViolationException, LockException, RepositoryException{
		Node folderNode = parentNode;
		String[] folderarr = folderName.split("/");
		for(String foldername: folderarr){
			if(folderNode.hasNode(foldername)){
				folderNode = folderNode.getNode(foldername);
			} else {
				folderNode = folderNode.addNode(foldername, "nt:folder");
			}
		}
		session.save();
		return folderNode;
	}

	private Node getFileContent(Session session, Node folderNode, String filename) throws Exception
    {
		Node contentNode = null;
		if(folderNode.hasNode(filename)){
			Node filenode = folderNode.getNode(filename);
			contentNode = filenode.getNode("jcr:content");
		}
		return contentNode;
    }
	
	private Node updateFile(Session session, Node folderNode, String filename, InputStream fileContent, String mimeType) throws Exception
    {
		Binary  binary= session.getValueFactory().createBinary(fileContent);

		Node filenode = null;
		if(folderNode.hasNode(filename)){
			filenode = folderNode.getNode(filename);
			Node resNode = filenode.getNode("jcr:content");
	        resNode.setProperty ("jcr:data", binary);
	        Calendar lastModified = Calendar.getInstance ();
	        resNode.setProperty ("jcr:lastModified", lastModified);
		} else {
	        filenode = folderNode.addNode(filename, "nt:file");
	        //create the mandatory child node - jcr:content
	        Node resNode = filenode.addNode ("jcr:content", "nt:resource");
	        //resNode.setProperty ("jcr:mimeType", binary.getStream();
	        resNode.setProperty ("jcr:encoding", "UTF-8");
	        resNode.setProperty ("jcr:data", binary);
	        Calendar lastModified = Calendar.getInstance ();
	        resNode.setProperty ("jcr:lastModified", lastModified);
		}
        session.save();
        
        return filenode;
    }

}
