package com.smart.heart.agent;

import com.smart.heart.util.Log;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * 自定义类文件转换器
 *
 * @author Arnold.zhao
 * @version MyClassFileTransformer.java, v 0.1 2022-05-01 18:20 Arnold.zhao Exp $$
 */
public class SmartClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
//通过protectionDomain的codesource属性中的信息，可以知道该className是来自于那个location。也就是可以知道它是加载自那个jar包等location信息。
        String interceptClassName = "com/smart/heart/agent/AgentTest";
        if (className.equals(interceptClassName)) {
            Log.out("SmartClassFileTransformer > AgentTest > ClassLoader = " + SmartClassFileTransformer.class.getClassLoader());
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass ct = cp.get("com.smart.heart.agent.AgentTest");
                CtMethod ctMethod = ct.getMethod("foo", "()V");

                //在该方法执行前添加执行代码
                ctMethod.insertBefore("System.out.println(\"Agent before code\");");
                //在该方法执行后添加执行代码
                ctMethod.insertAfter("System.out.println(\"Agent after code\");");

                return ct.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (className.equals("com/smart/heart/attach/AttachTest")) {
            try {
                Log.out("1、SmartClassFileTransformer > AttachTest > ClassLoader = " + SmartClassFileTransformer.class.getClassLoader());

                ClassPool cp = ClassPool.getDefault();

                Log.out("2、SmartClassFileTransformer > AttachTest ");

                CtClass ct = cp.get("com.smart.heart.attach.AttachTest");

                Log.out("3、SmartClassFileTransformer > AttachTest ");

                CtMethod ctMethod = ct.getMethod("foo", "()I");

                Log.out("4、SmartClassFileTransformer > AttachTest ");

                ctMethod.setBody("return 80;");

                Log.out("5、SmartClassFileTransformer > AttachTest ");
                return ct.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public static void main(String[] args) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass ct = cp.get("com.smart.heart.attach.AttachTest");
        CtMethod ctMethod = ct.getMethod("foo", "()I");
        ctMethod.setBody("return 50;");
        Log.out(ct.toBytecode());
    }
}
