+++
[menu.docs]
name = "In Memory"
parent = "backends"
weight = 1
+++

# Memory Backend

To use in memory storage as the storage type for Reaper, the `storageType` setting must be set to **memory** in the Reaper configuration YAML file. Note that the in memory storage is enabled by default.

In memory storage is volatile and as such all registered cluster, column families and repair un information will be lost upon service restart. This storage setting is intended for testing purposes only.

```yaml
storageType: memory
```