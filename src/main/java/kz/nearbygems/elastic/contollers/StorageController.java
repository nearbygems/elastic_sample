package kz.nearbygems.elastic.contollers;

import kz.nearbygems.elastic.models.web.StorageRecord;
import kz.nearbygems.elastic.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/storages")
public class StorageController {

  private final StorageService service;

  @GetMapping("/")
  public List<StorageRecord> load() {

    return service.load();
  }

  @GetMapping("/{id}")
  public StorageRecord loadById(@PathVariable("id") String id) {

    return service.loadById(id);
  }

  @PostMapping("/save")
  public String save(@RequestBody StorageRecord type) {

    return service.save(type);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") String id) {

    service.delete(id);
  }

}
