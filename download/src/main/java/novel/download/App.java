package novel.download;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App {

	public static String URL = "https://inkstone.webnovel.com/chapter/view/cbid/newCBID-X/ccid/CCID-X/tcbid/CBID-X";
	public static String jsonPath = "C:\\Users\\HEE-VN\\Documents\\workspace\\eclipse\\downloadNovel\\filelist.json";
	public static List<String> listTitle = new LinkedList<>();

	public static void main(String[] args) {
		String jsonString = readJsonFile(jsonPath);

		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray result = jsonObject.getJSONArray("list");
			for (int i = 0; i < result.length(); i++) {
				JSONObject j = result.getJSONObject(i);
				String index = j.getString("index");
				String newCBID = null;
				try {
					newCBID = j.getString("newCBID");
				} catch (Exception e) {
				}
				String ccid = j.getString("CCID");
				String CBID = null;
				try {
					CBID = j.getString("CBID");
				} catch (Exception e) {
				}

				String title = j.getString("chaptertitle");
				listTitle.add(title);
				if (newCBID == null || newCBID.equals("")) {
					System.out.println(URL.replaceAll("CCID-X", ccid).replaceAll("newCBID-X", CBID)
							.replaceAll("/tcbid/CBID-X", ""));
				} else {
					System.out.println(
							URL.replaceAll("CCID-X", ccid).replaceAll("newCBID-X", newCBID).replaceAll("CBID-X", CBID));
				}
				// printOutHtml(URL.replaceAll("XXX", ccid));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void writeListToFile() {
		try (FileWriter writer = new FileWriter("C:/Users/HEE-VN/Desktop/title.txt");
				BufferedWriter bw = new BufferedWriter(writer)) {
			for (int i = 0; i < listTitle.size(); i++) {
				bw.write(listTitle.get(i));
			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}

	public static String readJsonFile(String filePath) {
		String jsonData = "";
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				jsonData += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return jsonData;
	}

	public static void printOutHtml(String urlString) {
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		try {

			url = new URL(urlString);
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
	}
}
