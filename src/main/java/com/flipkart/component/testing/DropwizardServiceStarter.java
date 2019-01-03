package com.flipkart.component.testing;

import com.google.common.base.Preconditions;

import java.io.File;
import java.lang.reflect.Method;

/**
 * starts  a dropwizard service
 */
public class DropwizardServiceStarter implements SUT {

    private final String serviceConfigPath;
    private final String serviceUrl;
    private Class<?> serviceclass;
    private boolean serviceStarted = false;

    /**
     * @param serviceConfigPath the config path where the service yml resides
     * @param serviceUrl        : endpoint where the service Url is hosted
     * @param serviceClass      : the main application class of dropwizard
     */
    public DropwizardServiceStarter(String serviceConfigPath, String serviceUrl, Class<?> serviceClass) {

        Preconditions.checkNotNull(serviceConfigPath);
        Preconditions.checkNotNull(serviceUrl);
        Preconditions.checkNotNull(serviceClass);

        Preconditions.checkArgument(new File(serviceConfigPath).exists(), "configfilePath doesn't exist");
        Preconditions.checkArgument(serviceUrl.contains(":") && serviceUrl.contains("localhost"), "service url can only be a localhost: enforcing a safety check");

        this.serviceConfigPath = serviceConfigPath;
        this.serviceUrl = serviceUrl;
        this.serviceclass = serviceClass;
    }


    @Override
    public void start() {
        try {
            if(serviceStarted) {
                return;
            }
            serviceStarted = true;
            String[] args = new String[]{"server", this.serviceConfigPath};
            Object serviceClassInstance = serviceclass.newInstance();
            Method run = serviceClassInstance.getClass().getMethod("run", String[].class);
            run.invoke(serviceClassInstance, new Object[]{args});
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    @Override
    public String getUrl() {
        return this.serviceUrl;
    }

}




