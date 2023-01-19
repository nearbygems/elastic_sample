package kz.nearbygems.elastic.contollers;

import kz.nearbygems.elastic.models.web.TriggerRecord;
import kz.nearbygems.elastic.services.TriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/trigger")
public class TriggerController {

  private final TriggerService service;

  @GetMapping("/{id}")
  public TriggerRecord loadById(@PathVariable("id") String id) {

    return service.loadById(id);
  }

  @PostMapping("/save")
  public String save(@RequestBody TriggerRecord record) {

    return service.save(record);
  }

}
