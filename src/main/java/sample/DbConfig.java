package sample;

import javax.sql.DataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;

public class DbConfig implements Config {
  private final PostgresDialect dialect;
  private final LocalTransactionDataSource dataSource;
  private final LocalTransactionManager transactionManager;

  public DbConfig(String url, String user, String password) {
    dialect = new PostgresDialect();
    dataSource = new LocalTransactionDataSource(url, user, password);
    transactionManager =
        new LocalTransactionManager(dataSource.getLocalTransaction(getJdbcLogger()));
  }

  @Override
  public Dialect getDialect() {
    return dialect;
  }

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public LocalTransactionManager getTransactionManager() {
    return transactionManager;
  }
}
