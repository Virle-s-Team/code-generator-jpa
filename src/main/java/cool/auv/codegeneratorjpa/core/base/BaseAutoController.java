package cool.auv.codegeneratorjpa.core.base;

import cool.auv.codegeneratorjpa.core.exception.AppException;
import cool.auv.codegeneratorjpa.core.service.RequestInterface;
import cool.auv.codegeneratorjpa.core.utils.PaginationUtil;
import cool.auv.codegeneratorjpa.core.utils.ResponseUtil;
import cool.auv.codegeneratorjpa.core.vm.PageSortRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseAutoController<T, ID extends Serializable, REQ extends RequestInterface<T>, VM> {

    @Autowired
    protected BaseAutoService<T, ID, REQ, VM> autoService;

//    protected BaseAutoController(AbstractAutoService<T, ID, REQ, VM> autoService) {
//        this.autoService = autoService;
//    }


    @GetMapping("/{id}")
    @Operation(summary = "findById")
    public ResponseEntity<VM> findById(@PathVariable(name = "id") ID id) throws AppException {
        Optional<VM> vm = autoService.findById(id);
        return ResponseUtil.wrapOrNotFound(vm);
    }

    @GetMapping("/findByPage")
    @Operation(summary = "findByPage")
    public ResponseEntity<List<VM>> page(REQ req, PageSortRequest pageSortRequest) throws AppException {
        Page<VM> page = autoService.findPage(req.buildSpecification(), pageSortRequest.pageableWithSort());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping
    @Operation(summary = "save")
    public ResponseEntity<VM> save(@RequestBody VM vm) throws AppException {
        VM saved = autoService.save(vm);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update")
    public ResponseEntity<VM> update(@PathVariable(name = "id") ID id, @RequestBody VM vm) throws AppException {
        VM updated = autoService.update(id, vm);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "deleteById")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") ID id) throws AppException {
        autoService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
