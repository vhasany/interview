package com.zirofam.interview.controller;

import com.zirofam.interview.controller.dto.FinancialDto;
import com.zirofam.interview.controller.dto.PartialFinancialDto;
import com.zirofam.interview.controller.dto.UpdateFinancialDto;
import com.zirofam.interview.controller.mapper.FinancialMapper;
import com.zirofam.interview.controller.model.FinancialModel;
import com.zirofam.interview.domain.FinancialEntity;
import com.zirofam.interview.service.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FinancialController {

  private final FinancialMapper mapper;

  private final FinancialService service;

  @PostMapping("/v1/financial")
  public ResponseEntity<FinancialModel> create(@RequestBody FinancialDto dto) {

    if (dto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    FinancialEntity entity = mapper.toEntity(dto);
    entity = service.save(entity);
    FinancialModel model = mapper.toModel(entity);
    return new ResponseEntity<>(model, HttpStatus.CREATED);
  }

  @GetMapping("/v1/financial")
  public ResponseEntity<List<FinancialModel>> findAll() {
    List<FinancialEntity> entities = service.findAll();
    return new ResponseEntity<>(mapper.toModel(entities), HttpStatus.OK);
  }

  @PutMapping("/v1/financial/{id}")
  public ResponseEntity<FinancialModel> update(
      @PathVariable String id, @RequestBody @Valid UpdateFinancialDto dto) {
    FinancialEntity entity =
        service
            .findById(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

    mapper.updateEntity(entity, dto);
    service.save(entity);
    return new ResponseEntity<>(mapper.toModel(entity), HttpStatus.OK);
  }

  @PatchMapping("/v1/financial/{id}")
  public ResponseEntity<FinancialModel> partialUpdate(
      @PathVariable String id, @RequestBody @Valid PartialFinancialDto dto) {
    FinancialEntity entity =
        service
            .findById(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

    mapper.updateEntity(entity, dto);
    service.save(entity);
    return new ResponseEntity<>(mapper.toModel(entity), HttpStatus.OK);
  }

  @DeleteMapping("/v1/financial/{id}")
  public ResponseEntity delete(@PathVariable String id) {
    service
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

    service.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/v1/financial/{id}")
  public ResponseEntity<FinancialModel> findById(@PathVariable String id) {
    Optional<FinancialEntity> financialEntity = service.findById(id);
    if (financialEntity.isPresent()) {
      return new ResponseEntity<>(mapper.toModel(financialEntity.get()), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/v1/financial/search")
  public ResponseEntity<FinancialModel> findByUser(@RequestParam(name = "user") String userId) {
    Optional<FinancialEntity> financialEntity = service.findByUser(userId);
    if (financialEntity.isPresent()) {
      return new ResponseEntity<>(mapper.toModel(financialEntity.get()), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
