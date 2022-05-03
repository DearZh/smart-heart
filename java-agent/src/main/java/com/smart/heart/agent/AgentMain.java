package com.smart.heart.agent;

import com.smart.heart.util.Log;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @author Arnold.zhao
 * @version AgentMain.java, v 0.1 2022-05-01 18:19 Arnold.zhao Exp $$
 */
public class AgentMain {

    /**
     * 采用java -javaagent: 方式启动时，会加载该agent的premain方法
     *
     * @param agentArgs 启动参数
     * @param inst      由JVM进行实例化
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        /**
         * 从代码执行顺序及test后的结果可知：
         * 1、先输出 AgentMain > premain > 类转换完成
         * 2、然后再具体执行了SmartClassFileTransformer中的类逻辑。
         */
        Log.out("AgentMain > premain > agentArgs=" + agentArgs + " > ClassLoader= " + AgentMain.class.getClassLoader());
        inst.addTransformer(new SmartClassFileTransformer(), true);
        Log.out("AgentMain > premain > 类转换完成");
    }

    /**
     * 采用动态Attach的方式加载该Agent时，会执行该Agent的agentmain()方法
     *
     * @param agentArgs 启动参数
     * @param inst      由JVM进行实例化
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        Log.out("AgentMain > agentmain > agentArgs = " + agentArgs);
        inst.addTransformer(new SmartClassFileTransformer(), true);

        /**
         * 从代码执行顺序及test后的结果可知：
         * 1、如下inst.retransformClasses()的代码片段的确是先执行，然后再执行 SmartClassFileTransformer()类中的代码，
         * 但是这并不影响类的重新加载效果。
         * 当SmartClassFileTransformer()中return了对应的字节码后，仍然会触发类重加载的效果。
         */
        Class[] classes = inst.getAllLoadedClasses();
        for (int i = 0; i < classes.length; i++) {
            if ("com.smart.heart.attach.AttachTest".equals(classes[i].getName())) {
                try {
                    Log.out("AgentMain > agentmain > reload = " + classes[i].getName());
                    /**
                     * 对已加载类触发重加载
                     */
                    inst.retransformClasses(classes[i]);
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        Log.out("AgentMain > agentmain > end");
    }

}
