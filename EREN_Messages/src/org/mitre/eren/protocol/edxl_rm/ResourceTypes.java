package org.mitre.eren.protocol.edxl_rm;

public enum ResourceTypes {
  /** Point of Distribution facility. The facility that distributes medication to the public. */
  POD,
  /** Receipt, Store, Stage facility where push pack is repackaged for use at PODs */
  RSS ("an RSS facility"),
  /** A hospital */
  HOSPITAL,
  /** equipment for setting up a POD or RSS facility */
  EQUIPMENT_SET,
  /** law enforcement staff */
  LE_STAFF ("law enforcement staff"),
  /** clinical (medical) staff */
  CLINICAL_STAFF,
  /** operational staff */
  OPS_STAFF ("operational staff"),
  /** logistics (planning) staff */
  LOGISTICS_STAFF,
  /** Personal Protective Equipment (e.g. gloves and masks) */
  PPE,
  /** Food and Water **/
  FOOD_AND_WATER,
  /** The package of medication deployed by the Federal government to be used during an incident */
  SNS_PUSH_PACK,
  /** United States Postal Service **/
  USPS,
  /** US Postal Service letter carrier **/
  LETTER_CARRIER,
  /** Medication **/
  MEDICATION;
  
  private String description;
  
  private ResourceTypes () { 
    this.description = this.name().toLowerCase().replaceAll("_", " ");
  }
  private ResourceTypes (String description) { 
    this.description = description;
  }
  
  public String getDescription () { 
    return description;
  }
}
