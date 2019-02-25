/*
Copyright (c) 2018 VMware, Inc. All Rights Reserved.

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
package com.vmware.weathervane.auction.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KubernetesComputeResource extends ComputeResource {
	// RunConfiguration
	private String kubernetesConfigFile = "";

	// RunConfiguration getters and setters
	public String getKubernetesConfigFile() {
		return kubernetesConfigFile;
	}

	public void setKubernetesConfigFile(String kubernetesConfigFile) {
		this.kubernetesConfigFile = kubernetesConfigFile;
	}

	// RunTime
	private static final Logger logger = LoggerFactory.getLogger(KubernetesComputeResource.class);
	private static final String kubectl = "kubectl";

	private String processInvoke(List<String> cmdList) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(cmdList);
		Map<String, String> env = pb.environment();
		env.put("KUBECONFIG", kubernetesConfigFile);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		int exitCode = process.waitFor();
		String output = getString(process.getInputStream());
		if (logger.isDebugEnabled()) {
			logger.debug("processInvoke Command: " +pb.command().toString());
			logger.debug("processInvoke ExitCode: " +exitCode);
			logger.debug("processInvoke Output: [" +output +"]");
		}
		if (exitCode != 0) {
			return null;
		} else {
			return output;
		}
	}


	public boolean kubernetesApply(String fileName, String namespace) throws IOException, InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("kubernetesApply apply file {} in namespace {}", fileName, namespace);
		}
		ArrayList<String> cmdList = new ArrayList<>();
		cmdList.add(kubectl);
		cmdList.add("apply");
		cmdList.add("-f");
		cmdList.add(fileName);
		cmdList.add("--namespace="+namespace);
		String result = processInvoke(cmdList);
		if (result == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesApply: failed "+cmdList);
			}
			return false;
		}
		return true;
	}


	public boolean kubernetesAreAllPodRunning(String podLabelString, String namespace) throws IOException, InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("kubernetesAreAllPodRunning podLabelString {}, namespace {}", podLabelString, namespace);
		}
		ArrayList<String> cmdList = new ArrayList<>();
		cmdList.add(kubectl);
		cmdList.add("get");
		cmdList.add("pod");
		cmdList.add("--selector="+podLabelString);
		cmdList.add("-o=jsonpath='{.items[*].status.phase}'");
		cmdList.add("--namespace="+namespace);
		String result = processInvoke(cmdList);
		if (result == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesAreAllPodRunning: failed "+cmdList);
			}
			return false;
		}

		String output = result.replaceAll("^\'|\'$", "");
		String[] statuses = output.split("\\s+");
		if (statuses.length == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesAreAllPodRunning: There are no pods with label {} in namespace {}", podLabelString, namespace);
			}
			return false;
		}
		for (String status : statuses) {
			if (!status.equals("Running")) {
				if (logger.isDebugEnabled()) {
					logger.debug("kubernetesAreAllPodRunning: Found a non-running pod: {}", status);
				}
				return false;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("kubernetesAreAllPodRunning: All pods are running");
		}
		return true;
	}


	public String kubernetesExecOne(String serviceTypeImpl, List<String> commandStringList, String namespace) throws IOException, InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("kubernetesExecOne exec {} for serviceTypeImpl {}, namespace {}", commandStringList, serviceTypeImpl, namespace);
		}
		ArrayList<String> cmdList = new ArrayList<>();
		cmdList.add(kubectl);
		cmdList.add("get");
		cmdList.add("pod");
		cmdList.add("-o=jsonpath='{.items[*].metadata.name}'");
		cmdList.add("--selector=impl="+serviceTypeImpl);
		cmdList.add("--namespace="+namespace);
		String result = processInvoke(cmdList);
		if (result == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesExecOne: failed "+cmdList);
			}
			return null;
		}
		String output = result.replaceAll("^\'|\'$", "");
		String[] names = output.split("\\s+");
		if (names.length == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesExecOne: There are no pods with label {} in namespace {}", serviceTypeImpl, namespace);
			}
			return null;
		}

		String podName = names[0];
		cmdList = new ArrayList<>();
		cmdList.add(kubectl);
		cmdList.add("exec");
		cmdList.add("-c");
		cmdList.add(serviceTypeImpl);
		cmdList.add("--namespace="+namespace);
		cmdList.add(podName);
		cmdList.add("--");
		cmdList.addAll(commandStringList);
		result = processInvoke(cmdList);
		if (result == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesExecOne: failed "+cmdList);
			}
			return null;
		}
		return result;
	}


/*
	//TODO not yet used
	public boolean kubernetesDeleteAll(String resourceType, String namespace) throws IOException, InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("kubernetesDeleteAll of type {} in namespace {}", resourceType, namespace);
		}
		ArrayList<String> cmdList = new ArrayList<>();
		cmdList.add(kubectl);
		cmdList.add("delete");
		cmdList.add(resourceType);
		cmdList.add("--all");
		cmdList.add("--namespace="+namespace);
		String result = processInvoke(cmdList);
		if (result == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesDeleteAll: failed "+cmdList);
			}
			return false;
		}
		return true;
	}
*/

	public boolean kubernetesDeleteAllWithLabel(String selector, String namespace) throws IOException, InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("kubernetesDeleteAllWithLabel with label {} in namespace {}", selector, namespace);
		}
		ArrayList<String> cmdList = new ArrayList<>();
		cmdList.add(kubectl);
		cmdList.add("delete");
		cmdList.add("all");
		cmdList.add("--selector="+selector);
		cmdList.add("--namespace="+namespace);
		String result = processInvoke(cmdList);
		if (result == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesDeleteAllWithLabel: failed "+cmdList);
			}
			return false;
		}

		cmdList = new ArrayList<>();
		cmdList.add(kubectl);
		cmdList.add("delete");
		cmdList.add("configmap");
		cmdList.add("--selector="+selector);
		cmdList.add("--namespace="+namespace);
		result = processInvoke(cmdList);
		if (result == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesDeleteAllWithLabel: failed "+cmdList);
			}
			return false;
		}
		return true;
	}


/*
	//TODO not yet used
	public boolean kubernetesDeleteAllWithLabelAndResourceType(String selector, String resourceType, String namespace) throws IOException, InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("kubernetesDeleteAllWithLabelAndResourceType with resourceType {}, label {} in namespace {}", resourceType, selector, namespace);
		}
		ArrayList<String> cmdList = new ArrayList<>();
		cmdList.add(kubectl);
		cmdList.add("delete");
		cmdList.add("all");
		cmdList.add(resourceType);
		cmdList.add("--selector="+selector);
		cmdList.add("--namespace="+namespace);
		String result = processInvoke(cmdList);
		if (result == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("kubernetesDeleteAllWithLabelAndResourceType: failed "+cmdList);
			}
			return false;
		}
		return true;
	}
*/

	////
	private String getString(InputStream inputStream) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while (result != -1) {
			buf.write((byte) result);
			result = bis.read();
		}
		return buf.toString(StandardCharsets.UTF_8.name()).trim();
	}

}
