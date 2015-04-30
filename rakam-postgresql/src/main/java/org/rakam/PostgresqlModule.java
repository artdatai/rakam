package org.rakam;

import com.google.auto.service.AutoService;
import com.google.inject.Binder;
import io.airlift.configuration.ConfigurationFactory;
import org.rakam.analysis.postgresql.PostgresqlConfig;
import org.rakam.analysis.postgresql.PostgresqlContinuousQueryService;
import org.rakam.analysis.postgresql.PostgresqlEventStore;
import org.rakam.analysis.postgresql.PostgresqlMaterializedViewService;
import org.rakam.analysis.postgresql.PostgresqlMetastore;
import org.rakam.collection.event.metastore.Metastore;
import org.rakam.plugin.AbstractUserService;
import org.rakam.plugin.ConditionalModule;
import org.rakam.plugin.ContinuousQueryService;
import org.rakam.plugin.EventStore;
import org.rakam.plugin.EventStream;
import org.rakam.plugin.MaterializedViewService;
import org.rakam.plugin.RakamModule;
import org.rakam.plugin.user.PostgresqlUserService;
import org.rakam.report.QueryExecutor;
import org.rakam.report.postgresql.PostgresqlQueryExecutor;

import static io.airlift.configuration.ConfigurationModule.bindConfig;

/**
 * Created by buremba <Burak Emre Kabakcı> on 24/03/15 03:23.
 */
@AutoService(RakamModule.class)
public class PostgresqlModule extends RakamModule implements ConditionalModule {

    @Override
    protected void setup(Binder binder) {
        bindConfig(binder).to(PostgresqlConfig.class);
        binder.bind(Metastore.class).to(PostgresqlMetastore.class);
        // TODO: implement postgresql specific materialized view service
        binder.bind(MaterializedViewService.class).to(PostgresqlMaterializedViewService.class);
        binder.bind(EventStore.class).to(PostgresqlEventStore.class);
        binder.bind(QueryExecutor.class).to(PostgresqlQueryExecutor.class);
        binder.bind(ContinuousQueryService.class).to(PostgresqlContinuousQueryService.class);
        binder.bind(EventStream.class).to(PostgresqlEventStream.class);
        binder.bind(AbstractUserService.class).to(PostgresqlUserService.class);
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public boolean shouldInstall(ConfigurationFactory config) {
        boolean equals = config.getProperties().get("store.adapter").equals("postgresql");
        if(equals) {
            config.consumeProperty("store.adapter");
        }
        return equals;
    }
}