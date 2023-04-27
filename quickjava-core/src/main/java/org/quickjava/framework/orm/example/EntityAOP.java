package org.quickjava.framework.orm.example;//package org.quickjava.framework.database.example;
//
//import net.sf.cglib.proxy.Enhancer;
//import net.sf.cglib.proxy.MethodInterceptor;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//
//@Aspect
//@Component
//public class EntityAOP {
//
//    @Pointcut("execution(public * org.quickjava.framework.database.example..*.*(..))")
//    public void exampleFun() {
//        System.out.println("exampleFun");
//    }
//
////    @Around("exampleFun()")
////    @Around("@annotation(EntityAno)")
//    public Object aroundFun(ProceedingJoinPoint point) {
//        System.out.println("aroundFun, point=" + point);
//        try {
//            Object ret = point.proceed(point.getArgs());
//            System.out.println("model返回结果：" + point.getThis() + ", ret=" + ret);
//            ret = onProxyEntity2(ret);
//            System.out.println("model返回结果-替换后：" + ret);
//            return ret;
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public Object onProxyEntity2(Object entity)
//    {
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(entity.getClass());
//        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
//            System.out.println("实体方法被调用：" + method.getName());
//            if ("getUser".equals(method.getName())) {
//                User user = new User();
//                return user;
//            }
////            Object ret = method.invoke(o, objects);
////            System.out.println("实体方法被调用-完成：" + method.getName() + ", ret=" + ret);
//            return null;
//        });
//        return enhancer.create();
//    }
//
//    // 动态代理返回结果
//    public Object onProxyEntity(Object entity) {
//        if (!Proxy.isProxyClass(entity.getClass())) {
//            return Proxy.newProxyInstance(
//                    entity.getClass().getClassLoader(),
//                    entity.getClass().getInterfaces(),
//                    new EntityInvocationHandler(entity));
//        }
//        return entity;
//    }
//
//    private static class EntityInvocationHandler implements InvocationHandler {
//        private Object target;
//        public EntityInvocationHandler(Object target) {
//            this.target = target;
//        }
//
//        @Override
//        public Object invoke(Object o, Method method, Object[] objects) {
//            try {
//                System.out.println("实体方法被调用：" + method.getName());
//                return method.invoke(target, objects);
//            } catch (Throwable tr) {
//                throw new RuntimeException("实体方法调用失败");
//            }
//        }
//    }
//
//    @Before("exampleFun()")
//    public void beforeAdvice(JoinPoint point) {
//        System.out.println("beforeAdvice，point=" + point);
//    }
//
//}
