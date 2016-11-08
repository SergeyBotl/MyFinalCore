package DB;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nazar on 06.11.2016.
 */
public class DBUtils {

    private static String DBpath;

    public static String getDBpath() {
        if (DBpath != null)
            return DBpath;
        else {
            readDBPath();
            return DBpath;
        }
    }

    public static void readDBPath() {

        System.out.println("\nPlease, wright path to DB.\nSample: C:\\\\User\\\\Projects\\\\FinalCore\\\\DB");
        // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String line = "";


        //line = reader.readLine();
        //line = "E:\\\\GitHub\\\\Final\\\\src\\\\DB";
        String checkH = ".\\DB\\hotels";
        String checkR = ".\\DB\\rooms";
        String checkU = ".\\DB\\users";
        // String checkU = line + ".\\users";

        if (Paths.get(checkH).toFile().exists()
                && Paths.get(checkR).toFile().exists()
                && Paths.get(checkU).toFile().exists()) {
            System.out.println("Successful");
            DBpath = line;

        } else System.out.println("Wrong input path, please, try again");




          /*  try {
                reader.close();
            } catch (IOException e) {
                System.out.println("Read data error");
            }*/

    }


    public static List<List<String>> getDBtoList(File file) {

        List<List<String>> result = new ArrayList<>();
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(file));

            String line = br.readLine();

            while (line != null) {

                List<String> currentLine = new ArrayList<>(Arrays.asList(line.split(" ")));
                result.add(currentLine);
                line = br.readLine();

            }

        } catch (IOException e) {
            System.out.println("Read data error");
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                System.out.println("Read data error");
            }
        }

        return result;
    }

    public static String getDBtoString(File file) {

        BufferedReader br = null;
        StringBuilder sb;
        try {
            br = new BufferedReader(new FileReader(file));
            sb = new StringBuilder();

            String line = br.readLine();
            while (line != null) {
                sb.append(line + System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();

        } catch (IOException e) {
            System.out.println("Read data error");
            return null;
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                System.out.println("Read data error");
            }
        }
    }


}
