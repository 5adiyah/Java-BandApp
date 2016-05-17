import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Band {
  private int id;
  private String name;
  private String genre;

  public Band (String name, String genre) {
    this.name = name;
    this.genre = genre;
  }

  public int getId() {
    return id;
  }

  public String getName(){
    return name;
  }

  public String getGenre() {
    return genre;
  }

  public static List<Band> all() {
    String sql = "SELECT id, name, genre FROM bands";
      try(Connection con = DB.sql2o.open()) {
        return con.createQuery(sql).executeAndFetch(Band.class);
      }
  }

  @Override public boolean equals(Object otherBand) {
    if(!(otherBand instanceof Band)) {
      return false;
    } else {
        Band newBand = (Band) otherBand;
        return this.getName().equals(newBand.getName()) &&
               this.getGenre().equals(newBand.getGenre()) &&
               this.getId() == (newBand.getId());
      }
  }

  public static Band find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM bands WHERE id = :id";
      Band band = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Band.class);
      return band;
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands(name, genre) VALUES (:name, :genre)";
        this.id = (int) con.createQuery(sql, true)
          .addParameter("name", this.name)
          .addParameter("genre", this.genre)
          .executeUpdate()
          .getKey();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM bands WHERE id = :id";
        con.createQuery(deleteQuery)
          .addParameter("id", this.id)
          .executeUpdate();
      String joinDeleteQuery = "DELETE FROM bands_venues WHERE id = :id";
        con.createQuery(joinDeleteQuery)
          .addParameter("id", this.id)
          .executeUpdate();
    }
  }

  public void addVenue(Venue venue) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues(id, venue_id) VALUES (:id, :venue_id)";
      con.createQuery(sql)
        .addParameter("id", this.getId())
        .addParameter("venue_id", venue.getId())
        .executeUpdate();
    }
  }

  public List<Venue> getVenues() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT venue_id FROM bands_venues WHERE id = :id";
      List<Integer> venueIds = con.createQuery(joinQuery)
        .addParameter("id", this.getId())
        .executeAndFetch(Integer.class);

      List<Venue> venues = new ArrayList<Venue>();

      for(Integer venueId : venueIds) {
        String venueQuery = "SELECT * FROM venues WHERE id = :venueId";
        Venue venue = con.createQuery(venueQuery)
          .addParameter("venueId", venueId)
          .executeAndFetchFirst(Venue.class);
        venues.add(venue);
      }
      return venues;
    }
  }

  public void update(String bandName, String bandGenre) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE bands SET name = :name, genre = :genre WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", bandName)
        .addParameter("genre", bandGenre)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }


}
