import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String,Object> model = new HashMap<String,Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model,layout);
    }, new VelocityTemplateEngine());

    get("/bands", (request, response) -> {
      HashMap<String,Object> model = new HashMap<String,Object>();
      model.put("bands", Band.all());
      model.put("template", "templates/bands.vtl");
      return new ModelAndView(model,layout);
    }, new VelocityTemplateEngine());

    get("/venues", (request, response) -> {
      HashMap<String,Object> model = new HashMap<String,Object>();
      model.put("venues", Venue.allVenues());
      model.put("template", "templates/venues.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/bands", (request, response) -> {
      HashMap<String,Object> model = new HashMap<String,Object>();
      String name = request.queryParams("name");
      String type = request.queryParams("genre");
      Band newBand = new Band(name, type);
      newBand.save();
      model.put("allVenues", Venue.allVenues());
      model.put("allBands", Band.all());
      response.redirect("/bands");
      return null;
    });

    post("/venues", (request,response) -> {
      HashMap<String,Object> model = new HashMap<String,Object>();
      String venue = request.queryParams("name");
      String address = request.queryParams("address");
      String phone = request.queryParams("phone");
      Venue newVenue = new Venue(venue, address, phone);
      newVenue.save();
      model.put("allVenues", Venue.allVenues());
      model.put("allBands", Band.all());
      response.redirect("/venues");
    return null;
    });

    post("/add_band", (request, response) -> {
      HashMap<String,Object> model = new HashMap<String,Object>();
      int bandId = Integer.parseInt(request.queryParams("bandId"));
      int venueId = Integer.parseInt(request.queryParams("venueId"));
      Band band = Band.find(bandId);
      Venue venue = Venue.find(venueId);
      model.put("allVenues", Venue.allVenues());
      model.put("allBands", Band.all());
      venue.addBand(band);
      response.redirect("/venues/" + venueId);
      return null;
    });

    get("/bands/:bId", (request, response) -> {
      HashMap<String,Object> model = new HashMap<String,Object>();
      Band band = Band.find(Integer.parseInt(request.params(":bId")));
      model.put("bands", band);
      model.put("allVenues", Venue.allVenues());
      model.put("template", "templates/band.vtl");
      return new ModelAndView(model,layout);
    }, new VelocityTemplateEngine());

    get("/venues/:id", (request, response) -> {
      HashMap<String,Object>model = new HashMap<String,Object>();
      Venue venue = Venue.find(Integer.parseInt(request.params(":id")));
      model.put("venues", venue);
      model.put("allBands", Band.all());
      model.put("template", "templates/venue.vtl");
      return new ModelAndView(model,layout);
    }, new VelocityTemplateEngine());

    post("/bands/:bId/update", (request, response) -> {
      HashMap<String,Object> model = new HashMap<String,Object>();
      Band band = Band.find(Integer.parseInt(request.params(":bId")));
      int bandId = band.getId();
      String newName = request.queryParams("newName");
      String newGenre = request.queryParams("newGenre");
      band.update(newName, newGenre);
      model.put("bands", band);
      model.put("allVenues", Venue.allVenues());
      model.put("allBands", Band.all());
      response.redirect("/bands/" + bandId);
      return null;
    });

    get("bands/:bId/delete", (request,response) -> {
      HashMap<String,Object> model = new HashMap<String,Object>();
      Band band = Band.find(Integer.parseInt(request.params(":bId")));
      int bandId = band.getId();
      band.delete();
      model.put("bands", band);
      model.put("allVenues", Venue.allVenues());
      model.put("allBands", Band.all());
      response.redirect("/bands");
      return null;
    });
  }
}
