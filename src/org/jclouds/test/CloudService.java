package org.jclouds.test;

import static org.jclouds.cloudstack.compute.options.CloudStackTemplateOptions.Builder.*;

import static org.jclouds.compute.predicates.NodePredicates.inGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.apache.log4j.Logger;
import org.jclouds.ContextBuilder;
import org.jclouds.cloudstack.domain.AsyncCreateResponse;
import org.jclouds.cloudstack.domain.VirtualMachine;
import org.jclouds.cloudstack.features.VirtualMachineApi;
import org.jclouds.cloudstack.options.AssignVirtualMachineOptions;
import org.jclouds.cloudstack.options.DeployVirtualMachineOptions;
import org.jclouds.cloudstack.options.ListVirtualMachinesOptions;
import org.jclouds.cloudstack.options.StopVirtualMachineOptions;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.*;
import org.jclouds.cloudstack.CloudStackApiMetadata;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.cloudstack.compute.CloudStackComputeService;

import javax.ws.rs.QueryParam;

//templateBuilder.options(inboundPorts(22, 80, 8080, 443));


public class CloudService {

    private ComputeService compute;
    // private String location;


    public static void main(String args[]) {

        CloudService cloudService = new CloudService("cloudstack",
                "aPQCT2yewuDAlIudGuEqR4TKi13hFg3UrftfflcF5p0xgD9QC434N8-pLvjvPoCrKv6OnBHed0eDF6irv4nbPQ",
                "IBjU1B6BUwfpLJYEkcgFIpE1r9bQNsYVE56nuXXKSHlPDj2mpuRguC2fRIZi1HYXP_sI6xQgCJ1nOe05p58gMw");
        try {

            //cloudService.aquireServer("my-test-servers","CentOS","5.6",100,1);
            // cloudService.releaseGroup("my-test-servers");
            // cloudService.getInstancesData();
            cloudService.destroyInstance("46a2a217-48df-4b3d-b9d7-99a8dff25c03");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Set<? extends ComputeMetadata> getInstancesData() {

        Set<? extends ComputeMetadata> nodes = compute.listNodes();
        System.out.println("Total Number of Nodes = " + nodes.size());
        for (ComputeMetadata node : nodes) {
            System.out.println("\t" + node);
        }

        return nodes;

    }


    public CloudService(String provider, String apiKey, String secretKey) {
        //get the service context
        ComputeServiceContext context = ContextBuilder.newBuilder(provider).credentials(apiKey, secretKey)
                .buildView(ComputeServiceContext.class);
        compute = context.getComputeService();
        System.out.println("Cloud Compute Service Context Created");
    }


    public void aquireServer(String groupName, String os, String osVersion, Integer ram, Integer count) throws Exception {
        TemplateBuilder templateBuilder = compute.templateBuilder();

        //templateBuilder.options();
      Template template = templateBuilder
                .os64Bit(true)
                .osDescriptionMatches(os)
                .osVersionMatches(osVersion)
                .minRam(ram)
                .build();
        //todo keypair, security group, pod,zone

        System.out.println("Acquiring " + count + " server(s).");

        //create instances
        Set<? extends NodeMetadata> nodes = compute.createNodesInGroup(groupName, count, template);
        System.out.println(nodes.size() + " server(s) acquired!");
    }

    public void reBootInstance(String ID) {
        //todo not working this
        compute.rebootNode(ID);
    }

    public void stopInstance() {

    }

    public void destroyInstance(String InstanceId) {

        System.out.println("releasing server" + InstanceId);
        try {
            compute.destroyNode(InstanceId);
            System.out.println("Successfully Distroyed instance");
        } catch (Exception e) {
        }
    }


    //release servers belongs to a group, so that those instances will be destroyed from cloudstack
    public void releaseGroup(String groupName) throws Exception {
        System.out.println("Releasing server(s) from group " + groupName);
        Set<? extends NodeMetadata> servers = compute.destroyNodesMatching(inGroup(groupName));
        System.out.println(servers.size() + " released from group " + groupName);
    }
}