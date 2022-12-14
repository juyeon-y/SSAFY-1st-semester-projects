package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@WebServlet("/main")
public class DispatcherServlet extends HttpServlet {
	HashMap<String, Command> beans;

	public void init(ServletConfig config) throws ServletException {
		String path = config.getServletContext().getRealPath("/WEB-INF/beans.xml");
		try {
			XmlBeanFactory factory = new XmlBeanFactory(path);
			beans = factory.getBeans();
			System.out.println(beans);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		JsonObject reJson = new JsonObject();
		
		
		JsonObject json = (JsonObject) JsonParser.parseReader(request.getReader());
		String sign = json.get("sign").getAsString();
		System.out.println(sign);
		
		if(sign != null) {
			beans.get(sign).service(request, response, json, reJson);
			
			
		} else {
			reJson.addProperty("msg", "해킹 모니터링중...");
		}
		
		
		out.append(reJson.toString());
	}

}
