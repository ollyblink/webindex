/**
 *   
 * Special thanks to:
 * Damien Palacio
 * Geocomputation - UZH
 * 2012
 * 
 */

package index.spatialindex.utils.georeferencing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Useful functions for URL
 * 
 * @author Damien Palacio
 */
public class YPMQuery {

	/**
	 * Download a file
	 * 
	 * @param url
	 *            URL of the file
	 * @param file
	 *            File saved
	 */
	static public void download(String url, String file) {
		download(url, file, "", null);
	}

	/**
	 * Download a file
	 * 
	 * @param url
	 *            URL of the file
	 * @param file
	 *            File saved
	 * @param method
	 *            Method used for the download
	 * @param params
	 *            Parameters for the connection
	 */
	static public void download(String url, String file, String method, HashMap<String, String> params) {
		URL urlFile = null;
		InputStream input = null;
		InputStreamReader inpReader = null;
		BufferedReader buffRead = null;
		FileWriter fileWrite = null;
		BufferedWriter buffWrite = null;
		String line = new String();

		try {
			// second try in case closing problem
			try {
				urlFile = new URL(url);

				if (method.equals("POST")) {
					HttpURLConnection con = (HttpURLConnection) urlFile.openConnection();

					con.setRequestMethod(method);

					con.setDoInput(true);
					con.setDoOutput(true);

					OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
					for (Entry<String, String> param : params.entrySet()) {
						out.write(param.getKey() + "=" + URLEncoder.encode(param.getValue(), "UTF-8") + "&");
					}
					out.close();
					input = con.getInputStream();
				} else {
					input = urlFile.openStream();
				}

				inpReader = new InputStreamReader(input);
				buffRead = new BufferedReader(inpReader);

				fileWrite = new FileWriter(file);
				buffWrite = new BufferedWriter(fileWrite);

				// File reading line by line
				line = buffRead.readLine();
				while (line != null) {
					buffWrite.write(line + "\n");
					buffWrite.flush();
					line = buffRead.readLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// Stream closure if open
				if (buffRead != null) {
					buffRead.close();
				}
				if (inpReader != null) {
					inpReader.close();
				}
				if (buffWrite != null) {
					buffRead.close();
				}
				if (fileWrite != null) {
					fileWrite.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
