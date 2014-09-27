package com.wso2.iaasmobilemanager;

import static org.jclouds.compute.predicates.NodePredicates.inGroup;

import java.util.Set;

import javax.ws.rs.*;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;

@Path("/iaasservices")
public class ApiServices {
	private ComputeService compute;

	@GET
	@Path("/getinstancesdata")
	public Set<? extends ComputeMetadata> getInstancesData() {

		Set<? extends ComputeMetadata> nodes = compute.listNodes();
		System.out.println("Total Number of Nodes = " + nodes.size());
		for (ComputeMetadata node : nodes) {
			System.out.println("\t" + node);
		}

		return nodes;

	}
	
	@GET
	@Path("/initservices")
	public void initServices() {
		String provider="cloudstack";
		String apiKey="aPQCT2yewuDAlIudGuEqR4TKi13hFg3UrftfflcF5p0xgD9QC434N8-pLvjvPoCrKv6OnBHed0eDF6irv4nbPQ";
		String secretKey="IBjU1B6BUwfpLJYEkcgFIpE1r9bQNsYVE56nuXXKSHlPDj2mpuRguC2fRIZi1HYXP_sI6xQgCJ1nOe05p58gMw";
		// get the service context
		ComputeServiceContext context = ContextBuilder.newBuilder(provider)
				.credentials(apiKey, secretKey)
				.buildView(ComputeServiceContext.class);
		compute = context.getComputeService();
		System.out.println("Cloud Compute Service Context Created");
	}

	@POST
	@Path("/aquireserver")
	public void aquireServer(@PathParam("groupName")String groupName,@PathParam("os") String os,@PathParam("osVersion") String osVersion,
			@PathParam("ram")Integer ram, @PathParam("count")Integer count) throws Exception {
		TemplateBuilder templateBuilder = compute.templateBuilder();

		// templateBuilder.options();
		Template template = templateBuilder.os64Bit(true)
				.osDescriptionMatches(os).osVersionMatches(osVersion)
				.minRam(ram).build();
		// todo keypair, security group, pod,zone

		System.out.println("Acquiring " + count + " server(s).");

		// create instances
		Set<? extends NodeMetadata> nodes = compute.createNodesInGroup(
				groupName, count, template);
		System.out.println(nodes.size() + " server(s) acquired!");
	}

	@GET
	@Path("/rebootinstance/{instanceid}")
	public void reBootInstance(@PathParam("instanceid")String ID) {
		// todo not working this
		compute.rebootNode(ID);
	}

	@GET
	@Path("/stopinstance/{instanceid}")
	public void stopInstance(@PathParam("instanceid")String instanceId) {

	}

	@GET
	@Path("/destroyinstance/{instanceid}")
	public void destroyInstance(@PathParam("instanceid")String InstanceId) {

		System.out.println("releasing server" + InstanceId);
		try {
			compute.destroyNode(InstanceId);
			System.out.println("Successfully Distroyed instance");
		} catch (Exception e) {
		}
	}

	// release servers belongs to a group, so that those instances will be
	// destroyed from cloudstack
	@GET
	@Path("/releasegroup/{groupname}")
	public void releaseGroup(@PathParam("groupname")String groupName) throws Exception {
		System.out.println("Releasing server(s) from group " + groupName);
		Set<? extends NodeMetadata> servers = compute
				.destroyNodesMatching(inGroup(groupName));
		System.out
				.println(servers.size() + " released from group " + groupName);
	}
}