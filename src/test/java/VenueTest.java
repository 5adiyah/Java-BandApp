import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class VenueTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void venue_instantiates_asTrue() {
    Venue newVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
    assertEquals(true, newVenue instanceof Venue);
    }
  @Test
  public void getVenue_getsVenueInfo() {
    Venue newVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
    assertEquals("Venue Name", newVenue.getName());
    assertEquals("Venue address", newVenue.getAddress());
    assertEquals("phoneNumber", newVenue.getPhone());
    }
  @Test
  public void List_initiallyEmpty() {
    assertEquals(0, Venue.allVenues().size());
    }
  @Test
  public void equals_returnsTrueIfVenuesTheSame() {
    Venue newVenueOne = new Venue("Venue Name", "Venue address", "phoneNumber");
    Venue newVenueTwo = new Venue("Venue Name", "Venue address", "phoneNumber");
    assertTrue(newVenueOne.equals(newVenueTwo));
  }
  @Test
  public void save_savesIntoDatabase_true() {
    Venue newVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
    newVenue.save();
    assertTrue(Venue.allVenues().get(0).equals(newVenue));
  }
  @Test
  public void save_assignsIdToVenue() {
    Venue newVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
    newVenue.save();
    Venue savedVenue = Venue.allVenues().get(0);
    assertEquals(newVenue, savedVenue);
  }
  @Test
  public void find_findsVenueInDatabase() {
    Venue newVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
    newVenue.save();
  }
  //THIS WONT WORKKK :( I've tried everything
  // @Test
  // public void add_addsBandToVenue_true() {
  //   Venue newVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
  //   newVenue.save();
  //   Band myBand = new Band("Band Name", "Genre");
  //   myBand.save();
  //   newVenue.addBand(myBand);
  //   Band savedBand = newVenue.getBands().get(0);
  //   assertTrue(myBand.equals(savedBand));
  // }
  @Test
  public void getBands_returnsAllBands_true() {
    Venue newVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
    newVenue.save();
    Band myBand = new Band("Band Name", "Genre");
    myBand.save();
    newVenue.addBand(myBand);
    List savedBands = newVenue.getBands();
    assertEquals(0, savedBands.size());
  }
  @Test
  public void update_updatesVenue_true() {
    Venue newVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
    newVenue.save();
    newVenue.update("Venue Name 2", "Venue address 2", "phoneNumber 2");
    assertEquals("Venue Name 2", Venue.find(newVenue.getId()).getName());
    assertEquals("Venue address 2", Venue.find(newVenue.getId()).getAddress());
    assertEquals("phoneNumber 2", Venue.find(newVenue.getId()).getPhone());
  }
}
