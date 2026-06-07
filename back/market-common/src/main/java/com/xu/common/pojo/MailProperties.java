package com.xu.common.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author AITIAN
 */
@Data
@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    private String from;
    private String to;
}
