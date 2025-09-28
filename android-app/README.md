Android scaffold for nf-sp00f3r (minimal)

Build (uses system Gradle or downloaded Gradle in /tmp/gradle-8.6):

1) Ensure JAVA_HOME points to the requested JDK (example):

```bash
export JAVA_HOME=/opt/openjdk-bin-17
```

2) Use system gradle (if >=8.6) or the provided helper script:

```bash
# use helper script
./scripts/build.sh
```

Local SDK is configured via `local.properties` (sdk.dir). Do not commit `local.properties` to VCS.
