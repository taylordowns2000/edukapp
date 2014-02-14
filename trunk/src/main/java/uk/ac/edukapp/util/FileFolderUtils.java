package uk.ac.edukapp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileFolderUtils {

	private static final String tmp = System.getProperty("java.io.tmpdir");

	/**
	 * 
	 * @param fileName
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static File tempFileFromStream(String fileName, InputStream is)
			throws IOException {
		File f = new File(tmp + '/' + fileName);
		FileOutputStream out = new FileOutputStream(f);
		byte[] buff = new byte[8196];
		int i = 0;
		while ((i = is.read(buff)) != -1) {
			out.write(buff, 0, i);
		}
		out.close();
		return f;
	}

	/**
	 * 
	 * @param path
	 * @param content
	 * @throws IOException
	 */
	public static void writeStringToFile(String path, String content)
			throws IOException {
		File f = new File(path);
		FileOutputStream out = new FileOutputStream(f);
		out.write(content.getBytes());
		out.close();
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static File createTempDirectory(String name) {
		File tmpFolder = new File(tmp + '/' + name);
		tmpFolder.mkdir();
		return tmpFolder;
	}

	/**
	 * 
	 * @param input
	 * @param directoryPath
	 * @throws IOException
	 */
	public static void streamFileToDirectory(InputStream input,
			String directoryPath) throws IOException {
		File f = new File(directoryPath);
		FileOutputStream out = new FileOutputStream(f);
		int i = 0;
		byte[] buff = new byte[8196];
		while ((i = input.read(buff)) != -1) {
			out.write(buff, 0, i);
		}
		out.close();
	}

	/**
	 * 
	 * @param theFile
	 * @param theFolder
	 * @return
	 * @throws IOException
	 */
	public static File moveFileToFolder(File theFile, File theFolder)
			throws IOException {
		String folderDirectory = theFolder.getCanonicalPath();
		// move the file to it
		String fileName = theFile.getName();
		theFile.renameTo(new File(folderDirectory + "/" + fileName));
		return theFile;
	}

	/**
	 * 
	 * @param fileName
	 * @param folder
	 * @return
	 */
	public static boolean findFilenameInFolder(String fileName, File folder) {
		File[] contents = folder.listFiles();
		if (contents == null) {
			return false;
		}
		for (int i = 0; i < contents.length; i++) {
			File aFile = contents[i];
			String aFileName = aFile.getName();
			if (aFileName.equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param folder
	 * @param pattern
	 * @return
	 */
	public static File[] listFilesInFolderWithSuffix(File folder, final String suffix) {
		FilenameFilter textFilter = null;
		if (suffix != null) {

			//final Pattern r = Pattern.compile(pattern);

			textFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(suffix);
				}
			};
			return folder.listFiles(textFilter);
		} else {
			return folder.listFiles();
		}
	}
	
	
	
	public static File[] listFilesInFolderWithPattern(File folder, String pattern) {
		FilenameFilter textFilter = null;
		if (pattern != null) {

			final Pattern r = Pattern.compile(pattern);

			textFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					Matcher m = r.matcher(name);
					if (m.find()) {
						return true;
					} else {
						return false;
					}
				}
			};
			return folder.listFiles(textFilter);
		} else {
			return folder.listFiles();
		}
	}

	
	/**
	 * Create a new temporary directory. Use something like
	 * {@link #recursiveDelete(File)} to clean this directory up since it isn't
	 * deleted automatically
	 * @return  the new directory
	 * @throws IOException if there is an error creating the temporary directory
	 */
	public static File createTempDir() throws IOException
	{
	    final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
	    File newTempDir;
	    final int maxAttempts = 9;
	    int attemptCount = 0;
	    do {
	        attemptCount++;
	        if(attemptCount > maxAttempts) {
	            throw new IOException(
	                    "The highly improbable has occurred! Failed to " +
	                    "create a unique temporary directory after " +
	                    maxAttempts + " attempts.");
	        }
	        String dirName = UUID.randomUUID().toString();
	        newTempDir = new File(sysTempDir, dirName);
	    } while(newTempDir.exists());

	    if(newTempDir.mkdirs()) {
	        return newTempDir;
	    }
	    else {
	        throw new IOException(
	                "Failed to create temp dir named " +
	                newTempDir.getAbsolutePath());
	    }
	}

	/**
	 * Recursively delete file or directory
	 * @param fileOrDir
	 *          the file or dir to delete
	 * @return
	 *          true iff all files are successfully deleted
	 */
	public static boolean recursiveDelete(File fileOrDir)
	{
	    if(fileOrDir.isDirectory())
	    {
	        // recursively delete contents
	        for(File innerFile: fileOrDir.listFiles())
	        {
	            if(!FileFolderUtils.recursiveDelete(innerFile))
	            {
	                return false;
	            }
	        }
	    }

	    return fileOrDir.delete();
	}
}
