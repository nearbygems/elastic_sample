package kz.nearbygems.elastic.contollers;

import kz.nearbygems.elastic.models.web.SourceRecord;
import kz.nearbygems.elastic.services.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/source")
public class SourceController {

  private final SourceService service;

  @GetMapping("/{id}")
  public SourceRecord loadById(@PathVariable("id") String id) {

    return service.loadById(id);
  }

  @PostMapping("/save")
  public String save(@RequestBody SourceRecord record) {

    return service.save(record);
  }

}
