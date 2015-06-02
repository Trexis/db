package com.db.dbx.mvc.view;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.db.dbx.gateway.GatewayDispatcher;
import com.db.dbx.model.ApplicationJson;
import com.db.dbx.model.ContentJson;
import com.db.dbx.model.LinkJson;
import com.db.dbx.common.ScriptReader;
import com.db.dbx.config.Constants;
import com.db.dbx.utilities.Utilities;

public class PageView implements View {
	
	public String getContentType() {
		
		return null;
	}

	
	//http://bank1.db.com:8080/dbx/sitea/
	//getSchema: http
	//getServerName: bank1.db.com
	//getRequestURI: /dbx/sitea/
	//getServerPort:  8080
	//getContextPath: /dbx
	//getServletPath: /sitea/

	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String appurl = Utilities.getAppURLFromRequest(request);

		GatewayDispatcher gateway = new GatewayDispatcher(request, response);
		String jsonappmodel = gateway.DispatchDBS("/model/" + appurl);
		
		if(Utilities.validateDBSResponseJSON(jsonappmodel)){
			
			ApplicationJson app = new ApplicationJson(jsonappmodel);
			String pagecontent = "";

			if(!app.isAllowAnnoymous()&&!model.containsKey("user")){
				pagecontent = gateway.DispatchDBS("/content/tentant/" + app.getTenantName() + "/application/" + app.getName() + "/page/" + Constants.application401 + ".html");
				response.setStatus(401);
			} else {
				String linkurl = Utilities.getLinkURLFromRequest(request);
				linkurl = linkurl.replace("/", "*");  //we convert / to * so its treated as variable, and not as a url patern
				linkurl += ".html";
				String jsonlinkcontent = gateway.DispatchDBS("/model/tentant/" + app.getTenantName() + "/application/" + app.getName() + "/link/" + linkurl);
				if(Utilities.validateDBSResponseJSON(jsonlinkcontent)){
					ContentJson content = new ContentJson(jsonlinkcontent);
					pagecontent = content.getContent();
					response.setStatus(200);
				} else {
					pagecontent = gateway.DispatchDBS("/content/tentant/" + app.getTenantName() + "/application/" + app.getName() + "/page/" + Constants.application404 + ".html");
					response.setStatus(404);
				}
			}
			
			pagecontent = Utilities.resolveHTMLVariables(request, pagecontent);
			
			int insertpos = pagecontent.toLowerCase().lastIndexOf("</body>");
			if(insertpos>-1){
				String part1 = pagecontent.substring(0, insertpos);
				String part2 = pagecontent.substring(insertpos);
				
				pagecontent = part1;
				if(!pagecontent.contains("$dbx.js")){
					pagecontent += ScriptReader.ReadDBXScript(request, app.getJSON());
				}
				pagecontent += ScriptReader.ReadPageScript(request, app.getJSON());
				pagecontent += part2;
			}
			Utilities.printHTMLToResponse(response, pagecontent);
			
		} else {
			Utilities.printJSONToResponse(response, jsonappmodel);
			response.setStatus(500);
		}

	}

}
