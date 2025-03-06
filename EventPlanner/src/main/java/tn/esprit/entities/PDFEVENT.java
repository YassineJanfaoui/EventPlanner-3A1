package tn.esprit.entities;
import tn.esprit.utils.MyDatabase;
import tn.esprit.services.EventService;
import tn.esprit.services.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import tn.esprit.api.EventQRCodeGenerator;
import com.itextpdf.text.pdf.qrcode.WriterException;
import tn.esprit.entities.Event;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PDFEVENT {
    public PDFEVENT() {
    }

    public void generatePdf(String filename, ArrayList<Event> events, EventService eventService)
            throws FileNotFoundException, DocumentException, WriterException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filename + ".pdf"));
        document.open();

        // Add logo
        addLogo(document);

        // Add title and date
        addTitleAndDate(document);

        // Create and populate the event table
        PdfPTable table = createEventTable(events, eventService);
        document.add(table);

        document.close();
        System.out.println("✅ PDF generated successfully: " + filename + ".pdf");

        // Open PDF automatically (Windows only)
        openPdfAutomatically(filename);
    }

    private void addLogo(Document document) {
        try {
            Image logo = Image.getInstance(getClass().getResource("/images/event.png").toExternalForm());
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (IOException e) {
            System.err.println("❌ Unable to add logo: " + e.getMessage());
        } catch (BadElementException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTitleAndDate(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new BaseColor(5, 53, 54));
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 14, new BaseColor(64, 64, 64));

        Paragraph title = new Paragraph("📅 Liste des Événements", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph date = new Paragraph("Date de génération : " + LocalDate.now(), subTitleFont);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(10);
        document.add(date);
        document.add(new Paragraph("\n"));
    }

    private PdfPTable createEventTable(ArrayList<Event> events, EventService eventService)
            throws DocumentException {

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{2, 2, 2, 1, 3, 2, 2});

        BaseColor headerColor = new BaseColor(5, 53, 54);
        String[] headers = {"Name", "Date de début", "Date de fin", "Max",
                "Fee(TND)", "Image", "QR"};

        addTableHeaders(table, headers, headerColor);
        addTableRows(table, events, eventService);

        return table;
    }

    private void addTableHeaders(PdfPTable table, String[] headers, BaseColor headerColor) {
        Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }
    }

    private void addTableRows(PdfPTable table, ArrayList<Event> events, EventService eventService) {
        Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        for (Event event : events) {
            table.addCell(new Phrase(event.getName(), tableFont));
            //table.addCell(new Phrase(event.getDescription(), tableFont));
            table.addCell(new Phrase(event.getStartDate().toString(), tableFont));
            table.addCell(new Phrase(event.getEndDate(), tableFont));
            table.addCell(new Phrase(String.valueOf(event.getMaxParticipants()), tableFont));
            table.addCell(new Phrase(event.getFee() + " TND", tableFont));
            addEventImage(table, event);
            addQRCode(table, event, eventService);
        }
    }

    private void addEventImage(PdfPTable table, Event event) {
        Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        if (event.getImage() != null && !event.getImage().isEmpty()) {
            try {
                Image img = Image.getInstance(event.getImage());
                img.scaleToFit(70, 70);
                PdfPCell imgCell = new PdfPCell(img, true);
                imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                imgCell.setPadding(5);
                table.addCell(imgCell);
            } catch (Exception e) {
                addUnavailableImageCell(table, tableFont);
            }
        } else {
            addUnavailableImageCell(table, tableFont);
        }
    }

    private void addUnavailableImageCell(PdfPTable table, Font tableFont) {
        PdfPCell noImgCell = new PdfPCell(new Phrase("⚠️ Image non disponible", tableFont));
        noImgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(noImgCell);
    }

    private void addQRCode(PdfPTable table, Event event, EventService eventService) {
        Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        int eventId = event.getEventId();
        Event activity = eventService.getEventById(eventId);

        if (activity != null) {
            try {
                BufferedImage qrBufferedImage = EventQRCodeGenerator.generateQRCodeImage(activity);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(qrBufferedImage, "PNG", baos);
                Image qrPdfImage = Image.getInstance(baos.toByteArray());
                qrPdfImage.scaleToFit(70, 70);
                PdfPCell qrCell = new PdfPCell(qrPdfImage, true);
                qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                qrCell.setPadding(5);
                table.addCell(qrCell);
            } catch (IOException | BadElementException e) {
                addErrorCell(table, "⚠️ Erreur QR Code IO", tableFont);
            } catch (com.google.zxing.WriterException e) {
                throw new RuntimeException(e);
            }
        } else {
            addErrorCell(table, "⚠️ Activité non trouvée", tableFont);
        }
    }

    private void addErrorCell(PdfPTable table, String message, Font tableFont) {
        PdfPCell errorCell = new PdfPCell(new Phrase(message, tableFont));
        errorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(errorCell);
    }

    private void openPdfAutomatically(String filename) {
        try {
            Process pro = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filename + ".pdf");
        } catch (IOException e) {
            System.err.println("❌ Unable to open PDF file: " + e.getMessage());
        }
    }

}

