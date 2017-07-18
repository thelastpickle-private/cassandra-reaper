/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spotify.reaper.storage;

import com.google.common.base.Optional;

import com.spotify.reaper.core.HostMetrics;
import com.spotify.reaper.core.RepairSegment;
import com.spotify.reaper.service.RingRange;
import java.util.Collection;
import java.util.List;

import java.util.UUID;


/**
 * Definition for a storage that can run in distributed (peer-to-peer) mode. For example Cassandra.
 */
public interface IDistributedStorage {
    
  boolean takeLeadOnSegment(UUID segmentId);
  boolean renewLeadOnSegment(UUID segmentId);
  void releaseLeadOnSegment(UUID segmentId);
  void storeHostMetrics(HostMetrics hostMetrics);
  Optional<HostMetrics> getHostMetrics(String hostName);

  Collection<RepairSegment> getRepairSegmentsForRunInLocalMode(UUID runId, List<RingRange> localRanges);
  
  int countRunningReapers();
  void saveHeartbeat();
}
