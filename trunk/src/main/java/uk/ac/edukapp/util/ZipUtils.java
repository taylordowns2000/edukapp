package uk.ac.edukapp.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ZipUtils {

	public static File zipFolder(String folderPath, String ext)
			throws IOException {
		if (ext == null) {
			ext = ".zip";
		}
		File widgetFile = new File(folderPath + ext);
		addDirectoryToZip(new File(folderPath), widgetFile);

		return widgetFile;
	}
	
	public static File zipFolderToFile ( String folderPath, String filePath) throws IOException {
		File destination = new File ( filePath );
		File source = new File ( folderPath );
		addDirectoryToZip(source, destination);
		return destination;
	}

	public static File unzip(File zipFilePath, String targetFolderPath)
			throws IOException {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		File targetFolder = new File(targetFolderPath);
		if (targetFolder.exists() && targetFolder.isDirectory()) {
			FileUtils.deleteDirectory(targetFolder);
		}
		targetFolder.mkdirs();

		ZipFile zip = new ZipFile(zipFilePath);

		Enumeration<?> entries = zip.entries();
		ZipEntry entry = null;
		try {
			while (entries.hasMoreElements()) {
				entry = (ZipEntry) entries.nextElement();
				if (!entry.isDirectory()) {
					File outFile = new File(targetFolder, entry.getName());
					if (!outFile.getParentFile().exists()) {
						outFile.getParentFile().mkdirs();
					}
					in = new BufferedInputStream(zip.getInputStream(entry));
					out = new BufferedOutputStream(
							new FileOutputStream(outFile));
					IOUtils.copy(in, out);
					in.close();
					out.close();

				}
			}
		} catch (IOException e) {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.flush();
				out.close();
			}
			throw e;
		}

		return targetFolder;
	}
	
	
	
	// packaging code taken from Apache Wookie
	/**
	 * Packages the source file/folder up as a new Zip file
	 * @param source the source file or folder to be zipped
	 * @param target the zip file to create
	 * @throws IOException
	 */
	public static void addDirectoryToZip(File source, File target) throws IOException{
		ZipArchiveOutputStream out = new ZipArchiveOutputStream(target);
		out.setEncoding("UTF-8");
        for(File afile: source.listFiles()){
            pack(afile,out, "");
        }
		out.flush();
		out.close();
	}
	
	/**
	 * Recursively locates and adds files and folders to a zip archive
	 * @param file
	 * @param out
	 * @param path
	 * @throws IOException
	 */
	private static void pack(File file, ZipArchiveOutputStream out, String path) throws IOException {
        if(file.isDirectory()){
            path = path + file.getName() +"/";
            for(File afile: file.listFiles()){
                pack(afile,out, path);
            }
        } else {
        	ZipArchiveEntry entry = (ZipArchiveEntry) out.createArchiveEntry(file, path + file.getName());
    		out.putArchiveEntry(entry);
            byte[] buf = new byte[1024];
            int len;
            FileInputStream in = new FileInputStream(file);
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
              }
    		out.closeArchiveEntry();
        }
    }}
