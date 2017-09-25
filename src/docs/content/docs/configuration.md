+++
[menu.docs]
name = "Configuration Reference"
weight = 30
+++

# Configuration

An example testing configuration YAML file can be found from within this project repository:
`src/test/resources/cassandra-reaper.yaml`

The configuration file structure is provided by Dropwizard, and help on configuring the server,
database connection, or logging, can be found at:

http://www.dropwizard.io/1.1.0/docs/manual/configuration.html
    

### Reaper Specific Configuration Settings


The Reaper service specific configuration settings are:

`segmentCount`:

Defines the default amount of repair segments to create for newly registered Cassandra
repair runs (token rings). When running a repair run by the Reaper, each segment is
repaired separately by the Reaper process, until all the segments in a token ring
are repaired. The count might be slightly off the defined value, as clusters residing
in multiple data centers require additional small token ranges in addition to the expected.
You can overwrite this value per repair run, when calling the Reaper.

`repairParallelism`:

Defines the default type of parallelism to use for repairs.
Repair parallelism value must be one of: "sequential", "parallel", or "datacenter_aware".
If you try to use "datacenter_aware" in clusters that don't support it yet (older than 2.0.12),
Reaper will fall back into using "sequential" for those clusters.

`repairIntensity`:

Repair intensity is a value between 0.0 and 1.0, but not zero. Repair intensity defines the amount of time to sleep between triggering each repair segment while running a repair run. When intensity is one, it means that Reaper doesn't sleep at all before triggering next segment, and otherwise the sleep time is defined by how much time it took to repair the last segment divided by the intensity value. 0.5 means half of the time is spent sleeping, and half running. Intensity 0.75 means that 25% of the total time is used sleeping and 75% running. This value can also be overwritten per repair run when invoking repairs.

`incrementalRepair`:

Incremental repair is a boolean value (true | false). Note that this is only supported with the PARALLEL repairParallelism setting. For more details in incremental repair, please refer to the following article.http://www.datastax.com/dev/blog/more-efficient-repairs

*Note*: We do not currently recommend using incremental repair before Cassandra 4.0 as subtle bugs can lead to overstreaming and cluster instabililty.

`repairRunThreadCount`:

The amount of threads to use for handling the Reaper tasks. Have this big enough not to cause
blocking in cause some thread is waiting for I/O, like calling a Cassandra cluster through JMX.

`hangingRepairTimeoutMins`:

The amount of time in minutes to wait for a single repair to finish. If this timeout is reached,
the repair segment in question will be cancelled, if possible, and then scheduled for later
repair again within the same repair run process.

`scheduleDaysBetween`:

Defines the amount of days to wait between scheduling new repairs.
The value configured here is the default for new repair schedules, but you can also
define it separately for each new schedule. Using value 0 for continuous repairs
is also supported.

`storageType`:

Whether to use database or memory based storage for storing the system state.
Value can be either "memory", "database" or "cassandra".
If you are using the recommended (persistent) storage type "database" or "cassandra", you need to define
the database client parameters in a database/cassandra section in the configuration file. See the example
settings in provided testing configuration in *src/test/resources/cassandra-reaper.yaml*.

`jmxPorts`:

Optional mapping of custom JMX ports to use for individual hosts. The used default JMX port
value is 7199. [CCM](https://github.com/pcmanus/ccm) users will find IP and port number
to add in `~/.ccm/<cluster>/*/node.conf` or by running `ccm <node> show`.

`jmxAuth`:

Optional setting for giving username and password credentials for the used JMX connections
in case you are using password based JMX authentication with your Cassandra clusters.

```
jmxAuth:
  username: cassandra
  password: cassandra
```

`enableCrossOrigin`:

Optional setting which you can set to be "true", if you wish to enable the CORS headers
for running an external GUI application, like [this project](https://github.com/spodkowinski/cassandra-reaper-ui).

`autoScheduling`:

Optional setting to automatically setup repair schedules for all non-system keyspaces in a cluster.
If enabled, adding a new cluster will automatically setup a schedule repair 
for each keyspace. Cluster keyspaces are monitored based on a configurable frequency,
so that adding or removing a keyspace will result in adding / removing the corresponding scheduled repairs.

`repairManagerSchedulingIntervalSeconds`:  

Controls the pace at which the Repair Manager will schedule processing of the next segment. Reducing this value from 30s (default) to a lower value can speed up fast repairs by orders of magnitude.
 
`jmxConnectionTimeoutInSeconds`:  

Controls the timeout for establishing JMX connections. The value should be low enough to avoid stalling simple operations in multi region clusters, but high enough to allow connections under normal conditions.  
The default is set to 5 seconds.
  
Notice that in the *server* section of the configuration, if you want to bind the service
to all interfaces, use value "0.0.0.0", or just leave the *bindHost* line away completely.
Using "*" as bind value won't work.  


`datacenterAvailability`:

Indicates to Reapers its deployment in relation to cluster data center network locality. The value can be either **ALL** (default value), **LOCAL**, or **EACH**. Note that the `datacenterAvailability` setting controls the behavior for metrics collection.

For security reasons, it is possible that Reaper will have access limited to nodes in a single datacenter via JMX (multi region clusters for example). In this case, it is possible to deploy an operate an instance of Reaper in each datacenter where each instance only has access via JMX (with or without authentication) to the nodes in its local datacenter. Where multiple instances of Reaper are in operation in this configuration, only the Apache Cassandra storage option can be used with Reaper. All other storage options are unsuitable in this case. This is because Reaper instances will rely on lightweight transactions to get leadership on segments before processing them. In addition, Reaper will check the number of pending compactions and actively running repairs on all replicas prior to processing a segment.

Setting the value to **ALL** requires Reaper to have access via JMX to all nodes across all datacenters. In this mode Reaper can be backed by all available storage types.

Setting the value to **LOCAL** requires Reaper to have access via JMX to all nodes only in the same datacenter local to Reaper. A single Reaper instance can operate in this mode and repair its local data center. In this case, can be backed by all available storage types and repairs to any remote datacenters are be handled internally by Cassandra. A Reaper instance can be deployed to each datacenter and be configured to operate in this mode. In this case, Reaper can only use Apache Cassandra as its storage. In addition, metrics can be collected asynchronously through the Apache Cassandra storage.

Setting the value to **EACH** requires a minimum of one Reaper instance operating in each datacenter. Each Reaper instance is required to have access via JMX to all nodes only in its local datacenter. When operating in this mode, Reaper can only use Apache Cassandra as its storage. In addition, metrics from nodes in remote datacenters must be collected through the Cassandra storage backend. If all metrics are unavailable, the segment will be postponed for later processing.
