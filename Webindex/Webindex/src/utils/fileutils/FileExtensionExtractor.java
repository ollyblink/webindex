package utils.fileutils;

public class FileExtensionExtractor {
	public static String getExtension(String docToAdd) {
		String extension = null;
		try {
			extension = docToAdd.substring(docToAdd.lastIndexOf("."), docToAdd.length());
			extension = extension.replace(".", "");
		} catch (StringIndexOutOfBoundsException s) {
			System.err.println("Could not find any extension on this file!");
			s.printStackTrace();
		}
		System.out.println(extension);
		return extension;
	}
}
