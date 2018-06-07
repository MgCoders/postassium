package coop.magnesium.potassium.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import org.apache.commons.lang3.NotImplementedException;

public class PDFDocument {

    public Document documento;
    private PdfWriter writer;
    public String absolutePath;
    Date dtCreado;
    // FileStream del documento.
    FileOutputStream file = null;
    // MemoryStream
    ByteArrayOutputStream memory = null;

    public Font fb = FontFactory.getFont("Arial", 10, Font.BOLD);
    public Font f = FontFactory.getFont("Arial", 10, Font.NORMAL);
    public Font fu = FontFactory.getFont("Arial", 10, Font.UNDERLINE);
    public Font fbs = FontFactory.getFont("Arial", 8, Font.BOLD);
    public Font fs = FontFactory.getFont("Arial", 8, Font.NORMAL);
    public Font fxs = FontFactory.getFont("Arial", 5, Font.ITALIC);
    public Font fxsb = FontFactory.getFont("Arial", 5, Font.BOLD);
    public Font fbm = FontFactory.getFont("Arial", 15, Font.BOLD);
    public Font fm = FontFactory.getFont("Arial", 15, Font.NORMAL);
    public Font fum = FontFactory.getFont("Arial", 15, Font.UNDERLINE);
    public Font title_bold = FontFactory.getFont("Arial", 18, Font.BOLD);
    public Font title = FontFactory.getFont("Arial", 18, Font.NORMAL);
    public Font title_u = FontFactory.getFont("Arial", 18, Font.UNDERLINE);
    public Font title2 = FontFactory.getFont("Arial", 18, Font.ITALIC);
    public Font big_title = FontFactory.getFont("Arial", 24, Font.BOLD);
    public Font currierNow = FontFactory.getFont(BaseFont.COURIER, 10, Font.NORMAL);
    public Font currierNowS = FontFactory.getFont(BaseFont.COURIER, 7, Font.NORMAL);
    public Font currierNow8 = FontFactory.getFont(BaseFont.COURIER, 8, Font.NORMAL);
    public Font f3 = FontFactory.getFont("Arial", 3, Font.ITALIC);
    public Font f3b = FontFactory.getFont("Arial", 3, Font.BOLD);
    public Font f6 = FontFactory.getFont("Arial", 6, Font.ITALIC);
    public Font f6b = FontFactory.getFont("Arial", 6, Font.BOLD);
    public Font f7 = FontFactory.getFont("Arial", 7, Font.ITALIC);
    public Font f7b = FontFactory.getFont("Arial", 7, Font.BOLD);
    public Font f8 = FontFactory.getFont("Arial", 8, Font.NORMAL);
    public Font f8b = FontFactory.getFont("Arial", 8, Font.BOLD);
    public Font f9 = FontFactory.getFont("Arial", 9, Font.NORMAL);
    public Font f9b = FontFactory.getFont("Arial", 9, Font.BOLD);
    public Font f10 = FontFactory.getFont("Arial", 10, Font.NORMAL);
    public Font f10b = FontFactory.getFont("Arial", 10, Font.BOLD);
    public Font f11 = FontFactory.getFont("Arial", 11, Font.NORMAL);
    public Font f11b = FontFactory.getFont("Arial", 11, Font.BOLD);
    public Font f12 = FontFactory.getFont("Arial", 12, Font.NORMAL);
    public Font f12b = FontFactory.getFont("Arial", 12, Font.BOLD);
    public Font f12u = FontFactory.getFont("Arial", 12, Font.UNDERLINE);

