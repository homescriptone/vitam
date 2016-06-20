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
package fr.gouv.vitam.metadata.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.JsonNode;

import fr.gouv.vitam.api.config.MetaDataConfiguration;
import fr.gouv.vitam.api.exception.MetaDataAlreadyExistException;
import fr.gouv.vitam.api.exception.MetaDataDocumentSizeException;
import fr.gouv.vitam.api.exception.MetaDataExecutionException;
import fr.gouv.vitam.api.exception.MetaDataNotFoundException;
import fr.gouv.vitam.api.model.RequestResponseError;
import fr.gouv.vitam.api.model.RequestResponseOK;
import fr.gouv.vitam.api.model.VitamError;
import fr.gouv.vitam.common.exception.InvalidParseOperationException;
import fr.gouv.vitam.common.json.JsonHandler;
import fr.gouv.vitam.common.logging.VitamLogger;
import fr.gouv.vitam.common.logging.VitamLoggerFactory;
import fr.gouv.vitam.core.MetaDataImpl;
import fr.gouv.vitam.core.MongoDbAccessFactory;
import fr.gouv.vitam.core.database.collections.DbRequest;

/**
 * Units resource REST API
 */
@Path("/metadata/v1")
public class UnitResource {
    private static final VitamLogger LOGGER = VitamLoggerFactory.getInstance(UnitResource.class);
    private final MetaDataImpl metaDataImpl;

    // TODO: comment
    public UnitResource(MetaDataConfiguration configuration) {
        // FIXME REVIEW should not create the implementation directly but using the method as constructor
        metaDataImpl = new MetaDataImpl(configuration, new MongoDbAccessFactory(), DbRequest::new);
        LOGGER.info("init MetaData Resource server");
    }

    /**
     * Get unit status
     */
    @Path("status")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response status() {
        return Response.status(Status.OK).build();
    }

    /**
     * Create unit with json request
     */
    @Path("units")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertUnit(String insertRequest) {
        Status status;
        JsonNode queryJson;
        try {
            metaDataImpl.insertUnit(insertRequest);
            // FIXME REVIEW since you parse it, parse it first then pass it to insertUnit as a Json
            queryJson = JsonHandler.getFromString(insertRequest);
        } catch (final InvalidParseOperationException e) {
            LOGGER.error(e.getMessage());
            // Unprocessable Entity not implemented by Jersey
            status = Status.BAD_REQUEST;
            return Response.status(status)
                .entity(new RequestResponseError().setError(
                    new VitamError(status.getStatusCode())
                        .setContext("ingest")
                        .setState("code_vitam")
                        .setMessage(status.getReasonPhrase())
                        .setDescription(status.getReasonPhrase())))
                .build();
        } catch (final MetaDataNotFoundException e) {
            LOGGER.error(e.getMessage());
            status = Status.NOT_FOUND;
            return Response.status(status)
                .entity(new RequestResponseError().setError(
                    new VitamError(status.getStatusCode())
                        .setContext("ingest")
                        .setState("code_vitam")
                        .setMessage(status.getReasonPhrase())
                        .setDescription(status.getReasonPhrase())))
                .build();
        } catch (final MetaDataAlreadyExistException e) {
            LOGGER.error(e.getMessage());
            status = Status.CONFLICT;
            return Response.status(status)
                .entity(new RequestResponseError().setError(
                    new VitamError(status.getStatusCode())
                        .setContext("ingest")
                        .setState("code_vitam")
                        .setMessage(status.getReasonPhrase())
                        .setDescription(status.getReasonPhrase())))
                .build();
        } catch (final MetaDataExecutionException e) {
            LOGGER.error(e.getMessage());
            status = Status.INTERNAL_SERVER_ERROR;
            return Response.status(status)
                .entity(new RequestResponseError().setError(
                    new VitamError(status.getStatusCode())
                        .setContext("ingest")
                        .setState("code_vitam")
                        .setMessage(status.getReasonPhrase())
                        .setDescription(status.getReasonPhrase())))
                .build();
        } catch (final MetaDataDocumentSizeException e) {
            LOGGER.error(e.getMessage());
            status = Status.REQUEST_ENTITY_TOO_LARGE;
            return Response.status(status)
                .entity(new RequestResponseError().setError(
                    new VitamError(status.getStatusCode())
                        .setContext("ingest")
                        .setState("code_vitam")
                        .setMessage(status.getReasonPhrase())
                        .setDescription(status.getReasonPhrase())))
                .build();
        }
        return Response.status(Status.CREATED)
            .entity(new RequestResponseOK()
                .setHits(1, 0, 1)
                .setQuery(queryJson))
            .build();
    }

}
