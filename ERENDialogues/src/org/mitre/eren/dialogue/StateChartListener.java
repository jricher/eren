package org.mitre.eren.dialogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.scxml.SCXMLListener;
import org.apache.commons.scxml.model.Data;
import org.apache.commons.scxml.model.Datamodel;
import org.apache.commons.scxml.model.State;
import org.apache.commons.scxml.model.Transition;
import org.apache.commons.scxml.model.TransitionTarget;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class StateChartListener implements SCXMLListener,DialogueConstants {
  
  private final DialogueManager mgr;

  public StateChartListener (DialogueManager mgr) { 
    this.mgr = mgr;
  }

  @Override
  public void onEntry(TransitionTarget target) {

//    Datamodel datamodel = target.getDatamodel();
//    
//    Node message = null;
//    String addressee = null;
//    if (datamodel != null) { 
//      List<Data> data = datamodel.getData();
//      for (Data datum : data) {
//        if (datum.getNode() != null) { 
//          NodeList nodes = datum.getNode().getChildNodes();
//          for (int i = 0; i<nodes.getLength(); i++) {
//            Node n = nodes.item(i);
//            if (n.getNodeName().equals(DLG_ERENMESSAGE.getLocalPart())) {
//              message = n.getFirstChild();
//
//            } else if (n.getNodeName().equals(DLG_ADDRESSEE.getLocalPart())) {
//              addressee = n.getTextContent();
//            }
//          }
////          if (message != null) { 
////            mgr.sendMessage(message,addressee);
////          }
//        }
//      }
//    }
  }
  
  
  @Override
  public void onExit(TransitionTarget arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onTransition(TransitionTarget arg0, TransitionTarget arg1,
      Transition arg2) {
    System.out.println("Transition from " + arg0.getId() + " to " + arg1.getId());
    // TODO Auto-generated method stub

  }

 
}
