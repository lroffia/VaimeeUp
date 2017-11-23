/* This class abstracts a client of the SEPA Application Design Pattern
 * 
 * Author: Luca Roffia (luca.roffia@unibo.it)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package it.unibo.arces.wot.sepa.pattern;

import java.util.HashMap;
import java.util.Set;

import it.unibo.arces.wot.sepa.android.logging.LogManager;
import it.unibo.arces.wot.sepa.android.logging.Logger;

import it.unibo.arces.wot.sepa.api.SPARQL11SEProtocol;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;

public abstract class Client {	
	private final Logger logger = LogManager.getLogger("Client");
	
	protected HashMap<String,String> prefix2URIMap = new HashMap<String,String>();	
	protected ApplicationProfile appProfile;
	protected SPARQL11SEProtocol protocolClient = null;
	
	public ApplicationProfile getApplicationProfile() {
		return appProfile;
	}
	
	private void addNamespaces(ApplicationProfile appProfile) {
		Set<String> prefixes = appProfile.getPrefixes();
		for (String prefix : prefixes) {
			if (prefix2URIMap.containsKey(prefix)) {
				prefix2URIMap.remove(prefix);
			}
			prefix2URIMap.put(prefix, appProfile.getNamespaceURI(prefix));
		}
	}
	
	protected String prefixes() {
		String ret = "";
		for (String prefix : prefix2URIMap.keySet())
			ret += "PREFIX " + prefix + ":<" + prefix2URIMap.get(prefix) + "> ";
		return ret;
	}
	
	public Client(ApplicationProfile appProfile) throws SEPAProtocolException {
		if (appProfile == null) {
			logger.fatal("Application profile is null. Client cannot be initialized");
			throw new SEPAProtocolException(new IllegalArgumentException("Application profile is null"));
		}
		this.appProfile = appProfile;
		
		logger.debug("SEPA parameters: "+appProfile.printParameters());
		
		addNamespaces(appProfile);
	}
	
	protected String replaceBindings(String sparql, Bindings bindings){
		if (bindings == null || sparql == null) return sparql;
		
		String replacedSparql = String.format("%s", sparql);
		String selectPattern = "";
		
		if (sparql.toUpperCase().contains("SELECT")) {
			selectPattern = replacedSparql.substring(0, sparql.indexOf('{'));
			replacedSparql = replacedSparql.substring(sparql.indexOf('{'), replacedSparql.length());
		}
		for (String var : bindings.getVariables()) {
			if (bindings.getBindingValue(var) == null) continue;
			
			String value = bindings.getBindingValue(var);
			
			if (bindings.isLiteral(var)) value = "\""+value+"\"";
			
			replacedSparql = replacedSparql.replace("?"+var,value);
			
			selectPattern = selectPattern.replace("?"+var, "");
		}
		
		return selectPattern+replacedSparql;
	}
}
