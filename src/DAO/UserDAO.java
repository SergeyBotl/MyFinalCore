package DAO;

import DB.DBUtils;
import enti.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class UserDAO implements DAO<User> {

    private static File file = new File(DBUtils.getDBpath() + "\\\\users");
    private static List<User> list = new ArrayList<>();

    static {
        refreshList();
    }

    private static void refreshList() {
        if (!list.isEmpty())
            list.clear();
        List<List<String>> inputDBData = DBUtils.getDBtoList(file);
        try {
        list = inputDBData.stream()
                    .map(s -> (new User(Long.valueOf(s.get(0)), s.get(1), s.get(2))))
                    .collect(Collectors.toList());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("User data is't found in DB");
        } catch (ClassCastException e) {
            System.out.println("Incorrect type of input user's data");
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

            for (User user : list)
                sb.append(user.getId() + " " + user.getUserName() + " " + user.getPassword() + System.lineSeparator());

            bw.write(sb.toString());

        } catch (IOException e) {
            System.out.println("Wright user's data in DB was canceled");
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
    public boolean save(User user) {
        if (user == null)
            return false;
        if (list.isEmpty())
            user.setId(0);
        else user.setId(list.get(list.size() - 1).getId() + 1);
        list.add(user);
        syncListToDB();
        return true;
    }


    @Override
    public boolean delete(User user) {
        if (user == null)
            return false;
        list.remove(user);
        syncListToDB();
        return true;
    }

    @Override
    public User findById(long id) {

        try {
            return list.parallelStream().filter(e -> e.getId() == id).findFirst().orElse(null);
        } catch (NullPointerException e) {
            System.out.println("User with id=" + id + " not found");
            return null;
        }
    }

    @Override
    public List<User> getAll() {
        return list;
    }

}


