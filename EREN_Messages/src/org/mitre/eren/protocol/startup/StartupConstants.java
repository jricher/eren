package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.mitre.eren.protocol.ERENConstants;

public interface StartupConstants extends ERENConstants {
	
	public static final QName EREN_MESSAGE = new QName(EREN_NS, "mssg", EREN_PFX);
	public static final QName EREN_SENDTOBUS = new QName(EREN_NS, "sendtobus", EREN_PFX);
  public static final QName EREN_LOGIN = new QName(EREN_NS, "login", EREN_PFX);
  public static final QName EREN_LOGOUT = new QName(EREN_NS, "logout", EREN_PFX);
	public static final QName EREN_USERNAME = new QName(EREN_NS, "username", EREN_PFX);
	public static final QName EREN_PASSWORD = new QName(EREN_NS, "password", EREN_PFX);
	public static final QName EREN_CLIENT = new QName(EREN_NS, "client", EREN_PFX);
	
	public static final QName EREN_ROLEREQUEST = new QName(EREN_NS, "rolerequest", EREN_PFX);
	public static final QName EREN_SCENARIOID = new QName(EREN_NS, "scenarioId", EREN_PFX);
	public static final QName EREN_ROLEID = new QName(EREN_NS, "roleId", EREN_PFX);
	public static final QName EREN_CLIENTREADY = new QName(EREN_NS, "clientready", EREN_PFX);
	public static final QName EREN_SCENARIOLISTREQUEST = new QName(EREN_NS, "scenariolistrequest", EREN_PFX);
	public static final QName EREN_SCENARIOLIST = new QName(EREN_NS, "scenariolist", EREN_PFX);
	public static final QName EREN_ROLEFILLED = new QName(EREN_NS, "rolefilled", EREN_PFX);
	public static final QName EREN_ROLEDENIED = new QName(EREN_NS, "roledenied", EREN_PFX);
	public static final QName EREN_SENDING = new QName(EREN_NS, "sending", EREN_PFX);
	public static final QName EREN_FILENAME = new QName(EREN_NS, "filename", EREN_PFX);
	public static final QName EREN_FILESIZE = new QName(EREN_NS, "filesize", EREN_PFX);
	public static final QName EREN_GAMESTART = new QName(EREN_NS, "gamestart", EREN_PFX);
	
  public static final QName EREN_SCORE = new QName(EREN_NS, "score", EREN_PFX);
  public static final QName EREN_REQUESTSCORE = new QName(EREN_NS, "requestScore", EREN_PFX);
  public static final QName EREN_EXPOSURE = new QName(EREN_NS, "exposure", EREN_PFX);
  public static final QName EREN_MORBIDITY = new QName(EREN_NS, "morbidity", EREN_PFX);
  public static final QName EREN_MORTALITY = new QName(EREN_NS, "mortality", EREN_PFX);
  public static final QName EREN_TREATED = new QName(EREN_NS, "treated", EREN_PFX);

    public static final QName EREN_REQUESTPODSTATUS = new QName(EREN_NS, "requestPodStatus", EREN_PFX);
    public static final QName EREN_PODSTATUS = new QName(EREN_NS, "podStatus", EREN_PFX);
    public static final QName EREN_MEDICALSTAFF = new QName(EREN_NS, "medicalStaff", EREN_PFX);
    public static final QName EREN_SUPPORTSTAFF = new QName(EREN_NS, "supportStaff", EREN_PFX);
    public static final QName EREN_SECURITYSTAFF = new QName(EREN_NS, "securityStaff", EREN_PFX);
    public static final QName EREN_QUEUESIZE = new QName(EREN_NS, "queueSize", EREN_PFX);
    public static final QName EREN_THROUGHPUT = new QName(EREN_NS, "throughput", EREN_PFX);
    public static final QName EREN_HASMEDS = new QName(EREN_NS, "hasMeds", EREN_PFX);
    public static final QName EREN_HASEQUIPMENT = new QName(EREN_NS, "hasEquipment", EREN_PFX);
    public static final QName EREN_STANDARDOFCARE = new QName(EREN_NS, "standardOfCare", EREN_PFX);

    public static final QName EREN_SETSTANDARDOFCARE = new QName(EREN_NS, "setStandardOfCare", EREN_PFX);

    public static final QName EREN_GAMELIST = new QName(EREN_NS, "gamelist", EREN_PFX);
    public static final QName EREN_GAME = new QName(EREN_NS, "game", EREN_PFX);
    public static final QName EREN_ACTIVEROLE = new QName(EREN_NS, "activerole", EREN_PFX);
    public static final QName EREN_ACTIVEROLES = new QName(EREN_NS, "activeroles", EREN_PFX);
    public static final QName EREN_NUMFILLED = new QName(EREN_NS, "numFilled", EREN_PFX);
    public static final QName EREN_GAMEID = new QName(EREN_NS, "gameId", EREN_PFX);
    public static final QName EREN_GAMENAME = new QName(EREN_NS, "gameName", EREN_PFX);
    
    public static final QName EREN_JOINGAME = new QName(EREN_NS, "joinGame", EREN_PFX);
    public static final QName EREN_CREATEGAME = new QName(EREN_NS, "createGame", EREN_PFX);
    public static final QName EREN_GAMECREATED = new QName(EREN_NS, "gameCreated", EREN_PFX);
    public static final QName EREN_GAMEOVER = new QName(EREN_NS, "gameOver", EREN_PFX);
    public static final QName EREN_RESOURCEUSAGE = new QName(EREN_NS, "resourceUsage", EREN_PFX);
    public static final QName EREN_RESOURCEUSE = new QName(EREN_NS, "resourceUse", EREN_PFX);
    public static final QName EREN_RESOURCETYPE = new QName(EREN_NS, "resourceType", EREN_PFX);
    public static final QName EREN_RESOURCEAVAILABLE = new QName(EREN_NS, "resourceAvailable", EREN_PFX);
    public static final QName EREN_RESOURCETOTAL = new QName(EREN_NS, "resourceTotal", EREN_PFX);
}
