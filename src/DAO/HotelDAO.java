package DAO;

import DB.DBUtils;
import enti.Hotel;
import enti.Room;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HotelDAO implements DAO<Hotel> {

    private static File file = new File(".\\src\\DB\\hotels");
    private static List<Hotel> list = new ArrayList<>();
    private static List<Hotel> hotelsRoomsId = new ArrayList<>();
    private static DAO<Room> roomDAO = new RoomDAO();

    static {
        syncDBtoList();
    }

    private static void syncDBtoList() {
        if (!list.isEmpty()) {
            list.clear();
            hotelsRoomsId.clear();
        }

        List<List<String>> inputDBData = DBUtils.getDBtoList(file);
        try {
            list = inputDBData.stream()
                    .map(s -> (new Hotel(Long.valueOf(s.get(0)), s.get(1), s.get(2))))
                    .collect(Collectors.toList());
            Map<Long, List<Room>> map = roomDAO.getAll().stream().collect(Collectors.groupingBy(Room::getIdHotel));
            for (Hotel hote : list) {
                hote.setRooms(map.get(hote.getId()));
            }
           
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Hotel's data is't found in DB");
        } catch (ClassCastException e) {
            System.out.println("Incorrect type of input hotel's data");
        }

    }


    public void syncListToDB() {
        if (list == null) {
            return;
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, false));
            StringBuilder sb = new StringBuilder();

            for (Hotel hotel : list) {
                StringBuilder roomIds = new StringBuilder();
                for (Room room : hotel.getRooms()) {
                    roomIds.append(" " + room.getId());
                }
                sb.append(hotel.getId() + " " + hotel.getHotelName() + " " + hotel.getCityName()
                        + roomIds + System.lineSeparator());
            }
            bw.write(sb.toString());

        } catch (IOException e) {
            System.out.println("Wright hotel's data in DB was canceled");
        } finally {
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("Can't close DB connection");
                }
        }
    }

    @Override
    public boolean save(Hotel hotel) {
        if (hotel == null)
            return false;
        if (list.isEmpty())
            hotel.setId(0);
        else hotel.setId(list.get(list.size() - 1).getId() + 1);
        list.add(hotel);
        syncListToDB();
        return true;
    }


    @Override
    public boolean delete(Hotel hotel) {
        if (hotel == null)
            return false;
        if (list.remove(hotel)) {
            syncListToDB();
            return true;
        } else {
            System.out.println("Hotel not found");
            return false;
        }
    }

    @Override
    public Hotel findById(long id) {
        try {
            return list.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        } catch (NullPointerException e) {
            System.out.println("Hotel with id=" + id + " not found");
            return null;
        }
    }

    @Override
    public List<Hotel> getAll() {
        return list;
    }

    public static void main(String[] args) {
        HotelDAO h = new HotelDAO();
        List<Room> l = new ArrayList<>();
        l.add(new Room(3, 5000));
        l.add(new Room(4, 5000));
        l.add(new Room(2, 5000));
        h.save(new Hotel("Hilton", "Kiev", l));
    }

}
