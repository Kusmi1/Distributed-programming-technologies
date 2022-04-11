

import javax.swing.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        Service s = new Service("Italy");
        String weatherJson = s.getWeather("Rome");
        Double rate1 = s.getRateFor("EUR");
        Double rate2 = s.getNBPRate();

        SwingUtilities.invokeLater(GUI::new);


    }

}