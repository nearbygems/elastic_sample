package kz.nearbygems.elastic.contollers;

import kz.nearbygems.elastic.models.web.TagRecord;
import kz.nearbygems.elastic.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

  private final TagService service;

  @GetMapping("/")
  public List<TagRecord> load() {

    return service.load();
  }

  @GetMapping("/{id}")
  public TagRecord loadById(@PathVariable("id") String id) {

    return service.loadById(id);
  }

  @PostMapping("/save")
  public String save(@RequestBody TagRecord record) {

    return service.save(record);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") String id) {

    service.delete(id);
  }

}
