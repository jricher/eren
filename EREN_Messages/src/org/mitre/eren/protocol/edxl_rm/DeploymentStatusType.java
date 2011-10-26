package org.mitre.eren.protocol.edxl_rm;

/** Allowed values for resource deployment status in EREN */
public enum DeploymentStatusType {
  /** The resource is available for use */
  AVAILABLE,
  /** The resource has been requisitioned and is no longer available */
  REQUISITIONED,
  /** The resource has been committed for use by its owner. May be in 
   * response to a requisition. 
   */
  COMMITTED,
  /** The resource is ready to be used. It has arrived at its destination
   * and/or is fully prepared for use.
   */
  READY,
  /** The resource is being used */
  IN_USE,
  /** The resource has been released by whoever was using it and is no
   * longer in use.
   */
  RELEASED,
  /** The resource is not functioning as intended */ 
  NON_FUNCTIONAL
}
