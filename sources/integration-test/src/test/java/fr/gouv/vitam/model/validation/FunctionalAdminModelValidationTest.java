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
package fr.gouv.vitam.model.validation;

import fr.gouv.vitam.common.database.collections.VitamCollection;
import fr.gouv.vitam.common.model.administration.AccessContractModel;
import fr.gouv.vitam.common.model.administration.AccessionRegisterDetailModel;
import fr.gouv.vitam.common.model.administration.AccessionRegisterSummaryModel;
import fr.gouv.vitam.common.model.administration.AccessionRegisterSymbolicModel;
import fr.gouv.vitam.common.model.administration.AgenciesModel;
import fr.gouv.vitam.common.model.administration.ArchiveUnitProfileModel;
import fr.gouv.vitam.common.model.administration.ContextModel;
import fr.gouv.vitam.common.model.administration.FileFormatModel;
import fr.gouv.vitam.common.model.administration.FileRulesModel;
import fr.gouv.vitam.common.model.administration.IngestContractModel;
import fr.gouv.vitam.common.model.administration.OntologyModel;
import fr.gouv.vitam.common.model.administration.ProfileModel;
import fr.gouv.vitam.common.model.administration.SecurityProfileModel;
import fr.gouv.vitam.common.model.administration.preservation.GriffinModel;
import fr.gouv.vitam.common.model.administration.preservation.PreservationScenarioModel;
import fr.gouv.vitam.functional.administration.common.SecurityProfile;
import fr.gouv.vitam.functional.administration.common.server.ElasticsearchAccessFunctionalAdmin;
import fr.gouv.vitam.functional.administration.common.server.FunctionalAdminCollections;
import fr.gouv.vitam.functional.administration.common.server.ReferentialDocumentValidators;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalAdminModelValidationTest {

    private static List<FunctionalAdminCollections> checkedCollections = new ArrayList<>();

    @AfterClass
    public static void afterAll() {
        assertAllFunctionalAdminCollectionsHaveBeenTested();
    }

    private static void assertAllFunctionalAdminCollectionsHaveBeenTested() {
        // Skip vitam sequence (internal collection)
        checkedCollections.add(FunctionalAdminCollections.VITAM_SEQUENCE);

        // Check that all collections are tested
        assertThat(checkedCollections).containsExactlyInAnyOrder(FunctionalAdminCollections.values());
    }

    @Test
    public void testAccessContractElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_ACCESSCONTRACT_FILE,
            FunctionalAdminCollections.ACCESS_CONTRACT, AccessContractModel.class,
            ReferentialDocumentValidators.ACCESS_CONTRACT_SCHEMA_JSON);
    }

    @Test
    public void testAgenciesElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_AGENCIES_FILE,
            FunctionalAdminCollections.AGENCIES, AgenciesModel.class,
            ReferentialDocumentValidators.AGENCIES_SCHEMA_JSON);
    }

    @Test
    public void testArchiveUnitProfilesElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_ARCHIVE_UNIT_PROFILE_FILE,
            FunctionalAdminCollections.ARCHIVE_UNIT_PROFILE, ArchiveUnitProfileModel.class,
            ReferentialDocumentValidators.ARCHIVE_UNIT_PROFILE_SCHEMA_JSON);
    }

    @Test
    public void testContextElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_CONTEXT_FILE,
            FunctionalAdminCollections.CONTEXT, ContextModel.class,
            ReferentialDocumentValidators.CONTEXT_SCHEMA_JSON);
    }

    @Test
    public void testFormatElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_FORMAT_FILE,
            FunctionalAdminCollections.FORMATS, FileFormatModel.class,
            ReferentialDocumentValidators.FILE_FORMAT_SCHEMA_JSON);
    }

    @Test
    public void tesGriffinElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_GRIFFIN_FILE,
            FunctionalAdminCollections.GRIFFIN, GriffinModel.class,
            ReferentialDocumentValidators.GRIFFIN_SCHEMA_JSON);
    }

    @Test
    public void testIngestContractElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_INGESTCONTRACT_FILE,
            FunctionalAdminCollections.INGEST_CONTRACT, IngestContractModel.class,
            ReferentialDocumentValidators.INGEST_CONTRACT_SCHEMA_JSON);
    }

    @Test
    public void testOntologyElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_ONTOLOGY_FILE,
            FunctionalAdminCollections.ONTOLOGY, OntologyModel.class,
            ReferentialDocumentValidators.ONTOLOGY_SCHEMA_JSON);
    }

    @Test
    public void testPreservationScenarioElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_PRESERVATION_SCENARIO_FILE,
            FunctionalAdminCollections.PRESERVATION_SCENARIO, PreservationScenarioModel.class,
            ReferentialDocumentValidators.PRESERVATION_SCENARIO_SCHEMA_JSON);
    }

    @Test
    public void testProfilesElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_PROFILE_FILE,
            FunctionalAdminCollections.PROFILE, ProfileModel.class,
            ReferentialDocumentValidators.PROFILE_SCHEMA_JSON);
    }

    @Test
    public void testSecurityProfilesElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_SECURITY_PROFILE_FILE,
            FunctionalAdminCollections.SECURITY_PROFILE, SecurityProfileModel.class,
            ReferentialDocumentValidators.SECURITY_PROFILE_SCHEMA_JSON);
    }

    @Test
    public void testAccessionRegisterSummaryElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_ACCESSION_REGISTER_SUMMARY_FILE,
            FunctionalAdminCollections.ACCESSION_REGISTER_SUMMARY, AccessionRegisterSummaryModel.class,
            ReferentialDocumentValidators.ACCESSION_REGISTER_SUMMARY_SCHEMA_JSON);
    }

    @Test
    public void testAccessionRegisterDetailElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_ACCESSION_REGISTER_DETAIL_FILE,
            FunctionalAdminCollections.ACCESSION_REGISTER_DETAIL, AccessionRegisterDetailModel.class,
            ReferentialDocumentValidators.ACCESSION_REGISTER_DETAIL_SCHEMA_JSON);
    }

    @Test
    public void testAccessionRegisterSymbolicElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_ACCESSION_REGISTER_SYMBOLICS_FILE,
            FunctionalAdminCollections.ACCESSION_REGISTER_SYMBOLIC, AccessionRegisterSymbolicModel.class,
            ReferentialDocumentValidators.ACCESSION_REGISTER_SYMBOLIC_SCHEMA_JSON);
    }

    @Test
    public void testRulesElasticsearchMapping() throws Exception {
        validateDataModel(ElasticsearchAccessFunctionalAdmin.MAPPING_RULE_FILE,
            FunctionalAdminCollections.RULES, FileRulesModel.class,
            ReferentialDocumentValidators.FILE_RULES_SCHEMA_JSON);
    }

    private void validateDataModel(String resourceFileName, FunctionalAdminCollections collection,
        Class<?> modelClass, String jsonSchemaFile) throws Exception {

        checkedCollections.add(collection);

        VitamCollection vitamCollection = collection.getVitamCollection();

        ModelValidatorUtils.validateDataModel(
            modelClass,
            SecurityProfile.class.getResourceAsStream(resourceFileName),
            vitamCollection,
            SecurityProfile.class.getResourceAsStream(jsonSchemaFile));
    }
}
