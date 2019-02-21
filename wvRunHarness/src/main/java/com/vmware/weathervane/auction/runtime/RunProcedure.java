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
package com.vmware.weathervane.auction.runtime;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.vmware.weathervane.auction.model.AuctionAppInstance;
import com.vmware.weathervane.auction.model.AuctionService;
import com.vmware.weathervane.auction.model.AuctionWorkload;
import com.vmware.weathervane.auction.model.ComputeResource;
import com.vmware.weathervane.auction.model.RunConfiguration;
import com.vmware.weathervane.auction.model.Workload;

public class RunProcedure {

	//private String type = "full";
	RunConfiguration runConfig;

	public RunProcedure(RunConfiguration runConfig) {
		super();
		//this.type = runConfig.getRunProcedure();
		this.runConfig = runConfig;
	}

	public void run() {
		System.out.println("debugprint RunProcedure.run.....");

		Map<String, ComputeResource> computeResourcesMap = new HashMap<>();
		for (ComputeResource cr : runConfig.getComputeResources()) {
			computeResourcesMap.put(cr.getName(), cr);
		}

		//FullRunProcedure super() to PrepareOnlyRunProcedure
		//seqnum
		//clean up tmpdir
		//mkdir setuplogs

		//loggers
		//power control
		//getCpuMemConfig

		//killOldWorkloadDriver

		//stopServices... cleanup
		//redeploy
		//checkversions


		//prepareData
		String tierType = WeathervaneTypes.tierData;
		for (Workload workload : runConfig.getWorkloads()) {
			System.out.println("debugprint RunProcedure preparedata workload "+workload);

			if (workload instanceof AuctionWorkload){
				AuctionWorkload auctionWorkload = ((AuctionWorkload) workload);

				for (AuctionAppInstance appInstance : auctionWorkload.getAppInstances()) {
					System.out.println("debugprint RunProcedure preparedata appinstance "+appInstance);

					//TODO
					DataManager dataManager = new DataManager();
					appInstance.setDataManager(dataManager);
					appInstance.getDataManager().prepareData();
				}
			}
		}
		//maybe cleanupAfterFailure

		//clearReloadDb


		//startServices("data")
		tierType = WeathervaneTypes.tierData;

		int workloadNum = 1;
		for (Workload workload : runConfig.getWorkloads()) {

			if (workload instanceof AuctionWorkload){
				AuctionWorkload auctionWorkload = ((AuctionWorkload) workload);

				int appInstanceNum = 1;
				for (AuctionAppInstance appInstance : auctionWorkload.getAppInstances()) {

					String kubernetesNamespace = "auctionw" + workloadNum + "i" + appInstanceNum;

					for (AuctionService auctionService : appInstance.getServices())
					{
						auctionService.setKubernetesNamespace(kubernetesNamespace);

						//only start the right types for the tier
						if (auctionService.getTierType().equals(tierType)) {
							System.out.println("debugprint RunProcedure startServices("+tierType+") "+auctionService);
							auctionService.setComputeResourceMap(appInstance.getComputeResourceName(), computeResourcesMap);
							try {
								auctionService.start();
							} catch (IOException e) {
								e.printStackTrace();
								return;
							}
						}
					} //auctionService

					boolean done = false;
					int retries = 10;
					long initialSleep = 1500; //15000; //TODO
					long waitSleep = 15000;

					try {
						System.out.println("debugprint RunProcedure initialSleep1 "+initialSleep);
						Thread.sleep(initialSleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					while (!done && retries > 0) {
						System.out.println("debugprint RunProcedure running loop "+retries);
						done = true;
						for (AuctionService auctionService : appInstance.getServices())
						{
							if (auctionService.getTierType().equals(tierType)) {
								boolean result;
								try {
									result = auctionService.areRunning();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									return;
								}
								System.out.println("debugprint RunProcedure "+auctionService+" areRunning "+result);
								if (result == false) {
									done = false;
									break;
								}
							}
						} //auctionService
						retries--;
						if (!done && retries > 0) {
							try {
								System.out.println("debugprint RunProcedure waitSleep1 "+waitSleep);
								Thread.sleep(waitSleep);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

					done = false;
					retries = 12;
					initialSleep = 1500; //15000; //TODO
					waitSleep = 30000;

					try {
						System.out.println("debugprint RunProcedure initialSleep2 "+initialSleep);
						Thread.sleep(initialSleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					while (!done && retries > 0) {
						System.out.println("debugprint RunProcedure up loop "+retries);
						done = true;
						for (AuctionService auctionService : appInstance.getServices())
						{
							if (auctionService.getTierType().equals(tierType)) {
								boolean result;
								try {
									result = auctionService.areUp();
								} catch (IOException e) {
									e.printStackTrace();
									return;
								}
								System.out.println("debugprint RunProcedure "+auctionService+" areUp "+result);
								if (result == false) {
									done = false;
									break;
								}
							}
						} //auctionService
						retries--;
						if (!done && retries > 0) {
							try {
								System.out.println("debugprint RunProcedure waitSleep2 "+waitSleep);
								Thread.sleep(waitSleep);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					}

					appInstanceNum++;
				} //appInstance

			} //instance of

			workloadNum++;
		} //workload
		//setExternalPortNumbers

		//pretouchdata

		//startServices("backend")
		tierType = WeathervaneTypes.tierBackEnd;
		//setExternalPortNumbers

		//startServices("frontend")
		tierType = WeathervaneTypes.tierFrontEnd;
		//setExternalPortNumbers

		//startServices("infrastructure")
		tierType = WeathervaneTypes.tierInfrastructure;
		//setExternalPortNumbers

		//isUp

		//configureWorkloadDrivers
		//initializeWorkloadsDrivers
		//runWorkloads

		//get config,stats,logs

		//TODO if stopservices
		{
			//stopServices (all tiers)
			for (Workload workload : runConfig.getWorkloads()) {

				if (workload instanceof AuctionWorkload){
					AuctionWorkload auctionWorkload = ((AuctionWorkload) workload);

					for (AuctionAppInstance appInstance : auctionWorkload.getAppInstances()) {

						//TODO should the tier loop go outside the appInstance (or workload) loop?
						String[] tierTypes = {WeathervaneTypes.tierFrontEnd, WeathervaneTypes.tierBackEnd, WeathervaneTypes.tierData, WeathervaneTypes.tierInfrastructure};
						for (String stopTierType : tierTypes) {
							for (AuctionService auctionService : appInstance.getServices())
							{
								if (auctionService.getTierType().equals(stopTierType)) {
									try {
										System.out.println("debugprint RunProcedure stopServices " + "("+stopTierType+") "+auctionService);
										auctionService.stop();
									} catch (IOException e) {
										e.printStackTrace();
										return;
									}
								}
							}  //auctionService
						} //tierTypes

					} //appInstance

				} //instance of

			} //workload

//	 		$sanityPassed = $self->sanityCheckServices($cleanupLogDir);
//			$self->unRegisterPortNumbers();

//			$sanityPassed = $self->sanityCheckServices($cleanupLogDir);

//			# Let the appInstances clean any run specific data or services
//			$self->cleanupAppInstances($cleanupLogDir);

//			# clean up old logs and stats
//			$self->cleanup();
		}

		//save more stuff

		//stopWorkloads


	}

}
