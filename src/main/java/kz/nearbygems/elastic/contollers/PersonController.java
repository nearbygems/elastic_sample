package kz.nearbygems.elastic.contollers;

import kz.nearbygems.elastic.models.web.Persons;
import kz.nearbygems.elastic.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

  private final PersonService service;

  @GetMapping("/update")
  public void loadPersonsFromLdap() {

    final var thread = new Thread(service :: loadPersonsFromLdap);
    thread.start();
  }

  @GetMapping("/search")
  public Persons search(@RequestParam(value = "search", required = false) String search,
                        @RequestParam("offset") int offset,
                        @RequestParam("limit") int limit) {

    return service.search(search, offset, limit);
  }

  @PostMapping("/save")
  public String save(@RequestBody Persons.Record record) {

    return service.save(record);
  }

  @GetMapping("/{id}")
  public Persons.Record loadById(@PathVariable("id") String id) {

    return service.loadById(id);
  }

}
