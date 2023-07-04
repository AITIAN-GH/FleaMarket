package com.xu.common.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author AITIAN
 */
@Data
@Component
@ConfigurationProperties(prefix = "transfer")
public class FileTransferProperties {
    private String productImageHome;
    private String avatarHome;
}
