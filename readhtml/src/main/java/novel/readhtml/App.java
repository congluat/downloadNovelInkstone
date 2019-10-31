package novel.readhtml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Hello world!
 *
 */
public class App {

	public static String FOLDER = "C:\\Users\\HEE-VN\\Downloads\\test";
	public static String HTML_OUT = "C:/Users/HEE-VN/Desktop/outhtml.html";
	public static String jsonPath = "C:\\Users\\HEE-VN\\Documents\\workspace\\eclipse\\downloadNovel\\filelist.json";
	
	public static Map<String, String> htmlMap = new LinkedHashMap<String, String>();
	public static List<Page> listChapters = new LinkedList<Page>();

	public static List<String> failedChap = new LinkedList<>();
	
	public static void main(String[] args) {
		File folder = new File(FOLDER);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			readHtmlFile(listOfFiles[i]);
		}
		loadJson();
		exportHtml();
		System.out.println("FAILED REPORT");
		for (String s : failedChap) {
			System.out.println(s);
		}
	}

	public static void exportHtml() {
		try (FileWriter writer = new FileWriter(HTML_OUT); BufferedWriter bw = new BufferedWriter(writer)) {
			for (int i = 0; i < listChapters.size(); i++) {
				if (htmlMap.get(listChapters.get(i).getChaptertitle()) != null) {
					bw.write(htmlMap.get(listChapters.get(i).getChaptertitle()));
					/*System.out.println("Chapter - " + listChapters.get(i).getIndex() + " - "
							+ listChapters.get(i).getChaptertitle() + " ----------- DONE");*/
				} else {
					failedChap.add("Chapter - " + listChapters.get(i).getIndex() + " - "
							+ listChapters.get(i).getChaptertitle() + " ----------- FAILED");
					/*System.out.println("Chapter - " + listChapters.get(i).getIndex() + " - "
							+ listChapters.get(i).getChaptertitle() + " ----------- FAILED");*/
				}

			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	
	public static void loadJson() {

		String jsonString = readJsonFile(jsonPath);

		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray result = jsonObject.getJSONArray("list");
			Page p = null;
			for (int i = 0; i < result.length(); i++) {
				JSONObject j = result.getJSONObject(i);
				String index = j.getString("index");
				String ccid = j.getString("CCID");
				String title = j.getString("chaptertitle");
				p = new Page();
				p.setIndex(index);
				p.setChaptertitle(title);
				p.setcCID(ccid);
				listChapters.add(p);

			}
		} catch (JSONException e) {
			e.printStackTrace();
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

	public static void readHtmlFile(File file) {

		Document doc;
		try {
			doc = Jsoup.parse(file, "utf-8");

			// System.out.println("File: "+ file.getName());

			Element article = doc
					.selectFirst("html body div.g_mn div.g_mn div.g_scroll div.edit-pre-bd div.g_max_wrap.article");
			htmlMap.put(article.select("h3").text(), article.html());
			// System.out.println(article.html());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
