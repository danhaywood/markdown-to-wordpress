package com.danhaywood.md2wp;

import org.approvaltests.core.Scrubber;

class TimestampScrubber implements Scrubber {
    @Override
    public String scrub(String s) {
        return s.replaceAll("cnvs-block-core-(.+)-[0-9]{13}", "cnvs-block-core-$1-1234567890123");
    }
}
