package it.olegna.jpa.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

@Component
public class NoOpConnectionProvider
    implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {
  @Autowired DataSource dataSource;

  @Override
  public Connection getAnyConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public Connection getConnection(Object tenantIdentifier) throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public void releaseConnection(Object tenantIdentifier, Connection connection)
      throws SQLException {
    connection.close();
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @Override
  public void customize(Map<String, Object> hibernateProperties) {
    hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
  }

  @Override
  public boolean isUnwrappableAs(Class<?> unwrapType) {
    return ConnectionProvider.class.isAssignableFrom(unwrapType)
        || MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
  }
    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if ( MultiTenantConnectionProvider.class.isAssignableFrom( unwrapType ) ) {
            return (T) this;
        }
        else {
            throw new UnknownUnwrapTypeException( unwrapType );
        }
    }
}