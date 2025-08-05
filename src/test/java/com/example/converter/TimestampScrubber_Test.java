package com.example.converter;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimestampScrubber_Test {

    TimestampScrubber timestampScrubber = new TimestampScrubber();

    @Test
    void paragraph() {
        Assertions.assertThat(timestampScrubber.scrub("cnvs-block-core-paragraph-1754394875488"))
                .isEqualTo("cnvs-block-core-paragraph-1234567890123");
    }
    @Test
    void heading() {
        Assertions.assertThat(timestampScrubber.scrub("cnvs-block-core-heading-1754394875488"))
                .isEqualTo("cnvs-block-core-heading-1234567890123");
    }
}
