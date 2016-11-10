package Main;

import DAO.*;
import enti.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

import java.util.stream.Collectors;

class Controller {
    private DAO<Hotel> hotelDAO = new HotelDAO();
    private DAO<User> userDao = new UserDAO();
    private DAO<Room> roomDAO = new RoomDAO();



    /*private Collection<Room> getAllRooms() {
        return roomDAO.getAll();
    }*/

    void getAllRoom() {
        Help.hp(7);
        if (!getAllHotel().isEmpty()) {
            for (Hotel hotel : getAllHotel()) {
                System.out.println(hotel);
                for (Room room : hotel.getRooms()) {
                    System.out.println("   " + room);
                }
            }
        } else {
            System.out.println("\n  Отелей нет");
        }
    }


    private Collection<Hotel> getAllHotel() {
        return hotelDAO.getAll();
    }

    private Collection<User> getAllUser() {

        return userDao.getAll();
    }


    List<Hotel> findHotelByName(String name) {
        Help.hp(3);
        List<Hotel> hotels = getAllHotel()
                .stream()
                .filter(hotel -> hotel.getHotelName().equals(name))
                .collect(Collectors.toList());
        if (!hotels.isEmpty()) {
            hotels.forEach(System.out::println);
        } else {
            System.out.println("\n  Отелей с таким именем нет");
        }
        return hotels;
    }

    Collection<Hotel> findHotelByCity(String city) {
        Help.hp(2);
        Collection<Hotel> hotels = getAllHotel()
                .stream()
                .filter(hotel -> hotel.getCityName().equals(city))
                .collect(Collectors.toList());
        if (!hotels.isEmpty()) {
            hotels.forEach(System.out::println);
        } else {
            System.out.println("\n  Отелей с таким городом нет");
        }
        return hotels;
    }

    private Room check(long roomId, long userId, long hotelId) {
        Room room = null;
        try {

            if (userDao.findById(userId).isActive()) {
                System.out.print("User: ok");
                //noinspection OptionalGetWithoutIsPresent
                room = getAllHotel().stream().filter(hotel -> hotel.getId() == hotelId).findFirst().get().getRooms()
                        .stream().filter(room1 -> room1.getId() == roomId).findAny().get();
                System.out.println(", Room: ok, Hotel: ok.");
            } else {
                System.out.println(" Юзер не активен");
            }

        } catch (NullPointerException e) {
            System.out.println("Нет такого юзера");
        } catch (NoSuchElementException e) {
            System.out.println(", Room: there is no room.\n Импосибле");
        }
        return room;
    }


    void bookRoom(long roomId, long userId, long hotelId) {
        Help.hp(4);
        Room room = check(roomId, userId, hotelId);
        if (room == null) return;
        long id = room.getUserReservedId();

        if (id != userId) {
            if (room.getUserReservedId() == 0) {
                room.setUserReservedId(userId);
                roomDAO.syncListToDB();
                System.out.println("Гуд \n" + room);
            }
        } else {
            System.out.println("Impossible");
        }

    }

    void cancelReservation(long roomId, long userId, long hotelId) {
        Help.hp(5);
        Room room = check(roomId, userId, hotelId);
        if (room == null) return;
        if (room.getUserReservedId() == 0) {
            System.out.println("Импоссибле потому что комната не была забронирована");
        } else if (room.getUserReservedId() == userId) {
            room.setUserReservedId(0);
            roomDAO.syncListToDB();
            System.out.println("Гуд \n" + room);
        } else {
            System.out.println("Импоссибле потому что комната была заброеирована другим юзером" + room.getUserReservedId());

        }

    }


    Collection<Hotel> findRoom(Map<String, String> params) {
        Help.hp(6);
        int person, price;
        Function<String, Integer> toInteger = Integer::valueOf;
        try {
            person = toInteger.apply(params.get("Person"));
        } catch (NumberFormatException e) {
            person = 0;
        }
        try {
            price = toInteger.apply(params.get("MaxPrice"));
        } catch (NumberFormatException e) {
            price = 0;
        }
        String cityFind = params.get("City");
        String hotelFind = params.get("Hotel");
        List<Hotel> hotels = hotelDAO.getAll().stream().collect(Collectors.toList());

        if (cityFind != null) {
            hotels = hotels.stream().filter(h -> h.getCityName().equals(cityFind)).collect(Collectors.toList());
        }
        if (hotelFind != null) {
            hotels = hotels.stream().filter(h -> h.getHotelName().equals(hotelFind)).collect(Collectors.toList());
        }
        List<Room> rooms;
        if (price != 0) {
            int finalPrice = price;
            for (Hotel hotel : hotels) {
                rooms = hotel.getRooms();
                rooms = rooms.stream().filter(r -> r.getPrice() < finalPrice).collect(Collectors.toList());
                hotel.setRooms(rooms);
            }
        }
        if (person != 0) {
            int finalPerson = person;
            for (Hotel hotel : hotels) {
                rooms = hotel.getRooms();
                rooms = rooms.stream().filter(r -> r.getPerson() == finalPerson).collect(Collectors.toList());
                hotel.setRooms(rooms);
            }
        }

        String text = "";
        if (price == 0 && person == 0) text = "Print all rooms";
        for (Hotel hotel : hotels) {
            System.out.println(hotel);
            for (Room room : hotel.getRooms()) {
                System.out.println("   " + room);
            }
        }

        System.out.println("-----------------------------------------------"
                + "\n Search options || " + text
                + "\nCity:  " + params.get("City")
                + "\nHotel: " + params.get("Hotel")
                + "\nMaxPrice: " + price
                + "\nPerson: " + person
                + "\n-----------------------------------------------");

        return hotels;
    }

    long registerUser(User user) {
        Help.hp(1);
        User userFound = user;
        try {
            //noinspection OptionalGetWithoutIsPresent
            userFound = getAllUser()
                    .stream()
                    .filter(u -> u.equals(user)).findFirst().get();
            userFound.setActive(true);
            System.out.println(" Пользователь успешно зарегистрирован\n" + userFound);
        } catch (NoSuchElementException e) {
            System.out.println(" Отсутствует информация о пользователe");
        }
        return userFound.getId();
    }

}