    public PDFDocument(int tipo, String path, String fileName, Boolean inMemory) throws DocumentException {
        switch (tipo) {
        case 0:
            // Se crea un nuevo documento, con con el tamaño deseado,
            this.documento = new Document(PageSize.A4, 10, 10, 20, 20);
            break;
        case 1:
            // Se crea un nuevo documento, con con el tamaño deseado,
            this.documento = new Document(PageSize.A4, 10, 10, 20, 20);
            this.documento.setPageSize(PageSize.A4.rotate());
            break;
        case 2:
            // Se crea un nuevo documento, con con el tamaño deseado,
            this.documento = new Document(PageSize.A4, 10, 10, 10, 10);
            break;
        case 3:
            // Se crea un nuevo documento, con con el tamaño deseado,
            this.documento = new Document(PageSize.A4, 10, 10, 10, 0);
            break;
        }

        this.ConstructWriter(path, fileName, inMemory);

        this.documento.open();
    }

    private void ConstructWriter(String path, String fileName, Boolean inMemory) throws DocumentException {
        if (!inMemory) {
            // Se indica el directorio donde se guardara el archivo pdf, si no existe se
            // crea.
            /*
             * if (!Directory.Exists(path)) Directory.CreateDirectory(path);
             * 
             * this.file = new FileStream(path + fileName, FileMode.Create); this.writer =
             * PdfWriter.GetInstance(this.documento, file);
             * 
             * this.absolutePath = path + fileName;
             */
            throw new NotImplementedException("No esta implementado la construcción del PDF en el File System.");
        } else {
            memory = new ByteArrayOutputStream();
            this.writer = PdfWriter.getInstance(this.documento, this.memory);
            // PdfWriter.getInstance(this.documento, this.memory);
        }
    }

    public byte[] getBytes() {
        if (memory != null)
            return this.memory.toByteArray();
        else
            // return WebSite.Models.FuncionesGlobales.ReadFile(this.absolutePath, null,
            // true);
            throw new NotImplementedException("No esta implementado la construcción del PDF en el File System.");
    }

    public void addTituloContenido(List<Paragraph> paragraph, Float minHeigth) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(minHeigth);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(2);

        for (Iterator<Paragraph> i = paragraph.iterator(); i.hasNext();) {
            Paragraph item = i.next();
            item.setLeading(0, 1);
            cell.addElement(item);
        }

        table.addCell(cell);
        this.documento.add(table);
    }

    public void addImageBase64(String title, String base64, Float horizontalScale, Float indentation) throws DocumentException, IOException {

        Image img = new Base64ImageProvider().retrieve(base64);

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(1);

        if (title != null && !title.equals("")) {
            Paragraph p = new Paragraph();
            Chunk ch = new Chunk(title, this.title);
            p.add(ch);
            p.setAlignment(Paragraph.ALIGN_CENTER);
            p.setLeading(0, 1);
            cell.addElement(p);
        }

        float scaler = ((this.documento.getPageSize().getWidth() - this.documento.leftMargin()
        - this.documento.rightMargin() - indentation) / img.getWidth()) * 100;

        img.scalePercent(scaler * horizontalScale);

        img.setAlignment(Element.ALIGN_CENTER);

        cell.addElement(img);
        cell.setPaddingTop(10f);
        table.addCell(cell);
        
        this.documento.add(table);
    }

    public void addSpace(Float heigth) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(heigth);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(0);
        table.addCell(cell);
        this.documento.add(table);
    }

    public Image getImageFromBase64(String base64) throws DocumentException, IOException {

        Image img = new Base64ImageProvider().retrieve(base64);
        return img;
    }

    public void addHTML(String title, String html) throws DocumentException, IOException {
        XMLWorkerHelper.getInstance().parseXHtml(this.writer, this.documento,
                new ByteArrayInputStream(("<h3>" + title + "</h3>").getBytes()));
        XMLWorkerHelper.getInstance().parseXHtml(this.writer, this.documento,
                new ByteArrayInputStream(html.getBytes()));
    }

    public void addHTML(String html) throws DocumentException, IOException {
        XMLWorkerHelper.getInstance().parseXHtml(this.writer, this.documento,
                new ByteArrayInputStream(html.getBytes()));
    }
}