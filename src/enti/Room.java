package enti;

public class Room {

    private long id;
    private int person;
    private int price;
    private long userReservedId;
    private long idHotel;

    public Room(long id, int person, int price, long userReservedId, long idHotel) {
        this.id = id;
        this.person = person;
        this.price = price;
        this.userReservedId = userReservedId;
        this.idHotel = idHotel;
    }


    public Room(long id, int person, int price, long userReservedId) {
        this.id = id;
        this.person = person;
        this.price = price;
        this.userReservedId = userReservedId;
    }

    public Room(int person, int price) {
        this.person = person;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPerson() {
        return person;
    }

    public int getPrice() {
        return price;
    }

    public long getUserReservedId() {
        return userReservedId;
    }

    public void setUserReservedId(long userReservedId) {
        this.userReservedId = userReservedId;
    }

    public long getIdHotel() {
        return idHotel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;


        if (person == room.person) return true;
        return price < room.price;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + person;
        result = 31 * result + price;
        return result;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", person=" + person +
                ", price=" + price +
                ", booked=" +isEmpty(userReservedId) +
                // ", Hotel=" + idHotel +
                '}';
    }

    String isEmpty(long id) {
        return  id== 0 ? "Свободная" : "Забронирована";
    }
}
