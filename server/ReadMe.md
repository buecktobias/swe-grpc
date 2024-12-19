# GRPC Setup

- Move `KundeMapperService.java`, `KundeReadServiceGrpcImpl.java` to your service directory.
- Update your POM.xml with:

```

    <dependencies>
    <!-- gRPC -->
    <dependency>
        <groupId>io.grpc</groupId>
        <artifactId>grpc-netty</artifactId>
        <version>1.56.0</version>
    </dependency>
    <dependency>
        <groupId>io.grpc</groupId>
        <artifactId>grpc-protobuf</artifactId>
        <version>1.56.0</version>
    </dependency>
    <dependency>
        <groupId>io.grpc</groupId>
        <artifactId>grpc-stub</artifactId>
        <version>1.56.0</version>
    </dependency>
    <!-- Protocol Buffers -->
    <dependency>
        <groupId>com.google.protobuf</groupId>
        <artifactId>protobuf-java</artifactId>
        <version>3.24.0</version>
    </dependency>
</dependencies>

<build>
    <extensions>
        <extension>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <version>1.8</version>
        </extension>
    </extensions>
    <plugins>
        <plugin>
            <groupId>org.xolstice.maven.plugins</groupId>
            <artifactId>protobuf-maven-plugin</artifactId>
            <version>0.6.1</version>
            <configuration>
                <protocExecutable>protoc</protocExecutable>
                <pluginId>grpc-java</pluginId>
                <pluginExecutable>protoc-gen-grpc-java</pluginExecutable>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                        <goal>compile-custom</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>

```

Or if you use gradle, update `build.gradle.kts` as follows:

```
plugins {
    id 'java'
    id 'com.google.protobuf' version '0.9.4' // Protobuf plugin for Gradle
}

repositories {
    mavenCentral()
}

dependencies {
    // gRPC dependencies
    implementation 'io.grpc:grpc-netty:1.56.0'
    implementation 'io.grpc:grpc-protobuf:1.56.0'
    implementation 'io.grpc:grpc-stub:1.56.0'

    // Protocol Buffers runtime
    implementation 'com.google.protobuf:protobuf-java:3.24.0'

    // For testing (optional)
    testImplementation 'junit:junit:4.13.2'
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.0"
    }
    plugins {
        grpc {
            artifact = "io.grpc:protoc-gen-grpc-java:1.56.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                grpc {}
            }
        }
    }
}

```
