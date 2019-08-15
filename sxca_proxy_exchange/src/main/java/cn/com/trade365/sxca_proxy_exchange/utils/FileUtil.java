package cn.com.trade365.sxca_proxy_exchange.utils;

/**
 * 此类中封装一些常用的文件操作。
 * 所有方法都是静态方法，不需要生成此类的实例，
 * 为避免生成此类的实例，构造方法被申明为private类型的。
 * 
 */

import cn.hutool.core.util.RandomUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FileUtil {
	private static Log log = LogFactory.getLog(FileUtil.class);
	private static String tempDir = FileUtils.getTempDirectoryPath();
	/**
	 * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
	 */
	private FileUtil() {

	}

	public static String getTempDirectory() {
		return tempDir;
	}

	public static void setTempDirectory(String temp) {
		tempDir = temp;
	}

	public static String getTempFilePath() {
		Calendar calendar = new GregorianCalendar();
		return String.format("%s/%s/%s/%s", calendar.get(Calendar.YEAR),
				org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(calendar.get(Calendar.MONTH)+1),2,'0'),
				calendar.get(Calendar.DATE), RandomUtil.randomUUID());
	}

	public static File getTempFile() {
		return getTempFile("tmp");
	}

	public static File getTempFile(String suffix) {
	    File file;
		if (suffix == null || suffix.isEmpty()) {
			file = new File(tempDir, getTempFilePath() + ".tmp");
		} else {
			file = new File(tempDir, getTempFilePath() + "." + suffix);
		}
		if (!file.getParentFile().exists()) {
		    file.getParentFile().mkdirs();
        }
        return file;
	}

	public static File asTempFile(String fileName) {
		File file = new File(tempDir, getTempFilePath() + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
	}

	public static File fixedTempFile(String fileName) {
		return new File(tempDir, fileName);
	}

	public static String getFileWithoutExt(String filename) {
		if (filename == null || filename.isEmpty()) return filename;
		if (filename.lastIndexOf(".") == -1) return filename;
		return filename.substring(0, filename.lastIndexOf("."));
	}

    /**
     * 处理非机构化文档表中的doc文件获取html内容
     * @param filePath
     * @return
     * @throws Exception 
     */
	public static String getDocFileContent(String filePath,String fileName) throws Exception {
		InputStream in = null;
		try {
			File input = FileUtil.getTempFile("htm");
			File tempFile=FileUtil.asTempFile(fileName);
			IOUtils.copy(new FileInputStream(filePath+fileName),new FileOutputStream(tempFile));
			new PoiWordToHtml().convert(tempFile.getAbsolutePath(), input.getAbsolutePath(), "GBK");
			tempFile.delete();
			embedLocalHtmlImages(input);
			return IOUtils.toString(new FileInputStream(input), "GBK");
		} catch (Exception e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static void embedLocalHtmlImages(File htmlFile) throws IOException {
		Document doc = Jsoup.parse(new FileInputStream(htmlFile), "GBK", "");
		Elements links = doc.getElementsByTag("img");
		if(links.size() > 0){
			for (Element link : links) {
				String linkHref = link.attr("src");
				if (!linkHref.startsWith("data:image")) {
					File imgFile = new File(new File(htmlFile.getParent(),getFileWithoutExt(htmlFile.getName()) + ".files"), linkHref.substring(linkHref.lastIndexOf("/") + 1));
					if (imgFile.exists()) {
						String encodeStr = Base64.getEncoder().encodeToString(IOUtils.toByteArray(new FileInputStream(imgFile)));
                        String imgPath = imgFile.getPath();
                        String imgExt = imgPath.substring(imgPath.lastIndexOf(".") + 1);
                        link.attr("src", String.format("data:image/%s;base64,%s", imgExt, encodeStr));
					}
				}
			}
            IOUtils.write(doc.toString(), new FileOutputStream(htmlFile), "GBK");
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getDocFileContent("C:\\Users\\Administrator\\Desktop\\","201811221423043251085556430.doc"));
	}

}
