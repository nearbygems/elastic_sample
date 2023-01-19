package kz.nearbygems.elastic.contollers;

import kz.nearbygems.elastic.models.web.DatamartRecord;
import kz.nearbygems.elastic.services.DatamartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/data_mart")
public class DatamartController {

  private final DatamartService service;

  @GetMapping("/{id}")
  public DatamartRecord loadById(@PathVariable("id") String id) {

    return service.loadById(id);
  }

  @PostMapping("/save")
  public String save(@RequestBody DatamartRecord record) {

    return service.save(record);
  }

}
