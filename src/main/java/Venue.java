import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Venue {
  private int id;
  private String name;
  private String address;
  private String phone;


  public Venue(String name, String address, String phone) {
    this.name = name;
    this.address = address;
    this.phone = phone;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getPhone() {
    return phone;
  }

  public int getId() {
    return id;
  }

  public static List<Venue> allVenues() {
    String sql = "SELECT id, name, address, phone FROM venues*";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Venue.class);
    }
  }

  @Override public boolean equals(Object otherVenue) {
    if(!(otherVenue instanceof Venue)) {
      return false;
    } else {
      Venue newVenue = (Venue) otherVenue;
      return this.getName().equals(newVenue.getName()) &&
             this.getAddress().equals(newVenue.getAddress()) &&
             this.getPhone().equals(newVenue.getPhone());
      }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql ="INSERT INTO venues (name, address, phone) VALUES (:name, :address, :phone)";
      this.id = (int)con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("address", this.address)
        .addParameter("phone", this.phone)
        .executeUpdate()
        .getKey();
    }
  }

  public static Venue find (int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM venues WHERE id = :id";
      Venue venue = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Venue.class);
      return venue;
    }
  }

  public void addBand(Band band) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues (band_id, venue_id) VALUES (:band_id, :id)";
      con.createQuery(sql)
        .addParameter("id", this.getId())
        .addParameter("band_id", band.getId())
        .executeUpdate();
    }
  }

  public List<Band> getBands() {
    try (Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT band_id FROM bands_venues WHERE id = :id";
      List<Integer>bandIds = con.createQuery(joinQuery)
        .addParameter("id", this.getId())
        .executeAndFetch(Integer.class);

      List<Band> bands = new ArrayList<Band>();

      for(Integer bandId : bandIds) {
        String bandQuery = "SELECT * FROM bands WHERE id = :bandId";
        Band band = con.createQuery(bandQuery)
          .addParameter("bandId", bandId)
          .executeAndFetchFirst(Band.class);
        bands.add(band);
      }
      return bands;
    }
  }

  public void update(String newName, String newAddress, String newPhone) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE venues SET name = :name, address = :address, phone = :phone WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", newName)
        .addParameter("address", newAddress)
        .addParameter("phone", newPhone)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }
}
