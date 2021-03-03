package chapter4;

public class TravelCity implements City{
    City[] cities;
    public TravelCity(){
        cities=new City[]{new Beijing(),new Shanghai()};
    }
    @Override
    public void accept(Visitor visitor) {
        for(int i=0;i<cities.length;i++){
            cities[i].accept(visitor);
        }

    }
}
