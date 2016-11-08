package Main;

import DAO.*;
import enti.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Controller {
    private DAO<Hotel> hotelDAO = new HotelDAO();
    private DAO<User> userDao = new UserDAO();
    private DAO<Room> roomDAO = new RoomDAO();
    //private CreateData createData = new CreateData();
    private boolean isActiveUser = false;


    private Collection<Room> getAllRooms() {

        return roomDAO.getAll();
    }

    void getAllRoom() {
        if (!getAllHotel().isEmpty()) {
            System.out.println("\n  Список отелей");

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
        List<Hotel> hotels = getAllHotel()
                .stream()
                .filter(hotel -> hotel.getHotelName().equals(name))
                .collect(Collectors.toList());
        if (!hotels.isEmpty()) {
            System.out.println("\n  Список отелей по названию");

            for (Hotel hotel : hotels) {
                System.out.println(hotel);
                for (Room room : hotel.getRooms()) {
                    System.out.println("   " + room);
                }
            }
        } else {
            System.out.println("\n  Отелей с таким именем нет");
        }
        return hotels;
    }

    Collection<Hotel> findHotelByCity(String city) {
        Collection<Hotel> hotels = getAllHotel()
                .stream()
                .filter(hotel -> hotel.getCityName().equals(city))
                .collect(Collectors.toList());
        if (!hotels.isEmpty()) {
            System.out.println("\n  Список отелей по городам");

            for (Hotel hotel : hotels) {
                System.out.println(hotel);

                if (hotel.getRooms() != null) {

                    for (Room room : hotel.getRooms()) {
                        System.out.println("   " + room);
                    }
                }
            }
        } else {
            System.out.println("\n  Отелей с таким городом нет");
        }
        return hotels;

    }

    void bookRoom(long roomId, long userId, long hotelId) {
        if (userDao.findById(userId) != null) {
            User user = userDao.findById(userId);

            try {
          if (user != null && user.isActive()) {
                    Room room = getAllHotel().stream().filter(hotel -> hotel.getId() == hotelId).findFirst().get().getRooms()
                            .stream().filter(room1 -> room1.getId() == roomId).findAny().get();
                    if (room.getUserReservedId() == 0) {
                        room.setUserReservedId(userId);
                       //TODO update
                       roomDAO.syncListToDB();
                        System.out.println("\n  Комната успешно забронирована\n" + room);
                    } else {
                        System.out.println("\n  Извените эта комната забронирована");
                    }
                } else {
                    System.out.println("\n  Введите пожалуйста логин и пароль");
                }
            } catch (NoSuchElementException e) {
                System.out.println("\n  No information about hotels");
            }
        }
    }

    void cancelReservation(long roomId, long userId, long hotelId) {
        User user = userDao.findById(userId);
        try {
            if (user != null && user.isActive()) {
                Room room = getAllHotel().stream().filter(hotel -> hotel.getId() == hotelId).findFirst().get().getRooms()
                        .stream().filter(room1 -> room1.getId() == roomId).findAny().get();
                if (room.getUserReservedId() != 0) {
                    room.setUserReservedId(0);
                    roomDAO.syncListToDB();
                    System.out.println("\n  Отмена бронирования выполнено\n" + room);
                } else {
                    System.out.println("\n  Эта комната не была забронирована");
                }
            } else {
                System.out.println("\n  Введите пожалуйста логин и пароль");
            }
        } catch (NoSuchElementException e) {
            System.out.println("\n  No information about hotels");
        }
    }


    Collection<Hotel> findRoom(Map<String, String> params) {
        System.out.println("\n  Поиск комнат по параметрам");
        int person;
        int price;
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

        Room findRooms = new Room(person, price);
        // Hotel findHotel = new Hotel(params.get("Hotel"), params.get("City"));

        Predicate<Hotel> hot = (hotel -> hotel.getCityName()
                .equals(params.get("City")) || hotel.getHotelName()
                .equals(params.get("Hotel")));
        // Predicate<Hotel> hot = (hotel -> hotel.equals(findHotel));

        List<Hotel> foundhotel = getAllHotel().stream()
                .filter(hot).collect(Collectors.toList());
        String text = "";
        for (Hotel hotel : foundhotel) {
            System.out.println("\n" + hotel);
            if (person != 0 || price != 0) {
                if (hotel.getRooms()!=null){
                    for (Room room : hotel.getRooms()) {
                        if (room.equals(findRooms)) {
                            System.out.println("   " + room);
                        }
                    }
            }else {
                    System.out.println("\nНет информации о комнатах ");
                }
            } else {
                text = "Параметры для комнат не задано, печатается весь список";
                hotel.getRooms().forEach(room -> System.out.println("   " + room));
            }
        }

        System.out.println("\n Параметры поиска || " + text
                + "\nCity:  " + params.get("City")
                + "\nHotel: " + params.get("Hotel")
                + "\nMaxPrice: " + price
                + "\nPerson: " + person);

        return foundhotel;
    }


    long registerUser(User user) {
        User userFound = user;

        try {
        userFound = getAllUser()
                    .stream()
                    .filter(u -> u.equals(user)).findFirst().get();
            userFound.setActive(true);
            System.out.println("\n  Пользователь успешно зарегистрирован\n" + userFound);
        } catch (RuntimeException e) {
            System.out.println("\n  Отсутствует информация о пользователe");
        }
        return userFound.getId();
    }

}


