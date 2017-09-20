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

import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ConnectorSession;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.RecordSet;
import com.facebook.presto.spi.connector.ConnectorRecordSetProvider;
import com.facebook.presto.spi.connector.ConnectorTransactionHandle;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import io.airlift.log.Logger;

import java.util.List;

import static com.reyun.presto.kudu.Types.checkType;
import static java.util.Objects.requireNonNull;

public class KuduRecordSetProvider
        implements ConnectorRecordSetProvider
{
    public static final Logger logger = Logger.get(KuduRecordSetProvider.class);

    private final KuduTables kuduTable;
    private final KuduClientManager kuduClientManager;

    @Inject
    public KuduRecordSetProvider(KuduTables kuduTable, KuduClientManager kuduClientManager)
    {
        this.kuduTable = requireNonNull(kuduTable, "kuduTable is null");
        this.kuduClientManager = requireNonNull(kuduClientManager, "kuduClientManager is null");
    }

    @Override
    /**
     * @
     */
    public RecordSet getRecordSet(ConnectorTransactionHandle transactionHandle, ConnectorSession session, ConnectorSplit split, List<? extends ColumnHandle> columns)
    {
        requireNonNull(split, "split is null");
        KuduSplit kuduSplit = checkType(split, KuduSplit.class, "split");

        ImmutableList.Builder<KuduColumnHandle> handles = ImmutableList.builder();
        for (ColumnHandle handle : columns) {
            handles.add(checkType(handle, KuduColumnHandle.class, "handle"));
        }

        return new KuduRecordSet(kuduTable, kuduClientManager, kuduSplit, handles.build());
    }
}
