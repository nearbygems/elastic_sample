package kz.nearbygems.elastic.contollers;

import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.models.web.ComputingTypeRecord;
import kz.nearbygems.elastic.services.ComputingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/computing_types")
public class ComputingTypeController {

  private final ComputingTypeService service;

  @GetMapping("/load")
  public List<ComputingTypeRecord> load(@RequestParam(value = "system") SystemType system) {

    return service.load(system);
  }

  @GetMapping("/{id}")
  public ComputingTypeRecord loadById(@PathVariable("id") String id) {

    return service.loadById(id);
  }

  @PostMapping("/save")
  public String save(@RequestBody ComputingTypeRecord record) {

    return service.save(record);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id")
                     String id) {

    service.delete(id);
  }

}
