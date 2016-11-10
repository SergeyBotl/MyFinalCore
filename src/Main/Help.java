package Main;

import java.util.HashMap;
import java.util.Map;

public class Help {
   static void hp(int index){
        Map<Integer,String>help=new HashMap<>();
        help.put(1, " ============ Регистрация ============");
        help.put(2, " ===== Список отелей по городам ======");
        help.put(3, " ==== Список отелей по названию ======");
        help.put(4, " ====== Бронирование комнаты =========");
        help.put(5, " ======= Отмена бронирования =========");
        help.put(6, " ==== Поиск номера по параметрам =====");
        help.put(7, " =========== Список отелей ===========");

       System.out.println("\n"+help.get(index)+"\n");


    }


}
