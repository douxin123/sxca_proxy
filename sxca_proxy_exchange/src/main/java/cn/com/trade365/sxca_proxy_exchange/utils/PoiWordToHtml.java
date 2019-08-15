package cn.com.trade365.sxca_proxy_exchange.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

/**
 * Created by liang on 2017/5/17.
 *
 * 此类只能转换简单的word文档（例如公告），支持doc和docx格式，不支持word 2003的xml格式.
 * 请勿用于复杂的文档转换，复杂的文档转换只能使用office自身的服务进行.
 */
public class PoiWordToHtml {

    public void convert(String docPath, String htmlPath, String htmlEncoding) throws IOException, ParserConfigurationException, TransformerException {

        String fileNameNoExt = htmlPath.substring(0, htmlPath.lastIndexOf("."));
        String fileExt = docPath.substring(docPath.lastIndexOf(".") + 1).toLowerCase();
        String imgFolder = fileNameNoExt + ".files";

        if (fileExt.equals("doc")) {
            try {
                docToHtml(docPath, htmlPath, imgFolder, htmlEncoding);
            } catch (OfficeXmlFileException e) {
                docxToHtml(docPath, htmlPath, imgFolder, htmlEncoding);
            }
        } else {
            docxToHtml(docPath, htmlPath, imgFolder, htmlEncoding);
        }

        int converted = convertAllEmfToPng(new File(imgFolder));
        if (converted > 0) {
            org.jsoup.nodes.Document document = Jsoup.parse(new File(htmlPath), htmlEncoding);
            Elements elements = document.getElementsByTag("img");
            for (Element element : elements) {
                String linkHref = element.attr("src");
                if (linkHref.toLowerCase().endsWith(".emf")) {
                    element.attr("src", linkHref.substring(0, linkHref.lastIndexOf(".")) + ".png");
                }
            }
            FileUtils.write(new File(htmlPath), document.html(), htmlEncoding);
        }

    }

    private void docxToHtml(String docPath, String htmlPath, String imgFolder, String encoding) throws IOException {
        XWPFDocument word2007Doc = new XWPFDocument(new FileInputStream(docPath));
        File imageFolderFile = new File(imgFolder);
        XHTMLOptions options = XHTMLOptions.create().URIResolver(new BasicURIResolver(imageFolderFile.getName()));
        options.setExtractor(new FileImageExtractor(imageFolderFile));

        File output = new File(htmlPath);
        try (OutputStream out = new FileOutputStream(output)) {
            XHTMLConverter.getInstance().convert(word2007Doc, out, options);
        }

        //Add meta charset info, change the document to gbk encoding
        org.jsoup.nodes.Document document = Jsoup.parse(output,"utf-8");
        document.charset(Charset.forName(encoding));
        FileUtils.write(output, document.html(), encoding);
    }

    private void docToHtml(String docPath, String htmlPath, final String imgFolder, String encoding) throws IOException, ParserConfigurationException, TransformerException {
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(docPath));
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] bytes, PictureType pictureType, String s, float v, float v1) {
                return imgFolder.substring(imgFolder.lastIndexOf("/") + 1) + "/" + s;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        List pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null && pics.size() > 0) {
            boolean ignored =  new File(imgFolder).mkdirs();
            for (Object pic1 : pics) {
                Picture pic = (Picture) pic1;
                pic.writeImageContent(new FileOutputStream(imgFolder + "/" + pic.suggestFullFileName()));
            }
        }
        Document htmlDocument = wordToHtmlConverter.getDocument();
        File outFile = new File(htmlPath);
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(outStream);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, encoding);
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            FileUtils.write(outFile, new String(outStream.toByteArray(), encoding), encoding);
        }
    }

    private int convertAllEmfToPng(File imgFolder) throws IOException {
        int num = 0;
        if (!imgFolder.exists()) return num;

        Collection<File> emfFiles = FileUtils.listFiles(imgFolder, FileFilterUtils.suffixFileFilter("emf", IOCase.INSENSITIVE), FileFilterUtils.trueFileFilter());
        for (File emfFile : emfFiles) {
            File pngFile = new File(emfFile.getPath().substring(0, emfFile.getPath().lastIndexOf(".")) + ".png");
            try {
                emfToPng(emfFile, pngFile);
                num++;
            } catch (Exception ignored) {
            }
        }

        return num;
    }

    private void emfToPng(File emfFile, File pngFile) throws IOException {
        EMFInputStream inputStream = new EMFInputStream(new FileInputStream(emfFile), EMFInputStream.DEFAULT_VERSION);
        EMFRenderer emfRenderer = new EMFRenderer(inputStream);

        final int width = (int)inputStream.readHeader().getBounds().getWidth();
        final int height = (int)inputStream.readHeader().getBounds().getHeight();
        final BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = result.createGraphics();
        emfRenderer.paint(g2);

        ImageIO.write(result, "png", pngFile);
    }

    public static void main(String[] args) throws Throwable {
        new PoiWordToHtml().convert("c:/temp/电子招投标系统功能测试报告-0324.doc", "c:/temp/11.htm", "gbk");
    }
}