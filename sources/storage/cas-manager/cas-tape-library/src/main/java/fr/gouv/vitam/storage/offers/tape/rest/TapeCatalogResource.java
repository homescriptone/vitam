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
package fr.gouv.vitam.storage.offers.tape.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import fr.gouv.vitam.common.json.JsonHandler;
import fr.gouv.vitam.common.logging.VitamLogger;
import fr.gouv.vitam.common.logging.VitamLoggerFactory;
import fr.gouv.vitam.common.model.RequestResponseOK;
import fr.gouv.vitam.common.server.application.resources.ApplicationStatusResource;
import fr.gouv.vitam.storage.offers.tape.model.TapeModel;
import fr.gouv.vitam.storage.offers.tape.spec.TapeCatalogService;

/**
 * Default tape catalog REST Resource
 */
@Path("/offer/tape/catalog/v1")
@javax.ws.rs.ApplicationPath("webresources")
public class TapeCatalogResource extends ApplicationStatusResource {

    private static final String MISSING_THE_TAPE_ID =
        "Missing the tape ID or wrong ID";
    private static final VitamLogger LOGGER = VitamLoggerFactory.getInstance(TapeCatalogResource.class);

    private TapeCatalogService tapeCatalogService;

    /**
     * Constructor
     *
     * @param tapeCatalogService
     */
    public TapeCatalogResource(TapeCatalogService tapeCatalogService) {
        LOGGER.debug("TapeCatalogService initialized");
        this.tapeCatalogService = tapeCatalogService;
    }

    /**
     * Get container object list
     *
     * @param tapeId
     * @return a tape model from catalog
     */
    @GET
    @Path("/{tapeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTape(@PathParam("tapeId") String tapeId) {
        try {
            if (Strings.isNullOrEmpty(tapeId)) {
                LOGGER.error(MISSING_THE_TAPE_ID);
                return Response.status(Status.BAD_REQUEST).build();
            }

            final RequestResponseOK<JsonNode> responseOK = new RequestResponseOK<JsonNode>();

            tapeCatalogService.findById(tapeId);
            responseOK.addAllResults(Arrays.asList(JsonHandler.toJsonNode(tapeCatalogService.findById(tapeId))));
            LOGGER.debug("Result {}", responseOK);
            return Response.status(Status.OK).entity(responseOK).build();

        } catch (Exception e) {
            LOGGER.error(e);
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * updates a tape model by id.
     *
     * @param
     * @return
     */
    @PUT
    @Path("/tapeId")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response replaceTape(@PathParam("tapeId") String tapeId, TapeModel tapeModel) {

        try {
            if (Strings.isNullOrEmpty(tapeId)) {
                LOGGER.error(MISSING_THE_TAPE_ID);
                return Response.status(Status.BAD_REQUEST).build();
            }

            final RequestResponseOK<JsonNode> responseOK = new RequestResponseOK<JsonNode>();

            tapeCatalogService.replace(tapeModel);
            return Response.status(Status.OK).entity(responseOK).build();

        } catch (Exception e) {
            LOGGER.error(e);
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Creates a tape model.
     *
     * @param
     * @return
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTape(TapeModel tapeModel) {

        try {
            final RequestResponseOK<JsonNode> responseOK = new RequestResponseOK<JsonNode>();

            tapeCatalogService.create(tapeModel);
            return Response.status(Status.OK).entity(responseOK).build();

        } catch (Exception e) {
            LOGGER.error(e);
            return Response.status(Status.BAD_REQUEST).build();
        }
    }
}
