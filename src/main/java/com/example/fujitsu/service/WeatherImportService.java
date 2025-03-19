package com.example.fujitsu.service;

import com.example.fujitsu.model.WeatherData;
import com.example.fujitsu.repository.WeatherDataRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

@Service
public class WeatherImportService {

    private final WeatherDataRepository weatherDataRepository;
    private static final String WEATHER_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    public WeatherImportService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    /**
     * Fetches current weather data from the Estonian Weather Service API.
     * Processes only data for Tallinn-Harku, Tartu-Tõravere, and Pärnu stations.
     * Saves the retrieved data to the database.
     */
    public void fetchWeatherData() {
        try {
            URL url = new URL(WEATHER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(conn.getInputStream());
            document.getDocumentElement().normalize();

            NodeList stations = document.getElementsByTagName("station");
            for (int i = 0; i < stations.getLength(); i++) {
                Element station = (Element) stations.item(i);
                String name = station.getElementsByTagName("name").item(0).getTextContent();

                if (name.equals("Tallinn-Harku") || name.equals("Tartu-Tõravere") || name.equals("Pärnu")) {
                    double airTemperature = Double.parseDouble(station.getElementsByTagName("airtemperature").item(0).getTextContent());
                    double windSpeed = Double.parseDouble(station.getElementsByTagName("windspeed").item(0).getTextContent());
                    String weatherPhenomenon = station.getElementsByTagName("phenomenon").item(0).getTextContent();
                    Integer wmoCode = Integer.parseInt(station.getElementsByTagName("wmocode").item(0).getTextContent());

                    WeatherData weatherData = new WeatherData();
                    weatherData.setStationName(name);
                    weatherData.setWmoCode(wmoCode);
                    weatherData.setAirTemperature(airTemperature);
                    weatherData.setWindSpeed(windSpeed);
                    weatherData.setWeatherPhenomenon(weatherPhenomenon);
                    weatherData.setTimestamp(LocalDateTime.now());

                    weatherDataRepository.save(weatherData);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch weather data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
