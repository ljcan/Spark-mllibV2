package chapter4;

public class Demo {
    public static void main(String[] args) {
        TravelCity travelCity = new TravelCity();
        travelCity.accept(new SimpleVisitorImpl());
    }
}
