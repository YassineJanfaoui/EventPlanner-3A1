package tn.esprit.entities;

import tn.esprit.services.EventService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PDFEVENT {
    public PDFEVENT() {
    }

    public void GeneratePdf(String filename, ArrayList<Event> events) throws FileNotFoundException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filename + ".pdf"));
        document.open();

        // Ajouter un en-tête stylisé
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new BaseColor(5, 53, 54)); // Titre principal
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 14, new BaseColor(64, 64, 64)); // Gris foncé
        Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE); // Texte blanc pour l'en-tête
        Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK); // Texte noir pour les cellules du tableau

        Paragraph title = new Paragraph("📅 Liste des Événements", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph date = new Paragraph("Date de génération : " + LocalDate.now(), subTitleFont);

        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(10);
        document.add(date);

        document.add(new Paragraph("\n")); // Ajout d'un espace

        // Création d'un tableau
        PdfPTable table = new PdfPTable(5); // 5 colonnes (Nom, Date, Horaire, Prix, Image)
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{2, 2, 2, 1, 3}); // Largeurs relatives des colonnes

        // Définir la couleur de fond pour l'en-tête
        BaseColor headerColor = new BaseColor(5, 53, 54);

        // En-tête du tableau
        String[] headers = {"Nom de l'événement", "Date", "Horaire", "Prix (TND)", "Image"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
            cell.setBackgroundColor(headerColor); // Appliquer la nouvelle couleur
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }

        // Ajouter les événements dans le tableau
        for (Event p : events) {
            table.addCell(new Phrase(p.getDescription(), tableFont));
            table.addCell(new Phrase(p.getStartDate().toString(), tableFont));
            table.addCell(new Phrase(p.getEndDate(), tableFont));
            table.addCell(new Phrase(p.getFee() + " TND", tableFont));

            // Ajouter l'image
            try {
                Image img = Image.getInstance(p.getImage());
                img.scaleToFit(70, 70);
                PdfPCell imgCell = new PdfPCell(img, true);
                imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                imgCell.setPadding(5);
                table.addCell(imgCell);
            } catch (Exception e) {
                PdfPCell noImgCell = new PdfPCell(new Phrase("⚠️ Non disponible", tableFont));
                noImgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(noImgCell);
            }
        }

        document.add(table);
        document.close();

        System.out.println("✅ PDF généré avec succès : " + filename + ".pdf");

        // Ouvrir automatiquement le fichier PDF (Windows uniquement)
        try {
            Process pro = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filename + ".pdf");
        } catch (IOException e) {
            System.err.println("❌ Impossible d'ouvrir le fichier PDF : " + e.getMessage());
        }
    }

}
