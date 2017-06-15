/*******************************************************************************
 * Copyright French Prime minister Office/SGMAP/DINSIC/Vitam Program (2015-2019)
 *
 * contact.vitam@culture.gouv.fr
 *
 * This software is a computer program whose purpose is to implement a digital archiving back-office system managing
 * high volumetry securely and efficiently.
 *
 * This software is governed by the CeCILL 2.1 license under French law and abiding by the rules of distribution of free
 * software. You can use, modify and/ or redistribute the software under the terms of the CeCILL 2.1 license as
 * circulated by CEA, CNRS and INRIA at the following URL "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and rights to copy, modify and redistribute granted by the license,
 * users are provided only with a limited warranty and the software's author, the holder of the economic rights, and the
 * successive licensors have only limited liability.
 *
 * In this respect, the user's attention is drawn to the risks associated with loading, using, modifying and/or
 * developing or reproducing the software by the user in light of its specific status of free software, that may mean
 * that it is complicated to manipulate, and that also therefore means that it is reserved for developers and
 * experienced professionals having in-depth computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling the security of their systems and/or data
 * to be ensured and, more generally, to use and operate it in the same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had knowledge of the CeCILL 2.1 license and that you
 * accept its terms.
 *******************************************************************************/
package fr.gouv.vitam.processing.management.core;

import fr.gouv.vitam.common.model.ProcessState;
import fr.gouv.vitam.processing.common.config.ServerConfiguration;
import fr.gouv.vitam.processing.common.model.ProcessWorkflow;
import org.junit.Test;



import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessWorkFlowsCleanerTest {


    @Test
    public void testCleaner(){

        //GIVEN
        ConcurrentHashMap<Integer, Map<String , ProcessWorkflow>> map = new ConcurrentHashMap<>();
        map.put(0,new ConcurrentHashMap<String,ProcessWorkflow>());
        map.put(1,new ConcurrentHashMap<String,ProcessWorkflow>());
        map.get(0).put("id1_tenant_0",  new ProcessWorkflow());
        map.get(0).put("id2_tenant_0",  new ProcessWorkflow());
        map.get(0).put("id3_tenant_0",  new ProcessWorkflow());
        map.get(1).put("id1_tenant_1",  new ProcessWorkflow());
        map.get(1).put("id2_tenant_1",  new ProcessWorkflow());
        map.get(1).put("id3_tenant_1",  new ProcessWorkflow());

        ProcessManagementImpl  processManagement = mock(ProcessManagementImpl.class);
        ServerConfiguration serverConfiguration = mock(ServerConfiguration.class);

        //WHEN

        map.get(0).get("id3_tenant_0").setState(ProcessState.COMPLETED);
        map.get(0).get("id3_tenant_0").setProcessCompletedDate(LocalDateTime.now().minusHours(2));

        map.get(1).get("id3_tenant_1").setState(ProcessState.COMPLETED);
        map.get(1).get("id3_tenant_1").setProcessCompletedDate(LocalDateTime.now().minusHours(2));

        map.get(0).get("id2_tenant_0").setState(ProcessState.COMPLETED);
        map.get(0).get("id2_tenant_0").setProcessCompletedDate(LocalDateTime.now().minusHours(1).plusMinutes(50));


        when(serverConfiguration.getProcessingCleanerPeriod()).thenReturn(2);
        when(processManagement.getConfiguration()).thenReturn(serverConfiguration);
        when(processManagement.getWorkFlowList()).thenReturn(map);
       // THEN
        ProcessWorkFlowsCleaner processWorkFlowsCleaner = new ProcessWorkFlowsCleaner(processManagement, TimeUnit.HOURS);
        processWorkFlowsCleaner.run();

       assertThat(map.get(0).size()).isEqualTo(2);
       assertThat(map.get(1).size()).isEqualTo(2);

    }
}
