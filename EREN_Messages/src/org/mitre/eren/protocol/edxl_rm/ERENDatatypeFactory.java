package org.mitre.eren.protocol.edxl_rm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.mitre.javautil.logging.LoggingUtils;

public class ERENDatatypeFactory extends DatatypeFactory {
  private static Logger log = LoggingUtils.getLogger(ERENDatatypeFactory.class);
  

  public static DatatypeFactory factory;

  static  {
    try { 
      factory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) { 
      log.severe("Could not initialize DatatypeFactory. " + e.getMessage());
    };
  }

  /**
   * @param obj
   * @return
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
public boolean equals(Object obj) {
    return factory.equals(obj);
  }
  /**
   * @return
   * @see java.lang.Object#hashCode()
   */
  @Override
public int hashCode() {
    return factory.hashCode();
  }
  /**
   * @param isPositive
   * @param years
   * @param months
   * @param days
   * @param hours
   * @param minutes
   * @param seconds
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDuration(boolean, java.math.BigInteger, java.math.BigInteger, java.math.BigInteger, java.math.BigInteger, java.math.BigInteger, java.math.BigDecimal)
   */
  @Override
public Duration newDuration(boolean isPositive, BigInteger years,
      BigInteger months, BigInteger days, BigInteger hours, BigInteger minutes,
      BigDecimal seconds) {
    return factory.newDuration(isPositive, years, months, days, hours, minutes,
        seconds);
  }
  /**
   * @param isPositive
   * @param years
   * @param months
   * @param days
   * @param hours
   * @param minutes
   * @param seconds
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDuration(boolean, int, int, int, int, int, int)
   */
  @Override
public Duration newDuration(boolean isPositive, int years, int months,
      int days, int hours, int minutes, int seconds) {
    return factory.newDuration(isPositive, years, months, days, hours, minutes,
        seconds);
  }
  /**
   * @param durationInMilliSeconds
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDuration(long)
   */
  @Override
public Duration newDuration(long durationInMilliSeconds) {
    return factory.newDuration(durationInMilliSeconds);
  }
  /**
   * @param lexicalRepresentation
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDuration(java.lang.String)
   */
  @Override
public Duration newDuration(String lexicalRepresentation) {
    return factory.newDuration(lexicalRepresentation);
  }
  /**
   * @param isPositive
   * @param day
   * @param hour
   * @param minute
   * @param second
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDurationDayTime(boolean, java.math.BigInteger, java.math.BigInteger, java.math.BigInteger, java.math.BigInteger)
   */
  @Override
public Duration newDurationDayTime(boolean isPositive, BigInteger day,
      BigInteger hour, BigInteger minute, BigInteger second) {
    return factory.newDurationDayTime(isPositive, day, hour, minute, second);
  }
  /**
   * @param isPositive
   * @param day
   * @param hour
   * @param minute
   * @param second
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDurationDayTime(boolean, int, int, int, int)
   */
  @Override
public Duration newDurationDayTime(boolean isPositive, int day, int hour,
      int minute, int second) {
    return factory.newDurationDayTime(isPositive, day, hour, minute, second);
  }
  /**
   * @param durationInMilliseconds
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDurationDayTime(long)
   */
  @Override
public Duration newDurationDayTime(long durationInMilliseconds) {
    return factory.newDurationDayTime(durationInMilliseconds);
  }
  /**
   * @param lexicalRepresentation
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDurationDayTime(java.lang.String)
   */
  @Override
public Duration newDurationDayTime(String lexicalRepresentation) {
    return factory.newDurationDayTime(lexicalRepresentation);
  }
  /**
   * @param isPositive
   * @param year
   * @param month
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDurationYearMonth(boolean, java.math.BigInteger, java.math.BigInteger)
   */
  @Override
public Duration newDurationYearMonth(boolean isPositive, BigInteger year,
      BigInteger month) {
    return factory.newDurationYearMonth(isPositive, year, month);
  }
  /**
   * @param isPositive
   * @param year
   * @param month
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDurationYearMonth(boolean, int, int)
   */
  @Override
public Duration newDurationYearMonth(boolean isPositive, int year, int month) {
    return factory.newDurationYearMonth(isPositive, year, month);
  }
  /**
   * @param durationInMilliseconds
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDurationYearMonth(long)
   */
  @Override
public Duration newDurationYearMonth(long durationInMilliseconds) {
    return factory.newDurationYearMonth(durationInMilliseconds);
  }
  /**
   * @param lexicalRepresentation
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newDurationYearMonth(java.lang.String)
   */
  @Override
public Duration newDurationYearMonth(String lexicalRepresentation) {
    return factory.newDurationYearMonth(lexicalRepresentation);
  }
  /**
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar()
   */
  @Override
public XMLGregorianCalendar newXMLGregorianCalendar() {
    return factory.newXMLGregorianCalendar();
  }
  /**
   * @param year
   * @param month
   * @param day
   * @param hour
   * @param minute
   * @param second
   * @param fractionalSecond
   * @param timezone
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar(java.math.BigInteger, int, int, int, int, int, java.math.BigDecimal, int)
   */
  @Override
public XMLGregorianCalendar newXMLGregorianCalendar(BigInteger year, int month,
      int day, int hour, int minute, int second, BigDecimal fractionalSecond,
      int timezone) {
    return factory.newXMLGregorianCalendar(year, month, day, hour, minute,
        second, fractionalSecond, timezone);
  }
  /**
   * @param cal
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar(java.util.GregorianCalendar)
   */
  @Override
public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar cal) {
    return factory.newXMLGregorianCalendar(cal);
  }
  /**
   * @param year
   * @param month
   * @param day
   * @param hour
   * @param minute
   * @param second
   * @param millisecond
   * @param timezone
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar(int, int, int, int, int, int, int, int)
   */
  @Override
