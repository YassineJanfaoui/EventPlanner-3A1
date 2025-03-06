package tn.esprit.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import tn.esprit.utils.MyDatabase;

public class StatController implements Initializable {

    @FXML
    private PieChart voy_stat;

    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private Connection cnx;  // This will be initialized properly
    ObservableList<PieChart.Data> data = FXCollections.observableArrayList();  // Keep the member variable for data

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Use DataSource to get the connection instead of DriverManager
        cnx = MyDatabase.getInstance().getConnection();

        // Call the stat method to generate statistics for all months
        stat();  // No need to pass the current month
    }


    private void stat() {
        // Effacer les données existantes avant d'ajouter de nouvelles
        data.clear();

        // Tableau des noms des mois (1 = Janvier, 12 = Décembre)
        String[] moisNoms = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

        try {
            // Requête SQL pour récupérer le nombre d'événements par mois
            String query = "SELECT EXTRACT(MONTH FROM StartDate) AS month, COUNT(*) AS count FROM event GROUP BY month ORDER BY month";
            preparedStatement = cnx.prepareStatement(query);
            rs = preparedStatement.executeQuery();

            // Traitement des résultats et ajout des données au graphique
            while (rs.next()) {
                int monthIndex = rs.getInt("month");  // Numéro du mois (1-12)
                int eventCount = rs.getInt("count");  // Nombre d'événements

                // Convertir le numéro du mois en nom (Janvier, Février...)
                String moisNom = moisNoms[monthIndex - 1];

                // Ajouter les données au PieChart
                PieChart.Data slice = new PieChart.Data(moisNom, eventCount);
                data.add(slice);
            }

            // Appliquer les données au PieChart
            voy_stat.setTitle("📊 Statistiques des Événements par Mois");
            voy_stat.setLegendSide(Side.RIGHT);
            voy_stat.setData(data);

            // 🎨 Générer des couleurs aléatoires pour chaque tranche
            for (PieChart.Data slice : data) {
                // Générer une couleur aléatoire
                String randomColor = generateRandomColor();
                slice.getNode().setStyle("-fx-pie-color: " + randomColor + ";");
            }

            // 🔢 Effet interactif pour afficher les valeurs au survol
            for (PieChart.Data slice : data) {
                slice.getNode().setOnMouseEntered(event -> {
                    voy_stat.setTitle(slice.getName() + " : " + (int) slice.getPieValue() + " événements");
                });

                slice.getNode().setOnMouseExited(event -> {
                    voy_stat.setTitle("📊 Statistiques des Événements par Mois");
                });
            }

        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
        }
    }

    // Fonction pour générer une couleur aléatoire
    private String generateRandomColor() {
        // Générer des valeurs hexadécimales aléatoires pour RGB
        String red = Integer.toHexString((int) (Math.random() * 256));
        String green = Integer.toHexString((int) (Math.random() * 256));
        String blue = Integer.toHexString((int) (Math.random() * 256));

        // Assurer que chaque composant est sur deux chiffres (par exemple, '0f' au lieu de 'f')
        return "#" + padColorComponent(red) + padColorComponent(green) + padColorComponent(blue);
    }

    // Fonction pour compléter la valeur hexadécimale à deux chiffres
    private String padColorComponent(String colorComponent) {
        return colorComponent.length() == 1 ? "0" + colorComponent : colorComponent;
    }


}




