package tn.esprit.api;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import tn.esprit.entities.Participant;
import tn.esprit.services.ParticipantService;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDF_Participant {
    private final ParticipantService participantService = new ParticipantService();

    public void exportParticipantsToPDF(String eventName) {
        List<Participant> participants = participantService.returnListOfParticipantByEventName(eventName);

        if (participants.isEmpty()) {
            System.out.println("No participants found for event: " + eventName);
            return;
        }

        String filePath = Paths.get(System.getProperty("user.home"), "Desktop",
                "Participants_List_" + LocalDate.now() + ".pdf").toString();

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(PageSize.A4.rotate()); // Now works with proper imports
            Document document = new Document(pdf);
            document.setMargins(40, 40, 40, 40);

            // Add title
            Paragraph header = new Paragraph("Event Participants Report")
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);

            Paragraph subHeader = new Paragraph(eventName + "\n\n")
                    .setFontColor(ColorConstants.GRAY)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(header);
            document.add(subHeader);

            // Create table with relative column widths
            float[] columnWidths = {1, 3, 3, 1};
            Table table = new Table(UnitValue.createPercentArray(columnWidths))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);

            // Add table headers
            String[] headers = {"ID", "Name", "Affiliation", "Age"};
            for (String headerTitle : headers) {
                Cell cell = new Cell()
                        .setBackgroundColor(ColorConstants.BLUE)
                        .setPadding(8)
                        .add(new Paragraph(headerTitle)
                                .setFontColor(ColorConstants.WHITE)
                                .setBold());
                table.addHeaderCell(cell);
            }

            // Add table rows
            boolean isAlternate = false;
            for (Participant p : participants) {
                Cell idCell = new Cell().add(new Paragraph(String.valueOf(p.getParticipantId())));
                Cell nameCell = new Cell().add(new Paragraph(p.getName()));
                Cell affCell = new Cell().add(new Paragraph(p.getAffiliation()));
                Cell ageCell = new Cell().add(new Paragraph(String.valueOf(p.getAge())));

                for (Cell cell : new Cell[]{idCell, nameCell, affCell, ageCell}) {
                    cell.setPadding(8)
                            .setBackgroundColor(isAlternate ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE)
                            .setFontColor(ColorConstants.DARK_GRAY);
                }

                table.addCell(idCell);
                table.addCell(nameCell);
                table.addCell(affCell);
                table.addCell(ageCell);

                isAlternate = !isAlternate;
            }

            document.add(table);

            // Add footer
            Paragraph footer = new Paragraph()
                    .add("Total Participants: " + participants.size() + "\n")
                    .add("Generated on: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                    .setFontColor(ColorConstants.GRAY)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);

            document.add(footer);

            document.close();
            System.out.println("Beautiful PDF exported successfully: " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Error creating PDF file: " + e.getMessage());
        }
    }
}