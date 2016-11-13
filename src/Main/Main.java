package Main;

import enti.User;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
       Controller cont = new Controller();

        User dima = new User("Dima", "1111");



       cont.logIn(dima);

       // cont.printAllRoom();


        long hotelId = 6;
        long roomId = 110;
        long userId = 1;
        // cont.findHotelByCity("Kiev");
        // cont.findHotelByName("Hilton");
      cont.bookRoom(roomId, userId, hotelId);
       // cont.cancelReservation(roomId, userId, hotelId);


        Map<String, String> map = new HashMap<>();
        map.put("City", "Kiev");
        // map.put("Hotel", "Astoria");
         map.put("MaxPrice", "500");
        // map.put("Person", "4");

        cont.findRoom(map);


    }
}

