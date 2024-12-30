package it.olegna.jpa.multitenancy;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

@Component
public class DatabaseRouter extends AbstractRoutingDataSource {
  public static final String DBA = "DB_A";
  public static final String DBB = "DB_B";
  @Autowired
  private TenantIdentifierResolver tenantIdentifierResolver;    
  DatabaseRouter() {    
    setDefaultTargetDataSource(createEmbeddedDatabase("default"));    
    HashMap<Object, Object> targetDataSources = new HashMap<>();
    targetDataSources.put(DBA, createEmbeddedDatabase(DBA));
    targetDataSources.put(DBB, createEmbeddedDatabase(DBB));
    setTargetDataSources(targetDataSources);
  }

  @Override
  protected String determineCurrentLookupKey() {
    return tenantIdentifierResolver.resolveCurrentTenantIdentifier();
  }

  private EmbeddedDatabase createEmbeddedDatabase(String name) {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).setName(name).addScript("book-schema.sql")
        .build();
  }
}