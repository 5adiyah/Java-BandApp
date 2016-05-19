import org.sql2o.*;
import org.junit.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("BADASS NAME");
  }
  @Test
  public void venuesTest() {
    goTo("http://localhost:4567/");
    click("a", withText ("VIEW OUR VENUES"));
    assertThat(pageSource()).contains("Add New Venue:");
  }

  @Test
  public void bandsTest() {
    goTo("http://localhost:4567/");
    click("a", withText ("VIEW OUR BANDS"));
    assertThat(pageSource()).contains("Add New Band:");
  }

  @Test
    public void venuesFormTest() {
      goTo("http://localhost:4567/");
      click("a", withText ("VIEW OUR VENUES"));
      assertThat(pageSource()).contains("Add New Venue:");
      fill("#name").with("Name");
      fill("#phone").with("Phone");
      fill("#address").with("Address");
      submit("btn");
      assertThat(pageSource()).contains("Name");
  }

  @Test
    public void bandsFormTest() {
      goTo("http://localhost:4567/");
      click("a", withText ("VIEW OUR BANDS"));
      fill("#name").with("Name");
      fill("#genre").with("Genre");
      submit("btn");
      assertThat(pageSource()).contains("Name");
  }

  @Test
    public void venueIndividual() {
      Venue myVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
      myVenue.save();
      String link = String.format("http://localhost:4567/venues/%d", myVenue.getId());
      goTo(link);
      assertThat(pageSource()).contains("Venue Name");
      assertThat(pageSource()).contains("Venue address");
      assertThat(pageSource()).contains("phoneNumber");
  }

  @Test
    public void bandIndividual() {
      Band myBand = new Band("Band Name", "Genre");
      myBand.save();
      String link = String.format("http://localhost:4567/bands/%d", myBand.getId());
      goTo(link);
      assertThat(pageSource()).contains("Band Name");
      assertThat(pageSource()).contains("Genre");
  }

  @Test
    public void linkBandAndVenue() {
      Band myBand = new Band("Band Name", "Genre");
      myBand.save();
      Venue myVenue = new Venue("Venue Name", "Venue address", "phoneNumber");
      myVenue.save();
      String link = String.format("http://localhost:4567/venues/%d", myVenue.getId());
      goTo(link);
      fillSelect("#bandId").withText("Band Name");
      submit("btn");
      assertThat(pageSource()).contains("Band Name");
  }

  @Test
    public void bandUpdate() {
      Band myBand = new Band("Band Name", "Genre");
      myBand.save();
      myBand.update("New Band Name", "New Genre");
      String link = String.format("http://localhost:4567/bands/%d", myBand.getId());
      goTo(link);
      assertThat(pageSource()).contains("New Band Name");
    }

  @Test
    public void bandDelete() {
      Band myBand = new Band("Band Name", "Genre");
      myBand.save();
      myBand.delete();
      String link = String.format("http://localhost:4567/bands/%d", myBand.getId());
      goTo(link);
      click("a", withText("Delete Band"));
      assertEquals(0, Band.all().size());
    }








}
