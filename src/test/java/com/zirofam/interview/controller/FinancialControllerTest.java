package com.zirofam.interview.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zirofam.interview.InterviewApplication;
import com.zirofam.interview.controller.dto.FinancialDto;
import com.zirofam.interview.controller.dto.PartialFinancialDto;
import com.zirofam.interview.controller.dto.UpdateFinancialDto;
import com.zirofam.interview.domain.FinancialEntity;
import com.zirofam.interview.domain.enumeration.AccountingStatus;
import com.zirofam.interview.repository.FinancialRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest(classes = InterviewApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FinancialControllerTest {

  protected static final ObjectMapper mapper =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @Autowired private FinancialRepository repository;

  @Autowired private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void create() {
    int databaseSizeBeforeCreate = repository.findAll().size();
    FinancialDto dto =
        FinancialDto.builder()
            .amount(BigDecimal.valueOf(12.5))
            .status(AccountingStatus.CREDITOR)
            .user("user1")
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/financial")
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    List<FinancialEntity> entities = repository.findAll();
    assertThat(entities).hasSize(databaseSizeBeforeCreate + 1);
    FinancialEntity entity = entities.get(entities.size() - 1);
    assert entity.getAmount().compareTo(dto.getAmount()) == 0;
    assertThat(entity.getUser()).isEqualTo(dto.getUser());
    assertThat(entity.getStatus()).isEqualTo(dto.getStatus());
  }

  @Test
  @SneakyThrows
  void update() {
    FinancialEntity financialEntity =
        FinancialEntity.builder()
            .amount(BigDecimal.valueOf(50.12))
            .user("user2")
            .status(AccountingStatus.CREDITOR)
            .build();
    repository.save(financialEntity);
    UpdateFinancialDto dto =
        UpdateFinancialDto.builder()
            .amount(BigDecimal.valueOf(12.15))
            .status(AccountingStatus.DEBTOR.name())
            .user("user5")
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/v1/financial/" + financialEntity.getId())
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
    Optional<FinancialEntity> updatedEntity = repository.findById(financialEntity.getId());
    assertThat(updatedEntity.isPresent());
    assertThat(updatedEntity.get().getAmount()).isEqualTo(dto.getAmount());
    assertThat(updatedEntity.get().getUser()).isEqualTo(dto.getUser());
  }

  @Test
  @SneakyThrows
  void updateWithOutId() {
    UpdateFinancialDto dto =
        UpdateFinancialDto.builder()
            .amount(BigDecimal.valueOf(12.15))
            .status(AccountingStatus.DEBTOR.name())
            .user("user5")
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/v1/financial/" + "qqqqqqq-wwwwwwwwww-dddddd-dddddddd")
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @SneakyThrows
  void partialUpdate() {
    FinancialEntity financialEntity =
        FinancialEntity.builder()
            .amount(BigDecimal.valueOf(60.02))
            .user("user3")
            .status(AccountingStatus.DEBTOR)
            .build();
    repository.save(financialEntity);
    PartialFinancialDto dto = PartialFinancialDto.builder().user("user333").build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch("/api/v1/financial/" + financialEntity.getId())
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
    Optional<FinancialEntity> updatedEntity = repository.findById(financialEntity.getId());
    assertThat(updatedEntity.isPresent());
    assertThat(updatedEntity.get().getAmount()).isEqualTo(financialEntity.getAmount());
    assertThat(updatedEntity.get().getUser()).isEqualTo(dto.getUser());
  }

  @Test
  @SneakyThrows
  void delete() {
    FinancialEntity financialEntity =
        FinancialEntity.builder()
            .amount(BigDecimal.valueOf(14.14))
            .user("user45")
            .status(AccountingStatus.DEBTOR)
            .build();
    FinancialEntity entity = repository.save(financialEntity);
    int databaseSizeAfterCreate = repository.findAll().size();
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/api/v1/financial/" + entity.getId()))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
    assertThat(databaseSizeAfterCreate).isEqualTo(repository.findAll().size() + 1);
    assertThat(repository.existsById(entity.getId())).isFalse();
  }
  @Test
  @SneakyThrows
  void deleteWithOutId() {
    int databaseSize = repository.findAll().size();
    mockMvc
            .perform(MockMvcRequestBuilders.delete("/api/v1/financial/" + "qqqqqqqqqqqqqqqqqqqqqqq"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    assertThat(databaseSize).isEqualTo(repository.findAll().size());
  }

  @Test
  @SneakyThrows
  void findById() {
    FinancialEntity financialEntity =
            FinancialEntity.builder()
                    .amount(BigDecimal.valueOf(18.77))
                    .user("user25")
                    .status(AccountingStatus.CREDITOR)
                    .build();
    FinancialEntity entity = repository.save(financialEntity);
    MockHttpServletResponse result=mockMvc
            .perform(MockMvcRequestBuilders.get("/api/v1/financial/" + entity.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
    FinancialEntity returnedObject=mapper.readValue(result.getContentAsString(), FinancialEntity.class);
    assertThat(entity.getId()).isEqualTo(returnedObject.getId());
    assertThat(entity.getUser()).isEqualTo(returnedObject.getUser());
  }
  @Test
  @SneakyThrows
  void findByUser() {
    FinancialEntity financialEntity =
            FinancialEntity.builder()
                    .amount(BigDecimal.valueOf(20.20))
                    .user("user20")
                    .status(AccountingStatus.DEBTOR)
                    .build();
    FinancialEntity createdEntity = repository.save(financialEntity);
    MockHttpServletResponse result=mockMvc
            .perform(MockMvcRequestBuilders.get("/api/v1/financial/search?user=" + createdEntity.getUser()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
    FinancialEntity returnedObject=mapper.readValue(result.getContentAsString(), FinancialEntity.class);
    assertThat(createdEntity.getId()).isEqualTo(returnedObject.getId());
    assertThat(createdEntity.getUser()).isEqualTo(returnedObject.getUser());
  }
}
