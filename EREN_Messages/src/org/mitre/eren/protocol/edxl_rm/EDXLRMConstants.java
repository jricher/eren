package org.mitre.eren.protocol.edxl_rm;

import javax.xml.namespace.QName;

import org.mitre.eren.protocol.ERENConstants;

public interface EDXLRMConstants extends ERENConstants {

	public static final String EDXL_RM_NS = "urn:oasis:names:tc:emergency:EDXL:RM:1.0";
	public static final String RM_MSG_NS = "urn:oasis:names:tc:emergency:EDXL:RM:1.0:msg";
	public static final String EDXL_XNL_NS = "urn:oasis:names:tc:ciq:xnl:3";
	public static final String EDXL_XPIL_NS = "urn:oasis:names:tc:ciq:xpil:3";
	public static final String EDXL_GML_NS = "http://www.opengis.net/gml";

	// Vocab constants
	public static final String RM_RESOURCETYPES = "urn:x-hazard:vocab:resourceTypes";
  public static final String RM_DEPLOYMENTSTATUSTYPES = "urn:x-hazard:vocab:deploymentStatusTypes";

	public static final String RM_PFX = "rm";
	public static final String XNL_PFX = "xnl";
	public static final String XPIL_PFX = "xpil";
	public static final String GML_PFX = "gml";
	public static final String MSG_PFX = "msg";

	// Attributes
	public static final String RM_FIRSTNAME = "FirstName";
	public static final String RM_LASTNAME = "LastName";
	public static final String RM_ABBREVIATION = "Abbreviation";
	public static final String RM_TYPE = "Type";
	public static final String GML_ID = "id";
	public static final String GML_SRSNAME = "srsName";
	public static final String GML_SRSDIMENSION = "srsDimension";
	public static final String GML_AXISLABELS = "axisLabels";
	public static final String GML_UOMLABELS = "uomLabels";
	public static final String GML_COUNT = "count";
	

	// resource messages

  public static final QName RM_REQUISITIONRESOURCE = new QName(RM_MSG_NS, "RequisitionResource", MSG_PFX);
  public static final QName RM_REQUESTRESOURCE = new QName(RM_MSG_NS, "RequestResource", MSG_PFX);
  public static final QName RM_COMMITRESOURCE = new QName(RM_MSG_NS, "CommitResource", MSG_PFX);
  public static final QName RM_REPORTRESOURCEDEPLOYMENTSTATUS = new QName(RM_MSG_NS, "ReportResourceDeploymentStatus", MSG_PFX);
  public static final QName RM_REQUESTRESOURCEDEPLOYMENTSTATUS = new QName(RM_MSG_NS, "RequestResourceDeploymentStatus", MSG_PFX);
  public static final QName RM_RELEASERESOURCE = new QName(RM_MSG_NS, "ReleaseResource", MSG_PFX);
  public static final QName RM_REQUESTRETURN = new QName(RM_MSG_NS, "RequestReturn", MSG_PFX);
	public static final QName EREN_USERINPUT = new QName(EREN_NS, "UserInput", EREN_PFX);
	public static final QName EREN_SHUTDOWN = new QName(EREN_NS, "Shutdown", EREN_PFX);




