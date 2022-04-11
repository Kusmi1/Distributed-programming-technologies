import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Service {

    private String country;
    static String APIkey = "&appid=5af19223f6369152e6697ef0363458c2";
    static String OpenWeatherURL = "https://api.openweathermap.org/data/2.5/weather?q=";
    String ExchangeRateURL = "https://api.exchangerate.host/convert?from=";

    public Service(String country) {
        this.country = country;
    }

    public String getGSONWeather(String miasto) {
        String WeatherURL = (Service.OpenWeatherURL + miasto + "," + returnCity()  + Service.APIkey);
        try {
            return URLRequest(WeatherURL);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getWeather(String miasto) throws IOException {

        String WeatherURL = (Service.OpenWeatherURL + miasto + "," + returnCity()  + Service.APIkey);
        WeatherGSON weather = new WeatherGSON();

        if (this.getGSONWeather(miasto).equals(null)) {

        }
        String str = URLRequest(WeatherURL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonik = gson.fromJson(getGSONWeather(miasto), JsonElement.class);

        str = gson.toJson(jsonik);
        System.out.println("json" + str);

        return str;

    }

    public Double getWeather2(String miasto, int i) throws IOException {


        String WeatherURL = (Service.OpenWeatherURL + miasto + "," + returnCity()  + Service.APIkey);

        WeatherGSON weather = new WeatherGSON();

        if (this.getGSONWeather(miasto).equals(null)) {
        }
        String str = URLRequest(WeatherURL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonik = gson.fromJson(getGSONWeather(miasto), JsonElement.class);

        str = gson.toJson(jsonik);
        weather = gson.fromJson(getGSONWeather(miasto), WeatherGSON.class);
        Double gsonSTR= weather.main.temp;
        System.out.println("json" + str);

        return gsonSTR;

    }

    public Double getRateFor(String cash) throws IOException {
        ExchangeRateGSON exchange = new ExchangeRateGSON();

        String CurrencyURL = ExchangeRateURL + cash + "&to=" +returnCurrency() ;
        Gson gson2 = new Gson();

        exchange = gson2.fromJson(URLRequest(CurrencyURL), ExchangeRateGSON.class);
        double rate2 = exchange.info.rate;
        return rate2;
    }

    public Double getNBPRate() throws IOException {

        String cash = returnCurrency();
        double globalCash = 0.0;
        double PLNcash = 1.0;
        String ApiBNP = "http://api.nbp.pl/api/exchangerates/rates/";

        if (!cash.equals("PLN")) {
            try {
                globalCash = gsonReturn("a", cash);
            } catch (IOException e) {
                try {
                    globalCash = gsonReturn("b", cash);
                } catch (IOException er) {
                    er.printStackTrace();
                }
            }
        } else {
            return globalCash = PLNcash;
        }

        return PLNcash/globalCash ;
    }

    public double gsonReturn(String letter, String waluta) throws IOException {
        String ApiBNP="http://api.nbp.pl/api/exchangerates/rates/";
        String lastURL=ApiBNP+letter+"/"+waluta;

        String request=URLRequest(lastURL);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(request, ExchangeRateGSON.class).rates.get(0).mid;
    }

    public String returnCity() {

        Map<String, Locale> map = new HashMap<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            map.put(locale.getDisplayCountry(), locale);
        }
        String tempCountryID = String.valueOf(map.get(this.country));
        String[] tempstr = tempCountryID.split("_", 2);
        String city = tempstr[1];
        return city;
    }

    public String returnCurrency() {
        Map<String, Currency> map = new HashMap<>();

        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry() == this.country) {
                Currency currency = Currency.getInstance(locale);
                map.put(locale.getDisplayCountry(), currency);
            }
        }
        String currency = String.valueOf(map.get(country));
        return currency;

    }

    public static String URLRequest(String urlrequest) throws IOException {

        URL url = new URL(urlrequest);
        BufferedReader breader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
        String gsonWeather = breader.lines().collect(Collectors.joining());

        return gsonWeather;

    }

}





