import java.util.List;

public class ExchangeRateGSON { //"info":{"rate":4.267702}
    Infoclass info;
    //Infoclass rate2;
    Query firstvalue;
    List<Rates> rates;
}
class Query {
    String from;
    String to;
}
class Infoclass{
    double rate;
    double result;
}
class Rates{
    double mid;
}
