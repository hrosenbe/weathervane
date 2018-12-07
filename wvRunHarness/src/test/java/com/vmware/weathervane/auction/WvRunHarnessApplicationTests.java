/*
Copyright (c) 2017 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.vmware.weathervane.auction;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.LinkedList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.weathervane.auction.WvRunHarnessApplication;
import com.vmware.weathervane.auction.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WvRunHarnessApplication.class)
@WebAppConfiguration
public class WvRunHarnessApplicationTests {

    @Autowired
    protected WebApplicationContext wac;
 
    private MockMvc mockMvc;
 
    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }
    
	@Test
	public void testGet() throws Exception {
		//ResultActions resultActions = 
		mockMvc.perform(get("/runConfiguration")).andExpect(status().isOk());
		//resultActions.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void testPost() throws Exception {
		String input;
		RunConfiguration runConfiguration = new RunConfiguration();

		//should fail due to lacking required inputs
		input = new ObjectMapper().writeValueAsString(runConfiguration);
		mockMvc.perform(post("/runConfiguration").contentType(MediaType.APPLICATION_JSON).content(input)).andExpect(status().isBadRequest());

		// add required inputs for which there are no defaults
		runConfiguration.setDockerNamespace("testnamespace");
		
		ComputeResource computeResource = new DockerComputeResource();
		computeResource.setName("docker1");
		LinkedList<ComputeResource> computeResources = new LinkedList<ComputeResource>();
		computeResources.add(computeResource);
		runConfiguration.setComputeResources(computeResources);
		// end required inputs

		// add optional inputs that will override the defaults
		runConfiguration.setDescription("testPost");
		RunStrategy runStrategy = new IntervalRunStrategy();
		runConfiguration.setRunStrategy(runStrategy);
		//
		
		input = new ObjectMapper().writeValueAsString(runConfiguration);
		// should succeed
		mockMvc.perform(post("/runConfiguration").contentType(MediaType.APPLICATION_JSON).content(input)).andExpect(status().isOk());
		
		// verify description and runStrategy were overridden, dockerNameSpace and computeResource were set 
		mockMvc.perform(get("/runConfiguration"))
			//.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.runConfiguration.dockerNamespace").value("testnamespace"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.runConfiguration.computeResources[0].name").value("docker1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.runConfiguration.description").value("testPost"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.runConfiguration.runStrategy.type").value("interval"))
		;
	}

}