public XMLGregorianCalendar newXMLGregorianCalendar(int year, int month,
      int day, int hour, int minute, int second, int millisecond, int timezone) {
    return factory.newXMLGregorianCalendar(year, month, day, hour, minute,
        second, millisecond, timezone);
  }
  /**
   * @param lexicalRepresentation
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar(java.lang.String)
   */
  @Override
public XMLGregorianCalendar newXMLGregorianCalendar(String lexicalRepresentation) {
    return factory.newXMLGregorianCalendar(lexicalRepresentation);
  }
  /**
   * @param year
   * @param month
   * @param day
   * @param timezone
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendarDate(int, int, int, int)
   */
  @Override
public XMLGregorianCalendar newXMLGregorianCalendarDate(int year, int month,
      int day, int timezone) {
    return factory.newXMLGregorianCalendarDate(year, month, day, timezone);
  }
  /**
   * @param hours
   * @param minutes
   * @param seconds
   * @param fractionalSecond
   * @param timezone
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendarTime(int, int, int, java.math.BigDecimal, int)
   */
  @Override
public XMLGregorianCalendar newXMLGregorianCalendarTime(int hours, int minutes,
      int seconds, BigDecimal fractionalSecond, int timezone) {
    return factory.newXMLGregorianCalendarTime(hours, minutes, seconds,
        fractionalSecond, timezone);
  }
  /**
   * @param hours
   * @param minutes
   * @param seconds
   * @param milliseconds
   * @param timezone
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendarTime(int, int, int, int, int)
   */
  @Override
public XMLGregorianCalendar newXMLGregorianCalendarTime(int hours, int minutes,
      int seconds, int milliseconds, int timezone) {
    return factory.newXMLGregorianCalendarTime(hours, minutes, seconds,
        milliseconds, timezone);
  }
  /**
   * @param hours
   * @param minutes
   * @param seconds
   * @param timezone
   * @return
   * @see javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendarTime(int, int, int, int)
   */
  @Override
public XMLGregorianCalendar newXMLGregorianCalendarTime(int hours, int minutes,
      int seconds, int timezone) {
    return factory.newXMLGregorianCalendarTime(hours, minutes, seconds, timezone);
  }
  /**
   * @return
   * @see java.lang.Object#toString()
   */
  @Override
public String toString() {
    return factory.toString();
  }

}
