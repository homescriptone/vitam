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
package fr.gouv.vitam.security.internal.filter;

import fr.gouv.vitam.common.ParametersChecker;
import fr.gouv.vitam.common.logging.VitamLogger;
import fr.gouv.vitam.common.logging.VitamLoggerFactory;
import fr.gouv.vitam.common.model.AuthenticationLevel;
import fr.gouv.vitam.common.model.BasicAuthModel;
import fr.gouv.vitam.common.security.rest.VitamAuthentication;
import fr.gouv.vitam.functional.administration.common.server.AdminManagementConfiguration;
import sun.misc.BASE64Decoder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.httpclient.auth.AuthPolicy.BASIC;

/**
 * Filter used to handle the basic authentication for REST endpoints,
 * annotated with {@link VitamAuthentication}. <br/>
 */
public class EndpointAuthenticationFilter implements ContainerRequestFilter {

    private static final VitamLogger LOGGER = VitamLoggerFactory.getInstance(EndpointAuthenticationFilter.class);

    /**
     * Athentication level.
     */
    private final AuthenticationLevel authentLevel;

    /**
     * VitamAdmin configuration.
     */
    private AdminManagementConfiguration configuration;

    /**
     * Constructor with authentication level. <br/>
     *
     * @param authentLevel
     */
    public EndpointAuthenticationFilter(AuthenticationLevel authentLevel,
        AdminManagementConfiguration adminManagementConfig) {
        this.authentLevel = authentLevel;
        this.configuration = adminManagementConfig;
    }

    /**
     * Filtering and Verifying user authentication based on the Basic authent level.
     *
     * @param containerRequestContext
     * @throws IOException
     */
    @Override public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        ParametersChecker
            .checkParameter("VitamAuthentication failed! The reconstruction service needs user authentication.",
                containerRequestContext.getHeaders().get(HttpHeaders.AUTHORIZATION));

        // decode the authentication informations
        MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
        String authorization = headers.get(HttpHeaders.AUTHORIZATION).iterator().next();
        if (!authorization.startsWith(BASIC)) {
            throw new IllegalArgumentException(
                "VitamAuthentication failed: VitamAuthentication informations are missing.");
        }
        String decodedAuthent = "";
        String[] credentials = authorization.split("\\s");

        if (credentials.length != 2) {
            throw new IllegalArgumentException(
                "VitamAuthentication failed: VitamAuthentication informations are missing.");
        }

        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(credentials[1]);
            decodedAuthent = new String(bytes);
            LOGGER.debug("The decoded authentication informations : ", decodedAuthent);

        } catch (IOException ioe) {
            LOGGER.error("ERROR: Exception has been thrown when decoding the basic authentication: ", ioe);
        }

        // validate the authentication information with the Vitam configuration.
        List<String> decodedAuthentgInfos = Arrays.asList(decodedAuthent.split(":"));
        List<BasicAuthModel> basicAuthConfig = configuration.getAdminBasicAuth();
        if (decodedAuthentgInfos.isEmpty() || (basicAuthConfig != null &&
            (!basicAuthConfig.get(0).getUserName().equalsIgnoreCase(decodedAuthentgInfos.get(0)) ||
                !basicAuthConfig.get(0).getPassword()
                    .equalsIgnoreCase(decodedAuthentgInfos.get(1))))) {
            throw new IllegalArgumentException("VitamAuthentication failed: Wrong credentials.");

        }
    }

    public AuthenticationLevel getAuthentLevel() {
        return authentLevel;
    }



}
