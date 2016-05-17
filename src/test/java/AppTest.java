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
  public void venuesNewBandTest() {
    goTo("http://localhost:4567/");
    click("a", withText ("VIEW OUR VENUES"));
    assertThat(pageSource()).contains("Add New Venue:");
    fill("#name").with("myName");
    fill("#phone").with("myPhone");
    fill("#address").with("myAddress");
    submit("btn");
    click("a", withText ("myName"));
    fill("#name").with("Name2");
    fill("#genre").with("Genre");
    submit("btn");
    click("a", withText ("Name2"));
    assertThat(pageSource()).contains("Name2");
  }


}
