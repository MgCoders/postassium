package coop.magnesium.potassium.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by msteglich on 9/11/18.
 */
public class HeaderFooterPageEvent extends PdfPageEventHelper {

    private PdfTemplate t;
    private Image total;

    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    Font FONT = new Font(Font.FontFamily.HELVETICA, 52, Font.BOLD, new GrayColor(0.85f));

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            addHeader(writer);
            addFooter(writer);
            Image watermark = Image.getInstance(HeaderFooterPageEvent.class.getResource("/images/marca_agua.png"));

            float w = watermark.getWidth()/5;
            float h = watermark.getHeight()/5;
            Rectangle pagesize = writer.getPageSize();
            float x = (pagesize.getLeft() + pagesize.getRight()) / 2;
            float y = (pagesize.getTop() + pagesize.getBottom()) / 2;

            writer.getDirectContentUnder().addImage(watermark, w, 0, 0, h, x - (w / 2), y - (h / 2));
//            ColumnText.showTextAligned(writer.getDirectContentUnder(),
//                    Element.ALIGN_CENTER, new Phrase("Memorynotfound.com", FONT),
//                    297.5f, 421, writer.getPageNumber() % 2 == 1 ? 45 : -45);
        } catch (BadElementException e) {
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        } catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }

    }

    private void addHeader(PdfWriter writer){
        PdfPTable header = new PdfPTable(2);
        try {
            // set defaults
            header.setWidths(new int[]{13, 13});
            header.setTotalWidth(527);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(40);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.getDefaultCell().setBorderColor(BaseColor.GRAY);

            // add image
            header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Image logo = Image.getInstance(HeaderFooterPageEvent.class.getResource("/images/logo_sandonato.png"));
            //Image logo = Image.getInstance("resources/images/LOGOSANDONATO.png");
            header.addCell(logo);

            // add image
            header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Image logo2 = Image.getInstance(HeaderFooterPageEvent.class.getResource("/images/logo_vlados_texto.png"));
            header.addCell(logo2);

////            // add text
//            PdfPCell text = new PdfPCell();
//            text.setPaddingBottom(15);
//            text.setPaddingLeft(10);
//            text.setBorder(Rectangle.BOTTOM);
//            text.setBorderColor(BaseColor.LIGHT_GRAY);
//            text.addElement(new Phrase("iText PDF Header Footer Example", new Font(Font.FontFamily.HELVETICA, 12)));
//            text.addElement(new Phrase("https://memorynotfound.com", new Font(Font.FontFamily.HELVETICA, 8)));
//            header.addCell(text);

            // write content
            header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (MalformedURLException e) {
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(1);
        try {
            // set defaults
            footer.setWidths(new int[]{27});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.RED);
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);


            // add copyright
            footer.addCell(new Phrase("Cno. Tomkinson 1531, entre Cno. De la Granja y Cno. Fortín – MONTEVIDEO – URUGUAY – Telfax: (00598) 2313 1343*\n" +
                    "C.P. 12600 – E-mail: metsando@sandonato.com.uy – www.sandonato.com.uy – R.U.T. 21.067629.0019", FontFactory.getFont("Arial", 9, Font.NORMAL)));

            // add current page count

//            footer.addCell(new Phrase(String.format("Página %d de", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)));
//
//            // add placeholder for total page count
//            PdfPCell totalPageCount = new PdfPCell(total);
//            totalPageCount.setBorder(Rectangle.TOP);
//            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
//            footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        int totalLength = String.valueOf(writer.getPageNumber()).length();
        int totalWidth = totalLength * 5;
        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)),
                totalWidth, 6, 0);
    }
}
