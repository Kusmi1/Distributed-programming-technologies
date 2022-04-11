import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


public class GUI extends JFrame {
    private WebEngine Engine;
    private WebView view;
    private JComboBox CurrencyComboBox;
    public JComboBox CountryComboBox;
    private JPanel webPanel;
    private JPanel mainPanel;
    private JTextField CityTextField;
    private JButton LOADButton;
    private JLabel actualWeather;
    private JLabel rateLabel;
    private JLabel NBPLabel;
    private JFXPanel jfxPanel;

    public GUI() throws HeadlessException {
        DefaultComboBoxModel dm = new DefaultComboBoxModel(listofCountries());
        CountryComboBox.setModel(dm);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        DefaultComboBoxModel dm2 = new DefaultComboBoxModel(listofCurrency());
        CurrencyComboBox.setModel(dm2);

        //this.makeJFX();
        this.add(mainPanel);

        jfxPanel = new JFXPanel();
        Platform.runLater(this::makeJFX);
        webPanel.add(jfxPanel);
        this.pack();

        LOADButton.addActionListener((ActionEvent e)->{

            String countryGUI  = CountryComboBox.getSelectedItem().toString();
            String currencyGUI = CurrencyComboBox.getSelectedItem().toString();
            String miasteczko = CityTextField.getText();
            DecimalFormat df = new DecimalFormat("#.##");
            Service serv = new Service(countryGUI);
            Double weatherJson = null;
            try {
                weatherJson = (serv.getWeather2(miasteczko,0))-273.15;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                Double rateNBP = serv.getNBPRate();
                Double rateGUI = serv.getRateFor(currencyGUI );
               //Double rateGUI = serv.getRateFor(countryGUI );
                rateLabel.setText("rate: "+rateGUI);
                NBPLabel.setText("NBP zł: "+rateNBP);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            actualWeather.setText("Temperature: "+df.format(weatherJson)+" Celcjusza");
            String url = String.format("https://en.wikipedia.org/wiki/%s", miasteczko);
            Platform.runLater(() -> {
                view = new WebView();
                Scene scene = new Scene(view);
               // String cityGUI = "Warszawa";
                view.getEngine().load(url);
                jfxPanel.setScene(scene);});

            //this.reloadPage(url);
        });
    }
    public void makeJFX() {
        Platform.runLater(() -> {
        view = new WebView();
        Scene scene = new Scene(view);
        String cityGUI = "Warszawa";
        //view.getEngine().load("https://en.wikipedia.org/wiki/"+cityGUI);
        jfxPanel.setScene(scene);});
        this.pack();
    }
//
//    private void reloadPage(String url) {
//        Platform.runLater(() -> {
//            Engine.load(url);
//        });
//    }


    public ArrayList<String> cityList() {
        ArrayList<String> listOfCities = new ArrayList();

        for (Locale locale : Locale.getAvailableLocales()) {
            listOfCities.add(locale.getDisplayCountry());

        }
        return listOfCities;
    }

    public String[] listofCountries() {
        ArrayList<String> countryArray = new ArrayList<>();

        for (Locale locale: Locale.getAvailableLocales()){
            String lok = locale.getDisplayCountry();
           // lok.replace("", "null");
            if (lok.equals("")){
            }else
            countryArray.add(lok);
        }
        Collections.sort(countryArray);
        return countryArray.toArray(new String[countryArray.size()]);
    }

    public String[] listofCurrency() {
        ArrayList<String> currencyArray = new ArrayList<>();
        HashSet<String> currencySet= new HashSet<>();
        for (Currency currency : Currency.getAvailableCurrencies()) {

          currencySet.add(currency.getCurrencyCode());
          currencyArray = new ArrayList<>(currencySet);
          Collections.sort(currencyArray);

        }
        return currencyArray.toArray(new String[currencyArray.size()]);
    }
}