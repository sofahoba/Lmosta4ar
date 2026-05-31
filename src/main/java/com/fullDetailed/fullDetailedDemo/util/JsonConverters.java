package com.fullDetailed.fullDetailedDemo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.*;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;
import java.util.List;

public class JsonConverters {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Converter
    public static class DefendantListConverter implements AttributeConverter<List<DefendantDto>, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(List<DefendantDto> a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public List<DefendantDto> convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, DefendantDto.class));
        }
    }

    @Converter
    public static class ChargeListConverter implements AttributeConverter<List<ChargeDto>, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(List<ChargeDto> a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public List<ChargeDto> convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, ChargeDto.class));
        }
    }

    @Converter
    public static class IncidentListConverter implements AttributeConverter<List<IncidentDto>, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(List<IncidentDto> a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public List<IncidentDto> convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, IncidentDto.class));
        }
    }

    @Converter
    public static class EvidenceListConverter implements AttributeConverter<List<EvidenceDto>, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(List<EvidenceDto> a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public List<EvidenceDto> convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, EvidenceDto.class));
        }
    }

    @Converter
    public static class WitnessListConverter implements AttributeConverter<List<WitnessStatementDto>, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(List<WitnessStatementDto> a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public List<WitnessStatementDto> convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, WitnessStatementDto.class));
        }
    }

    @Converter
    public static class ConfessionListConverter implements AttributeConverter<List<ConfessionDto>, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(List<ConfessionDto> a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public List<ConfessionDto> convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, ConfessionDto.class));
        }
    }

    @Converter
    public static class LabReportListConverter implements AttributeConverter<List<LabReportDto>, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(List<LabReportDto> a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public List<LabReportDto> convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, LabReportDto.class));
        }
    }

    @Converter
    public static class CriminalProceedingListConverter implements AttributeConverter<List<CriminalProceedingDto>, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(List<CriminalProceedingDto> a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public List<CriminalProceedingDto> convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, CriminalProceedingDto.class));
        }
    }

    @Converter
    public static class DefenseDocumentListConverter implements AttributeConverter<List<DefenseDocumentDto>, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(List<DefenseDocumentDto> a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public List<DefenseDocumentDto> convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, DefenseDocumentDto.class));
        }
    }

    @Converter
    public static class ProceduralAuditConverter implements AttributeConverter<ProceduralAuditDto, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(ProceduralAuditDto a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public ProceduralAuditDto convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, ProceduralAuditDto.class);
        }
    }

    @Converter
    public static class SuggestedVerdictConverter implements AttributeConverter<SuggestedVerdictDto, String> {
        @Override @SneakyThrows
        public String convertToDatabaseColumn(SuggestedVerdictDto a) {
            return a == null ? null : mapper.writeValueAsString(a);
        }
        @Override @SneakyThrows
        public SuggestedVerdictDto convertToEntityAttribute(String s) {
            return s == null ? null : mapper.readValue(s, SuggestedVerdictDto.class);
        }
    }
}