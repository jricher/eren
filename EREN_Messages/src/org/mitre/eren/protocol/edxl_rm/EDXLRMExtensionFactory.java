package org.mitre.eren.protocol.edxl_rm;

import org.apache.abdera.util.AbstractExtensionFactory;
import org.opencare.lib.model.BaseWrapper;


public class EDXLRMExtensionFactory extends AbstractExtensionFactory implements EDXLRMConstants {

	public EDXLRMExtensionFactory() {
		super(EREN_NS);

		// message controls
    addImpl(RM_REQUISITIONRESOURCE, RequisitionResourceWrapper.class);
    addImpl(RM_REQUESTRESOURCE, RequestResourceWrapper.class);
    addImpl(RM_REPORTRESOURCEDEPLOYMENTSTATUS, ReportResourceDeploymentStatusWrapper.class);
    addImpl(RM_REQUESTRESOURCEDEPLOYMENTSTATUS, RequestResourceDeploymentStatusWrapper.class);
    addImpl(RM_COMMITRESOURCE, CommitResourceWrapper.class);
    addImpl(RM_RELEASERESOURCE, ReleaseResourceWrapper.class);
    addImpl(RM_REQUESTRETURN, RequestReturnWrapper.class);
		addImpl(RM_INCIDENTINFORMATION, IncidentInformationTypeWrapper.class);
		addImpl(RM_FUNDING, FundingTypeWrapper.class);
		addImpl(RM_CONTACTINFORMATION, ContactInformationTypeWrapper.class);
		addImpl(RM_RADIO, RadioInformationTypeWrapper.class);
		addImpl(RM_CONTACTLOCATION, LocationTypeWrapper.class);
		addImpl(RM_ADDITIONALCONTACTINFORMATION, PartyTypeWrapper.class);
		addImpl(XPIL_PARTYNAME, PartyNameTypeWrapper.class);
		addImpl(XNL_PERSONNAME, PersonNameWrapper.class);
		addImpl(XNL_NAMEELEMENT, NameElementWrapper.class);
		addImpl(XNL_ORGANISATIONNAME, OrganisationNameWrapper.class);
		addImpl(XPIL_OCCUPATIONS, OccupationsWrapper.class);
		addImpl(XPIL_OCCUPATION, OccupationWrapper.class);
		addImpl(XPIL_OCCUPATIONELEMENT, OccupationElementWrapper.class);
		addImpl(XPIL_EMPLOYER, Employer.class);
		addImpl(RM_RESOURCEINFORMATION, ResourceInformationWrapper.class);
		addImpl(RM_RESPONSEINFORMATION, ResponseInformationTypeWrapper.class);
		addImpl(RM_RESOURCE, ResourceWrapper.class);
		addImpl(RM_RESOURCESTATUS, ResourceStatusWrapper.class);
		addImpl(RM_QUANTITY, QuantityTypeWrapper.class);
		addImpl(RM_MEASUREDQUANTITY, MeasuredQuantityWrapper.class);
		addImpl(RM_ASSIGNMENTINFORMATION, AssignmentInformationWrapper.class);

		addImpl(RM_SCHEDULEINFORMATION, ScheduleInformationWrapper.class);
		addImpl(RM_LOCATION, LocationTypeWrapper.class);
		addImpl(RM_TARGETAREA, WhereType.class);
		addImpl(GML_POINT, PointType.class);
		addImpl(GML_POS, DirectPositionType.class);

		addImpl(GML_LINESTRING, LineStringType.class);
		addImpl(RM_TYPESTRUCTURE, ValueListTypeWrapper.class);
		addImpl(RM_DEPLOYMENTSTATUS, ValueListTypeWrapper.class);
		addImpl(RM_TYPEINFO, BaseWrapper.class);
		addImpl(RM_UNITOFMEASURE, ValueListTypeWrapper.class);
		addImpl(RM_PRICEQUOTE, PriceQuoteTypeWrapper.class);

	}

}
