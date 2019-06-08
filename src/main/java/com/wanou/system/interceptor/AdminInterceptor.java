package com.wanou.system.interceptor;

import com.wanou.system.tools.MySessionContext;
import com.wanou.system.util.ComUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;


public class AdminInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");  
        /*后台session控制*/
        String[] noFilters = new String[]{"login", "login.do", "loginVlidate.do","logout.do","error"};
        String uri = request.getRequestURI();
        boolean beFilter = true;
        for (String s : noFilters) {
            if (uri.indexOf(s) != -1) {
                beFilter = false;
                break;
            }
        }
        if (beFilter) {
            Object obj = null;
            String sessionId = request.getParameter("sessionId");
            MySessionContext myc = MySessionContext.getInstance();
            HttpSession session = myc.getSession(sessionId);
            if (session != null) {
                obj = session.getAttribute("projectUser");
            } else {
                obj = request.getSession().getAttribute("projectUser");
            }
            if (obj == null) {
                if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                    response.setHeader("sessionstatus", "sessionOut");
                    return false;
                } else {
                    PrintWriter out = response.getWriter();
                        /*未登陆*/
                    StringBuilder builder = new StringBuilder();
                    builder.append("<js>");
                    builder.append("alert(\"请先登录！\");");
                    builder.append("window.location.href=\"");
                    builder.append(ComUtils.getBasePath(request));
                    builder.append("login\";</js>");
                    out.write(builder.toString());
                    logger.info("{} ,user not find!", uri);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

    }

}
