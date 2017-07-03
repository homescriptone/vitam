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
package fr.gouv.vitam.common;

import org.assertj.core.api.Fail;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import fr.gouv.vitam.common.digest.DigestType;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static org.junit.Assert.*;

public class VitamConfigurationTest {

    private static final String SHOULD_RAIZED_AN_EXCEPTION = "Should raized an exception";
    private static final String SECRET = "vitamsecret";
    private static final String VITAM_CONF_FILE_NAME = "vitam.conf";

    @Test
    public void testPojo() {
        final VitamConfiguration vitamConfiguration = new VitamConfiguration();
        assertNull(vitamConfiguration.getConfig());
        assertNull(vitamConfiguration.getLog());
        assertNull(vitamConfiguration.getData());
        assertNull(vitamConfiguration.getTmp());

        vitamConfiguration.setInternalConfiguration(VitamConfiguration.getConfiguration());;
        assertNotNull(vitamConfiguration.getConfig());
        assertNotNull(vitamConfiguration.getLog());
        assertNotNull(vitamConfiguration.getData());
        assertNotNull(vitamConfiguration.getTmp());

        VitamConfiguration vitamConfiguration2 = VitamConfiguration.getConfiguration();
        assertEquals(vitamConfiguration.getConfig(), vitamConfiguration2.getConfig());
        assertEquals(vitamConfiguration.getLog(), vitamConfiguration2.getLog());
        assertEquals(vitamConfiguration.getData(), vitamConfiguration2.getData());
        assertEquals(vitamConfiguration.getTmp(), vitamConfiguration2.getTmp());
        assertNotEquals(vitamConfiguration, vitamConfiguration2);

        VitamConfiguration.setConfiguration(vitamConfiguration);
        vitamConfiguration2 = VitamConfiguration.getConfiguration();
        assertEquals(vitamConfiguration.getConfig(), vitamConfiguration2.getConfig());
        assertEquals(vitamConfiguration.getLog(), vitamConfiguration2.getLog());
        assertEquals(vitamConfiguration.getData(), vitamConfiguration2.getData());
        assertEquals(vitamConfiguration.getTmp(), vitamConfiguration2.getTmp());
        assertNotEquals(vitamConfiguration, vitamConfiguration2);

        VitamConfiguration.setConfiguration(vitamConfiguration.getConfig(),
            vitamConfiguration.getLog(),
            vitamConfiguration.getData(),
            vitamConfiguration.getTmp());
        assertEquals(vitamConfiguration.getConfig(), vitamConfiguration2.getConfig());
        assertEquals(vitamConfiguration.getLog(), vitamConfiguration2.getLog());
        assertEquals(vitamConfiguration.getData(), vitamConfiguration2.getData());
        assertEquals(vitamConfiguration.getTmp(), vitamConfiguration2.getTmp());
        assertNotEquals(vitamConfiguration, vitamConfiguration2);

        vitamConfiguration2 =
            new VitamConfiguration(vitamConfiguration.getConfig(),
                vitamConfiguration.getLog(),
                vitamConfiguration.getData(),
                vitamConfiguration.getTmp());
        assertEquals(vitamConfiguration.getConfig(), vitamConfiguration2.getConfig());
        assertEquals(vitamConfiguration.getLog(), vitamConfiguration2.getLog());
        assertEquals(vitamConfiguration.getData(), vitamConfiguration2.getData());
        assertEquals(vitamConfiguration.getTmp(), vitamConfiguration2.getTmp());

        VitamConfiguration.setSecret(SECRET);
        VitamConfiguration.setFilterActivation(true);

        assertEquals(SECRET, VitamConfiguration.getSecret());
        assertEquals(true, VitamConfiguration.isFilterActivation());

        assertEquals(new Long(10), VitamConfiguration.getAcceptableRequestTime());
        assertEquals(DigestType.SHA256, VitamConfiguration.getSecurityDigestType());
        assertEquals(DigestType.SHA512, VitamConfiguration.getDefaultDigestType());


        try {
            vitamConfiguration2.setConfig(null);
            fail(SHOULD_RAIZED_AN_EXCEPTION);
        } catch (final IllegalArgumentException e) {
            // ignore
        }
        try {
            vitamConfiguration2.setData(null);
            fail(SHOULD_RAIZED_AN_EXCEPTION);
        } catch (final IllegalArgumentException e) {
            // ignore
        }
        try {
            vitamConfiguration2.setLog(null);
            fail(SHOULD_RAIZED_AN_EXCEPTION);
        } catch (final IllegalArgumentException e) {
            // ignore
        }
        try {
            vitamConfiguration2.setTmp(null);
            fail(SHOULD_RAIZED_AN_EXCEPTION);
        } catch (final IllegalArgumentException e) {
            // ignore
        }
    }
    @Test
    public void testInitialConfiguration(){
        assertThat(VitamConfiguration.getVitamCleanPeriod()).isEqualTo(1);

        assertThat(VitamConfiguration.getChunkSize()).isEqualTo(65536);
        assertThat(VitamConfiguration.getThreadsAllowedToBlockForConnectionMultipliers()).isEqualTo(1500);
        assertThat(VitamConfiguration.getDefaultDigestType()).isEqualTo(DigestType.SHA512);
        assertThat(VitamConfiguration.getDefaultLang()).isEqualTo(Locale.FRENCH.toString());

    }
    @Test
    public void testConfiguration(){
        try (final InputStream yamlIS = PropertiesUtils.getConfigAsStream(VITAM_CONF_FILE_NAME)) {
            final VitamConfigurationParameters vitamConfigurationParameters =
                PropertiesUtils.readYaml(yamlIS, VitamConfigurationParameters.class);

            VitamConfiguration.setSecret(vitamConfigurationParameters.getSecret());
            VitamConfiguration.setFilterActivation(vitamConfigurationParameters.isFilterActivation());


            VitamConfiguration.importConfigurationParameters(vitamConfigurationParameters);
            assertThat(VitamConfiguration.getVitamCleanPeriod()).isEqualTo(5);

            assertThat(VitamConfiguration.getAcceptableRequestTime()).isEqualTo(25L);
            assertThat(VitamConfiguration.getDefaultDigestType()).isEqualTo(DigestType.SHA384);


        } catch (final IOException e) {
            fail("fail"+e);
        }
    }


}
