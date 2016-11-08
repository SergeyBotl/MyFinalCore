package Main;

import enti.User;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Controller cont = new Controller();

        User dima = new User("Dima", "1111");


        long hotelId = 2;
        long roomId = 8;
        long userId = 5;

        userId = cont.registerUser(dima);

        //cont.findHotelByCity("Kiev");

        //cont.bookRoom(roomId, userId, hotelId);
        //cont.findHotelByName("Hilton");


        //cont.cancelReservation(roomId, userId, hotelId);


        Map<String, String> map = new HashMap<>();
        map.put("City", "Kiev");
       // map.put("Hotel", "Astoria");
        map.put("MaxPrice", "500");
       // map.put("Person", "4");

        cont.findRoom(map);
    }
}

