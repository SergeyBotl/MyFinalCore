package DAO;

import DB.DBUtils;
import enti.Room;
import enti.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomDAO implements DAO<Room> {
    private static RoomDAO roomDAO;

    private RoomDAO() {
    }

    public static RoomDAO getRoomDAO() {
        if (roomDAO == null) {
            roomDAO = new RoomDAO();
        }
        return roomDAO;
    }

    private static File file = new File(DBUtils.getDBpath() + "\\\\rooms");
    private static List<Room> list = new ArrayList<>();

    static {
        syncDBtoList();
    }

    private static void syncDBtoList() {
        if (!list.isEmpty())
            list.clear();

        List<List<String>> inputDBData = DBUtils.getDBtoList(file);
        try {
            list = inputDBData.stream()
                    .map(s -> (new Room(Long.valueOf(s.get(0)), Integer.valueOf(s.get(1)), Integer.valueOf(s.get(2)), Long.valueOf(s.get(3)), Long.valueOf(s.get(4)))))
                    .collect(Collectors.toList());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Room data is't found in DB");
        } catch (ClassCastException e) {
            System.out.println("Incorrect type of input room's data");
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

            for (Room room : list) {
                sb.append(room.getId() + " " + room.getPerson() + " " + room.getPrice()
                        + " " + room.getUserReservedId() + " " + room.getIdHotel() + System.lineSeparator());
            }
            bw.write(sb.toString());

        } catch (IOException e) {
            System.out.println("Wright room's data in DB was canceled");
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
    public Room save(Room room) {

        room.setId(list.size() + 1);
        room.setUserReservedId(0);
        list.add(room);
        syncListToDB();
        return room;
    }


    @Override
    public boolean delete(Room room) {
        if (room == null)
            return false;
        if (list.remove(room)) {
            syncListToDB();
            return true;
        } else {
            System.out.println("Room not found");
            return false;
        }
    }

    @Override
    public Room findById(long id) {
        return list.stream()
                .filter(e -> e.getId() != 0 && e.getId() == id).findFirst().orElse(null);


    }

    @Override
    public List<Room> getAll() {
        return list;
    }

}
