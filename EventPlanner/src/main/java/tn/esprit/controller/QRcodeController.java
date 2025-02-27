package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import tn.esprit.entities.Equipment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRcodeController {

    @FXML
    private ImageView qrImageView;

    @FXML
    private Label equipmentInfoLabel;

    @FXML
    private VBox qrContainer;

    public void setEquipmentData(Equipment equipment) {
        // Set equipment details in label
        String equipmentInfo = "Name: " + equipment.getName() + "\n"
                + "State: " + equipment.getState() + "\n"
                + "Category: " + equipment.getCategory() + "\n"
                + "Quantity: " + equipment.getQuantity();

        // Generate and display QR Code
        Image qrCode = generateQRCode(equipmentInfo);
        qrImageView.setImage(qrCode);
    }

    private Image generateQRCode(String data) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 200;
        int height = 200;
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

            // Create WritableImage instead of BufferedImage
            WritableImage image = new WritableImage(width, height);
            PixelWriter pixelWriter = image.getPixelWriter();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    pixelWriter.setArgb(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            return image;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
