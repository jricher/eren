/**
 * 
 */
package org.mitre.eren.protocol.scenario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.AtomDate;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author jricher
 *
 */
public class Timing extends BaseWrapper implements ScenarioConstants {

	/**
	 * @param internal
	 */
	public Timing(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public Timing(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Duration.class);
	
    public String getGameTime() {
    	return getSimpleExtension(EREN_GAMETIME);
    }
    
    public void setGameTime(String val) {
	    if (val != null) {
			setElementText(EREN_GAMETIME, val);
		} else {
			removeElement(EREN_GAMETIME);
		}
    }    
    
    public String getWallTime() {
    	return getSimpleExtension(EREN_WALLTIME);
    }
    
    public void setWallTime(String val) {
	    if (val != null) {
			setElementText(EREN_WALLTIME, val);
		} else {
			removeElement(EREN_WALLTIME);
		}
    }    
    public Date getDate() {
		// TODO Auto-generated method stub
		String val = getSimpleExtension(EREN_DATE);
		return val == null ? null : AtomDate.parse(val);
	}

	public void setDate(Date val) {
		
		if (val == null) {
			return;
		}
		
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ", new Locale("en", "US"));

		StringBuffer dateBuff = new StringBuffer();
		dateBuff.append(format.format(val));

		dateBuff.insert(dateBuff.length() - 2, ':');

		setElementText(EREN_DATE, dateBuff.toString());
	}
    
    public String getRatio() {
    	return getSimpleExtension(EREN_RATIO);
    }
    
    public void setRatio(String val) {
	    if (val != null) {
			setElementText(EREN_RATIO, val);
		} else {
			removeElement(EREN_RATIO);
		}
    }
    
	
	
}
