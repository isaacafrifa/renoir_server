package com.iam.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties(prefix = "datastax.astra")
public record DataStaxAstraProperties(File secureConnectBundle) {
}
