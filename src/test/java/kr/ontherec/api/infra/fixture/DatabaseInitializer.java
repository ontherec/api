package kr.ontherec.api.infra.fixture;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.google.common.base.CaseFormat;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

@Component
public class DatabaseInitializer implements BeforeEachCallback {

    private static final String INIT_SCRIPT_PATH = "src/main/resources/data.sql";
    private static final String[] INIT_SCRIPT_QUERIES;

    static {
        try {
            INIT_SCRIPT_QUERIES = Arrays.stream(
                            FileUtils.readFileToString(new File(INIT_SCRIPT_PATH), StandardCharsets.UTF_8).split(";"))
                    .filter(str -> !str.isBlank())
                    .toArray(String[]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public void beforeEach(ExtensionContext context) {
        DatabaseInitializer initializer = SpringExtension.getApplicationContext(context)
                .getBean(DatabaseInitializer.class);
        initializer.cleanup();
        initializer.init();
    }

    @Transactional
    public void init() {
        for (String query : INIT_SCRIPT_QUERIES) {
            em.createNativeQuery(query).executeUpdate();
        }
    }

    @Transactional
    public void cleanup() {
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        for (String tableName : extractTableNames()) {
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    protected List<String> extractTableNames() {
        return em.getMetamodel().getEntities()
                .stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }
}
