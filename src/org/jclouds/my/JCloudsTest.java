package org.jclouds.my;


import org.jclouds.ContextBuilder;
//import org.jclouds.cloudstack.compute.CloudStackComputeService;
import org.jclouds.compute.ComputeService;

import org.jclouds.compute.ComputeServiceContext;


import org.jclouds.compute.domain.ComputeMetadata;

import java.util.Set;

public class JCloudsTest {

    private ComputeService compute;

    public static void main(String[] args) {
        JCloudsTest jCloudsTest = new JCloudsTest();
        jCloudsTest.init();
        jCloudsTest.listServers();

        System.exit(0);
    }

    private void listServers() {
        System.out.println("Calling listNodes...");
        Set<? extends ComputeMetadata> nodes = compute.listNodes();

        System.out.println("Total Number of Nodes = " + nodes.size());
        for (ComputeMetadata node: nodes) {
            System.out.println("\t" + node);
        }
    }

    private void init() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        String provider = "cloudstack";
        String apiKey = "YyAubhGT4F_2cQ7r74rkn9olKwCW6hR8pgFADRku8YzjQGrcdiMSVOoE542crWHzHFghI-KAOqxTFeZrJz4KKQ";
        String secretKey = "PyCh6Q9ICXPNzHEjZhTtXCzgmePE9bW8KHKqjPRJp7Lejhxpi2Lv6YwnE0iH6l4Dz6mtDRSY3REXUM6FN5pvHg";

        ComputeServiceContext context = ContextBuilder.newBuilder(provider)
                .credentials(apiKey, secretKey)
                .buildView(ComputeServiceContext.class);
        compute = context.getComputeService();
    }




}
