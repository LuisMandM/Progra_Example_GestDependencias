package com.ikasgela;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Main {

    public static void main(String[] args) {

        // madrid_21802.csv: Datos de febrero de 2018 con las mediciones horarias de una estación situada en la Escuela Técnica Superior de Ingenieros Industriales de la UPM, en el Paseo de la Castellana.
        // REF: Air Quality in Madrid (2001-2018): https://www.kaggle.com/decide-soluciones/air-quality-madrid

        double max_pm10 = Double.MIN_VALUE;
        double max_pm25 = Double.MIN_VALUE;
        double total_pm10 = 0;
        double total_pm25 = 0;
        int registros = 0;
        final double LIMITE_PM10 = 40;    // REF: https://ec.europa.eu/environment/air/quality/standards.htm
        final double LIMITE_PM25 = 25;
        int superado_pm10 = 0;
        int superado_pm25 = 0;

        // REF: Apache Commons CSV: http://commons.apache.org/proper/commons-csv/user-guide.html

        try {
            Reader in = new FileReader("madrid_201802.csv");
            Iterable<CSVRecord> lecturas = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

            for (CSVRecord lectura : lecturas) {

                // PM10
                double pm10;
                try {
                    pm10 = Double.parseDouble(lectura.get("PM10"));
                } catch (NumberFormatException e) {
                    pm10 = 0;
                }

                if (max_pm10 < pm10)
                    max_pm10 = pm10;

                total_pm10 += pm10;

                if (pm10 > LIMITE_PM10)
                    superado_pm10 += 1;

                // PM25
                double pm25;
                try {
                    pm25 = Double.parseDouble(lectura.get("PM25"));
                } catch (NumberFormatException e) {
                    pm25 = 0;
                }

                if (max_pm25 < pm25)
                    max_pm25 = pm25;

                total_pm25 += pm25;

                if (pm25 > LIMITE_PM25)
                    superado_pm25 += 1;

                // Total de filas
                registros += 1;
            }

        } catch (IOException e) {
            System.err.println("Error al cargar datos: " + e.getLocalizedMessage());
        }

        if (registros > 0) {
            System.out.format("Max PM10: %.1f\n", max_pm10);
            System.out.format("Media PM10: %.2f\n", total_pm10 / registros);
            System.out.format("Superado tope PM10 (%.1f): %d\n", LIMITE_PM10, superado_pm10);
            System.out.println();
            System.out.format("Max PM25: %.1f\n", max_pm25);
            System.out.format("Media PM25: %.2f\n", total_pm25 / registros);
            System.out.format("Superado tope PM25 (%.1f): %d\n", LIMITE_PM25, superado_pm25);
        }
    }
}
