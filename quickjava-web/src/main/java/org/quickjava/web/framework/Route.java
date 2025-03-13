package org.quickjava.web.framework;

import org.quickjava.web.common.QuickLog;
import org.quickjava.web.common.QuickUtil;
import org.quickjava.web.framework.bean.Dict;
import org.quickjava.web.framework.exception.ActionNotFoundException;
import org.quickjava.web.framework.exception.QuickException;
import org.quickjava.web.framework.module.Controller;
import org.quickjava.web.framework.module.path.Action;
import org.quickjava.web.framework.module.path.ControllerPath;
import org.quickjava.web.framework.module.path.Module;
import org.quickjava.web.framework.http.Pathinfo;
import org.quickjava.web.framework.http.Request;
import org.quickjava.web.framework.http.Response;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    public static void init() throws Exception
    {
        get().scanPackages();
        QuickLog.debug("Route init complete");
    }

    /**
     * 扫描包下面的控制器-方法(public)
     * 格式：
     *      application/{module}/module/{module}
     *      /{module}/{module}/{method}
     */
    @SuppressWarnings({"unchecked"})
    private void scanPackages()
        throws Exception
    {
        ClassLoader classLoader = Kernel.getClassLoader();
        Dict config = Kernel.config();
        String classPath = Env.getString("classPath");
        String basePackages = Env.getString("basePackages");
        String packagePath = basePackages.replace(".", "/");
        QuickLog.debug("basePackages => " + basePackages + ", packagePath => " + packagePath);

        Enumeration<URL> enumeration = classLoader.getResources(packagePath);
        if (enumeration == null) {
            throw new ClassNotFoundException("package not found, basePackages=" + basePackages);
        }

        boolean caseSensitive = config.getDict("url").getBoolean("caseSensitive", false);

        Dict dirname = config.getDict("module").getDict("dirname");
        String controllerDirName = dirname.getString("controller", "controller");
        QuickLog.debug("classPath=" + classPath);

        if (enumeration.hasMoreElements()) {
            URL item = enumeration.nextElement();

            // #quickLang classes文件模式
            if (item.getProtocol().equals("file"))
            {
                String classesPath = QuickUtil.getClassesPath();
                // NOTE::模块列表
                File appFile = new File(item.getFile());
                File[] moduleFiles = appFile.listFiles();
                for (File moduleFile : moduleFiles) {
                    if (moduleFile.isDirectory()) {
                        Module module = new Module(moduleFile.getName(), moduleFile.getAbsolutePath().replace(classPath, ""));
                        moduleList.put(nameCaseSensitive(module.name, caseSensitive), module);

                        // NOTE::控制器列表
                        File controllerDir = new File(module.controllerPath);
                        if (controllerDir.exists()) {
                            for (File controllerFile : controllerDir.listFiles()) {
                                if (controllerFile.isFile()) {
                                    String controllerPath = packageReplace(controllerFile.getAbsolutePath(), classesPath);
                                    ControllerPath controller = new ControllerPath(module, classLoader.loadClass(controllerPath));
                                    controllerLoadAction(controller, caseSensitive);
                                    module.controllerList.put(nameCaseSensitive(controller.name, caseSensitive), controller);
                                }
                            }
                        }
                    }
                }

            }

            // jar包模式
            else if (item.getProtocol().equals("jar"))
            {
                QuickLog.debug("jar schema, jar=" + item.getFile());
                String[] jarInfo = item.getFile().split("!");
                String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
//                String packagePath = jarInfo[1].substring(1);

                JarFile jarFile = new JarFile(jarFilePath);
                Enumeration<JarEntry> entrys = jarFile.entries();
                while (entrys.hasMoreElements()) {
                    JarEntry jarEntry = entrys.nextElement();
                    String entryName = jarEntry.getName();
                    if (entryName.endsWith(".class")) {
                        if (entryName.startsWith(packagePath)) {

                            String className = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            String moduleClassName = className.replace(basePackages + ".", "");
                            moduleClassName = caseSensitive ? moduleClassName : moduleClassName.toLowerCase();
                            String[] nameArr = moduleClassName.split("\\.");

                            // 模块
                            Module module = moduleList.get(nameArr[0]);
                            if (nameArr.length >= 1 && module == null) {
                                module = new Module(nameArr[0], className);
                                moduleList.put(nameCaseSensitive(module.name, caseSensitive), module);
                            }
                            // 控制器（目前只支持三级
                            if (nameArr.length == 3 && nameArr[1].equals(controllerDirName)) {
                                ControllerPath controller = new ControllerPath(module, classLoader.loadClass(className));
                                controllerLoadAction(controller, caseSensitive);
                                module.controllerList.put(nameCaseSensitive(controller.name, caseSensitive), controller);
                            }

                        }
                    }
                }
            }

            QuickLog.debug("moduleList: " + moduleList.size() + moduleList);
        }
    }

    private String packageReplace(String path, String classes)
    {
        path = path.replace("\\", "/").replace(classes + "/", "").replaceAll("/", ".");
        path = path.replace(".class", "");
        return path;
    }

    private String nameCaseSensitive(String name, Boolean caseSensitive)
    {
        return caseSensitive ? name : name.toLowerCase();
    }

    /**
     * #quickLang 加载控制器所有方法
     */
    public void controllerLoadAction(ControllerPath controller, Boolean caseSensitive)
    {
        Map<String, Action> actionMap = new LinkedHashMap<>();
        for (Method method : controller.target.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                Action action = new Action(controller, method);
                actionMap.put(nameCaseSensitive(action.name, caseSensitive), action);
            }
        }
        controller.actionList.putAll(actionMap);
    }


    /**
     * #quickLang 找到控制器方法
     * @param request 请求
     */
    public RequestAction findMappingAction(Request request)
    {
        String path = request.path;
        Pathinfo pathinfo = request.pathinfo;
        RequestAction requestAction = null;

        // NOTE::先寻找资源文件
        if (1 == 2) {
            QuickLog.debug("资源模式：" + path);
            return requestAction;
        }

        // NOTE::路由模式
        if (routeList.containsKey(path)) {
            QuickLog.debug("路由模式：" + path);
            return requestAction;
        }

        // NOTE::REST模式
        Module module = moduleList.get(pathinfo.module.toLowerCase());
        if (module != null) {
            ControllerPath controller = module.controllerList.get(pathinfo.controller.toLowerCase());
            if (controller != null) {
                Action action = controller.actionList.get(pathinfo.action.toLowerCase());
                if (action != null) {
                    try {
                        requestAction = new RequestAction(action, request);
                    } catch (Exception e) {
                        throw new QuickException("控制器初始化失败", e);
                    }
                } else {
                    throw new ActionNotFoundException("方法不存在 " + pathinfo.action);
                }
            } else {
                throw new ActionNotFoundException("控制器不存在 " + pathinfo.controller);
            }
        } else {
            throw new ActionNotFoundException("模块不存在 " + pathinfo.module);
        }

        return requestAction;
    }

    /**
     * #quickLang 请求对应控制器方法
     */
    public class RequestAction {

        public Action action;

        public RequestAction(Action action, Request request)
                throws InstantiationException, IllegalAccessException
        {
            this.action = request.action = action;
        }

        /**
         * #quickLang 调用目标方法
         * @param request
         * @param response
         * @return
         * @throws Exception
         */
        public Object call(Request request, Response response)
                throws Throwable
        {
            Controller controller =
                    (Controller) action.controller.target.newInstance();
            controller._initRequest(request, response);
            controller._initialize(this.action);

            try {
                /*
                 * #quickLang 函数参数注解{@code{ @GetParam('id') String id }注入调用：前台表单数据注入、前台json数据注入、前台文件注入
                 * #quickLang 函数参数注解{@code{ @PostParam('file') File file }注入调用：前台表单数据注入、前台json数据注入、前台文件注入
                 */

                /*
                 * #quickLang 函数注解处理：限定GET、POST等
                 */

                /*
                 * #quickLang 默认调用
                 */
                return action.method.invoke(controller);

            } catch (InvocationTargetException exc) {
                throw exc.getTargetException();
            }

        }
    }
}
