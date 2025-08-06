package com.danhaywood.md2wp;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ActiveProfiles("private")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // needed for non-static @BeforeAll
public interface E2eTest {

    ResourceLoader getResourceLoader();

    @BeforeAll
    default void checkPrivateProfileAvailable() throws IOException {
        Resource resource = getResourceLoader().getResource("classpath:application-private.yml");
        assumeTrue(resource.exists(), "Skipping tests: application-private.yml not found on classpath");
    }

}
