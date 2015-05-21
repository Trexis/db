package com.db.dbx.mvc;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.db.dbx.model.Application;
import com.db.dbx.model.Page;
import com.db.dbx.service.JSONModel;
import com.db.dbx.utilities.ScriptReader;
import com.db.dbx.utilities.Utils;

public class PageView implements View {
	
	@Inject JSONModel jsonModel;

	public String getContentType() {
		
		return null;
	}

	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Page page = (Page) model.get("page");
		Application application = (Application) model.get("application");
		
		String pagecontent = page.getContent();
		
		String serverjsonmodel = jsonModel.getApplicationJsonModel(application);
		
		int insertpos = pagecontent.toLowerCase().lastIndexOf("</body>");
		String part1 = pagecontent.substring(0, insertpos);
		String part2 = pagecontent.substring(insertpos);
		
		pagecontent = part1;
		if(!pagecontent.contains("$dbx.js")){
			pagecontent += ScriptReader.ReadDBXScript(request, serverjsonmodel);
		}
		pagecontent += ScriptReader.ReadPageScript(request, serverjsonmodel);
		pagecontent += part2;
		
		response.getOutputStream().print(pagecontent);
	}

}
