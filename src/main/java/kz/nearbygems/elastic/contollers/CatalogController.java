package kz.nearbygems.elastic.contollers;

import kz.nearbygems.elastic.models.web.DataCatalogRecord;
import kz.nearbygems.elastic.models.web.DataRecordList;
import kz.nearbygems.elastic.models.web.SearchRequest;
import kz.nearbygems.elastic.services.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/data_catalog")
public class CatalogController {

  private final CatalogService service;

  @GetMapping("/search")
  public DataRecordList search(@RequestBody
                               SearchRequest filter) {

    return service.search(filter);
  }

  @GetMapping("/count")
  public long count(@RequestBody
                    DataCatalogRecord.DocTypeRecord docType) {

    return service.count(docType);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id")
                     String id) {

    service.delete(id);
  }

  @GetMapping("/{id}/{name}/fields")
  public List<DataCatalogRecord.StagingRecord.ColumnRecord> loadFields(@PathVariable("id") String id,
                                                                       @PathVariable("name") String name) {

    return service.loadFields(id, name);
  }

}
