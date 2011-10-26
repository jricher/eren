package org.mitre.eren.dialogue.custom;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.State;

public class EvalExpression extends Action {
  
  private String expr;
  

  /**
   * @return the exprValue
   */
  public String getExpr() {
    return expr;
  }


  /**
   * @param exprValue the exprValue to set
   */
  public void setExpr(String expr) {
    this.expr = expr;
  }


  @Override
  public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
      SCInstance scInstance, Log appLog, Collection derivedEvents)
      throws ModelException, SCXMLExpressionException {
    Context ctx = scInstance.getContext(getParentTransitionTarget()); 
    scInstance.getEvaluator().eval(ctx, expr);
  }

}
