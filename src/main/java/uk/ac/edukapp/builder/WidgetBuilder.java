/*
 *  (c) 2014 University of Bolton
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.edukapp.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.util.FileFolderUtils;
import uk.ac.edukapp.util.ZipUtils;

public class WidgetBuilder {

	
	private static final String tmp = System.getProperty("java.io.tmpdir");
	static Logger logger = Logger.getLogger(WidgetBuilder.class.getName());

	private void copyIconToWidgetDirectory(String iconName, String widgetDirectory, ServletContext context) throws IOException {
		String path = context.getRealPath("icons");
		File icon = new File( path+"/"+iconName );
		InputStream is = new FileInputStream(icon);
		String iconPath = widgetDirectory+"/"+iconName;
		FileFolderUtils.streamFileToDirectory(is, iconPath);
	}
	
	
	public boolean checkWidgetCanBeEdited(Widgetprofile widgetProfile ) {
		
		String uri = widgetProfile.getWidId();
		
		// this is crude and error prone but will do for now
		
		if ( uri.indexOf("/widgets/flash") != -1 ) {
			return true;
		}
		if ( uri.indexOf("/widgets/webfolder") != -1 ) {
			return true;
		}
		if ( uri.indexOf("/widget/embed") != -1 ) {
			return true;
		}
		return false;
	}
	
	
	public WidgetData getDataForWidget (Widgetprofile widgetProfile ) {
		WidgetData widgetData = new WidgetData();
		
		
		
		return widgetData;
	}
	
	
	/**
	 * 
	 * @param mediaFile
	 * @param name
	 * @param username
	 * @param description
	 * @param width
	 * @param height
	 * @param icon
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public File buildWidgetFromFile ( File mediaFile, 
										String name, 
										String username, 
										String description, 
										String width, 
										String height,
										String icon,
										ServletContext context ) throws IOException {
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		String author = userAccount.getAccountInfo().getRealname();
		
		// sort widget name
		name = this.zapTitleGremlins(name);
		// create a folder
		String safeName = this.createSafeFileName(name);
		
		File widgetFolder = FileFolderUtils.createTempDirectory ( safeName );
		FileFolderUtils.moveFileToFolder(mediaFile, widgetFolder);
		
		String folderDirectory = widgetFolder.getCanonicalPath();
		
		// build an enclosing html file
		String objectTag = "<object width=\""+width+"\" height=\""+height+"\" data=\""+mediaFile.getName()+"\"></object>";
		String html = this.constructIndexHTML(name, objectTag);
		FileFolderUtils.writeStringToFile(folderDirectory+"/index.html", html);
		
		// build the widget config file
		String config = this.constructConfig(safeName, name, "1.0", width, height, description, icon, "index.html", author, "flash");
		FileFolderUtils.writeStringToFile(folderDirectory+"/config.xml", config);

		// put the default widget flash widget icon in it
		this.copyIconToWidgetDirectory(icon, folderDirectory, context);
		
		// zip it up
		File widgetFile = ZipUtils.zipFolder(folderDirectory, ".wgt");
		widgetFolder.delete();
		
		// return a file pointer to it
		return widgetFile;
	}
	
	
	
	public File createFolderFromZipFile ( File zipFile, String widgetName ) throws IOException {
		String targetFolderPath = WidgetBuilder.tmp + "/"+widgetName+UUID.randomUUID().toString();
		
		File widgetFolder = ZipUtils.unzip(zipFile, targetFolderPath);
		// depending upon how the zip file has been created the widgetFolder might contain a folder
		// with the files in, this needs to removed and the files copied directly into
		// widgetFolder
		resolveWidgetFolder ( widgetFolder );
		
		return widgetFolder;
	}
	
	
	public List<File> findIndexFilesInWidgetFolder ( File widgetFolder ) {
		ArrayList<File> indexFiles = new ArrayList<File>();
		
		if ( FileFolderUtils.findFilenameInFolder("index.html", widgetFolder)) {
			File indexFile = new File ( widgetFolder+"index.html");
			indexFiles.add(indexFile);
		}
		else if ( FileFolderUtils.findFilenameInFolder("index.htm", widgetFolder)){
			File indexFile = new File ( widgetFolder+"index.htm");
			indexFiles.add(indexFile);
		}
		else {
			File[] htmlFiles = FileFolderUtils.listFilesInFolderWithSuffix(widgetFolder, ".html");
			File[] htmFiles = FileFolderUtils.listFilesInFolderWithSuffix(widgetFolder, ".htm");
			indexFiles.addAll(Arrays.asList(htmlFiles));
			indexFiles.addAll(Arrays.asList(htmFiles));
		}
		return indexFiles;
	}
	
	
	public File buildWidgetFromTempFolder (String tempFolderName,
										String name, 
										String username, 
										String description, 
										String width, 
										String height,
										String icon,
										String chosenIndexFile,
										ServletContext context ) throws IOException {
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		String author = userAccount.getAccountInfo().getRealname();
		String safeName = this.createSafeFileName(tempFolderName);

		String tempFolderPath = WidgetBuilder.tmp + "/"+tempFolderName;
		File widgetFolder = new File(tempFolderPath);
		if ( !widgetFolder.exists()) {
			throw new FileNotFoundException(tempFolderPath);
		}
		
		
		String config = this.constructConfig(safeName, name, "1.0", width, height, description, icon, chosenIndexFile, author, "webfolder");
		FileFolderUtils.writeStringToFile(tempFolderPath+"/config.xml", config);

		// put the default widget flash widget icon in it
		this.copyIconToWidgetDirectory(icon, tempFolderPath, context);
		String widgetPath = WidgetBuilder.tmp + "/"+name+".wgt";
		File widgetFile = ZipUtils.zipFolderToFile(tempFolderPath, widgetPath);
		widgetFolder.delete();
		
		// return a file pointer to it
		return widgetFile;
	}
	
	
	public File buildWidgetFromUrl (String Url, 
										String name, 
										String username, 
										String description, 
										String width, 
										String height,
										String icon,
										ServletContext context ) throws IOException {
		Integer newWidth;
		Integer newHeight;
		try {
			newWidth = Integer.valueOf(width);
			newHeight = Integer.valueOf(height);
			// offset for container
			newWidth = newWidth - 4;
			newHeight = newHeight - 4;
		}
		catch (NumberFormatException exp ) {
			// user defaults
			newWidth = new Integer ( 300 );
			newHeight = new Integer ( 400 );
		}
		String embedCode = "<iFrame src=\""+Url+"\" width=\""+newWidth+"\" height=\""+newHeight+"\"></Frame>";
		return this.buildWidgetFromEmbed(embedCode, name, username, description, width, height, icon, context);
	}
	
	
	/**
	 * 
	 * @param embed
	 * @param name
	 * @param username
	 * @param description
	 * @param width
	 * @param height
	 * @param icon
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public File buildWidgetFromEmbed (String embed, 
										String name, 
										String username, 
										String description, 
										String width, 
										String height,
										String icon,
										ServletContext context ) throws IOException {
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		String author = userAccount.getAccountInfo().getRealname();
		
		// sort widget name
		name = this.zapTitleGremlins(name);
		
		String safeName = this.createSafeFileName(name);
		
		// create a folder
		File widgetFolder = FileFolderUtils.createTempDirectory ( safeName );
		String folderDirectory = widgetFolder.getCanonicalPath();
		
		
		// build an enclosing html file
		String html = this.constructIndexHTML(name, embed);
		FileFolderUtils.writeStringToFile(folderDirectory+"/index.html", html);
		
		// build the widget config file
		String config = this.constructConfig(safeName, name, "1.0", width, height, description, icon, "index.html", author, "embed");
		FileFolderUtils.writeStringToFile(folderDirectory+"/config.xml", config);

		// put the default widget flash widget icon in it
		this.copyIconToWidgetDirectory(icon, folderDirectory, context);
		
		// zip it up
		File widgetFile = ZipUtils.zipFolder(folderDirectory, ".wgt");
		widgetFolder.delete();
		
		// return a file pointer to it
		return widgetFile;		
		
	}
	
	

	
	
	private String zapTitleGremlins ( String title )
	{
		//title = title.replaceAll("\\s+", "_");

		return title;
	}
	
	private String createSafeFileName ( String title )
	{
		String safename = title.replaceAll("\\W+", "");
		return safename;
	}
	

	
	
	private String constructConfig ( String widgetIDName, String widgetName, String version, 
									String width, String height, 
									String description, String icon, 
									String contentFile, String author, String builder ) throws IOException {
		String config = this.getResourceAsString("/new_widget_config.xml");
		//defaults
		if ( width == null || width == "" ) {
			width = "200";
		}
		if ( height == null || height == "" ) {
			height = "200";
		}
		if ( description == null ) {
			description = "";
		}
		if ( icon == null || icon == "" ) {
			icon = "default_widget.png";
		}
		if ( contentFile == null || contentFile == "" ) {
			contentFile = "index.html";
		}
		if ( author == null || author == "" ) {
			author = "Apache Wookie Team";
		}
		config = config.replace("{id}", "http://wookie.apache.org/widgets/"+builder+"/"+widgetIDName);
		config = config.replace("{version}", "1.0");
		config = config.replace("{width}", width);
		config = config.replace("{height}", height);
		config = config.replace("{name}", widgetName);
		config = config.replace("{description}", description);
		config = config.replace("{icon}", icon);
		config = config.replace("{content}", contentFile);
		config = config.replace("{author}", author);
		return config;
	}
	
	
	private String constructIndexHTML ( String title, String widgetContent ) throws IOException {
		String html = this.getResourceAsString("/new_widget_index.html");
		html = html.replace("{title}", title);
		html = html.replace("{content}", widgetContent );
		return html;
	}
	
	
	private String getResourceAsString ( String resourcePath ) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(resourcePath);
		return IOUtils.toString(is);
	}
	
	
	private void resolveWidgetFolder ( File widgetFolder ) throws IOException {
		File[] fileList = widgetFolder.listFiles();
		if ( fileList.length == 1 && fileList[0].isDirectory() ) {
			File d = fileList[0];
			File[] innerList = d.listFiles();
			for ( int i = 0; i < innerList.length;i++) {
				FileFolderUtils.moveFileToFolder(innerList[i], widgetFolder);
			}
		}
	}
	

}
