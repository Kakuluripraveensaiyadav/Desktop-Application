import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp extends JFrame {
    private JTextField cityField;
    private JLabel resultLabel;

    public WeatherApp() {
        setTitle("Weather App");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        JLabel cityLabel = new JLabel("Enter City:");
        cityField = new JTextField(15);
        JButton getWeatherButton = new JButton("Get Weather");
        resultLabel = new JLabel();

        getWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = cityField.getText().trim();
                if (!city.isEmpty()) {
                    try {
                        String weather = getWeather(city);
                        resultLabel.setText("<html>" + weather.replaceAll("\n", "<br>") + "</html>");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        resultLabel.setText("Error fetching weather data");
                    }
                }
            }
        });

        panel.add(cityLabel);
        panel.add(cityField);
        panel.add(getWeatherButton);
        panel.add(resultLabel);
        add(panel);
    }

    private String getWeather(String city) throws IOException {
        String apiKey = "035ee44ef9dcf37395671d23a8d1dfb5";
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            // Extract temperature, feels like, pressure, humidity, sunrise, sunset, cloud, and weather info
            String temperature = extractValue(response.toString(), "temp");
            String feelsLike = extractValue(response.toString(), "feels_like");
            String pressure = extractValue(response.toString(), "pressure");
            String humidity = extractValue(response.toString(), "humidity");
            String sunrise = extractValue(response.toString(), "sunrise");
            String sunset = extractValue(response.toString(), "sunset");
            String cloud = extractValue(response.toString(), "all");
            String weatherInfo = extractWeatherInfo(response.toString());

            // Construct weather information string
        StringBuilder weatherInfoLines = new StringBuilder();
        weatherInfoLines.append("Temperature (Celsius): ").append(temperature).append("°C\n");
        weatherInfoLines.append("Feels like in (Celsius): ").append(feelsLike).append("°C\n");
        weatherInfoLines.append("Pressure: ").append(pressure).append(" hPa\n");
        weatherInfoLines.append("Humidity: ").append(humidity).append("%\n");
        weatherInfoLines.append("Cloud: ").append(cloud).append("%\n");
        weatherInfoLines.append("Info: ").append(weatherInfo);

        System.out.println("Weather Info: " + weatherInfoLines.toString()); // Print weather info for debugging
        return weatherInfoLines.toString();
            //return String.format("Temperature (Celsius): %s°C\n\n Feels like in (Celsius): %s°C\n Pressure: %s hPa\n Humidity: %s%%\n Sunrise at %s and Sunset at %s\n Cloud: %s%%\n Info: %s",temperature, feelsLike, pressure, humidity, sunrise, sunset, cloud, weatherInfo);
        } else {
            throw new IOException("HTTP error code: " + responseCode);
        }
    }

    // Method to extract value corresponding to a key from JSON-like response
    private String extractValue(String response, String key) {
        int startIndex = response.indexOf("\"" + key + "\":") + key.length() + 3;
        int endIndex = response.indexOf(",", startIndex);
        return response.substring(startIndex, endIndex);
    }

    // Method to extract weather info from JSON-like response
    private String extractWeatherInfo(String response) {
        int startIndex = response.indexOf("\"description\":\"") + 15;
        int endIndex = response.indexOf("\"", startIndex);
        return response.substring(startIndex, endIndex);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WeatherApp app = new WeatherApp();
            app.setVisible(true);
        });
    }
}
