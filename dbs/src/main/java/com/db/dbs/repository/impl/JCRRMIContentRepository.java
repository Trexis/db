package com.db.dbs.repository.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

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

	public InputStream findContentByPath(String relativePathToContent, String fileName) throws Exception {
		try{
			InputStream content = null;
			Session dbssession = getSession();
			Node foldernode = getFolder(dbssession, relativePathToContent, false);
			if(foldernode!=null){
				Node contentnode = getFileContent(dbssession, foldernode, fileName);
				Binary binary = contentnode.getProperty("jcr:data").getBinary();
				content = binary.getStream();
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
			}
			return contentjson;
		} catch(Exception ex){
			throw new Exception("Content not found", ex);
		}	
	}	

	public String findPageContent(String tenantName, String applicationName, String pageName) throws Exception {
		try{
			String pathtocontent = makeRelPath(tenantName, applicationName);
			InputStream is = findContentByPath(pathtocontent, pageName + ".html");
			return IOUtils.toString(is, "utf-8");
		} catch(Exception ex){
			throw new Exception("Page Content not found", ex);
		}
	}
	
	public String findPageContentAsJson(String tenantName, String applicationName, String pageName) throws Exception {
		try{
			String pathtocontent = makeRelPath(tenantName, applicationName);
			return findContentAsJsonByPath(pathtocontent, pageName + ".html");
		} catch(Exception ex){
			throw new Exception("Page Content not found", ex);
		}
	}

	public String findComponentContent(String tenantName, String applicationName, String pageName, String componentName) throws Exception {
		try{
			String pathtocontent = makeRelPath(tenantName, applicationName, pageName);
			InputStream is = findContentByPath(pathtocontent, componentName + ".html");
			return IOUtils.toString(is, "utf-8");
		} catch(Exception ex){
			throw new Exception("Component Content not found", ex);
		}
	}

	
	public void createPage(String tenantName, String applicationName, String pageName, String content) throws Exception {
		Session dbssession = getSession();
		InputStream fileContent = new ByteArrayInputStream(content.getBytes("UTF-8"));
		createPage(dbssession, tenantName, applicationName, pageName, fileContent);
	}
	private void createPage(Session dbsSession, String tenantName, String applicationName, String pageName, InputStream content) throws Exception {
		String relfolderpath = makeRelPath(tenantName, applicationName);
		try{
			Node folder = getFolder(dbsSession, relfolderpath, true);
			Node pagefile = updateFile(dbsSession, folder, pageName + ".html", content, "text/html");
			
		} catch (Exception ex){
			throw new Exception("Unable to create page: " + relfolderpath + "/" + pageName);
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
		return makeRelPath(tenantName, applicationName, null);
	}
	private String makeRelPath(String tenantName, String applicationName, String pageName){
		String path = tenantName;
		if((applicationName!=null)&&(!applicationName.equals(""))) path += "/" + applicationName;
		if(pageName!=null) path += "/" + pageName;
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
