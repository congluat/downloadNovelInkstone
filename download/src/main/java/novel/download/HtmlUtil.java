package novel.download;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlUtil {

	public static String FOLDER = "C:\\Users\\HEE-VN\\Downloads\\test";
	public static Map<String, String> fromHtml = new LinkedHashMap<>();

	public static void processHtml() {

		File folder = new File(FOLDER);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			readFile(listOfFiles[i]);
		}

	}

	public static void readFile(File file) {

		Document doc;
		try {
			doc = Jsoup.parse(file, "utf-8");

			// System.out.println("File: "+ file.getName());

			Element article = doc
					.selectFirst("html body div.g_mn div.g_mn div.g_scroll div.edit-pre-bd div.g_max_wrap.article");
			fromHtml.put(article.select("h3").text(), article.html());
			// System.out.println(article.html());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
