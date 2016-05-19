import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class BandTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void method_instantiates_true() {
    Band newBand = new Band("Band Name", "Genre");
    assertEquals(true, newBand instanceof Band);
  }

  @Test
  public void allGetMethods_returnsBandInfo() {
    Band newBand = new Band("Band Name", "Genre");
    assertEquals("Band Name", newBand.getName());
    assertEquals("Genre", newBand.getGenre());
  }

  @Test
  public void bandArray_initiallyEmpty() {
    assertEquals(0, Band.all().size());
  }

  @Test
  public void equals_returnsTrueifBandsTheSame() {
    Band newBandOne = new Band("Band Name", "Genre");
    Band newBandTwo = new Band("Band Name", "Genre");
    assertTrue(newBandOne.equals(newBandTwo));
  }

  @Test
  public void save_savesIdToBand_true() {
    Band newBand = new Band("Band Name", "Genre");
    newBand.save();
    Band savedBand = Band.all().get(0);
  }

  @Test
  public void delete_deletesBandsAndVenuesAssociations() {
    Band newBand = new Band("Band Name", "Genre");
    newBand.save();
    newBand.delete();
    assertEquals(0, newBand.getVenues().size());
  }

  @Test
  public void update_updatesBands_true() {
    Band newBand = new Band("Band Name", "Genre");
    newBand.save();
    newBand.update("Band Name 2", "Band Genre 2");
    assertEquals("Band Name 2", Band.find(newBand.getId()).getName());
    assertEquals("Band Genre 2", Band.find(newBand.getId()).getGenre());
  }
}
