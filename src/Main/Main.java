package Main;

import enti.User;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Controller cont = new Controller();

        User dima = new User("Dima", "11111");

        long userId;

       cont.getAllRoom();


        userId = cont.registerUser(dima);
        userId = 3;

        Map<String, String> map = new HashMap<>();
        map.put("City", "Kiev");
        // map.put("Hotel", "Astoria");
        map.put("MaxPrice", "500");
        // map.put("Person", "4");

        cont.findRoom(map);

        //cont.findHotelByCity("Kiev");

        long hotelId = 6;
        long roomId = 44;


        //System.out.println(cont.check(roomId,userId, hotelId));

        //

        //cont.findHotelByName("Hilton");
        cont.bookRoom(roomId, userId, hotelId);
        //cont.cancelReservation(roomId, userId, hotelId);


    }
}

