package org.quickjava.framework;

import org.quickjava.common.QUtils;
import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.config.AppConfig;
import org.quickjava.framework.controller.Action;
import org.quickjava.framework.controller.Controller;
import org.quickjava.framework.controller.Module;
import org.quickjava.framework.exception.ActionNotFoundException;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.utils.QFileUtils;

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

    private static Map<String, Controller> controllerList = new LinkedHashMap<>();

    private static Map<String, Action> actionList = new LinkedHashMap<>();

    private static Map<String, Action> routeList = new LinkedHashMap<>();

    public static Route get() {
        return route;
    }

    public static void init(String[] args)
            throws Exception
    {
        get().scanPackages();
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
        System.out.println("classesPath => " + classesPath);
        String basePackages = Env.getString("basePackages");
        String packagePath = basePackages.replace(".", "/");
        System.out.println("basePackages => " + basePackages + ", packagePath => " + packagePath);

        Enumeration<URL> enumeration = App.classLoader.getResources(packagePath);
        if (enumeration == null) {
            throw new ClassNotFoundException("package not found, basePackages=" + basePackages);
        }

        // 加载配置文件
        URL configYmlUrl = App.classLoader.getResource("config.yml");
        String configYmlContent = QFileUtils.getFileContents(configYmlUrl.getPath());
        AppConfig.Factory.loadFormYml(configYmlContent);
        Dict config = AppConfig.config;
//        System.out.println("AppConfig.config=" + AppConfig.config);

        // 控制器列表
        Map<String, Controller> controllerList = new HashMap<>();

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
                        Module module = new Module(moduleFile.getName(), packageReplace(moduleFile.getAbsolutePath(), classesPath));
                        moduleList.put(module.getPath(), module);
                        // 模块-控制器列表
                        File controllerDir = new File(moduleFile.getAbsolutePath() + "/" +
                                config.get("module").get("dirname").getString("controller"));
                        if (controllerDir.exists()) {
                            for (File controllerFile : controllerDir.listFiles()) {
                                String name = controllerFile.getName();
                                String controllerPackages = packageReplace(controllerFile.getAbsolutePath(), classesPath );
                                Controller controller = (Controller) App.classLoader.loadClass(controllerPackages).newInstance();
                                controller.setModule(module);
                                controller.setName(name);
                                controller.setPackages(controllerPackages);
                                controllerList.put(controller.getPath(), controller);
                                // 寻找方法
                                Map<String, Action> actionMap = controllerLoadAction(App.classLoader, controller);
                                Route.actionList.putAll(actionMap);
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
            System.out.println("actionList: " + actionList);
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
    public Map<String, Action> controllerLoadAction(ClassLoader loader, Controller controller)
        throws Exception
    {
        String classesPath = QUtils.getClassesPath();
        String controllerPath = controller.getPackages().replaceAll("\\\\", "/");
        String classPackageName = controllerPath.replace(classesPath + "/", "");
        classPackageName = classPackageName.replaceAll("/", ".").replace(".class", "");

        // 加载类
        Class clazz = loader.loadClass(classPackageName);
        Method[] methods = clazz.getDeclaredMethods(); // 获取public方法
        Map<String, Action> actionMap = new LinkedHashMap<>();
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                Action action = new Action(controller, method);
                actionMap.put(action.getPath().toLowerCase(), action);
            }
        }
        return actionMap;
    }


    /**
     * 找到控制器方法
     * @param request
     */
    public MapAction findMappingAction(Request request)
    {
        String path = request.getPath();
        System.out.println("path:" + path);
        MapAction mapAction = null;

        // TODO::匹配路由
        if (routeList.containsKey(path)) {
            System.out.println("路由匹配：" + routeList.containsKey(path));
        }

        // TODO::PathInfo 匹配
        String pathNew = pathAutoComple(path);
        pathNew = pathNew.toLowerCase();    // 忽略路径大小写
        if (actionList.containsKey(pathNew)) {
            System.out.println("Path 匹配：" + actionList.containsKey(pathNew));
            mapAction = new MapAction(actionList.get(pathNew));
        } else {
            throw new ActionNotFoundException("方法不存在, path=" + path);
        }

        return mapAction;
    }

    private String pathAutoComple(String pathDir)
    {
        String[] pathArray = pathDir.split("/");
        List<String> pathFullArray = new ArrayList<String>();
        if (pathArray.length < 4) {
            if (pathArray.length <= 1) {
                pathFullArray.add("");
                pathFullArray.add("index");
                pathFullArray.add("index");
                pathFullArray.add("index");
            } else if (pathArray.length == 2) {
                pathFullArray.add(pathArray[0]);
                pathFullArray.add(pathArray[1]);
                pathFullArray.add("index");
                pathFullArray.add("index");
            } else if (pathArray.length == 3) {
                pathFullArray.add(pathArray[0]);
                pathFullArray.add(pathArray[1]);
                pathFullArray.add(pathArray[2]);
                pathFullArray.add("index");
            }
            pathArray = pathFullArray.toArray(new String[pathFullArray.size()]);
        }

        if (pathArray.length > 4) {
            pathArray = Arrays.copyOfRange(pathArray, 0, 4);
        }

        return String.join("/", pathArray);
    }


    /**
     * 对应操作方法
     */
    public class MapAction {

        public Action action;

        public MapAction(Action action) {
            this.action = action;
        }

        public Object invoke(Request request, Response response) throws Exception {
            Controller controller = action.getController().getClass().newInstance();
            controller.setRequest(request);
            controller.setResponse(response);
            return action.getMethod().invoke(controller);
        }
    }
}
