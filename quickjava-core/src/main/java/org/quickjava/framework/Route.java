package org.quickjava.framework;

import org.quickjava.common.QLog;
import org.quickjava.common.QUtils;
import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.config.AppConfig;
import org.quickjava.framework.controller.Action;
import org.quickjava.framework.controller.Controller;
import org.quickjava.framework.controller.Module;
import org.quickjava.framework.exception.ActionNotFoundException;
import org.quickjava.framework.http.Pathinfo;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.common.utils.QFileUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * @author Qlo1062
 * @date 2021/01/17
 */
public class Route {

    private static Route route = new Route();

    private static Map<String, Module> moduleList = new LinkedHashMap<>();

    private static Map<String, Action> routeList = new LinkedHashMap<>();

    public static Route get() {
        return route;
    }

    public static void init(String[] args)
            throws Exception
    {
        get().scanPackages();
        QLog.info("Route init complete");
    }

    /**
     * 扫描包下面的控制器-方法(public)
     * 格式：
     *      application/{module}/controller/{controller}
     *      /{module}/{controller}/{method}
     */
    @SuppressWarnings({"unchecked"})
    private void scanPackages()
        throws Exception
    {
        String classesPath = QUtils.getClassesPath();
        QLog.info("classesPath => " + classesPath);
        String basePackages = Env.getString("basePackages");
        String packagePath = basePackages.replace(".", "/");
        QLog.info("basePackages => " + basePackages + ", packagePath => " + packagePath);

        Enumeration<URL> enumeration = App.classLoader.getResources(packagePath);
        if (enumeration == null) {
            throw new ClassNotFoundException("package not found, basePackages=" + basePackages);
        }

        // 加载配置文件
        URL configYmlUrl = App.classLoader.getResource("config.yml");
        String configYmlContent = QFileUtils.getFileContents(configYmlUrl.getPath());
        AppConfig.Factory.loadFormYml(configYmlContent);
        Dict config = AppConfig.config;
//        QLog.info("AppConfig.config=" + AppConfig.config);

        boolean caseSensitive = false;

        // 解析
        if (enumeration.hasMoreElements()) {
            URL item = enumeration.nextElement();

            // 文件形式
            if (item.getProtocol().equals("file"))
            {
                File appFile = new File(item.getFile());
                File[] moduleFiles = appFile.listFiles();
                // 模块列表
                for (File moduleFile : moduleFiles) {
                    if (moduleFile.isDirectory()) {
                        Module module = new Module(moduleFile.getName(), packageReplace(moduleFile.getAbsolutePath(), classesPath), moduleFile.getAbsolutePath());
                        String modulePath = caseSensitive ? module.path : module.path.toLowerCase();
                        moduleList.put(modulePath, module);
                        // 模块-控制器列表
                        File controllerDir = new File(moduleFile.getAbsolutePath() + "/" +
                                config.get("module").get("dirname").getString("controller"));
                        if (controllerDir.exists()) {
                            for (File controllerFile : controllerDir.listFiles()) {
                                String controllerPackages = packageReplace(controllerFile.getAbsolutePath(), classesPath );
                                Controller controller = (Controller) App
                                        .classLoader.loadClass(controllerPackages).newInstance();
                                controller.setModule(module);
                                controller.setViewPath(moduleFile.getAbsolutePath());
                                String controllerPath = caseSensitive ? controller.path : controller.path.toLowerCase();
                                module.controllerList.put(controllerPath, controller);
                                // 加载控制器里的方法
                                controllerLoadAction(controller);
                            }
                        }
                    }
                }

            }
            // jar包
            else if (item.getProtocol().equals("jar"))
            {

            }

            /**
             * 扫描完成
             */
            QLog.info("actionList: " + moduleList);
        }
    }

    private String packageReplace(String path, String classes)
    {
        path = path.replace("\\", "/").replace(classes + "/", "").replaceAll("/", ".");
        path = path.replace(".class", "");
        return path;
    }

    /**
     * @langCn 加载控制器所有方法
     */
    public void controllerLoadAction(Controller controller)
    {
        Map<String, Action> actionMap = new LinkedHashMap<>();
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                Action action = new Action(controller, method);
                actionMap.put(action.path.toLowerCase(), action);
            }
        }
        controller.actionList.putAll(actionMap);
    }


    /**
     * 找到控制器方法
     * @param request
     */
    public MapAction findMappingAction(Request request)
    {
        String path = request.path;
        MapAction mapAction;

        // TODO::路由模式
        if (routeList.containsKey(path)) {
            QLog.debug("路由模式, " + path);
        }

        // TODO::REST模式
        QLog.debug("REST模式, " + path);
        Pathinfo pathinfo = request.pathinfo;
        String actionPath = "/" + pathinfo.module;
        if (moduleList.containsKey(actionPath)) {
            Module module = moduleList.get(actionPath);
            if (module.controllerList.containsKey((actionPath += "/" + pathinfo.controller))) {
                Controller controller = module.controllerList.get(actionPath);
                if (controller.actionList.containsKey((actionPath += "/" + pathinfo.action))) {
                    mapAction = new MapAction(controller.actionList.get(actionPath));
                } else {
                    throw new ActionNotFoundException("方法不存在 " + actionPath);
                }
            } else {
                throw new ActionNotFoundException("控制器不存在 " + actionPath);
            }
        } else {
            throw new ActionNotFoundException("模块不存在 " + actionPath);
        }

        return mapAction;
    }

    /**
     * @langCn 对应操作方法
     */
    public class MapAction {

        public Action action;

        public MapAction(Action action) {
            this.action = action;
        }

        /**
         * @langCn 调用目标方法
         * @param request
         * @param response
         * @return
         * @throws Exception
         */
        public Object invoke(Request request, Response response)
                throws Exception
        {
            Controller controller = action.controller.getClass().newInstance();
            controller._initRequest(request, response);
            controller.action = action;
            controller._initialize();
            return action.method.invoke(controller);
        }
    }
}
