/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reyun.presto.kudu;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import static io.airlift.configuration.ConfigBinder.configBinder;
import static java.util.Objects.requireNonNull;

public class KuduModule
        implements Module
{
    private final String connectorId;

    public KuduModule(String connectorId)
    {
        this.connectorId = requireNonNull(connectorId, "connectorId is null");
    }

    @Override
    public void configure(Binder binder)
    {
        configBinder(binder).bindConfig(KuduConfig.class);

        binder.bind(KuduConnector.class).in(Scopes.SINGLETON);
        binder.bind(KuduClientManager.class).in(Scopes.SINGLETON);
        binder.bind(KuduMetadata.class).in(Scopes.SINGLETON);
        binder.bind(KuduSplitManager.class).in(Scopes.SINGLETON);
        binder.bind(KuduRecordSetProvider.class).in(Scopes.SINGLETON);
        binder.bind(KuduHandleResolver.class).in(Scopes.SINGLETON);

        binder.bind(KuduTables.class).in(Scopes.SINGLETON);
    }
}
