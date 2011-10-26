/**
 * 
 */
package org.mitre.eren.protocol.startup;

import org.apache.abdera.util.AbstractExtensionFactory;

/**
 * @author jricher
 *
 */
public class StartupExtensionFactory extends AbstractExtensionFactory implements StartupConstants {
	public StartupExtensionFactory() {
		super(EREN_NS);

		addImpl(EREN_MESSAGE, Message.class);
		addImpl(EREN_SENDTOBUS, SendToBus.class);
		addImpl(EREN_LOGIN, Login.class);
		addImpl(EREN_LOGOUT, Logout.class);
		addImpl(EREN_USERNAME, Username.class);
		addImpl(EREN_PASSWORD, Password.class);
		addImpl(EREN_CLIENT, Client.class);
		
		addImpl(EREN_ROLEREQUEST, RoleRequest.class);
		addImpl(EREN_SCENARIOID, ScenarioId.class);
		addImpl(EREN_ROLEID, RoleId.class);
		addImpl(EREN_CLIENTREADY, ClientReady.class);
		addImpl(EREN_SCENARIOLISTREQUEST, ScenarioListRequest.class);
		addImpl(EREN_SCENARIOLIST, ScenarioList.class);
		addImpl(EREN_ROLEFILLED, RoleFilled.class);
		addImpl(EREN_SENDING, Sending.class);
		addImpl(EREN_FILENAME, Filename.class);
		addImpl(EREN_FILESIZE, Filesize.class);
		addImpl(EREN_GAMESTART, GameStart.class);
		addImpl(EREN_ROLEDENIED, RoleDenied.class);
		
		addImpl(EREN_SCORE, Score.class);
		addImpl(EREN_EXPOSURE, Exposure.class);
		addImpl(EREN_MORBIDITY, Morbidity.class);
		addImpl(EREN_MORTALITY, Mortality.class);
		addImpl(EREN_TREATED, Treated.class);

        addImpl(EREN_REQUESTPODSTATUS, RequestPodStatus.class);
        addImpl(EREN_PODSTATUS, PodStatus.class);
        addImpl(EREN_SETSTANDARDOFCARE, SetStandardOfCare.class);
	
        addImpl(EREN_GAMELIST, GameList.class);
        addImpl(EREN_GAME, Game.class);
        addImpl(EREN_ACTIVEROLE, ActiveRole.class);
        addImpl(EREN_GAMEID, GameId.class);
        addImpl(EREN_ACTIVEROLES, ActiveRoles.class);
        addImpl(EREN_GAMENAME, GameName.class);
        
        addImpl(EREN_JOINGAME, JoinGame.class);
        addImpl(EREN_CREATEGAME, CreateGame.class);
        addImpl(EREN_GAMECREATED, GameCreated.class);
        addImpl(EREN_GAMEOVER, GameOver.class);
        addImpl(EREN_RESOURCEUSAGE, ResourceUsage.class);
        addImpl(EREN_RESOURCEUSE, ResourceUse.class);
        addImpl(EREN_RESOURCEUSE, ResourceUse.class);
	}
}