	// resource information
	public static final QName RM_MESSAGEID = new QName(RM_MSG_NS, "MessageID", MSG_PFX);
	public static final QName RM_SENTDATETIME = new QName(RM_MSG_NS, "SentDateTime", MSG_PFX);
	public static final QName RM_MESSAGECONTENTTYPE = new QName(RM_MSG_NS, "MessageContentType", MSG_PFX);
	public static final QName RM_MESSAGEDESCRIPTION = new QName(RM_MSG_NS, "MessageDescription", MSG_PFX);
	public static final QName RM_ORIGINATINGMESSAGEID = new QName(RM_MSG_NS, "OriginatingMessageID", MSG_PFX);
	public static final QName RM_PRECEDINGMESSAGEID = new QName(RM_MSG_NS, "PrecedingMessageID", MSG_PFX);
	public static final QName RM_INCIDENTINFORMATION = new QName(RM_MSG_NS, "IncidentInformation", MSG_PFX);
	public static final QName RM_INCIDENTID = new QName(RM_MSG_NS, "IncidentID", MSG_PFX);
	public static final QName RM_INCIDENTDESCRIPTION = new QName(RM_MSG_NS, "IncidentDescription", MSG_PFX);
	public static final QName RM_MESSAGERECALL = new QName(RM_MSG_NS, "MessageRecall", MSG_PFX);
	public static final QName RM_RECALLMESSAGEID = new QName(RM_MSG_NS, "RecallMessageID", MSG_PFX);
	public static final QName RM_RECALLTYPE = new QName(RM_MSG_NS, "RecallType", MSG_PFX);
	public static final QName RM_FUNDING = new QName(RM_MSG_NS, "Funding", MSG_PFX);
	public static final QName RM_FUNDCODE = new QName(RM_MSG_NS, "FundCode", MSG_PFX);
	public static final QName RM_FUNDINGINFO = new QName(RM_MSG_NS, "FundingInfo", MSG_PFX);
	public static final QName RM_RESOURCEINFORMATION = new QName(RM_MSG_NS, "ResourceInformation", MSG_PFX);
	public static final QName RM_CONTACTINFORMATION = new QName(RM_MSG_NS, "ContactInformation", MSG_PFX);
	public static final QName RM_CONTACTDESCRIPTION = new QName(RM_MSG_NS, "ContactDescription", MSG_PFX);
	public static final QName RM_CONTACTROLE = new QName(RM_MSG_NS, "ContactRole", MSG_PFX);
	public static final QName RM_RADIO = new QName(RM_MSG_NS, "Radio", MSG_PFX);
	public static final QName RM_CONTACTLOCATION = new QName(RM_MSG_NS, "ContactLocation", MSG_PFX);
	public static final QName RM_ADDITIONALCONTACTINFORMATION = new QName(RM_MSG_NS, "AdditionalContactInformation", MSG_PFX);
	public static final QName RM_RESOURCEINFOELEMENTID = new QName(RM_MSG_NS, "ResourceInfoElementID", MSG_PFX);
	public static final QName RM_RESPONSEINFORMATION = new QName(RM_MSG_NS, "ResponseInformation", MSG_PFX);
	public static final QName RM_RESOURCE = new QName(RM_MSG_NS, "Resource", MSG_PFX);
	public static final QName RM_ASSIGNMENTINFORMATION = new QName(RM_MSG_NS, "AssignmentInformation", MSG_PFX);
	public static final QName RM_SCHEDULEINFORMATION = new QName(RM_MSG_NS, "ScheduleInformation", MSG_PFX);
	public static final QName RM_RESOURCEID = new QName(RM_MSG_NS, "ResourceID", MSG_PFX);
	public static final QName RM_RESOURCENAME = new QName(RM_MSG_NS, "Name", MSG_PFX);
	public static final QName RM_TYPESTRUCTURE = new QName(RM_MSG_NS, "TypeStructure", MSG_PFX);
	public static final QName RM_TYPEINFO = new QName(RM_MSG_NS, "TypeInfo", MSG_PFX);
	public static final QName RM_KEYWORD = new QName(RM_MSG_NS, "Keyword", MSG_PFX);
	public static final QName RM_DESCRIPTION = new QName(RM_MSG_NS, "Description", MSG_PFX);
	public static final QName RM_CREDENTIALS = new QName(RM_MSG_NS, "Credentials", MSG_PFX);
	public static final QName RM_CERTIFICATIONS = new QName(RM_MSG_NS, "Certifications", MSG_PFX);
	public static final QName RM_SPECIALREQUIREMENTS = new QName(RM_MSG_NS, "SpecialRequirements", MSG_PFX);
	public static final QName RM_RESPONSIBLEPARTY = new QName(RM_MSG_NS, "ResponsibleParty", MSG_PFX);
	public static final QName RM_OWNERSHIPINFORMATION = new QName(RM_MSG_NS, "OwnershipInformation", MSG_PFX);
	public static final QName RM_RESOURCESTATUS = new QName(RM_MSG_NS, "ResourceStatus", MSG_PFX);
	public static final QName RM_INVENTORYREFRESHDATETIME = new QName(RM_MSG_NS, "InventoryRefreshDatType", MSG_PFX);
	public static final QName RM_DEPLOYMENTSTATUS = new QName(RM_MSG_NS, "DeploymentStatus", MSG_PFX);
	public static final QName RM_AVAILABILITY = new QName(RM_MSG_NS, "Availability", MSG_PFX);
	public static final QName RM_VALUELISTURN = new QName(EDXL_RM_NS, "ValueListURN", RM_PFX);
	public static final QName RM_VALUE = new QName(EDXL_RM_NS, "Value", RM_PFX);
	public static final QName RM_PRECEDINGRESOURCEINFOELEMENTID = new QName(RM_MSG_NS, "PrecedingResourceInfoElementID", MSG_PFX);
	public static final QName RM_RESPONSETYPE = new QName(RM_MSG_NS, "ResponseType", MSG_PFX);
	public static final QName RM_REASONCODE = new QName(RM_MSG_NS, "ReasonCode", MSG_PFX);
	public static final QName RM_RESPONSEREASON = new QName(RM_MSG_NS, "ResponseReason", MSG_PFX);
	public static final QName RM_SCHEDULETYPE = new QName(RM_MSG_NS, "ScheduleType", MSG_PFX);
	public static final QName RM_DATETIME = new QName(RM_MSG_NS, "DateTime", MSG_PFX);
	public static final QName RM_LOCATION = new QName(RM_MSG_NS, "Location", MSG_PFX);
	public static final QName RM_RADIOTYPE = new QName(RM_MSG_NS, "RadioType", MSG_PFX);
	public static final QName RM_RADIOCHANNEL = new QName(RM_MSG_NS, "RadioChannel", MSG_PFX);
	public static final QName RM_OWNER = new QName(RM_MSG_NS, "Owner", MSG_PFX);
	public static final QName RM_OWNINGJURISDICTION = new QName(RM_MSG_NS, "OwningJurisdiction", MSG_PFX);
	public static final QName RM_HOMEDISPATCH = new QName(RM_MSG_NS, "HomeDispatch", MSG_PFX);
	public static final QName RM_HOMEUNIT = new QName(RM_MSG_NS, "HomeUnit", MSG_PFX);
	public static final QName RM_RESOURCETYPE = new QName(RM_MSG_NS, "ResourceType", MSG_PFX);
	public static final QName RM_AMOUNT = new QName(RM_MSG_NS, "Amount", MSG_PFX);
	public static final QName RM_UNITOFMEASURE = new QName(RM_MSG_NS, "UnitOfMeasure", MSG_PFX);
	public static final QName RM_QUANTITYTEXT = new QName(RM_MSG_NS, "QuantityText", MSG_PFX);
	public static final QName RM_MEASUREDQUANTITY = new QName(RM_MSG_NS, "MeasuredQuantity", MSG_PFX);
	public static final QName RM_QUANTITY = new QName(RM_MSG_NS, "Quantity", MSG_PFX);
	public static final QName RM_RESTRICTIONS = new QName(RM_MSG_NS, "Restrictions", MSG_PFX);
	public static final QName RM_ANTICIPATEDFUNCTION = new QName(RM_MSG_NS, "AnticipatedFunction", MSG_PFX);
	public static final QName RM_PRICEQUOTE = new QName(RM_MSG_NS, "PriceQuote", MSG_PFX);
	public static final QName RM_ORDERID = new QName(RM_MSG_NS, "OrderID", MSG_PFX);
	public static final QName RM_ASSIGNMENTINSTRUCTIONS = new QName(RM_MSG_NS, "AssignmentInstructions", MSG_PFX);
	public static final QName RM_MODEOFTRANSPORTATION = new QName(RM_MSG_NS, "ModeOfTransportation", MSG_PFX);
	public static final QName RM_NAVIGATIONINSTRUCTIONS = new QName(RM_MSG_NS, "NavigationInstructions", MSG_PFX);
	public static final QName RM_REPORTINGINSTRUCTIONS = new QName(RM_MSG_NS, "ReportingInstructions", MSG_PFX);
	public static final QName XNL_ELEMENTTYPE = new QName(EDXL_XNL_NS, "ElementType", XNL_PFX);
	public static final QName XNL_NAMEELEMENT = new QName(EDXL_XNL_NS, "NameElement", XNL_PFX);
	public static final QName XPIL_PARTYNAME = new QName(EDXL_XPIL_NS, "PartyName", XPIL_PFX);
	public static final QName XPIL_OCCUPATIONS = new QName(EDXL_XPIL_NS, "Occupations", XPIL_PFX);
	public static final QName XPIL_OCCUPATION = new QName(EDXL_XPIL_NS, "Occupation", XPIL_PFX);
  public static final QName XPIL_OCCUPATIONELEMENT = new QName(EDXL_XPIL_NS, "OccupationElement", XPIL_PFX);
  public static final QName XPIL_TYPE = new QName(EDXL_XPIL_NS, "Type", XPIL_PFX);
	public static final QName XPIL_EMPLOYER = new QName(EDXL_XPIL_NS, "Employer", XPIL_PFX);
	public static final QName XPIL_ID = new QName(EDXL_XPIL_NS,"ID", XPIL_PFX);
	public static final QName XNL_PERSONNAME = new QName(EDXL_XNL_NS, "PersonName", XNL_PFX);
	public static final QName XNL_ORGANISATIONNAME = new QName(EDXL_XNL_NS, "OrganisationName", XNL_PFX);
  public static final QName RM_TARGETAREA = new QName(EDXL_RM_NS, "TargetArea", RM_PFX);
  public static final QName RM_LOCATIONDESCRIPTION = new QName(EDXL_RM_NS, "LocationDescription", RM_PFX);
	public static final QName GML_POINT = new QName(EDXL_GML_NS, "Point", GML_PFX);
	public static final QName GML_LINESTRING = new QName(EDXL_GML_NS, "LineString", GML_PFX);
	public static final QName GML_POS = new QName(EDXL_GML_NS, "pos", GML_PFX);
	public static final QName GML_SEGMENTS = new QName(EDXL_GML_NS, "segments", GML_PFX);
}
