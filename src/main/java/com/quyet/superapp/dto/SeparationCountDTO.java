    package com.quyet.superapp.dto;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class SeparationCountDTO {
        private String date;
        private Long count;
    }
