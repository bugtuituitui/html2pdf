package org.demo;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;

/**
 * html 填充数据渲染 转 pdf
 */
public class HtmlToPdf {

    /**
     * 使用Freemarker引擎加载HTML模板文件并填充变量值，并将HTML字符串转换为PDF文件
     *
     * @param
     * @return
     * @throws Exception
     */
    public static String generatePDF(String templateDir, String templateName, String pdfPath, String fileName) throws Exception {
        // 使用Freemarker引擎加载HTML模板文件并填充变量值
        TemplateLoader templateLoader = new FileTemplateLoader(new File(templateDir));
        Configuration cfg = new Configuration(Configuration.getVersion());
        cfg.setTemplateLoader(templateLoader);

        //读取文件
        Template template = cfg.getTemplate(templateName, "UTF-8");
        //或者定义变量
        String templates = "            <html>\n" +
                "                <head>\n" +
                "                    <meta charset=\"UTF-8\">\n" +
                "                    <style>\n" +
                "                        body { font-family: 'SimSun'; }\n" +
                "                        .bold { font-weight: bold; }\n" +
                "                        .italic { font-style: italic; }\n" +
                "                    </style>\n" +
                "                </head>\n" +
                "                <body>\n" +
                "                    <p>这是中文文本。</p>\n" +
                "                    <p class=\"bold\">这是粗体文本。</p>\n" +
                "                    <p class=\"italic\">这是斜体文本。</p>\n" +
                "                </body>\n" +
                "            </html>";
        System.out.println(templates);
/*        StringWriter out = new StringWriter();
        template.process(data, out);
        out.flush();
        String htmlContent = out.toString();*/
        //return convertHtmlToPdf(template.toString(), pdfPath, fileName);
        return convertHtmlToPdf(template.toString(), pdfPath, fileName);
    }

    /**
     * 使用iText 7将HTML字符串转换为PDF文件，并返回PDF文件的二进制数据
     *
     * @param htmlString 待转换的HTML字符串
     * @return 返回生成的PDF文件内容
     * @throws IOException
     */
    private static String convertHtmlToPdf(String htmlString, String path, String fileName) throws IOException {
        File compressedImageFile = new File(path, fileName);
        OutputStream os = new FileOutputStream(compressedImageFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, new PageSize(600.0F, 1150.0F));
        // 设置左、右、上、下四个边距的值，以点（pt）为单位
        document.setMargins(0, 0, 0, 0);
        // 设置中文字体
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        //添加自定义字体，例如微软雅黑
        PdfFont songti1 = PdfFontFactory.createFont(FontProgramFactory.createFont("fonts/SongtiSC-Black.ttf"));
        fontProvider.addFont(songti1.getFontProgram(), PdfEncodings.IDENTITY_H);
        PdfFont songti2 = PdfFontFactory.createFont(FontProgramFactory.createFont("fonts/SongtiSC-Bold.ttf"));
        fontProvider.addFont(songti2.getFontProgram(), PdfEncodings.IDENTITY_H);
        PdfFont songti3 = PdfFontFactory.createFont(FontProgramFactory.createFont("fonts/SongtiSC-Light.ttf"));
        fontProvider.addFont(songti3.getFontProgram(), PdfEncodings.IDENTITY_H);
        PdfFont songti4 = PdfFontFactory.createFont(FontProgramFactory.createFont("fonts/SongtiSC-Regular.ttf"));
        fontProvider.addFont(songti4.getFontProgram(), PdfEncodings.IDENTITY_H);


        ConverterProperties converterProps = new ConverterProperties();
        converterProps.setFontProvider(fontProvider);
        // 调用HtmlConverter类的convertToPdf函数，将HTML字符串转换为PDF文件
        converterProps.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.PRINT));
        converterProps.setCssApplierFactory(new DefaultCssApplierFactory());
        HtmlConverter.convertToPdf(htmlString, pdf, converterProps);
        pdf.close();
        // 将PDF文件转换为字节数组并返回
        outputStream.writeTo(os);
        return compressedImageFile.getPath();
    }

    public static void main(String[] args) throws Exception {
        generatePDF("E:\\my-p\\java-p\\backend-framework", "test.html", "E:\\my-p\\java-p\\backend-framework", "测试程序生成的.pdf");
    }

}
