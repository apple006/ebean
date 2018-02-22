package org.multitenant.partition;

import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.ebean.config.TenantMode;
import org.ebeantest.LoggedSqlCollector;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiTenantPartitionTest {

  private static String[] names = {"Ace", "Base", "Case", "Dae", "Eva"};

  static List<MtTenant> tenants() {
    List<MtTenant> tenants = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      tenants.add(new MtTenant("ten_"+i, names[i], names[i]+"@foo.com".toLowerCase()));
    }
    return tenants;
  }

  private static EbeanServer server = init();

  static {
    server.saveAll(tenants());
  }

  @Test
  public void start() {

    UserContext.set("rob", "ten_1");

    LoggedSqlCollector.start();
    MtContent content = new MtContent("first title");
    server.save(content);

    List<String> sql = LoggedSqlCollector.current();
    assertThat(sql.get(0)).contains("insert into mt_content (title, body, when_modified, when_created, version, tenant_id) values (?,?,?,?,?,?)");

    content.setBody("some body");
    server.save(content);

    sql = LoggedSqlCollector.current();
    assertThat(sql.get(0)).contains("update mt_content set body=?, when_modified=?, version=? where id=? and tenant_id=? and version=?");

    server.delete(content);

    sql = LoggedSqlCollector.current();
    assertThat(sql.get(0)).contains("delete from mt_content where id=? and tenant_id=? and version=?");

    LoggedSqlCollector.stop();

  }


  private static EbeanServer init() {
    System.setProperty("ebean.ignoreExtraDdl", "true");

    ServerConfig config = new ServerConfig();

    config.setName("h2multitenant");
    config.loadFromProperties();
    config.setDdlGenerate(true);
    config.setDdlRun(true);
    config.setRegister(false);
    config.setDefaultServer(false);
    config.setCurrentTenantProvider(new CurrentTenant());
    config.setTenantMode(TenantMode.PARTITION);

    config.getClasses().add(MtTenant.class);
    config.getClasses().add(MtContent.class);

    return EbeanServerFactory.create(config);
  }
}